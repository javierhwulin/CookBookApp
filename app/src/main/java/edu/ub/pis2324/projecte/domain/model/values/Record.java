package edu.ub.pis2324.projecte.domain.model.values;

import java.util.HashMap;

import edu.ub.pis2324.projecte.domain.model.entities.Recipe;

public class Record {
    private String userId;
    private HashMap<String, Recipe> recipes;

    public Record(String userId) {
        this.userId = userId;
        this.recipes = new HashMap<>();
    }

    public void addRecipe(Recipe recipe) {
        recipes.put(recipe.getId(), recipe);
    }

    public Recipe getRecipe(String recipeId) {
        return recipes.get(recipeId);
    }

    public void removeRecipe(String recipeId) {
        recipes.remove(recipeId);
    }

    public void clear() {
        recipes.clear();
    }
}
