package com.example.travelinpeace;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.travelinpeace.Utils.LetterImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WeekActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText etDayName;
    private Button btnDayName;
    public static final String DAY_NAME = "dayname";
    public static final String DAY_ID = "dayid";
    public static final String COUNTRY_ID = "countryid";

    private DatabaseReference databaseDays;
    private DatabaseReference databaseActivitiesparent;
    private DatabaseReference databaseActivitiesString;
    private ListView listViewDays;
    private List<Days> daysList;
    private List<DayActivitiesList> activitiesList;

    private String countryName;
    private String countryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        setupUIViews();
        initToolbar();

        setupListView();

        btnDayName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDay();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseDays.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                daysList.clear();
                for (DataSnapshot daysSnapshot : dataSnapshot.getChildren()) {
                    Days days = daysSnapshot.getValue(Days.class);
                    daysList.add(days);
                }
                if (daysList.isEmpty()) {
                    Toast.makeText(WeekActivity.this,"Please add a new Day plan!", Toast.LENGTH_SHORT).show();
                    WeekAdapter adapter = new WeekAdapter(WeekActivity.this, R.layout.activity_week_single_item, daysList);
                    listViewDays.setAdapter(adapter);
                }
                else {
                    WeekAdapter adapter = new WeekAdapter(WeekActivity.this, R.layout.activity_week_single_item, daysList);
                    listViewDays.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(WeekActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        databaseActivitiesString.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                activitiesList.clear();
                for (DataSnapshot activitiesSnapshot : dataSnapshot.getChildren()) {
                    DayActivitiesList activities = activitiesSnapshot.getValue(DayActivitiesList.class);
                    activitiesList.add(activities);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(WeekActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupUIViews(){
        toolbar = findViewById(R.id.ToolbarWeek);
        etDayName = findViewById(R.id.etDayName);
        btnDayName = findViewById(R.id.btnDayName);

        Intent intent = getIntent();
        countryName = intent.getStringExtra(NewItineraryActivity.NEWCOUNTRY_NAME);
        countryId = intent.getStringExtra(NewItineraryActivity.NEWCOUNTRY_ID);

        databaseDays = FirebaseDatabase.getInstance().getReference("newdays").child(countryId);
        databaseActivitiesString = FirebaseDatabase.getInstance().getReference("actString").child(countryId);
        databaseActivitiesparent = FirebaseDatabase.getInstance().getReference("activity");

        daysList = new ArrayList<>();
        listViewDays = findViewById(R.id.lvWeek);

        activitiesList = new ArrayList<>();

    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Itinerary: Days in " + countryName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupListView() {
        listViewDays.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Days days = daysList.get(position);
            Intent intent = new Intent(WeekActivity.this, DayDetail.class);
            intent.putExtra(DAY_NAME, days.getDayName());
            intent.putExtra(DAY_ID, days.getDayId());
            intent.putExtra(COUNTRY_ID, countryId);
            startActivity(intent);
            }
        });
    }

    public class WeekAdapter extends ArrayAdapter<Days>{

        private int resource;
        private LayoutInflater layoutInflater;
        private List<Days> daysList;

        public WeekAdapter(Context context, int resource, List<Days> daysList) {
            super(context, resource, daysList);
            this.resource = resource;
            this.daysList = daysList;
            layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = layoutInflater.inflate(resource, null);
                holder.ivLogo = convertView.findViewById(R.id.ivLetter);
                holder.tvWeek = convertView.findViewById(R.id.tvWeek);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            Days days = daysList.get(position);
            holder.ivLogo.setOval(true);
            holder.ivLogo.setLetter((char)(position + '0' + 1));
            holder.tvWeek.setText(days.getDayName());

            return convertView;
        }

        class ViewHolder{
            private LetterImageView ivLogo;
            private TextView tvWeek;
        }
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose the days to be deleted");

        if (!daysList.isEmpty()) {
            String[] dayNames = new String[daysList.size()];
            for (int i = 0; i < daysList.size(); i++) {
                dayNames[i] = daysList.get(i).getDayName();
            }
            boolean[] checkedItems = null; //{true, false, false, true, false};

            final ArrayList<Integer> selectedItems = new ArrayList<>();
            builder.setMultiChoiceItems(dayNames, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    // user checked or unchecked a box
                    if (isChecked) {
                        //add it to the selected items
                        selectedItems.add(which);
                    } else {
                        selectedItems.remove(Integer.valueOf(which));
                    }
                }
            });

            // add Delete and Cancel buttons
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // user clicked Delete
                    deleteDay(selectedItems);
                }
            });
            builder.setNegativeButton("Cancel", null);
        }
        else {
            builder.setMessage("There are no items to be deleted");
            builder.setPositiveButton("OK", null);
        }

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.deleteconfirm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.confirmMenu: {
                if (daysList.isEmpty()) {
                    Toast.makeText(this, "Please add at least one day", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (activitiesList.size() != daysList.size()) {
                    Toast.makeText(this, "Please add at least one activity for each day!", Toast.LENGTH_SHORT).show();
                    break;
                }
                else {
                    Toast.makeText(this, "New itinerary added for " + countryName + "!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
            case R.id.deleteMenu: {
                showDeleteDialog();
                break;
            }
            case android.R.id.home : {
                if (activitiesList.size() != daysList.size()) {
                    Toast.makeText(this, "Please add at least one activity for each day!", Toast.LENGTH_SHORT).show();
                    break;
                }
                else {
                    onBackPressed();
                    Toast.makeText(this, "Your new itinerary is not completed, please finish or delete it at View and Edit Trips", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void addDay() {
        final String name = etDayName.getText().toString().trim();
        if (!TextUtils.isEmpty(name)) {
            databaseDays.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String currDayId = "";
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Days days = ds.getValue(Days.class);
                        if (days.getDayName() != null && days.getDayName().equals(name)) {
                            currDayId = days.getDayId();
                            break;
                        }
                    }
                    if (currDayId.isEmpty()) {
                        addNewDay(name);
                    } else {
                        Toast.makeText(WeekActivity.this, "Error: That day already exists!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            Toast.makeText(this,"Please fill in all the fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void addNewDay(String name) {
        String id = databaseDays.push().getKey();
        Days days = new Days(id, name);
        databaseDays.child(id).setValue(days);
        Toast.makeText(this,"New day added!", Toast.LENGTH_SHORT).show();
    }

    private void deleteDay(ArrayList<Integer> selectedItems) {
        for (int i : selectedItems) {
            String id = daysList.get(i).getDayId();
            databaseActivitiesString.child(id).removeValue(); //deleting actString
            databaseActivitiesparent.child(id).removeValue(); //deleting all activities under the day
            databaseDays.child(id).removeValue(); //deleting days
        }
    }
}
