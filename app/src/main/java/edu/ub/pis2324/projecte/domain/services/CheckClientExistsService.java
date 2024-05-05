package edu.ub.pis2324.projecte.domain.services;

import edu.ub.pis2324.projecte.domain.exceptions.AppError;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import io.reactivex.rxjava3.core.Observable;

public interface CheckClientExistsService {
    Observable<Boolean> execute(ClientId clientId);

    enum Error implements AppError {
        CLIENT_NOT_FOUND,
        CLIENTS_DATA_ACCESS_ERROR
    }
}
