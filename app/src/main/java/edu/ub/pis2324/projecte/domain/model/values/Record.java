package edu.ub.pis2324.projecte.domain.model.values;

import java.util.HashMap;
import java.util.List;

import edu.ub.pis2324.projecte.domain.model.entities.Recipe;

public class Record {
    private final ClientId id;
    private final HashMap<RecipeId, Recipe> recipes;

    public Record(ClientId id) {
        this.recipes = new HashMap<>();
        this.id = id;
    }

    public List<Recipe> getAllRecipes(){
        return (List<Recipe>) recipes.values();
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
