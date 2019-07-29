package com.example.travelinpeace;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelinpeace.Utils.LetterImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DayDetail extends AppCompatActivity {

    private Toolbar toolbar;

    private EditText etSingleActivityName;
    private EditText etSingleActivityTime;
    private Button btnSingleActivity;
    private String dayName;
    private String dayId;
    private String countryId;

    private DatabaseReference databaseActivities;
    private DatabaseReference databaseActivitiesString;
    private DatabaseReference databaseSingleString;
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
        setContentView(R.layout.activity_day_detail);

        setupUIViews();
        initToolbar();
        setupListView();

        btnSingleActivity.setOnClickListener(new View.OnClickListener() {
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
                DayAdapter adapter = new DayAdapter(DayDetail.this, R.layout.day_detail_single_item, activitiesList);
                listViewActivities.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DayDetail.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setupUIViews() {
        toolbar = findViewById(R.id.ToolbarDayDetail);
        etSingleActivityName = findViewById(R.id.etSingleActivityName);
        etSingleActivityTime = findViewById(R.id.etSingleActivityTime);
        btnSingleActivity = findViewById(R.id.btnSingleActivity);
        listViewActivities = findViewById(R.id.lvDayDetail);

        activitiesList = new ArrayList<>();

        Intent intent = getIntent();
        dayName = intent.getStringExtra(DAY_NAME);
        dayId = intent.getStringExtra(DAY_ID);
        countryId = intent.getStringExtra(COUNTRY_ID);

        databaseActivities = FirebaseDatabase.getInstance().getReference("activity").child(dayId);
        databaseActivitiesString = FirebaseDatabase.getInstance().getReference("actString").child(countryId);
        databaseSingleString = databaseActivitiesString.child(dayId);
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
                Intent intent = new Intent(DayDetail.this, EditorDay1.class);
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

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose the itineraries to be deleted");

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
        getMenuInflater().inflate(R.menu.delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.deleteMenu: {
                showDeleteDialog();
                break;
            }
            case android.R.id.home : {
                onBackPressed();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void addActivity() {
        String name = etSingleActivityName.getText().toString().trim();
        String time = etSingleActivityTime.getText().toString().trim();
        String day = dayName;

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(time) && !TextUtils.isEmpty(day)) {
            databaseActivitiesString.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String currActivityString = "";
                    String currActivityId = "";
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        DayActivitiesList dayActivitiesList = ds.getValue(DayActivitiesList.class);
                        if (dayActivitiesList.getDayId() != null && dayActivitiesList.getDayId().equals(dayId)) {
                            currActivityString = dayActivitiesList.getActivityString();
                            currActivityId = dayActivitiesList.getDayId();
                            break;
                        }
                    }
                    if (currActivityString.isEmpty()) {
                        addNewActivityString();
                    }
                    else {
                        updateActivityString(currActivityString, currActivityId);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(DayDetail.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(this,"Please fill in all the fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void addNewActivityString() {
        String name = etSingleActivityName.getText().toString().trim();
        String time = etSingleActivityTime.getText().toString().trim();
        DayActivitiesList dayActivitiesList = new DayActivitiesList(dayName, dayId, name);
        databaseActivitiesString.child(dayId).setValue(dayActivitiesList);
        Toast.makeText(this,"New activity added!", Toast.LENGTH_SHORT).show();
        createNewActivity(name, dayName, time);
    }

    private void updateActivityString(final String currActivityString, final String currActivityId) {
        final String name = etSingleActivityName.getText().toString().trim();
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
                    Toast.makeText(DayDetail.this,"Error: An activity with the same name already exists!", Toast.LENGTH_SHORT).show();
                }
                else {
                    String newString = currActivityString + ", " + name;
                    String time = etSingleActivityTime.getText().toString().trim();
                    DayActivitiesList dayActivitiesList = new DayActivitiesList(dayName, dayId, newString);
                    databaseActivitiesString.child(currActivityId).setValue(dayActivitiesList);
                    Toast.makeText(DayDetail.this,"Activity updated!", Toast.LENGTH_SHORT).show();
                    createNewActivity(name, dayName, time);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DayDetail.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
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
                }
                else {
                    newActivityString = newActivityString.substring(0, newActivityString.length() - 2);
                    DayActivitiesList dayActivitiesListNew = new DayActivitiesList(dayName, dayId, newActivityString);
                    databaseSingleString.setValue(dayActivitiesListNew);
                    Toast.makeText(DayDetail.this, "Activity deleted! Refreshing", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DayDetail.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
