package edu.ub.pis2324.projecte.domain.usecases;

import edu.ub.pis2324.projecte.domain.exceptions.AppError;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.domain.model.values.RecipeId;
import edu.ub.pis2324.projecte.domain.model.values.Record;
import io.reactivex.rxjava3.core.Observable;

public interface HistorialUsecase {
    //Observers
    Observable<Boolean> add(ClientId clientId);
    Observable<Boolean> remove(ClientId clientId, RecipeId recipeId);
    Observable<Boolean> clear(ClientId clientId);
    Observable<Record> get(ClientId clientId);

    enum Error implements AppError {
        ADD_UNKNOWN_ERROR,
        REMOVE_UNKNOWN_ERROR,
        CLEAR_UNKNOWN_ERROR,
        GET_UNKNOWN_ERROR;
    }
}

