package edu.ub.pis2324.projecte.domain.model.repositories;

import java.util.List;

import edu.ub.pis2324.projecte.domain.exceptions.AppError;
import edu.ub.pis2324.projecte.domain.model.entities.Recipe;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import io.reactivex.rxjava3.core.Observable;

public interface IHistoryRepository {
    Observable<List<Recipe>> getAll(ClientId clientId);

    enum Error implements AppError {
        GETALL_UNKNOWN_ERROR
    }
}
