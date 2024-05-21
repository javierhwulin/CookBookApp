package edu.ub.pis2324.projecte.data.repositories;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
        Log.i("HistoryRepository", "add: " + clientId.toString() + " " + recipeId.toString());
        return Observable.create(emitter -> {
            db.collection(HISTORY_COLLECTION_NAME)
                    .whereEqualTo("clientId", clientId.toString())
                    .whereEqualTo("recipeId", recipeId.toString())
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Document already exists
                            Log.i("HistoryRepository", "Document with same clientId and recipeId already exists.");
                            emitter.onNext(false);
                            emitter.onComplete();
                        } else {
                            // Document does not exist, proceed to add
                            Map<String, Object> data = new HashMap<>();
                            data.put("clientId", clientId.toString());
                            data.put("recipeId", recipeId.toString());

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
        Log.i("HistoryRepository", "getAll");
        Log.i("HistoryRepository", "clientId: " + clientId.toString());

        return Observable.create(emitter -> {
            db.collection(HISTORY_COLLECTION_NAME)
                    .whereEqualTo("clientId", clientId.toString())
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        Log.i("HistoryRepository", "queryDocumentSnapshots: " + queryDocumentSnapshots.getDocuments().size());
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
                                    .subscribe(recipe -> {
                                        recipeList.add(recipe);
                                    }, emitter::onError, () -> {
                                        emitter.onNext(recipeList);
                                        emitter.onComplete();
                                    });
                        }
                    })
                    .addOnFailureListener(e -> emitter.onError(new AppThrowable((AppError) e)));
        });
    }
}
