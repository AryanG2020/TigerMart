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

public class RegistrationActivity extends AppCompatActivity {

    EditText name, email, password;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        name= findViewById(R.id.name);
        email=findViewById(R.id.email);
        password= findViewById(R.id.password);
      //  getSupportActionBar().hide();
        auth=FirebaseAuth.getInstance();


        //Auto LogIn

       if (auth.getCurrentUser()!=null){

            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
        }

    }

    public void signin(View view) {
        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
    }

    public void signup(View view) {
        String username = name.getText().toString();
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please Enter Your Username!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userPassword)) {
            Toast.makeText(this, "Please Enter Your Pin Number!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userEmail)) {
            Toast.makeText(this, "Please Enter Email!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!userEmail.endsWith("@depauw.edu")) {
            Toast.makeText(this, "Please use a DePauw email account (@depauw.edu) to register.", Toast.LENGTH_LONG).show();
            return;
        }


        if (userPassword.length()<=5)
        {
            Toast.makeText(this, "The pin should be more than 5 digits", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
            @Override


            public void onComplete(@NonNull Task<AuthResult> task) {  // Send verification email
                if (task.isSuccessful()) {

                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null) {
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegistrationActivity.this, "Registration successful. Verification email sent!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                                } else {
                                    Toast.makeText(RegistrationActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(RegistrationActivity.this, "Registration Failed" + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
            //No email verification
           /* public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(RegistrationActivity.this,"Registration Complete", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                }
                else {
                    Toast.makeText(RegistrationActivity.this, "Registration Failed"+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }*/
        });
    }
}