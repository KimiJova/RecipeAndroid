package com.example.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailsActivity extends AppCompatActivity {

    private TextView textViewRecipeName, textViewIngredients, textViewInstructions, textViewAuthor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details_activity);

        textViewRecipeName = findViewById(R.id.textViewRecipeName);
        textViewIngredients = findViewById(R.id.textViewIngredients);
        textViewInstructions = findViewById(R.id.textViewInstructions);
        textViewAuthor = findViewById(R.id.textViewAuthor);

        // Retrieve recipe details from intent
        Intent intent = getIntent();
        String recipeName = intent.getStringExtra("recipeName");
        String ingredients = intent.getStringExtra("ingredients");
        String instructions = intent.getStringExtra("instructions");
        String recipeAuthor = intent.getStringExtra("recipeAuthor");

        textViewRecipeName.setText(recipeName);
        textViewIngredients.setText("Ingredients: " + ingredients);
        textViewInstructions.setText("Instructions: " + instructions);
        textViewAuthor.setText("Recipe made by: " + recipeAuthor);
        //FOR COMMIT

    }
}
