package com.example.recipeapp;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipes> recipeList;
    private Context context;
    private String currentUser;

    // Constructor to initialize the list of recipes
    public RecipeAdapter(List<Recipes> recipeList, Context context, String currentUser) {

        this.recipeList = recipeList;
        this.context = context;
        this.currentUser = currentUser;

    }

    // ViewHolder class to hold the views for each recipe item
    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView dishNameTextView;
        TextView preparationTimeTextView;
        TextView dishTypeTextView;
        TextView authorTextView;
        ImageButton imageButtonSettings;
        ImageView imageRecipe;



        public RecipeViewHolder(View itemView) {
            super(itemView);
            dishNameTextView = itemView.findViewById(R.id.textDishName);
            preparationTimeTextView = itemView.findViewById(R.id.textPreparationTime);
            dishTypeTextView = itemView.findViewById(R.id.textDishType);
            imageButtonSettings = itemView.findViewById(R.id.buttonSettings);
            imageRecipe = itemView.findViewById(R.id.imageRecipe);
        }
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each recipe item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        // Bind the data to the views for each recipe item
        Recipes recipe = recipeList.get(position);
        holder.dishNameTextView.setText(recipe.getDishName());
        holder.preparationTimeTextView.setText(recipe.getPreparationTime());
        holder.dishTypeTextView.setText(recipe.getDishType());

        // Check if the recipe's author matches the current user
        if (recipe.getRecipeAuthor().equals(currentUser)) {
            // Show the settings button
            holder.imageButtonSettings.setVisibility(View.VISIBLE);
        } else {
            // Hide the settings button
            holder.imageButtonSettings.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RecipeDetailsActivity.class);
                intent.putExtra("recipeName", recipe.getDishName());
                intent.putExtra("ingredients", recipe.getIngredients());
                intent.putExtra("instructions", recipe.getInstructions());
                intent.putExtra("recipeAuthor", recipe.getRecipeAuthor());
                context.startActivity(intent);
            }
        });

        // TODO: 3/26/2024  IMPLEMENT DELETE ITEM
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Recipes recipe = recipeList.get(position);

                if (recipe.getRecipeAuthor().equals(currentUser)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to delete this recipe?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            recipeList.remove(position);
                            notifyItemRemoved(position);

                            DatabaseReference recipeRef = FirebaseDatabase.getInstance().getReference("Recipes").child(recipe.getDishName());
                            recipeRef.removeValue();

                            Toast.makeText(context, "Recipe deleted successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("No", null);
                    builder.show();
                } else {
                    Toast.makeText(context, "You can only delete your own recipes!", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        holder.imageButtonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String recipeId = recipeList.get(position).getID();
                Intent intent = new Intent(context, EditRecipeActivity.class);
                intent.putExtra("dishName", recipe.getDishName());
                intent.putExtra("recipeAuthor", recipe.getRecipeAuthor());
                context.startActivity(intent);
            }
        });

        holder.imageRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        // Return the size of the recipe list
        return recipeList.size();
    }

    // Method to update the recipe list and notify adapter about data changes
    public void updateList(List<Recipes> newList) {
        recipeList.clear();
        recipeList.addAll(newList);
        notifyDataSetChanged();
    }

}
