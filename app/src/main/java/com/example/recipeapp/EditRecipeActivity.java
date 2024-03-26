package com.example.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditRecipeActivity extends AppCompatActivity {

    private EditText editTextRecipeName, editTextIngredients, editTextInstructions, editTextDishType, editTextPreperation;
    private Button buttonSaveChanges;
    private String dishName, authorName, recipeID;
    private DatabaseReference recipeRef;
    private DatabaseReference newRecipeRef;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        editTextRecipeName = findViewById(R.id.editTextRecipeName2);
        editTextPreperation = findViewById(R.id.editTextPreparationTime2);
        editTextDishType = findViewById(R.id.editTextDishType2);
        editTextIngredients = findViewById(R.id.editTextIngredients2);
        editTextInstructions = findViewById(R.id.editTextInstructions2);
        buttonSaveChanges = findViewById(R.id.buttonSaveChanges);

        //Get name from intent extras
        dishName = getIntent().getStringExtra("dishName");

        //Get author from intent extras
        authorName = getIntent().getStringExtra("recipeAuthor");

        recipeRef = FirebaseDatabase.getInstance().getReference("Recipes").child(dishName);

        recipeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Recipes recipe = snapshot.getValue(Recipes.class);
                    if (recipe != null) {
                        editTextRecipeName.setText(recipe.getDishName());
                        editTextPreperation.setText(recipe.getPreparationTime());
                        editTextDishType.setText(recipe.getDishType());
                        editTextIngredients.setText(recipe.getIngredients());
                        editTextInstructions.setText(recipe.getInstructions());
                        recipeID = recipe.getID();
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
        recipeRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                String newName = editTextRecipeName.getText().toString().trim();
                String newPreparationTime = editTextPreperation.getText().toString().trim();
                String newDishType = editTextDishType.getText().toString().trim();
                String newIngredients = editTextIngredients.getText().toString().trim();
                String newInstructions = editTextInstructions.getText().toString().trim();

                newRecipeRef = FirebaseDatabase.getInstance().getReference("Recipes").child(newName);
                Recipes recipeNew = new Recipes(newName, newPreparationTime, newDishType, newIngredients, newInstructions, recipeID, authorName);

                newRecipeRef.setValue(recipeNew).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditRecipeActivity.this, "Successfully updated recipe", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to update new recipe:", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "ERROR 85 LINIJA KODA!", Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = new Intent(EditRecipeActivity.this, BrowseRecipeActivity.class);
        intent.putExtra("username", authorName);
        startActivity(intent);
    }

}
