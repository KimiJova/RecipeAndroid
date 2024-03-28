package com.example.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BrowseRecipeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    private List<Recipes> recipeList;
    private ImageButton homeButton;
    private ImageButton refreshButton;
    private EditText editTextFilter;
    private String username;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private DatabaseReference recipesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_recipe);

        recyclerView = findViewById(R.id.recyclerViewRecipes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        username = getIntent().getStringExtra("username");

        recipeList = new ArrayList<>();
        adapter = new RecipeAdapter(recipeList, this, username);
        recyclerView.setAdapter(adapter);

        homeButton = findViewById(R.id.btn_home);
        refreshButton = findViewById(R.id.btn_refresh);
        editTextFilter = findViewById(R.id.editTextFilter);




        recipesRef = FirebaseDatabase.getInstance().getReference("Recipes");

        // Retrieve and display all recipes initially
        fetchRecipes();

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHome = new Intent(BrowseRecipeActivity.this, MainActivity.class);
                intentHome.putExtra("username", username);
                startActivity(intentHome);
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to refresh the RecyclerView
                refreshRecyclerView();
            }
        });

        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterRecipes(s.toString().trim().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Not used
            }
        });
    }

    // Method to fetch recipes from Firebase using ExecutorService
    private void fetchRecipes() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                recipesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Recipes> fetchedRecipes = new ArrayList<>();
                        for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                            fetchedRecipes.add(recipeSnapshot.getValue(Recipes.class));
                        }
                        updateRecipeList(fetchedRecipes);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        handleDatabaseError(databaseError);
                    }
                });
            }
        });
    }

     //Method to filter recipes based on the dish name
    private void filterRecipes(String query) {
        List<Recipes> filteredList = new ArrayList<>();
        if (query.isEmpty()) {
            filteredList.addAll(recipeList); // Show all recipes if query is empty
        } else {
            for (Recipes recipe : recipeList) {
                if (recipe.getDishName().toLowerCase().contains(query)) {
                    filteredList.add(recipe);
                }
            }
        }
        adapter.updateList(filteredList);
    }

    // Method to update the RecyclerView with fetched recipes
    private void updateRecipeList(List<Recipes> fetchedRecipes) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recipeList.clear();
                recipeList.addAll(fetchedRecipes);
                adapter.notifyDataSetChanged();
            }
        });
    }

    // Method to handle database errors
    private void handleDatabaseError(DatabaseError databaseError) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BrowseRecipeActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to refresh the RecyclerView with all recipes
    private void refreshRecyclerView() {
        fetchRecipes();
        // Clear the filter text
        editTextFilter.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}