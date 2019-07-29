package com.example.travelinpeace;

import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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

public class NewItineraryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    public static final String NEWCOUNTRY_NAME = "newcountryname";
    public static final String NEWCOUNTRY_ID = "newcountryid";
    private EditText etCountryName;
    private Button btnCountryName;
    private String newCountryId;
    private String newCountryName;

    private DatabaseReference databaseCountries;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_itinerary);

        setupUIViews();
        initToolbar();

        btnCountryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addCountry()) {
                    Intent intent = new Intent(NewItineraryActivity.this, WeekActivity.class);
                    intent.putExtra(NEWCOUNTRY_NAME, newCountryName);
                    intent.putExtra(NEWCOUNTRY_ID, newCountryId);
                    startActivity(intent);
                    finish();
                }
                else {
                }
            }
        });
    }

    private void setupUIViews(){
        toolbar = findViewById(R.id.ToolbarNewCountry);

        mAuth = FirebaseAuth.getInstance();
        String id = mAuth.getUid();

        databaseCountries = FirebaseDatabase.getInstance().getReference("countries").child(id);

        etCountryName = findViewById(R.id.etNewCountryName);
        btnCountryName = findViewById(R.id.btnNewCountryName);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Country of your Visit");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home : {
                onBackPressed();
                Toast.makeText(this, "Your new itinerary is not completed, please finish or delete it at View and Edit Trips", Toast.LENGTH_LONG).show();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean addCountry() {
        newCountryName = etCountryName.getText().toString().trim();
        if (!TextUtils.isEmpty(newCountryName)) {
            newCountryId = databaseCountries.push().getKey();
            Countries countries = new Countries(newCountryId, newCountryName);
            databaseCountries.child(newCountryId).setValue(countries);
            Toast.makeText(this,"New country added!", Toast.LENGTH_SHORT).show();
            return true;
        }
        else {
            Toast.makeText(this,"Please enter the country name!", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

}

