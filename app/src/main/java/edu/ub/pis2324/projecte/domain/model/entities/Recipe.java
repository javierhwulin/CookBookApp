package edu.ub.pis2324.projecte.domain.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.PropertyName;

public class Recipe implements Parcelable {
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

    public Recipe() {}

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

    @PropertyName("isPremium")
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(Integer.toString(this.duration));
        dest.writeString(this.ingredients);
        dest.writeString(this.steps);
        dest.writeString(this.nutritionInfo);
        dest.writeString(this.imageUrl);
        dest.writeValue(this.isPremium);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.name = source.readString();
        this.ingredients = source.readString();
        this.description = source.readString();
        this.steps = source.readString();
        this.nutritionInfo = source.readString();
        this.duration = Integer.parseInt(source.readString());
        this.imageUrl = source.readString();
        this.isPremium = (Boolean) source.readValue(Boolean.class.getClassLoader());
    }

    protected Recipe(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.ingredients = in.readString();
        this.description = in.readString();
        this.steps = in.readString();
        this.nutritionInfo = in.readString();
        this.duration = Integer.parseInt(in.readString());
        this.imageUrl = in.readString();
        this.isPremium = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}

