package edu.ub.pis2324.projecte.domain.usecases;

import edu.ub.pis2324.projecte.domain.exceptions.AppError;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import io.reactivex.rxjava3.core.Observable;

public interface SignUpUsecase {
    Observable<Boolean> execute(
            ClientId clientId,
            String username,
            String email,
            String password,
            String passwordConfirmation
    );

    enum Error implements AppError {
        USERNAME_EMPTY,
        EMAIL_EMPTY,
        PASSWORD_EMPTY,
        PASSWORD_CONFIRMATION_EMPTY,
        PASSWORD_AND_CONFIRMATION_MISMATCH,
        CLIENT_ALREADY_EXISTS,
        CLIENTS_DATA_ACCESS_ERROR
    }
}
