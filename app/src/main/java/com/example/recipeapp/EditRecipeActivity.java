package com.example.recipeapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditRecipeActivity extends AppCompatActivity {

    private EditText editTextRecipeName, editTextIngredients, editTextInstructions;
    private Button buttonSaveChanges;
    private String dishName;
    private DatabaseReference recipeRef;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        editTextRecipeName = findViewById(R.id.editTextRecipeName2);
        editTextIngredients = findViewById(R.id.editTextIngredients2);
        editTextInstructions = findViewById(R.id.editTextInstructions2);
        buttonSaveChanges = findViewById(R.id.buttonSaveChanges);

        //Get name from intent extras
        dishName = getIntent().getStringExtra("dishName");

        recipeRef = FirebaseDatabase.getInstance().getReference("Recipes").child(dishName);

        recipeRef.orderByChild("dishName").equalTo(dishName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Recipes recipe = snapshot.getValue(Recipes.class);
                    if (recipe !=null) {
                        editTextRecipeName.setText(recipe.getDishName());
                        editTextIngredients.setText(recipe.getIngredients());
                        editTextInstructions.setText(recipe.getInstructions());
                    }
                } else {
                    Toast.makeText(EditRecipeActivity.this, "Recipe not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditRecipeActivity.this, "Error while loading the database! ", Toast.LENGTH_SHORT).show();
            }
        });

        buttonSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

    }

    private void saveChanges() {
        String newName = editTextRecipeName.getText().toString().trim();
        String newIngredients = editTextIngredients.getText().toString().trim();
        String newInstructions = editTextInstructions.getText().toString().trim();

        // Update recipe data in Firebase
        recipeRef.child("dishName").setValue(newName);
        recipeRef.child("ingredients").setValue(newIngredients);
        recipeRef.child("instructions").setValue(newInstructions);

        Toast.makeText(this, "Recipe updated successfully", Toast.LENGTH_SHORT).show();
    }

}
