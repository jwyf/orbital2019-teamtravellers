package com.example.travelinpeace;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;
import android.widget.AdapterView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity{
    private Toolbar toolbar;
    private ListView listView;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        setupUIView();
        initToolbar();
        setupListView();
    }

    private void setupUIView(){
        toolbar = findViewById(R.id.ToolbarMain);
        listView = findViewById(R.id.lvMain);
        mAuth = FirebaseAuth.getInstance();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Travel in Peace");
    }

    private void setupListView(){
        String[] title = getResources().getStringArray(R.array.TravelInPeace);
        String[] description = getResources().getStringArray(R.array.Description);

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, title, description);
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0: {
                        Intent intent = new Intent(MainActivity.this, NewItineraryActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 1: {
                        Intent intent = new Intent(MainActivity.this, ExistingTripsActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 2: {
                        Intent intent = new Intent(MainActivity.this, ReminderActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 3: {
                        Logout();
                        break;
                    }
                    default: break;
                }
            }
        });
    }

    private void Logout() {
        mAuth.signOut();
        finish();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    public class SimpleAdapter extends BaseAdapter{

        private Context mContext;
        private LayoutInflater layoutInflater;
        private TextView title, description;
        private String[] titleArray;
        private String[] descriptionArray;
        private ImageView imageView;

        public SimpleAdapter(Context context, String[] title, String[] description) {
            mContext = context;
            titleArray = title;
            descriptionArray = description;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return titleArray.length;
        }

        @Override
        public Object getItem(int position) {
            return titleArray[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.test_single_item, null);
            }

            title = (TextView)convertView.findViewById(R.id.tvMain);
            description = (TextView)convertView.findViewById(R.id.tvDescription);
            imageView = (ImageView)convertView.findViewById(R.id.ivMain);

            title.setText(titleArray[position]);
            description.setText(descriptionArray[position]);

            if (titleArray[position].equalsIgnoreCase("Create and customize")) {
                imageView.setImageResource(R.drawable.create);
            } else if (titleArray[position].equalsIgnoreCase("View trips")) {
                imageView.setImageResource(R.drawable.view);
            } else if (titleArray[position].equalsIgnoreCase("Remind me!")) {
                imageView.setImageResource(R.drawable.reminder);
            } else {
                imageView.setImageResource(R.drawable.logo);
            }
            return convertView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.logoutMenu: {
                Logout();
                break;
            }
            case R.id.profileMenu: {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
