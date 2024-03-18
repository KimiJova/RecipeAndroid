package com.example.recipeapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView welcomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeTextView = findViewById(R.id.textViewWelcomeMessage);

        // Retrieving the username from the LoginActivity
        String username = getIntent().getStringExtra("message_key");

        // Displaying the welcome message with the username of the current logged in user
        if (username != null && !username.isEmpty()) {
            welcomeTextView.setText("Welcome " + username + " to Recipe App!");
        }
    }
}