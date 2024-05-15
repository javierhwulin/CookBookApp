package edu.ub.pis2324.projecte.domain.usecases;

import edu.ub.pis2324.projecte.domain.exceptions.AppError;
import edu.ub.pis2324.projecte.domain.model.entities.User;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import io.reactivex.rxjava3.core.Observable;

public interface ChangeUsernameUseCase {
    Observable<User> execute(ClientId clientId, String newUsername);

    enum Error implements AppError {
        USERNAME_EMPTY,
        CLIENTS_DATA_ACCESS_ERROR;
    }
}
