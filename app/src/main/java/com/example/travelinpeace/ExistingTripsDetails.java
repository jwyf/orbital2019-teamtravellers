package com.example.travelinpeace;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExistingTripsDetails extends AppCompatActivity {

    private Toolbar toolbar;
//    private EditText etActivityDay;
//    private Button btnActivity;
    private String countryName;
    private String countryId;
    public static final String DAY_NAME = "dayname";
    public static final String DAY_ID = "dayid";
    public static final String COUNTRY_ID = "countryid";


    private DatabaseReference databaseActivitiesparent;
    private DatabaseReference databaseActivitiesString;
    private DatabaseReference databaseDays;
    private ListView listViewActivities;
    private List<DayActivitiesList> activitiesList;
//    private ArrayList<String> dayIds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_details);

        setupUIViews();
        initToolbar();

//        btnActivity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addDay();
//            }
//        });

        listViewActivities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DayActivitiesList activities = activitiesList.get(position);
                Intent intent = new Intent(ExistingTripsDetails.this, DayDetailExisting.class);
                intent.putExtra(DAY_NAME, activities.getDayName());
                intent.putExtra(DAY_ID, activities.getDayId());
                intent.putExtra(COUNTRY_ID, countryId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseActivitiesString.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                activitiesList.clear();
                for (DataSnapshot activitiesSnapshot : dataSnapshot.getChildren()) {
                    DayActivitiesList activities = activitiesSnapshot.getValue(DayActivitiesList.class);
                    activitiesList.add(activities);
                }
                if (activitiesList.isEmpty()) {
                    Toast.makeText(ExistingTripsDetails.this, countryName + " has no Day plan currently! Add a new Day from the upper right menu!", Toast.LENGTH_LONG).show();
                    SubjectDetailsAdapter adapter = new SubjectDetailsAdapter(ExistingTripsDetails.this, R.layout.subject_details_single_item, activitiesList);
                    listViewActivities.setAdapter(adapter);
                }
                else {
                    SubjectDetailsAdapter adapter = new SubjectDetailsAdapter(ExistingTripsDetails.this, R.layout.subject_details_single_item, activitiesList);
                    listViewActivities.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ExistingTripsDetails.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupUIViews(){
        toolbar = findViewById(R.id.ToolbarSubjectDetails);
//        etActivityDay = findViewById(R.id.etActivityDay);
//        btnActivity = findViewById(R.id.btnActivity);

        activitiesList = new ArrayList<>();
        listViewActivities = findViewById(R.id.lvSubjectDetails);

        Intent intent = getIntent();

        countryName = intent.getStringExtra(ExistingTripsActivity.COUNTRY_NAME);
        countryId = intent.getStringExtra(ExistingTripsActivity.COUNTRY_ID);

        databaseActivitiesString = FirebaseDatabase.getInstance().getReference("actString").child(countryId);
        databaseActivitiesparent = FirebaseDatabase.getInstance().getReference("activity");
        databaseDays = FirebaseDatabase.getInstance().getReference("newdays").child(countryId);

//        dayIds = new ArrayList<>();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(countryName + "'s Itinerary overview");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

//    public class SubjectDetailsAdapter extends ArrayAdapter<DayActivities>{
////    public class SubjectDetailsAdapter extends /*ArrayAdapter<DayActivities>*/ ArrayAdapter<String> {
//
//        private int resource;
//        private LayoutInflater layoutInflater;
//        private List<DayActivities> activitiesList;
////        private ArrayList<String> AL;
//
//        private TextView day, itinerary;
//
////        public SubjectDetailsAdapter(Context context, int resource, /*List<DayActivities> activitiesList*/ ArrayList<String> AL) {
//        public SubjectDetailsAdapter(Context context, int resource, List<DayActivities> activitiesList) {
////            super(context, resource, /*activitiesList*/ AL);
//            super(context, resource, activitiesList);
//            this.resource = resource;
////            this.AL = AL;
//            this.activitiesList = activitiesList;
//            layoutInflater = LayoutInflater.from(context);
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            if (convertView == null) {
//                convertView = layoutInflater.inflate(R.layout.subject_details_single_item, null);
//            }
//
//            day = convertView.findViewById(R.id.tvSubjectTitle);
//            itinerary = convertView.findViewById(R.id.tvSyllabus);
//
//            DayActivities activities = activitiesList.get(position);
//
////            day.setText("Day " + (position + 1) + ": " + /*activities.getActivityDay()*/ AL.get(position));
//            day.setText("Day " + (position + 1) + ": " + activities.getActivityDay());
//
////            itinerary.setText(/*activities.getActivityName()*/AL.get(position));
//            itinerary.setText(activities.getActivityName());
//
//            return convertView;
//        }
//    }

    public class SubjectDetailsAdapter extends ArrayAdapter<DayActivitiesList>{

        private int resource;
        private LayoutInflater layoutInflater;
        private List<DayActivitiesList> activitiesList;

        private TextView day, itinerary;

        public SubjectDetailsAdapter(Context context, int resource, List<DayActivitiesList> activitiesList) {
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

            DayActivitiesList activities = activitiesList.get(position);

            day.setText("Day " + (position + 1) + ": " + activities.getDayName());

            itinerary.setText(activities.getActivityString());

            return convertView;
        }
    }

    private void showUpdateDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextPerson = dialogView.findViewById(R.id.editTextPerson);
        final Button buttonUpdate = dialogView.findViewById(R.id.buttonUpdate);

        dialogBuilder.setTitle("Adding a new Day");

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextPerson.getText().toString().trim();

                if(TextUtils.isEmpty(name)) {
                    editTextPerson.setError("Name of Day cannot be blank");
                    return;
                }
                else {
                    addDay(name);
                    alertDialog.dismiss();
                }
            }
        });
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose the days to be deleted");

        if (!activitiesList.isEmpty()) {
            String[] dayNames = new String[activitiesList.size()];
            for (int i = 0; i < activitiesList.size(); i++) {
                dayNames[i] = activitiesList.get(i).getDayName();
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

    private void addDay(final String name) {
//        final String name = etActivityDay.getText().toString().trim();
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
                    }
                    else {
                        Toast.makeText(ExistingTripsDetails.this, "Error: That day already exists!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ExistingTripsDetails.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
            Toast.makeText(this,"Error: Please fill in the field!", Toast.LENGTH_SHORT).show();
        }
    }

    private void addNewDay(String name) {
        //adding day
        String id = databaseDays.push().getKey();
        Days days = new Days(id, name);
        databaseDays.child(id).setValue(days);
        Toast.makeText(this,"New day added!", Toast.LENGTH_SHORT).show();

        //adding actString
        DayActivitiesList dayActivitiesList = new DayActivitiesList(name, id, "Placeholder Activity Name");
        databaseActivitiesString.child(id).setValue(dayActivitiesList);

        //adding activity
        String activityId = databaseActivitiesparent.child(id).push().getKey();
        DayActivities activities = new DayActivities(activityId, "Placeholder Activity Name", name, "Placeholder Activity Time", id);
        databaseActivitiesparent.child(id).child(activityId).setValue(activities);
    }

    private void deleteDay(ArrayList<Integer> selectedItems) {
        for (int i : selectedItems) {
            String id = activitiesList.get(i).getDayId();
            databaseActivitiesString.child(id).removeValue(); //deleting actString
            databaseActivitiesparent.child(id).removeValue(); //deleting all activities under the day
            databaseDays.child(id).removeValue(); //deleting days
        }
    }
}

