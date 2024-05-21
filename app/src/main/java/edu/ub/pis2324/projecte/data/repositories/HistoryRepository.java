package edu.ub.pis2324.projecte.data.repositories;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ub.pis2324.projecte.domain.exceptions.AppError;
import edu.ub.pis2324.projecte.domain.exceptions.AppThrowable;
import edu.ub.pis2324.projecte.domain.model.entities.Recipe;
import edu.ub.pis2324.projecte.domain.model.repositories.IHistoryRepository;
import edu.ub.pis2324.projecte.domain.model.repositories.IRecipeRepository;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.domain.model.values.RecipeId;
import io.reactivex.rxjava3.core.Observable;


public class HistoryRepository implements IHistoryRepository {
    private final FirebaseFirestore db;
    private final IRecipeRepository recipeRepository;
    private static final String HISTORY_COLLECTION_NAME = "history";
    public HistoryRepository(IRecipeRepository recipeRepository) {
        db = FirebaseFirestore.getInstance();
        this.recipeRepository = recipeRepository;
    }

    public Observable<Boolean> add(ClientId clientId, RecipeId recipeId) {
        Log.i("HistoryRepository", "addOrUpdate: " + clientId.toString() + " " + recipeId.toString());
        return Observable.create(emitter -> {
            db.collection(HISTORY_COLLECTION_NAME)
                    .whereEqualTo("clientId", clientId.toString())
                    .whereEqualTo("recipeId", recipeId.toString())
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Document already exists, update the timestamp
                            Log.i("HistoryRepository", "Document with same clientId and recipeId already exists. Updating timestamp.");
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            documentSnapshot.getReference().update("timestamp", FieldValue.serverTimestamp())
                                    .addOnSuccessListener(aVoid -> {
                                        emitter.onNext(true);
                                        emitter.onComplete();
                                    })
                                    .addOnFailureListener(emitter::onError);
                        } else {
                            // Document does not exist, proceed to add
                            Map<String, Object> data = new HashMap<>();
                            data.put("clientId", clientId.toString());
                            data.put("recipeId", recipeId.toString());
                            data.put("timestamp", FieldValue.serverTimestamp());

                            db.collection(HISTORY_COLLECTION_NAME)
                                    .add(data)
                                    .addOnSuccessListener(documentReference -> {
                                        Log.i("HistoryRepository", "Document added with ID: " + documentReference.getId());
                                        emitter.onNext(true);
                                        emitter.onComplete();
                                    })
                                    .addOnFailureListener(emitter::onError);
                        }
                    })
                    .addOnFailureListener(emitter::onError);
        });
    }


    public Observable<List<Recipe>> getAll(ClientId clientId) {
        Log.i("HistoryRepository", "getHistoryByRecent: " + clientId.toString());
        return Observable.create(emitter -> {
            db.collection(HISTORY_COLLECTION_NAME)
                    .whereEqualTo("clientId", clientId.toString())
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<Recipe> recipeList = new ArrayList<>();
                        List<Observable<Recipe>> observables = new ArrayList<>();

                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            String recipeId = doc.getString("recipeId");
                            Log.i("HistoryRepository", "recipeId: " + recipeId);
                            observables.add(recipeRepository.getById(new RecipeId(recipeId)));
                        }

                        if (observables.isEmpty()) {
                            emitter.onNext(recipeList);
                            emitter.onComplete();
                        } else {
                            Observable.merge(observables)
                                    .subscribe(recipeList::add, emitter::onError, () -> {
                                        emitter.onNext(recipeList);
                                        emitter.onComplete();
                                    });
                        }
                    })
                    .addOnFailureListener(emitter::onError);
        });
    }

    public Observable<List<Recipe>> getRecipesByName(ClientId clientId, String recipeName) {
        return getAll(clientId)
                .map(recipes -> {
                    List<Recipe> filteredRecipes = new ArrayList<>();
                    for (Recipe recipe : recipes) {
                        if (recipe.getName().toLowerCase().contains(recipeName.toLowerCase())) {
                            filteredRecipes.add(recipe);
                        }
                    }
                    return filteredRecipes;
                });
    }
}
