package edu.ub.pis2324.projecte.domain.model.entities;

public class Recipe {
    private String id;
    private String name;
    private String description;
    private int duration;
    private String difficulty;
    private String category;
    private String ingredients;
    private String steps;
    private String nutritionInfo;
    private String imageUrl;

    public Recipe(String id, String name, String description, String imageUrl) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public Recipe(String id, String name, String description, int duration, String difficulty, String category, String ingredients, String steps, String nutritionInfo, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.difficulty = difficulty;
        this.category = category;
        this.ingredients = ingredients;
        this.steps = steps;
        this.nutritionInfo = nutritionInfo;
        this.imageUrl = imageUrl;
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

    public String getDifficulty() {
        return difficulty;
    }

    public String getCategory() {
        return category;
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

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
