package com.example.recipeapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddRecipeFragment extends Fragment {

    private EditText editTextRecipeName, editTextPreparationTime, editTextDishType, editTextIngredients, editTextInstructions;
    private Button buttonAddRecipe, buttonCancel;
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private String recipeAuthor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_recipe, container, false);

        // Initialize views
        editTextRecipeName = view.findViewById(R.id.editTextRecipeName);
        editTextPreparationTime = view.findViewById(R.id.editTextPreparationTime);
        editTextDishType = view.findViewById(R.id.editTextDishType);
        editTextIngredients = view.findViewById(R.id.editTextIngredients);
        editTextInstructions = view.findViewById(R.id.editTextInstructions);
        buttonAddRecipe = view.findViewById(R.id.buttonAddRecipe);
        buttonCancel = view.findViewById(R.id.buttonCancel);

        Bundle args = getArguments();
        if (args != null) {
            recipeAuthor = args.getString("username");
        }

        // Set click listener for Add Recipe button
        buttonAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered recipe details
                String recipeName = editTextRecipeName.getText().toString().trim();
                String preparationTime = editTextPreparationTime.getText().toString().trim();
                String dishType = editTextDishType.getText().toString().trim();
                String ingredients = editTextIngredients.getText().toString().trim();
                String instructions = editTextInstructions.getText().toString().trim();

                // Validate recipe details
                if (recipeName.isEmpty() || preparationTime.isEmpty() || dishType.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }


                db = FirebaseDatabase.getInstance();
                ref = db.getReference("Recipes");

                ref.orderByChild("recipeName").equalTo(recipeName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Toast.makeText(getContext(), "Recipe already exists.", Toast.LENGTH_SHORT).show();
                        } else {
                            String recipeID = ref.push().getKey();
                            Recipes recipe = new Recipes(recipeName, preparationTime, dishType, ingredients, instructions, recipeID, recipeAuthor);

                            ref.child(recipeName).setValue(recipe).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Clear the input fields
                                        editTextRecipeName.setText("");
                                        editTextPreparationTime.setText("");
                                        editTextDishType.setText("");
                                        editTextIngredients.setText("");
                                        editTextInstructions.setText("");

                                        // Show a toast message indicating success
                                        Toast.makeText(getContext(), "Recipe added successfully!", Toast.LENGTH_SHORT).show();
                                        hideFragmentContainer();
                                        getActivity().getSupportFragmentManager().popBackStack();
                                    } else {
                                        Toast.makeText(getContext(), "Failed to add recipe!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        return view;
    }

    private void hideFragmentContainer() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hideFragmentContainer();
        }
    }
}

