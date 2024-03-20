package com.example.recipeapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipes> recipeList;

    // Constructor to initialize the list of recipes
    public RecipeAdapter(List<Recipes> recipeList) {
        this.recipeList = recipeList;
    }

    // ViewHolder class to hold the views for each recipe item
    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView dishNameTextView;
        TextView preparationTimeTextView;
        TextView dishTypeTextView;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            dishNameTextView = itemView.findViewById(R.id.textDishName);
            preparationTimeTextView = itemView.findViewById(R.id.textPreparationTime);
            dishTypeTextView = itemView.findViewById(R.id.textDishType);
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
