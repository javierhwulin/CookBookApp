package edu.ub.pis2324.projecte.data.services;


import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import edu.ub.pis2324.projecte.domain.model.entities.Recipe;


/**
 * Cloud Firestore implementation of the data store.
 */
public class RecipeListService {
    private final FirebaseFirestore db;

    public RecipeListService() {
        super();
        db = FirebaseFirestore.getInstance();
    }

    public interface OnFetchRecipesListener {
        void OnFetchRecipes(List<Recipe> recipes);
        void OnFetchRecipes(Throwable throwable);
    }


    public void getByName(String name, OnFetchRecipesListener listener) {
        db.collection("recipes").whereEqualTo("name", name).get()
                .addOnFailureListener(e -> listener.OnFetchRecipes(e))
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Recipe> recipes = queryDocumentSnapshots.toObjects(Recipe.class);
                    listener.OnFetchRecipes(recipes);
                });
    }

    public void getAll(OnFetchRecipesListener listener) {
        db.collection("recipes").get()
                .addOnFailureListener(e -> listener.OnFetchRecipes(e))
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Recipe> recipes = queryDocumentSnapshots.toObjects(Recipe.class);
                    listener.OnFetchRecipes(recipes);
                });
    }

}
