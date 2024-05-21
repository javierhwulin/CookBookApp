package edu.ub.pis2324.projecte.domain.usecases;



import edu.ub.pis2324.projecte.domain.exceptions.AppError;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import io.reactivex.rxjava3.core.Observable;

public interface ChangePhotoUseCase {

    Observable<Boolean> execute(ClientId clientId,String url);

    enum Error implements AppError {
        CLIENTS_DATA_ACCESS_ERROR;
    }
}
