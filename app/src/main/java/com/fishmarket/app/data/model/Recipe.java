package com.fishmarket.app.data.model;

public class Recipe {
    private String name;
    private String description;
    private String imageUrl;
    private String recipeUrl;

    public Recipe(String name, String description, String imageUrl, String recipeUrl) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.recipeUrl = recipeUrl;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public String getRecipeUrl() { return recipeUrl; }
}
