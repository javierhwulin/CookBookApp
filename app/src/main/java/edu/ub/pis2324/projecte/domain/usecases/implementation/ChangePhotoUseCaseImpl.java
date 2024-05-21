package edu.ub.pis2324.projecte.domain.usecases.implementation;

import android.util.Log;

import edu.ub.pis2324.projecte.data.repositories.UserRepository;
import edu.ub.pis2324.projecte.domain.exceptions.AppThrowable;
import edu.ub.pis2324.projecte.domain.exceptions.AppThrowableMapper;
import edu.ub.pis2324.projecte.domain.model.entities.User;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.domain.usecases.ChangePhotoUseCase;
import edu.ub.pis2324.projecte.domain.usecases.FetchClientUsecase;
import io.reactivex.rxjava3.core.Observable;

public class ChangePhotoUseCaseImpl implements ChangePhotoUseCase{
    
    private final AppThrowableMapper throwableMapper;
    private final FetchClientUsecase fetchClientUseCase;

    private final UserRepository userRepository;

    public ChangePhotoUseCaseImpl(FetchClientUsecase fetchClientUseCase) {
        this.fetchClientUseCase = fetchClientUseCase;
        throwableMapper = new AppThrowableMapper();
        throwableMapper.add(UserRepository.Error.USER_NOT_FOUND, ChangePhotoUseCase.Error.CLIENTS_DATA_ACCESS_ERROR);
        userRepository = new UserRepository();
    }

    @Override
    public Observable<Boolean> execute(ClientId clientId, String url) {

        return checkIdEmpty(clientId)
                .concatMap(ignored -> changePhoto(clientId, url))
                .onErrorResumeNext(throwable -> Observable.error(throwableMapper.map(throwable)));

    }

    private Observable<Boolean> changePhoto(ClientId clientId,String url) {
        return userRepository.changePhoto(clientId, url);
    }
    private Observable<Boolean> checkIdEmpty(ClientId clientId) {
        if (clientId == null || clientId.toString().isEmpty()) {
            return Observable.error(new AppThrowable(ChangePhotoUseCase.Error.CLIENTS_DATA_ACCESS_ERROR));
        }
        return Observable.just(true);
    }
}
