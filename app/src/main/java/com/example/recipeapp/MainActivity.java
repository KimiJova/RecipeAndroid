package com.example.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView welcomeTextView;
    private Button browseButton;
    private Button addNewRecipe;
    private FrameLayout fragmentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeTextView = findViewById(R.id.textViewWelcomeMessage);
        browseButton = findViewById(R.id.buttonBrowseRecipes);
        addNewRecipe = findViewById(R.id.buttonAddRecipe);
        fragmentLayout = findViewById(R.id.fragmentLayout);

        AddRecipeFragment addRecipeFragment = new AddRecipeFragment();

        // Retrieving the username from the LoginActivity
        String username = getIntent().getStringExtra("username");

        // Displaying the welcome message with the username of the current logged in user
        if (username != null && !username.isEmpty()) {
            welcomeTextView.setText("Welcome " + username + " to Recipe App!");
        }

        browseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BrowseRecipeActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        addNewRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentLayout.setVisibility(View.VISIBLE);
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                addRecipeFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.fragmentLayout, addRecipeFragment).addToBackStack(null).commit();
            }
        });

    }

    public void hideFragmentContainer() {
        if (fragmentLayout != null) {
            fragmentLayout.setVisibility(View.GONE);
        }
    }
}