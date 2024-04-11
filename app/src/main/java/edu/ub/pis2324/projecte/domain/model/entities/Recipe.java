package edu.ub.pis2324.projecte.domain.model.entities;

public class Recipe {
    private String id;
    private String name;
    private String description;
    private String imageUrl;

    public Recipe(String id, String name, String description, String imageUrl) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public Recipe() {
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
}
