package edu.ub.pis2324.projecte.data.services;


import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import edu.ub.pis2324.projecte.domain.IRecipeListService;
import edu.ub.pis2324.projecte.domain.model.entities.Recipe;

/**
 * Cloud Firestore implementation of the data store.
 */
public class RecipeListService implements IRecipeListService {
    private final FirebaseFirestore db;

    public interface OnGetRecipeListener {
        void OnGetRecipeSuccess(Recipe recipe);
        void OnGetRecipeError(Throwable throwable);
    }

    public interface OnGetAllRecipesListener {
        void OnGetAllRecipesSuccess(ArrayList<Recipe> recipes);
        void OnGetAllRecipesError(Throwable throwable);
    }

    public RecipeListService() {
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

    public void getAllRecipe(OnGetAllRecipesListener listener){
        db.collection("recipes").get()
            .addOnFailureListener(e -> listener.OnGetAllRecipesError(e))
            .addOnSuccessListener(recipes -> {
                java.util.List<DocumentSnapshot> documents = recipes.getDocuments();
                ArrayList<Recipe> recipeList = new ArrayList<>();
                for(DocumentSnapshot doc : documents){
                    Recipe recipe = doc.toObject(Recipe.class);
                    recipeList.add(recipe);
                }
                listener.OnGetAllRecipesSuccess(recipeList);
            });
    }
}
