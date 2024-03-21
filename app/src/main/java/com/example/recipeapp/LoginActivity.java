package com.example.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private Button buttonLogin, buttonSignup;
    FirebaseDatabase db;
    DatabaseReference ref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);

        username = findViewById(R.id.editTextUsername);
        password = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignup = findViewById(R.id.buttonSignUp);

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if (TextUtils.isEmpty(user)) {
                    username.setError("Please enter your username!");
                    return;
                }

                if (TextUtils.isEmpty(pass)) {
                    password.setError("Please enter your password!");
                    return;
                }

                if (pass.length() < 8) {
                    password.setError("Password must be at least 8 characters!");
                    password.setText("");
                    return;
                }
                db = FirebaseDatabase.getInstance();
                ref = db.getReference("Users");

                ref.orderByChild("username").equalTo(user).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // User with the provided username already exists
                            Toast.makeText(LoginActivity.this, "User already exists.", Toast.LENGTH_SHORT).show();
                        } else {
                            // User doesn't exist, proceed with signup
                            Users users = new Users(user, pass);
                            ref.child(user).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Signup successful.", Toast.LENGTH_SHORT).show();
                                        username.setText("");
                                        password.setText("");
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("username", user);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Signup failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LoginActivity.this, "Database error.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if (TextUtils.isEmpty(user)) {
                    username.setError("Please enter your username!");
                    return;
                }

                if (TextUtils.isEmpty(pass)) {
                    password.setError("Please enter your password!");
                    return;
                }

                db = FirebaseDatabase.getInstance();
                ref = db.getReference("Users");

                ref.orderByChild("username").equalTo(user).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            //User with the inputed username found
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                String storedPassword = userSnapshot.child("password").getValue(String.class);
                                //Comparing password with the inputed password
                                if (storedPassword.equals(pass)) {
                                    Toast.makeText(LoginActivity.this, "Login successfull.", Toast.LENGTH_SHORT).show();
                                    username.setText("");
                                    password.setText("");
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("username", user);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(LoginActivity.this, "Incorrect password.", Toast.LENGTH_SHORT).show();
                                    password.setText("");
                                }
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "User not found.", Toast.LENGTH_SHORT).show();
                            username.setText("");
                            password.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LoginActivity.this, "Database error.", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

}
