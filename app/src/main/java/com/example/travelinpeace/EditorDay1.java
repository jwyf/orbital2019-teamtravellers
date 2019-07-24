package com.example.travelinpeace;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


public class EditorDay1 extends AppCompatActivity {

    private Toolbar toolbar;
    EditText activity;
    EditText time;
    EditText location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_day1);

        setupUIViews();
        initToolbar();

        activity = findViewById(R.id.editorActivity);
        time = findViewById(R.id.editorTime);
        location = findViewById(R.id.editorLocation);
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

                time.setText(time.getText());
                Intent intent = new Intent(this, DayDetail.class);
                intent.putExtra("activity", activity.getText().toString());
                intent.putExtra("time", time.getText().toString());
                startActivity(intent);
                finish();
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