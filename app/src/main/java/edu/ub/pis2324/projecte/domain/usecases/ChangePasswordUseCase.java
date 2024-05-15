package edu.ub.pis2324.projecte.domain.usecases;



import edu.ub.pis2324.projecte.domain.exceptions.AppError;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import io.reactivex.rxjava3.core.Observable;

public interface ChangePasswordUseCase {

    Observable<Boolean> execute(ClientId clientId,String oldPassword ,String newPassword, String confirmPassword);

    enum Error implements AppError {
        PASSWORD_EMPTY,
        CLIENTS_DATA_ACCESS_ERROR,
        PASSWORD_INCORRECT,
        PASSWORD_DONT_MATCH;
    }
}
