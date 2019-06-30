package com.example.orbital2019;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.orbital2019.Utils.LetterImageView;

public class DayDetail extends AppCompatActivity {

    private ListView listView;
    private Toolbar toolbar;

    public static String[] Day1;
    public static String[] Day2;
    public static String[] Day3;
    public static String[] Day4;
    public static String[] Day5;
    public static String[] Day6;

    public static String[] Time1;
    public static String[] Time2;
    public static String[] Time3;
    public static String[] Time4;
    public static String[] Time5;
    public static String[] Time6;

    private String[] PreferredDay;
    private String[] PreferredTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_detail);

        setupUIViews();
        initToolbar();
        setupListView();
    }

    private void setupUIViews() {
        listView = findViewById(R.id.lvDayDetail);
        toolbar = findViewById(R.id.ToolbarDayDetail);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(WeekActivity.sharedPreferences.getString(WeekActivity.SEL_DAY, null));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupListView() {

        Day1 = getResources().getStringArray(R.array.Monday);
        Day2 = getResources().getStringArray(R.array.Tuesday);
        Day3 = getResources().getStringArray(R.array.Wednesday);
        Day4 = getResources().getStringArray(R.array.Thursday);
        Day5 = getResources().getStringArray(R.array.Friday;
        Day6 = getResources().getStringArray(R.array.Saturday);

        Time1 = getResources().getStringArray(R.array.time1);
        Time2 = getResources().getStringArray(R.array.time2);
        Time3 = getResources().getStringArray(R.array.time3);
        Time4 = getResources().getStringArray(R.array.time4);
        Time5 = getResources().getStringArray(R.array.time5);
        Time6 = getResources().getStringArray(R.array.time6);

        String selected_day = WeekActivity.sharedPreferences.getString(WeekActivity.SEL_DAY, null);

        if (selected_day.equalsIgnoreCase("Day1")) {
            PreferredDay = Day1;
            PreferredTime = Time1;
        } else if (selected_day.equalsIgnoreCase("Day2")) {
            PreferredDay = Day2;
            PreferredTime = Time2;
        } else if (selected_day.equalsIgnoreCase("Day3")) {
            PreferredDay = Day3;
            PreferredTime = Time3;
        } else if (selected_day.equalsIgnoreCase("Day4")) {
            PreferredDay = Day4;
            PreferredTime = Time4;
        } else if (selected_day.equalsIgnoreCase("Day5")) {
            PreferredDay = Day5;
            PreferredTime = Time5;
        } else {
            PreferredDay = Day6;
            PreferredTime = Time6;
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, PreferredDay, PreferredTime);
        listView.setAdapter(simpleAdapter);
    }
    public class SimpleAdapter extends BaseAdapter {

        private Context mContext;
        private LayoutInflater layoutInflater;
        private TextView subject, time;
        private String[] subjectArray;
        private String[] timeArray;
        private LetterImageView letterImageView;

        public SimpleAdapter(Context context, String[] subjectArray, String[] timeArray) {
            mContext = context;
            this.subjectArray= subjectArray;
            this.timeArray = timeArray;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return subjectArray.length;
        }

        @Override
        public Object getItem(int position) {
            return subjectArray[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.day_detail_single_item, null);
            }

            subject = (TextView)convertView.findViewById(R.id.tvSubjectDayDetail);
            time = (TextView)convertView.findViewById(R.id.tvTimeDayDetail);
            letterImageView = (LetterImageView)convertView.findViewById(R.id.ivDayDetail);

            subject.setText(subjectArray[position]);
            time.setText(timeArray[position]);

            letterImageView.setOval(true);
            letterImageView.setLetter(subjectArray[position].charAt(0));

            return convertView;
        }
    }

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
