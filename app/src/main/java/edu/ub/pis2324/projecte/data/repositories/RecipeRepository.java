package edu.ub.pis2324.projecte.data.repositories;


import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import edu.ub.pis2324.projecte.domain.model.repositories.IRecipeRepository;
import edu.ub.pis2324.projecte.domain.model.entities.Recipe;

/**
 * Cloud Firestore implementation of the data store.
 */
public class RecipeRepository implements IRecipeRepository {
    private final FirebaseFirestore db;

    public interface OnGetRecipeListener {
        void OnGetRecipeSuccess(Recipe recipe);
        void OnGetRecipeError(Throwable throwable);
    }

    public interface OnFetchRecipesListener {
        void OnFetchRecipes(List<Recipe> gottenRecipes);
        void OnFetchRecipes(Throwable throwable);
    }

    public RecipeRepository() {
        super();
        db = FirebaseFirestore.getInstance();
    }

    public void getRecipe(String recipe, OnGetRecipeListener listener){
        // Add user to database)
        if (recipe.isEmpty()) {
            listener.OnGetRecipeError(new Throwable("Recipe cannot be empty"));
        }

        db.collection("recipes").document(recipe).get()
            .addOnFailureListener(e -> listener.OnGetRecipeError(e))
            .addOnSuccessListener(rec -> {
                if (!rec.exists()){
                    listener.OnGetRecipeError(new Throwable("Recipe does not exist"));
                } else {
                    Recipe recipes = rec.toObject(Recipe.class);
                        listener.OnGetRecipeSuccess(recipes);
                }
            });
    }

    public void getAll(OnFetchRecipesListener listener){
        db.collection("recipes").get()
            .addOnFailureListener(e -> listener.OnFetchRecipes(e))
            .addOnSuccessListener(recipes -> {
                java.util.List<DocumentSnapshot> documents = recipes.getDocuments();
                ArrayList<Recipe> recipeList = new ArrayList<>();
                for(DocumentSnapshot doc : documents){
                    Recipe recipe = doc.toObject(Recipe.class);
                    recipeList.add(recipe);
                }
                listener.OnFetchRecipes(recipeList);
            });
    }

    public void getByName(String name, OnFetchRecipesListener listener){
        List<Recipe> recipeList = new ArrayList<>();
        db.collection("recipes").whereEqualTo("name", name).get()
            .addOnFailureListener(e -> System.out.println("Error getting documents: " + e))
            .addOnSuccessListener(recipes -> {
                java.util.List<DocumentSnapshot> documents = recipes.getDocuments();
                for(DocumentSnapshot doc : documents){
                    Recipe recipe = doc.toObject(Recipe.class);
                    recipeList.add(recipe);
                }
                listener.OnFetchRecipes(recipeList);
            });
    }
}
