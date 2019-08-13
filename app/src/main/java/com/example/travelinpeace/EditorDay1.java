package com.example.travelinpeace;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class EditorDay1 extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText activity;
    private EditText time;

    private String activityName;
    private String activityNo;
    private String activityTime;
    private String activityId;

    private String newActivityName;

    private DatabaseReference databaseActivities;
    private DatabaseReference databaseActivitiesString;
    private DatabaseReference databaseSingleString;
    private String dayId;
    private String dayName;
    private String countryId;

    private DayActivitiesList dayActivitiesListNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_day1);

        setupUIViews();
        initToolbar();
    }

    private void setupUIViews() {
        toolbar = findViewById(R.id.ToolbarEditor1);
        activity = findViewById(R.id.editorActivity);
        time = findViewById(R.id.editorTime);

        Intent intent = getIntent();
        activityName = intent.getStringExtra(DayDetail.ACTIVITY_NAME);
        activityNo = intent.getStringExtra(DayDetail.ACTIVITY_NO);
        activityTime = intent.getStringExtra(DayDetail.ACTIVITY_TIME);
        activityId = intent.getStringExtra(DayDetail.ACTIVITY_ID);
        dayId = intent.getStringExtra(DayDetail.DAY_ID);
        dayName = intent.getStringExtra(DayDetail.DAY_NAME);
        countryId = intent.getStringExtra(DayDetail.COUNTRY_ID);

        activity.setText(activityName);
        time.setText(activityTime);

        databaseActivities = FirebaseDatabase.getInstance().getReference("activity").child(dayId);
        databaseActivitiesString = FirebaseDatabase.getInstance().getReference("actString").child(countryId);
        databaseSingleString = databaseActivitiesString.child(dayId);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Activity " + activityNo + ": " + activityName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.confirmtick, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save: {
                //boolean done = save();
                //if (done) {
                //item.setIcon(R.drawable.ic_action_name);
                // } else
                //    item.setIcon(R.drawable.ic_notdone);
                //    break;
                newActivityName = activity.getText().toString().trim();
                activityTime = time.getText().toString().trim();
                if (!TextUtils.isEmpty(newActivityName) && !TextUtils.isEmpty(activityTime)) {
                    updateActivityString();
                    finish();
                    break;
                }
                else {
                    Toast.makeText(this, "Error: Please fill in all the fields!", Toast.LENGTH_SHORT).show();
                    break;
                }

            }
            case android.R.id.home: {
                onBackPressed();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateActivity(String id, String name, String day, String time) {
        DatabaseReference databaseReference = databaseActivities.child(id);
        DayActivities activities = new DayActivities(id, name, day, time, dayId);
        databaseReference.setValue(activities);
//        Toast.makeText(this, "Activity updated! Refreshing", Toast.LENGTH_LONG).show();
    }

    private void updateActivityString() {

//        databaseSingleString.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                DayActivitiesList dayActivitiesList = dataSnapshot.getValue(DayActivitiesList.class);
//                String newActivityString = dayActivitiesList.getActivityString().replace(oldActivityName, newActivityName);
//                dayActivitiesListNew = new DayActivitiesList(dayName, dayId, newActivityString);
//                databaseSingleString.setValue(dayActivitiesListNew);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(EditorDay1.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
//            }
//        });
        updateActivity(activityId, newActivityName, dayName, activityTime);
        databaseActivities.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String newActivityString = "";
                Boolean isSame = false;
                int count = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    DayActivities dayActivities = ds.getValue(DayActivities.class);
                    if (dayActivities.getActivityName() != null && dayActivities.getActivityName().equals(activityName)) {
                        isSame = true;
                        break;
                    }
                    else {
                        if (dayActivities.getActivityName().equals(newActivityName)) {
                            count += 1;
                        }
                        newActivityString += dayActivities.getActivityName() + ", ";
                    }
                }
                if (isSame) {
                    Toast.makeText(EditorDay1.this, "Activity details were not changed!", Toast.LENGTH_LONG).show();
                    updateActivity(activityId, activityName, dayName, activityTime);
                }
                else if (count >= 2) {
                    Toast.makeText(EditorDay1.this, "Error: There already exists an activity with that name", Toast.LENGTH_LONG).show();
                    updateActivity(activityId, activityName, dayName, activityTime);
                }
                else {
                    newActivityString = newActivityString.substring(0, newActivityString.length() - 2);
                    dayActivitiesListNew = new DayActivitiesList(dayName, dayId, newActivityString);
                    databaseSingleString.setValue(dayActivitiesListNew);
                    Toast.makeText(EditorDay1.this, "Activity updated! Refreshing", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditorDay1.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
