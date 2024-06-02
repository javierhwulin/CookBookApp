package edu.ub.pis2324.projecte.data.repositories;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import edu.ub.pis2324.projecte.domain.exceptions.AppThrowable;
import edu.ub.pis2324.projecte.domain.model.repositories.IRecipeRepository;
import edu.ub.pis2324.projecte.domain.model.entities.Recipe;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.domain.model.values.RecipeId;
import io.reactivex.rxjava3.core.Observable;

/**
 * Cloud Firestore implementation of the data store.
 */
public class RecipeRepository implements IRecipeRepository {
    private final FirebaseFirestore db;

    private static final String RECIPES_COLLECTION_NAME = "recipes";

    public RecipeRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public Observable<Recipe> getById(RecipeId id){
        return Observable.create(emitter -> {
            db.collection(RECIPES_COLLECTION_NAME)
                .document(id.toString())
                .get()
                .addOnFailureListener(exception -> {
                    emitter.onError(new AppThrowable(Error.GETBYID_UNKNOWN_ERROR));
                })
                .addOnSuccessListener(ds -> {
                    if (ds.exists()) {
                        Recipe recipe = ds.toObject(Recipe.class);
                        emitter.onNext(recipe);
                        emitter.onComplete();
                    } else {
                        emitter.onError(new AppThrowable(Error.RECIPE_NOT_FOUND));
                    }
                });
        });
    }

    /*
     * Agafa totes les receptes de la base de dades
     */
    public Observable<List<Recipe>> getAll(){
        return Observable.create(emitter -> {
            db.collection(RECIPES_COLLECTION_NAME).get()
                .addOnFailureListener(exception -> {
                    emitter.onError(new AppThrowable(Error.GETALL_UNKNOWN_ERROR));
                })
                .addOnSuccessListener(recipes -> {
                    List<DocumentSnapshot> documents = recipes.getDocuments();
                    List<Recipe> recipeList = new ArrayList<>();
                    for(DocumentSnapshot doc : documents){
                        Recipe recipe = doc.toObject(Recipe.class);
                        recipeList.add(recipe);
                    }
                    emitter.onNext(recipeList);
                    emitter.onComplete();
                });
        });
    }

    /*
     * Agafa totes les receptes de la base de dades que contenen part del String argument
     */
    public Observable<List<Recipe>> getByName(String recipeName){
        return Observable.create(emitter -> {
            db.collection(RECIPES_COLLECTION_NAME).get()
                .addOnFailureListener(exception -> {
                    emitter.onError(new AppThrowable(Error.GETBYNAME_UNKNOWN_ERROR));
                })
                .addOnSuccessListener(recipes -> {
                    List<DocumentSnapshot> documents = recipes.getDocuments();
                    List<Recipe> recipeList = new ArrayList<>();
                    for(DocumentSnapshot doc : documents){
                        Recipe recipe = doc.toObject(Recipe.class);
                        // Comprova si el nom de la recepta conté el nom de la recepta que es vol buscar
                        if (recipe.getName().toLowerCase().contains(recipeName.toLowerCase())) {
                            recipeList.add(recipe);
                        }
                    }
                    emitter.onNext(recipeList);
                    emitter.onComplete();
                });
        });
    }
}
