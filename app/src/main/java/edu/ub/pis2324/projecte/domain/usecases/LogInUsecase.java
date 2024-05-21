package edu.ub.pis2324.projecte.domain.usecases;

import edu.ub.pis2324.projecte.domain.exceptions.AppError;
import edu.ub.pis2324.projecte.domain.model.entities.User;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;

import io.reactivex.rxjava3.core.Observable;

public interface LogInUsecase {
    Observable<User> execute(ClientId username, String enteredPassword);
    enum Error implements AppError {
        USERNAME_EMPTY,
        PASSWORD_EMPTY,
        CLIENT_NOT_FOUND,
        PASSWORD_INCORRECT,
        CLIENTS_DATA_ACCESS_ERROR,

        USER__NOT_FOUND;
    }
}
