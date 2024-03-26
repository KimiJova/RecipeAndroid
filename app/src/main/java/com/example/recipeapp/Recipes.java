package com.example.recipeapp;

public class Recipes {
    private String dishName;
    private String preparationTime;
    private String dishType;
    private String ingredients;
    private String instructions;
    private String ID;
    private String recipeAuthor;

    public Recipes() {
    }

    public Recipes(String dishName, String preparationTime, String dishType, String ingredients, String instructions, String ID, String recipeAuthor) {
        this.dishName = dishName;
        this.preparationTime = preparationTime;
        this.dishType = dishType;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.ID = ID;
        this.recipeAuthor = recipeAuthor;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(String preparationTime) {
        this.preparationTime = preparationTime;
    }

    public String getDishType() {
        return dishType;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setDishType(String dishType) {
        this.dishType = dishType;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getRecipeAuthor() {
        return recipeAuthor;
    }

    public void setRecipeAuthor(String recipeAuthor) {
        this.recipeAuthor = recipeAuthor;
    }
}
