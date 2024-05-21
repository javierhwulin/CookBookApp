package edu.ub.pis2324.projecte.domain.usecases;

import java.util.List;

import edu.ub.pis2324.projecte.domain.exceptions.AppError;
import edu.ub.pis2324.projecte.domain.model.entities.Recipe;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.domain.model.values.RecipeId;
import edu.ub.pis2324.projecte.domain.model.values.Record;
import io.reactivex.rxjava3.core.Observable;

public interface HistorialUsecase {
    //Observers
    Observable<Boolean> add(ClientId clientId, RecipeId recipeId);
    Observable<Boolean> remove(ClientId clientId, RecipeId recipeId);
    Observable<Boolean> clear(ClientId clientId);
    Observable<List<Recipe>> get(ClientId clientId);

    Observable<List<Recipe>> getRecipesByName(ClientId clientId, String recipeName);

    enum Error implements AppError {
        ADD_UNKNOWN_ERROR,
        REMOVE_UNKNOWN_ERROR,
        CLEAR_UNKNOWN_ERROR,
        GET_UNKNOWN_ERROR;
    }
}

