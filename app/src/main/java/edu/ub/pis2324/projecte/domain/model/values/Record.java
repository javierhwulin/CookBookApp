package edu.ub.pis2324.projecte.domain.model.values;

import java.util.HashMap;

import edu.ub.pis2324.projecte.domain.model.entities.Recipe;
import edu.ub.pis2324.projecte.domain.model.entities.User;

public class Record {
    private User user;
    private HashMap<String, Recipe> recipes;

    public Record(User user) {
        this.user = user;
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

    public boolean isEmpty() {
        return recipes.isEmpty();
    }
}
