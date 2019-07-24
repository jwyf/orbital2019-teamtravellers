package com.example.travelinpeace;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class ExistingTripsDetails extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText etActivityName;
    private EditText etActivityDay;
    private EditText etActivityTime;
    private Button btnActivity;
    private String countryName;
    private String countryId;

    private DatabaseReference databaseActivities;
    private ListView listViewActivities;
    private List<DayActivities> activitiesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_details);

        setupUIViews();
        initToolbar();

        btnActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addActivity();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseActivities.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                activitiesList.clear();
                for (DataSnapshot activitiesSnapshot : dataSnapshot.getChildren()) {
                    DayActivities activities = activitiesSnapshot.getValue(DayActivities.class);
                    activitiesList.add(activities);
                }
                SubjectDetailsAdapter adapter = new SubjectDetailsAdapter(ExistingTripsDetails.this, R.layout.subject_details_single_item, activitiesList);
                listViewActivities.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ExistingTripsDetails.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupUIViews(){
        toolbar = findViewById(R.id.ToolbarSubjectDetails);
        etActivityName = findViewById(R.id.etActivityName);
        etActivityDay = findViewById(R.id.etActivityDay);
        etActivityTime = findViewById(R.id.etActivityTime);
        btnActivity = findViewById(R.id.btnActivity);

        activitiesList = new ArrayList<>();
        listViewActivities = findViewById(R.id.lvSubjectDetails);

        Intent intent = getIntent();

        countryName = intent.getStringExtra(ExistingTripsActivity.COUNTRY_NAME);
        countryId = intent.getStringExtra(ExistingTripsActivity.COUNTRY_ID);

        databaseActivities = FirebaseDatabase.getInstance().getReference("activities").child(countryId);

    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(countryName + "'s Itinerary");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

//    private void setupListView() {
//        String subject_selected = ExistingTripsActivity.subjectPreferences.getString(ExistingTripsActivity.SUB_PREF, null);
//
//        String[] itinerary = new String[]{};
//        String[] titles = getResources().getStringArray(R.array.titles);
//
//        if (subject_selected.equalsIgnoreCase("Japan")) {
//            itinerary = getResources().getStringArray(R.array.Japan);
//        }
//        else if (subject_selected.equalsIgnoreCase("Singapore")) {
//            itinerary = getResources().getStringArray(R.array.Singapore);
//        }
//        else {
//            itinerary = getResources().getStringArray(R.array.Malaysia);
//        }
//
//        SubjectDetailsAdapter subjectDetailsAdapter = new SubjectDetailsAdapter(this, titles, itinerary);
//        listView.setAdapter(subjectDetailsAdapter);
//    }

    public class SubjectDetailsAdapter extends ArrayAdapter<DayActivities> {

        private int resource;
        private LayoutInflater layoutInflater;
        private List<DayActivities> activitiesList;

        private TextView day, itinerary;

        public SubjectDetailsAdapter(Context context, int resource, List<DayActivities> activitiesList) {
            super(context, resource, activitiesList);
            this.resource = resource;
            this.activitiesList = activitiesList;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.subject_details_single_item, null);
            }

            day = convertView.findViewById(R.id.tvSubjectTitle);
            itinerary = convertView.findViewById(R.id.tvSyllabus);

            DayActivities activities = activitiesList.get(position);

            day.setText("Day " + (position + 1) + activities.getActivityDay());
            //day.setText(activities.getActivityDay());
            itinerary.setText(activities.getActivityName());

            return convertView;
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

    private void addActivity() {
        String name = etActivityName.getText().toString().trim();
        String day = etActivityDay.getText().toString().trim();
        String time = etActivityTime.getText().toString().trim();

        if (!TextUtils.isEmpty(name)) {
            String id = databaseActivities.push().getKey();
            DayActivities activities = new DayActivities(id, name, day, time);
            databaseActivities.child(id).setValue(activities);
            Toast.makeText(this,"New activity added!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,"activity failed!", Toast.LENGTH_SHORT).show();
        }

    }

}
