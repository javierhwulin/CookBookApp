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

    public interface OnGetRecipeListener {
        void OnGetRecipeSuccess(Recipe recipe);
        void OnGetRecipeError(Throwable throwable);
    }

    public interface OnGetAllRecipesListener {
        void OnGetAllRecipesSuccess(List<Recipe> recipes);
        void OnGetAllRecipesError(Throwable throwable);
    }


    public void getRecipe(String id, OnGetRecipeListener listener){
        // Add user to database)
        if (id.isEmpty()){
            listener.OnGetRecipeError(new Throwable("Username cannot be empty"));
        }


        db.collection("recipes").document(id).get()
                .addOnFailureListener(e -> listener.OnGetRecipeError(e))
                .addOnSuccessListener(rec -> {
                    if (!rec.exists()){
                        listener.OnGetRecipeError(new Throwable("Recipe does not exist"));
                    } else {
                        Recipe recipe = rec.toObject(Recipe.class);
                        listener.OnGetRecipeSuccess(recipe);
                    }
                });
    }

    public void getAll(OnGetAllRecipesListener listener) {
        db.collection("recipes").get()
                .addOnFailureListener(e -> listener.OnGetAllRecipesError(e))
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Recipe> recipes = queryDocumentSnapshots.toObjects(Recipe.class);
                    listener.OnGetAllRecipesSuccess(recipes);
                });
    }

}
