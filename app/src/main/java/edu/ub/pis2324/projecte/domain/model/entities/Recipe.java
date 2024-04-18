package edu.ub.pis2324.projecte.domain.model.entities;

public class Recipe {
    private String id;
    private String name;
    private String description;
    private int duration;
    private String ingredients;
    private String steps;
    private String nutritionInfo;
    private String imageUrl;
    private boolean isPremium;

    public Recipe(String id, String name, String description, String imageUrl, boolean isPremium) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.imageUrl = imageUrl;
        this.isPremium = isPremium;
    }

    public Recipe(String id, String name, String description, int duration, String ingredients, String steps, String nutritionInfo, String imageUrl, boolean isPremium) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.ingredients = ingredients;
        this.steps = steps;
        this.nutritionInfo = nutritionInfo;
        this.imageUrl = imageUrl;
        this.isPremium = isPremium;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getSteps() {
        return steps;
    }

    public String getNutritionInfo() {
        return nutritionInfo;
    }

    public int getDuration() {
        return duration;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public void setNutritionInfo(String nutritionInfo) {
        this.nutritionInfo = nutritionInfo;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setPremium(boolean isPremium) {
        this.isPremium = isPremium;
    }
}
