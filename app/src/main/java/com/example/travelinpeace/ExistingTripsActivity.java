package com.example.travelinpeace;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelinpeace.Utils.LetterImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.opencensus.stats.Aggregation;

public class ExistingTripsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    public static final String COUNTRY_NAME = "countryname";
    public static final String COUNTRY_ID = "countryid";
//    private EditText etCountryName;
//    private Button btnCountryName;

    private DatabaseReference databaseCountries;
    private DatabaseReference databaseActivitiesparent;
    private DatabaseReference databaseActivitiesStringparent;
    private DatabaseReference databaseDaysparent;
    private ListView listViewCountries;
    private List<Countries> countriesList;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        setupUIViews();
        initToolbar();

        setupListView();

//        btnCountryName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addCountry();
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseCountries.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                countriesList.clear();
                for (DataSnapshot countriesSnapshot : dataSnapshot.getChildren()) {
                    Countries countries = countriesSnapshot.getValue(Countries.class);
                    countriesList.add(countries);
                }
                if (countriesList.isEmpty()) {
                    Toast.makeText(ExistingTripsActivity.this, "Create your first Itinerary from the Main Menu!", Toast.LENGTH_LONG).show();
                    SubjectAdapter adapter = new SubjectAdapter(ExistingTripsActivity.this, R.layout.subject_single_item, countriesList);
                    listViewCountries.setAdapter(adapter);
                }
                else {
                    SubjectAdapter adapter = new SubjectAdapter(ExistingTripsActivity.this, R.layout.subject_single_item, countriesList);
                    listViewCountries.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ExistingTripsActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupUIViews(){
        toolbar = findViewById(R.id.ToolbarSubject);
        //subjectPreferences = getSharedPreferences("Subject", MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
        String id = mAuth.getUid();

        databaseCountries = FirebaseDatabase.getInstance().getReference("countries").child(id);
        databaseActivitiesStringparent = FirebaseDatabase.getInstance().getReference("actString");//.child(countryId);
        databaseActivitiesparent = FirebaseDatabase.getInstance().getReference("activity");
        databaseDaysparent = FirebaseDatabase.getInstance().getReference("newdays");//.child(countryId);

        countriesList = new ArrayList<>();
        listViewCountries = findViewById(R.id.lvSubject);

//        etCountryName = findViewById(R.id.etCountryName);
//        btnCountryName = findViewById(R.id.btnCountryName);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Existing Itineraries");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupListView() {

        listViewCountries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Countries countries = countriesList.get(position);
                Intent intent = new Intent(ExistingTripsActivity.this, ExistingTripsDetails.class);
                intent.putExtra(COUNTRY_NAME, countries.getCountriesName());
                intent.putExtra(COUNTRY_ID, countries.getCountriesId());
                startActivity(intent);
            }
        });
    }

    public class SubjectAdapter extends ArrayAdapter<Countries> {

        private int resource;
        private LayoutInflater layoutInflater;
        private List<Countries> countriesList;

        public SubjectAdapter(Context context, int resource, List<Countries> countriesList) {
            super(context, resource, countriesList);
            this.resource = resource;
            this.countriesList = countriesList;
            layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = layoutInflater.inflate(resource, null);
                holder.ivLogo = convertView.findViewById(R.id.ivLetterSubject);
                holder.tvSubject = convertView.findViewById(R.id.tvSubject);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            Countries countries = countriesList.get(position);
            holder.ivLogo.setOval(true);
            holder.ivLogo.setLetter(countries.getCountriesName().charAt(0));
            holder.tvSubject.setText(countries.getCountriesName());

            return convertView;
        }

        class ViewHolder{
            private LetterImageView ivLogo;
            private TextView tvSubject;
        }
    }

    private void showUpdateDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextPerson = dialogView.findViewById(R.id.editTextPerson);
        final Button buttonUpdate = dialogView.findViewById(R.id.buttonUpdate);

        dialogBuilder.setTitle("Adding a new Country");

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextPerson.getText().toString().trim();

                if(TextUtils.isEmpty(name)) {
                    editTextPerson.setError("Name of Country cannot be blank");
                    return;
                }
                else {
                    addCountry(name);
                    alertDialog.dismiss();
                }
            }
        });
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose the itineraries to be deleted");

        String[] countryNames = new String[countriesList.size()];
        for (int i = 0; i < countriesList.size(); i++) {
            countryNames[i] = countriesList.get(i).getCountriesName();
        }
        boolean[] checkedItems = null; //{true, false, false, true, false};

        final ArrayList<Integer> selectedItems = new ArrayList<>();
        builder.setMultiChoiceItems(countryNames, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // user checked or unchecked a box
                if (isChecked) {
                    //add it to the selected items
                    selectedItems.add(which);
                }
                else {
                    selectedItems.remove(Integer.valueOf(which));
                }
            }
        });

        // add Delete and Cancel buttons
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked Delete
                deleteCountry(selectedItems);
            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.adddelete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home : {
                onBackPressed();
                break;
            }
            case R.id.addMenu: {
                showUpdateDialog();
                break;
            }
            case R.id.deleteMenu: {
                showDeleteDialog();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void addCountry(String name) {
//        String name = etCountryName.getText().toString().trim();
        if (!TextUtils.isEmpty(name)) {
            String id = databaseCountries.push().getKey();
            Countries countries = new Countries(id, name);
            databaseCountries.child(id).setValue(countries);
            Toast.makeText(this,"New country added!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,"Please fill in the country name!", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteCountry(ArrayList<Integer> selectedItems) {
        for (int i : selectedItems) {
            final String id = countriesList.get(i).getCountriesId();
            databaseDaysparent.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Days days = ds.getValue(Days.class);
                        if (days.getDayId() != null) {
                            databaseActivitiesparent.child(days.getDayId()).removeValue();
                        }
                    }
                    databaseActivitiesStringparent.child(id).removeValue(); //deleting all actString of country
                    databaseDaysparent.child(id).removeValue(); //deleting all days of country
                    databaseCountries.child(id).removeValue(); //deleting the country itself
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ExistingTripsActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
