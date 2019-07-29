package com.example.travelinpeace;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;


public class NotificationActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText etReminderName;
    private EditText etReminderTime;
    private Button btnReminder;

    private String reminderName;
    private String reminderId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        setupUIViews();
        initToolbar();

        btnReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etReminderName.getText().toString().trim();
                String time = etReminderTime.getText().toString().trim();
                setReminder(Integer.parseInt(time));
                finish();
            }
        });

    }

    private void setupUIViews(){
        toolbar = findViewById(R.id.ToolbarReminder);
        etReminderName = findViewById(R.id.etReminderName);
        etReminderTime = findViewById(R.id.etReminderTime);
        btnReminder = findViewById(R.id.btnReminder);

        Intent intent = getIntent();
        reminderName = intent.getStringExtra(ReminderActivity.REMINDER_NAME);
        reminderId = intent.getStringExtra(ReminderActivity.REMINDER_ID);

        etReminderName.setText(reminderName);
        }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Reminder: " + reminderName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home : {
                onBackPressed();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setReminder(Integer amount) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, amount);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
    }
}
