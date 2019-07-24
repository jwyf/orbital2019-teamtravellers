package com.example.travelinpeace;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
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
    //public static SharedPreferences subjectPreferences;
    //public static String SUB_PREF;
    public static final String COUNTRY_NAME = "countryname";
    public static final String COUNTRY_ID = "countryid";
    private EditText etCountryName;
    private Button btnCountryName;

    private DatabaseReference databaseCountries;
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

        btnCountryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCountry();
            }
        });
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
                SubjectAdapter adapter = new SubjectAdapter(ExistingTripsActivity.this, R.layout.subject_single_item, countriesList);
                listViewCountries.setAdapter(adapter);
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
        countriesList = new ArrayList<>();
        listViewCountries = findViewById(R.id.lvSubject);

        etCountryName = findViewById(R.id.etCountryName);
        btnCountryName = findViewById(R.id.btnCountryName);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Current Itineraries");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupListView() {

        listViewCountries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                switch (position) {
//                    case 0: {
//                        subjectPreferences.edit().putString(SUB_PREF, "Japan").apply();
//                        Intent intent = new Intent(ExistingTripsActivity.this, ExistingTripsDetails.class);
//                        startActivity(intent);
//                        break;
//                    }
//                    case 1: {
//                        subjectPreferences.edit().putString(SUB_PREF, "Singapore").apply();
//                        Intent intent = new Intent(ExistingTripsActivity.this, ExistingTripsDetails.class);
//                        startActivity(intent);
//                        break;
//                    }
//                    case 2: {
//                        subjectPreferences.edit().putString(SUB_PREF, "Malaysia").apply();
//                        Intent intent = new Intent(ExistingTripsActivity.this, ExistingTripsDetails.class);
//                        startActivity(intent);
//                        break;
//                    }
//                }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home : {
                onBackPressed();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void addCountry() {
        String name = etCountryName.getText().toString().trim();
        if (!TextUtils.isEmpty(name)) {
            String id = databaseCountries.push().getKey();
            Countries countries = new Countries(id, name);
            databaseCountries.child(id).setValue(countries);
            Toast.makeText(this,"New country added!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,"country failed!", Toast.LENGTH_SHORT).show();
        }

    }

}
