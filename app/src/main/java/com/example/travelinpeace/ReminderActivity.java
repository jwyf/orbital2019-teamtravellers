package com.example.travelinpeace;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.ListView;
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

public class ReminderActivity extends AppCompatActivity {

    private Toolbar toolbar;
//    public static SharedPreferences sharedPreferences;
//    public static String SEL_FACULTY;

    private DatabaseReference databaseReminder;
    private FirebaseAuth mAuth;
    String userId;

    private List<Reminder> reminderList;
    private ListView listViewReminders;
    public static final String REMINDER_NAME = "remindername";
    public static final String REMINDER_ID = "reminderid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);

        setupUIViews();
        initToolbar();

        setupListView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReminder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            reminderList.clear();
                for (DataSnapshot reminderSnapshot : dataSnapshot.getChildren()) {
                Reminder reminder = reminderSnapshot.getValue(Reminder.class);
                reminderList.add(reminder);
            }
                if (reminderList.isEmpty()) {
                Toast.makeText(ReminderActivity.this, "Add your first Reminder from the top right menu!", Toast.LENGTH_LONG).show();
                ReminderAdapter adapter = new ReminderAdapter(ReminderActivity.this, R.layout.faculty_single_item, reminderList);
                listViewReminders.setAdapter(adapter);
            }
                else {
                ReminderAdapter adapter = new ReminderAdapter(ReminderActivity.this, R.layout.faculty_single_item, reminderList);
                listViewReminders.setAdapter(adapter);
            }
        }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReminderActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupUIViews(){
        toolbar = findViewById(R.id.ToolbarFaculty);
//        listView = findViewById(R.id.lvFaculty);
//        sharedPreferences = getSharedPreferences("myFaculty", MODE_PRIVATE);
        listViewReminders = findViewById(R.id.lvFaculty);
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid();
        databaseReminder = FirebaseDatabase.getInstance().getReference("Reminders").child(userId);
        reminderList = new ArrayList<>();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Reminders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupListView() {

//        ReminderAdapter adapter = new ReminderAdapter(this, R.layout.faculty_single_item, faculty_name);
//        listView.setAdapter(adapter);

        listViewReminders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                switch(position) {
//                    case 0: {
//                        startActivity(new Intent(ReminderActivity.this, ReminderDetails.class));
//                        sharedPreferences.edit().putInt(SEL_FACULTY, 0).apply();
//                        break;
//                    }
//                    case 1: {
//                        startActivity(new Intent(ReminderActivity.this, ReminderDetails.class));
//                        sharedPreferences.edit().putInt(SEL_FACULTY, 1).apply();
//                        break;
//                    }
//                    case 2: {
//                        startActivity(new Intent(ReminderActivity.this, ReminderDetails.class));
//                        sharedPreferences.edit().putInt(SEL_FACULTY, 2).apply();
//                        break;
//                    }
//                    case 3: {
//                        startActivity(new Intent(ReminderActivity.this, ReminderDetails.class));
//                        sharedPreferences.edit().putInt(SEL_FACULTY, 3).apply();
//                        break;
//                    }
//                    default: break;
//                }
                Reminder reminder = reminderList.get(position);
                Intent intent = new Intent(ReminderActivity.this, ReminderDetails.class);
                intent.putExtra(REMINDER_NAME, reminder.getReminderName());
                intent.putExtra(REMINDER_ID, reminder.getReminderId());
                startActivity(intent);
            }
        });
    }

    public class ReminderAdapter extends ArrayAdapter<Reminder> {

        private int resource;
        private LayoutInflater layoutInflater;
        private List<Reminder> reminderList;

        public ReminderAdapter(Context context, int resource, List<Reminder> reminderList) {
            super(context, resource, reminderList);
            this.resource = resource;
            this.reminderList = reminderList;
            layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = layoutInflater.inflate(resource, null);
                holder.ivLogo = convertView.findViewById(R.id.ivLetterFaculty);
                holder.tvFaculty = convertView.findViewById(R.id.tvFacultyName);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            Reminder reminder = reminderList.get(position);
            holder.ivLogo.setOval(true);
            holder.ivLogo.setLetter((char)(position+'0' + 1));
            holder.tvFaculty.setText(reminder.getReminderName());

            return convertView;
        }
        class ViewHolder{
            private LetterImageView ivLogo;
            private TextView tvFaculty;
        }
    }


    private void showUpdateDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextPerson = dialogView.findViewById(R.id.editTextPerson);
        final Button buttonUpdate = dialogView.findViewById(R.id.buttonUpdate);

        dialogBuilder.setTitle("Adding a new Reminder");

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextPerson.getText().toString().trim();

                if(TextUtils.isEmpty(name)) {
                    editTextPerson.setError("Name of Reminder cannot be blank");
                    return;
                }
                else {
                    addReminder(name);
                    alertDialog.dismiss();
                }
            }
        });
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose the reminders to be deleted");

        if (!reminderList.isEmpty()) {
            String[] reminderNames = new String[reminderList.size()];
            for (int i = 0; i < reminderList.size(); i++) {
                reminderNames[i] = reminderList.get(i).getReminderName();
            }
            boolean[] checkedItems = null; //{true, false, false, true, false};

            final ArrayList<Integer> selectedItems = new ArrayList<>();
            builder.setMultiChoiceItems(reminderNames, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
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
                    deleteReminder(selectedItems);
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
        getMenuInflater().inflate(R.menu.adddelete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home : {
                onBackPressed();
                break;
            }
            case R.id.addMenu: {
                showUpdateDialog();
                break;
            }
            case R.id.deleteMenu: {
                showDeleteDialog();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void addReminder(String name) {
        if (!TextUtils.isEmpty(name)) {
            String id = databaseReminder.push().getKey();
            Reminder reminder = new Reminder(id, name);
            databaseReminder.child(id).setValue(reminder);
            Toast.makeText(this,"New reminder added!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,"Please fill in the reminder name!", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteReminder(ArrayList<Integer> selectedItems) {
        for (int i : selectedItems) {
            final String reminderId = reminderList.get(i).getReminderId();
            databaseReminder.child(reminderId).removeValue();
        }
    }
}
