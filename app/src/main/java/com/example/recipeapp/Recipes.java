package com.example.recipeapp;

public class Recipes {
    private String dishName;
    private String preparationTime;
    private String dishType;

    public Recipes() {}

    public Recipes(String dishName, String preparationTime, String dishType) {
        this.dishName = dishName;
        this.preparationTime = preparationTime;
        this.dishType = dishType;
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

    public void setDishType(String dishType) {
        this.dishType = dishType;
    }
}
