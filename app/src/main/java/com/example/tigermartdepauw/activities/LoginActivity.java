package com.example.tigermartdepauw.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tigermartdepauw.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        email=findViewById(R.id.email);
        password= findViewById(R.id.password);
        //  getSupportActionBar().hide();
        auth=FirebaseAuth.getInstance();

    }

    public void signin(View view) {
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();

        if (TextUtils.isEmpty(userPassword)) {
            Toast.makeText(this, "Please Enter Your Pin Number!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userEmail)) {
            Toast.makeText(this, "Please Enter Email!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userPassword.length()<=5)
        {
            Toast.makeText(this, "The pin should be more than 5 digits", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
           /* @Override
            public void onComplete(@NonNull Task<AuthResult> task) { (No Email Verification)
           if (task.isSuccessful())
           {
               Toast.makeText(LoginActivity.this, "Log in Successful",Toast.LENGTH_SHORT).show();
               startActivity(new Intent(LoginActivity.this, MainActivity.class));
           }
           else {
               Toast.makeText(LoginActivity.this, "Error: "+task.getException(), Toast.LENGTH_SHORT ).show();
           }
            }*/

            public void onComplete(@NonNull Task<AuthResult> task) { //Email Verification(@depauw.edu)
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null && user.isEmailVerified()) {
                        // Proceed to main part of the app
                        Toast.makeText(LoginActivity.this, "Log in Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        // User's email is not verified
                        Toast.makeText(LoginActivity.this, "Please verify your email first.", Toast.LENGTH_LONG).show();
                        auth.signOut(); // Optional: Sign out the user until they verify their email
                    }
                } else {
                    // Authentication failed
                    Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        });

    }

    public void signup(View view) {
        startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));

    }
}