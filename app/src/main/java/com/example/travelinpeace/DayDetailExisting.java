package com.example.travelinpeace;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DayDetailExisting extends AppCompatActivity {

    private Toolbar toolbar;

//    private EditText etSingleActivityName;
//    private EditText etSingleActivityTime;
//    private Button btnSingleActivity;
    private String dayName;
    private String dayId;
    private String countryId;

    private DatabaseReference databaseActivities;
    private DatabaseReference databaseActivitiesString;
    private DatabaseReference databaseSingleString;
    private DatabaseReference databaseDays;
    private ListView listViewActivities;
    private List<DayActivities> activitiesList;

    public static final String ACTIVITY_NAME = "activityname";
    public static final String ACTIVITY_NO = "activitynumber";
    public static final String ACTIVITY_TIME = "activitytime";
    public static final String ACTIVITY_ID = "activityid";

    public static final String DAY_ID = "dayid";
    public static final String DAY_NAME = "dayname";
    public static final String COUNTRY_ID = "countryid";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_day_detail);

        setupUIViews();
        initToolbar();
        setupListView();

//        btnSingleActivity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addActivity();
//            }
//        });

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
                if (activitiesList.isEmpty()) {
                    Toast.makeText(DayDetailExisting.this, dayName + " has no activity and is deleted", Toast.LENGTH_LONG).show();
                    DayAdapter adapter = new DayAdapter(DayDetailExisting.this, R.layout.day_detail_single_item, activitiesList);
                    listViewActivities.setAdapter(adapter);
                }
                else {
                    DayAdapter adapter = new DayAdapter(DayDetailExisting.this, R.layout.day_detail_single_item, activitiesList);
                    listViewActivities.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DayDetailExisting.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupUIViews() {
        toolbar = findViewById(R.id.ToolbarDayDetail);
//        etSingleActivityName = findViewById(R.id.etSingleActivityName);
//        etSingleActivityTime = findViewById(R.id.etSingleActivityTime);
//        btnSingleActivity = findViewById(R.id.btnSingleActivity);
        listViewActivities = findViewById(R.id.lvDayDetail);

        activitiesList = new ArrayList<>();

        Intent intent = getIntent();
        dayName = intent.getStringExtra(DAY_NAME);
        dayId = intent.getStringExtra(DAY_ID);
        countryId = intent.getStringExtra(COUNTRY_ID);

        databaseActivities = FirebaseDatabase.getInstance().getReference("activity").child(dayId);
        databaseActivitiesString = FirebaseDatabase.getInstance().getReference("actString").child(countryId);
        databaseSingleString = databaseActivitiesString.child(dayId);
        databaseDays = FirebaseDatabase.getInstance().getReference("newdays").child(countryId);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Activities on " + dayName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupListView() {
        listViewActivities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DayActivities activities = activitiesList.get(position);
                Intent intent = new Intent(DayDetailExisting.this, EditorDay1.class);
                intent.putExtra(ACTIVITY_NAME, activities.getActivityName());
                intent.putExtra(ACTIVITY_NO, Integer.toString(position + 1));
                intent.putExtra(ACTIVITY_TIME, activities.getActivityTime());
                intent.putExtra(ACTIVITY_ID, activities.getActivityId());

                intent.putExtra(DAY_NAME, dayName);
                intent.putExtra(DAY_ID, dayId);
                intent.putExtra(COUNTRY_ID, countryId);
                startActivity(intent);
            }
        });
    }
    public class DayAdapter extends ArrayAdapter<DayActivities> {

        private int resource;
        private LayoutInflater layoutInflater;
        private List<DayActivities> activitiesList;

        private TextView subject, time;
        private LetterImageView letterImageView;

        public DayAdapter(Context context, int resource, List<DayActivities> activitiesList) {
            super(context, resource, activitiesList);
            this.resource = resource;
            this.activitiesList = activitiesList;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.day_detail_single_item, null);
            }

            subject = convertView.findViewById(R.id.tvSubjectDayDetail);
            time = convertView.findViewById(R.id.tvTimeDayDetail);
            letterImageView = convertView.findViewById(R.id.ivDayDetail);

            DayActivities activities = activitiesList.get(position);

            subject.setText(activities.getActivityName());
            time.setText(activities.getActivityTime());

            letterImageView.setOval(true);
            letterImageView.setLetter(activities.getActivityName().charAt(0));

            return convertView;
        }
    }

    private void showUpdateDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.addactivity_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = dialogView.findViewById(R.id.editTextName);
        final EditText editTextTime = dialogView.findViewById(R.id.editTextTime);
        final Button buttonUpdate = dialogView.findViewById(R.id.buttonUpdate);

        dialogBuilder.setTitle("Adding a new Activity");

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String time = editTextTime.getText().toString().trim();

                if(TextUtils.isEmpty(name)) {
                    editTextName.setError("Name of Day cannot be blank");
                    return;
                }
                if(TextUtils.isEmpty(time)) {
                    editTextTime.setError("Name of Day cannot be blank");
                    return;
                }
                else {
                    addActivity(name, time);
                    alertDialog.dismiss();
                }
            }
        });
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose the activities to be deleted");

        if (!activitiesList.isEmpty()) {
            String[] activityNames = new String[activitiesList.size()];
            for (int i = 0; i < activitiesList.size(); i++) {
                activityNames[i] = activitiesList.get(i).getActivityName();
            }
            boolean[] checkedItems = null; //{true, false, false, true, false};

            final ArrayList<Integer> selectedItems = new ArrayList<>();
            builder.setMultiChoiceItems(activityNames, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
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
                    deleteActivity(selectedItems);
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

    private void addActivity(final String name, final String time) {
//        String name = etSingleActivityName.getText().toString().trim();
//        String time = etSingleActivityTime.getText().toString().trim();
        String day = dayName;

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(time) && !TextUtils.isEmpty(day)) {
            databaseActivitiesString.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String currActivityString = "";
                    String currDayId = "";
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        DayActivitiesList dayActivitiesList = ds.getValue(DayActivitiesList.class);
                        if (dayActivitiesList.getDayId() != null && dayActivitiesList.getDayId().equals(dayId)) {
                            currActivityString = dayActivitiesList.getActivityString();
                            currDayId = dayActivitiesList.getDayId();
                            break;
                        }
                    }
                    if (currActivityString.isEmpty()) {
                        addNewActivityString(name, time);
                    }
                    else {
                        updateActivityString(currActivityString, currDayId, name, time);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(DayDetailExisting.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(this,"Please fill in all the fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void addNewActivityString(String name, String time) {
//        String name = etSingleActivityName.getText().toString().trim();
//        String time = etSingleActivityTime.getText().toString().trim();
        DayActivitiesList dayActivitiesList = new DayActivitiesList(dayName, dayId, name);
        databaseActivitiesString.child(dayId).setValue(dayActivitiesList);
        Toast.makeText(this,"New activity added!", Toast.LENGTH_SHORT).show();
        createNewActivity(name, dayName, time);
    }

    private void updateActivityString(final String currActivityString, final String currDayId, final String name, final String time) {
//        final String name = etSingleActivityName.getText().toString().trim();
        databaseActivities.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean isSame = false;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    DayActivities dayActivities = ds.getValue(DayActivities.class);
                    if (dayActivities.getActivityName() != null && dayActivities.getActivityName().equals(name)) {
                        isSame = true;
                        break;
                    }
                }
                if (isSame) {
                    Toast.makeText(DayDetailExisting.this,"Error: An activity with the same name already exists!", Toast.LENGTH_SHORT).show();
                }
                else {
                    String newString = currActivityString + ", " + name;
                    DayActivitiesList dayActivitiesList = new DayActivitiesList(dayName, dayId, newString);
                    databaseActivitiesString.child(currDayId).setValue(dayActivitiesList);
                    Toast.makeText(DayDetailExisting.this,"Activity updated!", Toast.LENGTH_SHORT).show();
                    createNewActivity(name, dayName, time);
//                    createNewActivity(name, dayName, time);
//                    String newString = "";
//                    for (DataSnapshot ds1 : dataSnapshot.getChildren()) {
//                        DayActivities dayActivities = ds1.getValue(DayActivities.class);
//                        newString += dayActivities.getActivityName() + ", ";
//                    }
//                    newString = newString.substring(0, newString.length() - 2);
//                    DayActivitiesList dayActivitiesList = new DayActivitiesList(dayName, dayId, newString);
//                    databaseActivitiesString.child(currDayId).setValue(dayActivitiesList);
//                    Toast.makeText(DayDetailExisting.this,"Activity updated!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DayDetailExisting.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createNewActivity(String name, String day, String time) {
        String id = databaseActivities.push().getKey();
        DayActivities activities = new DayActivities(id, name, day, time, dayId);
        databaseActivities.child(id).setValue(activities);
    }

    private void deleteActivity(ArrayList<Integer> selectedItems) {
        for (int i : selectedItems) {
            String id = activitiesList.get(i).getActivityId();
            databaseActivities.child(id).removeValue();
        }

        databaseActivities.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String newActivityString = "";
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    DayActivities dayActivities = ds.getValue(DayActivities.class);
                    newActivityString += dayActivities.getActivityName() + ", ";
                }
                if (newActivityString.isEmpty()) {
                    databaseSingleString.removeValue(); //deleteActString
                    databaseDays.child(dayId).removeValue(); //deleteDay
                    finish();
                }
                else {
                    newActivityString = newActivityString.substring(0, newActivityString.length() - 2);
                    DayActivitiesList dayActivitiesListNew = new DayActivitiesList(dayName, dayId, newActivityString);
                    databaseSingleString.setValue(dayActivitiesListNew);
                    Toast.makeText(DayDetailExisting.this, "Activity deleted! Refreshing", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DayDetailExisting.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
