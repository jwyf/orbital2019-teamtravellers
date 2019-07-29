package com.example.travelinpeace;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText name;
    private EditText password;
    private TextView info;
    private Button login;
    private int counter = 5;
    private TextView userRegistration;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private TextView forgotPasssword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = findViewById(R.id.etLoginName);
        password = findViewById(R.id.etLoginPassword);
        info = findViewById(R.id.tvLoginInfo);
        login = findViewById(R.id.btnLogin);
        userRegistration = findViewById(R.id.tvLoginRegister);
        forgotPasssword = findViewById(R.id.tvForgotPassword);

        info.setText("No. of attempts remaining: " + counter);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            finish();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate (name.getText().toString(), password.getText().toString()); //custom private function to validate user from firebase
            }
        });

        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

        forgotPasssword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPassActivity.class));
            }
        });
    }

    private void validate(String userName, String userPassword) {
        progressDialog.setMessage("Please wait, verifying...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    //Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    checkEmailVerification(); //custom function to check if email verified
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    counter--;
                    info.setText("No. of Attempts remaining: " + counter);
                    if (counter == 0) {
                        login.setEnabled(false);
                    }
                }
            }
        });
    }

    private void checkEmailVerification() {
        FirebaseUser firebaseUser = mAuth.getInstance().getCurrentUser();
        Boolean emailFlag = firebaseUser.isEmailVerified();

        if (emailFlag) {
            finish();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
        else {
            Toast.makeText(this, "Please verify your email address first", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
        }
    }

}
