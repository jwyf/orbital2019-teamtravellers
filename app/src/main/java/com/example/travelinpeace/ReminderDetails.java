package com.example.travelinpeace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReminderDetails extends AppCompatActivity {

    private CircleImageView facultyImage;
    private Toolbar toolbar;
    private TextView facultyName;
    private TextView phoneNumber;
    private TextView email;
    private TextView place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_details);

        setupUIViews();
        initToolbar();

//        setupDetails();
    }

    private void setupUIViews(){
        toolbar = findViewById(R.id.ToolbarFacultyDetails);
        facultyImage = findViewById(R.id.ivFaculty);
        facultyName = findViewById(R.id.tvFacultySelName);
        phoneNumber = findViewById(R.id.tvPhoneNumber);
        email = findViewById(R.id.tvEmail);
        place = findViewById(R.id.tvPlace);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Faculty Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

//    private void setupDetails() {
//
//        int faculty_pos = ReminderActivity.sharedPreferences.getInt(ReminderActivity.SEL_FACULTY, 0);
//        String[] facultyNames = getResources().getStringArray(R.array.Reminders);
//        int[] facultyImages = new int[]{R.drawable.ic_menu_camera, R.drawable.ic_menu_gallery, R.drawable.ic_menu_send, R.drawable.ic_menu_slideshow};
//        int[] facultyArray = new int[]{R.array.Reminder1, R.array.Reminder2, R.array.Reminder3, R.array.Reminder4};
//        String[] facultyDetails = getResources().getStringArray(facultyArray[faculty_pos]);
//        phoneNumber.setText(facultyDetails[0]);
//        email.setText(facultyDetails[1]);
//        place.setText(facultyDetails[2]);
//        facultyImage.setImageResource(facultyImages[faculty_pos]);
//        facultyName.setText(facultyNames[faculty_pos]);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home : {
                onBackPressed();
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
