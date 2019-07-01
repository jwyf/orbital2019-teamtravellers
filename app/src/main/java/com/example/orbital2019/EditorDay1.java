package com.example.orbital2019;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;


public class EditorDay1 extends AppCompatActivity {

    private Toolbar toolbar;
    EditText activity;
    AutoCompleteTextView time;
    EditText location;
    TextView tvTest;
    TextView[] Day1;

    public static SharedPreferences changePreferences;
    public static String CHANGE_PREF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_day1);

        setupUIViews();
        initToolbar();

        activity = (EditText) findViewById(R.id.editorActivity);
        time = (AutoCompleteTextView) findViewById(R.id.editorTime);
        location = (EditText) findViewById(R.id.editorLocation);
        tvTest = findViewById(R.id.tvTest);
    }

    private void setupUIViews() {
        toolbar = findViewById(R.id.ToolbarEditor1);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(DayDetail.eventPreferences.getString(DayDetail.EVENT_PREF, null));
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

                //DayDetail.Day1[0] = activity.getText().toString();
                //changePreferences.edit().putString(CHANGE_PREF, activity.getText().toString()).apply();
                time.setText(time.getText());
                location.setText(location.getText());
                Intent intent = new Intent(this, DayDetail.class);
                startActivity(intent);

                /*} else
                    //item.setIcon(R.drawable.ic_notdone);
                    break;*/
            }
            case android.R.id.home: {
                onBackPressed();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: {
                onBackPressed();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
*/