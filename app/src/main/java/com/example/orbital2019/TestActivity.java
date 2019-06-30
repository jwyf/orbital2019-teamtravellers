package com.example.orbital2019;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
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

import com.example.orbital2019.intermediate.auth.LoginFragment;
import com.example.orbital2019.intermediate.auth.RegisterFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class TestActivity extends AppCompatActivity{
    private Toolbar toolbar;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        setupUIView();
        initToolbar();
        setupListView();
    }

    private void setupUIView(){
        toolbar = (Toolbar)findViewById(R.id.ToolbarMain);
        listView = (ListView)findViewById(R.id.lvMain);
    }

    private void initToolbar() {
     setSupportActionBar(toolbar);
     getSupportActionBar().setTitle("Timetable app");
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
                        Intent intent = new Intent(TestActivity.this, WeekActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 1: {
                        Intent intent = new Intent(TestActivity.this, SubjectActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 2: {
                        Intent intent = new Intent(TestActivity.this, FacultyActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 3: {
                        break;
                    }
                    default: break;
                }
            }
        });
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

            if (titleArray[position].equalsIgnoreCase("Timetable")) {
                imageView.setImageResource(R.drawable.ic_menu_camera);
            } else if (titleArray[position].equalsIgnoreCase("Subjects")) {
                imageView.setImageResource(R.drawable.ic_menu_gallery);
            } else if (titleArray[position].equalsIgnoreCase("Faculty")) {
                imageView.setImageResource(R.drawable.icon);
            } else {
                imageView.setImageResource(R.drawable.logo);
            }
            return convertView;
        }
    }
}
