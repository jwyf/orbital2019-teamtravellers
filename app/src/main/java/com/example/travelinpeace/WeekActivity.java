package com.example.travelinpeace;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.travelinpeace.Utils.LetterImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WeekActivity extends AppCompatActivity {

    private Toolbar toolbar;
    //private ListView listView;
    private EditText etDayName;
    private Button btnDayName;
//    public static SharedPreferences sharedPreferences;
//    public static String SEL_DAY;
    public static final String DAY_NAME = "dayname";
    public static final String DAY_ID = "dayid";

    private FirebaseAuth mAuth;
    private DatabaseReference databaseDays;
    private ListView listViewDays;
    private List<Days> daysList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        setupUIViews();
        initToolbar();

        setupListView();

        btnDayName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDay();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseDays.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                daysList.clear();
                for (DataSnapshot daysSnapshot : dataSnapshot.getChildren()) {
                    Days days = daysSnapshot.getValue(Days.class);
                    daysList.add(days);
                }
                WeekAdapter adapter = new WeekAdapter(WeekActivity.this, R.layout.activity_week_single_item, daysList);
                listViewDays.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(WeekActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupUIViews(){
        toolbar = findViewById(R.id.ToolbarWeek);
        //listView = findViewById(R.id.lvWeek);
        //haredPreferences = getSharedPreferences("MY_DAY", MODE_PRIVATE);
        etDayName = findViewById(R.id.etDayName);
        btnDayName = findViewById(R.id.btnDayName);

        mAuth = FirebaseAuth.getInstance();
        String id = mAuth.getUid();
        databaseDays = FirebaseDatabase.getInstance().getReference("newdays").child(id);
        daysList = new ArrayList<>();
        listViewDays = findViewById(R.id.lvWeek);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Travel Week");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupListView() {
        //String[] week = getResources().getStringArray(R.array.TravelWeek);

        //WeekAdapter adapter = new WeekAdapter(this, R.layout.activity_week_single_item, week);

        //listViewDays.setAdapter(adapter);

        listViewDays.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                switch(position) {
//                    case 0: {
//                        startActivity(new Intent(WeekActivity.this, DayDetail.class));
//                        sharedPreferences.edit().putString(SEL_DAY, "Day1").apply();
//                        break;
//                    }
//                    case 1: {
//                        startActivity(new Intent(WeekActivity.this, DayDetail.class));
//                        sharedPreferences.edit().putString(SEL_DAY, "Day2").apply();
//                        break;
//                    }
//                    case 2: {
//                        startActivity(new Intent(WeekActivity.this, DayDetail.class));
//                        sharedPreferences.edit().putString(SEL_DAY, "Day3").apply();
//                        break;
//                    }
//                    case 3: {
//                        startActivity(new Intent(WeekActivity.this, DayDetail.class));
//                        sharedPreferences.edit().putString(SEL_DAY, "Day4").apply();
//                        break;
//                    }
//                    case 4: {
//                        startActivity(new Intent(WeekActivity.this, DayDetail.class));
//                        sharedPreferences.edit().putString(SEL_DAY, "Day5").apply();
//                        break;
//                    }
//                    case 5: {
//                        startActivity(new Intent(WeekActivity.this, DayDetail.class));
//                        sharedPreferences.edit().putString(SEL_DAY, "Day6").apply();
//                        break;
//                    }
//                    default: break;
//                }
            Days days = daysList.get(position);
            Intent intent = new Intent(WeekActivity.this, DayDetail.class);
            intent.putExtra(DAY_NAME, days.getDayName());
            intent.putExtra(DAY_ID, days.getDayId());
            startActivity(intent);
            }
        });
    }

    public class WeekAdapter extends ArrayAdapter<Days>{

        private int resource;
        private LayoutInflater layoutInflater;
        private List<Days> daysList;

        public WeekAdapter(Context context, int resource, List<Days> daysList) {
            super(context, resource, daysList);
            this.resource = resource;
            this.daysList = daysList;
            layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = layoutInflater.inflate(resource, null);
                holder.ivLogo = convertView.findViewById(R.id.ivLetter);
                holder.tvWeek = convertView.findViewById(R.id.tvWeek);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            Days days = daysList.get(position);
            holder.ivLogo.setOval(true);
            holder.ivLogo.setLetter((char)(position + '0' + 1));
            holder.tvWeek.setText(days.getDayName());

            return convertView;
        }

        class ViewHolder{
            private LetterImageView ivLogo;
            private TextView tvWeek;
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

    private void addDay() {
        String name = etDayName.getText().toString().trim();
        if (!TextUtils.isEmpty(name)) {
            String id = databaseDays.push().getKey();
            Days days = new Days(id, name);
            databaseDays.child(id).setValue(days);
            Toast.makeText(this,"New day added!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,"Day failed!", Toast.LENGTH_SHORT).show();
        }

    }
}
