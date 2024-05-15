package edu.ub.pis2324.projecte.domain.usecases.implementation;

import android.util.Log;

import edu.ub.pis2324.projecte.data.repositories.UserRepository;
import edu.ub.pis2324.projecte.domain.exceptions.AppThrowable;
import edu.ub.pis2324.projecte.domain.exceptions.AppThrowableMapper;
import edu.ub.pis2324.projecte.domain.model.entities.User;
import edu.ub.pis2324.projecte.domain.model.repositories.IUserRepository;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.domain.usecases.FetchClientUsecase;
import io.reactivex.rxjava3.core.Observable;

public class FetchClientUsecaseImpl implements FetchClientUsecase {
    private final UserRepository userRepository;
    private final AppThrowableMapper throwableMapper;

    public FetchClientUsecaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.throwableMapper = new AppThrowableMapper();
        throwableMapper.add(UserRepository.Error.USER_NOT_FOUND, Error.CLIENT_NOT_FOUND);
        throwableMapper.add(IUserRepository.Error.GETBYID_UNKNOWN_ERROR, Error.CLIENTS_DATA_ACCESS_ERROR);
    }

    @Override
    public Observable<User> execute(ClientId clientId) {
        Log.i("FetchClientUsecaseImpl", "execute");
        if(clientId == null || clientId.toString().isEmpty()){
            Log.e("FetchClientUsecaseImpl", "Client id is empty");
            return Observable.error(throwableMapper.map(new AppThrowable(Error.CLIENT_NOT_FOUND)));
        }
        Log.i("FetchClientUsecaseImpl", "Client id: " + clientId.toString());
        if(userRepository.getById(clientId) == null){
            Log.e("FetchClientUsecaseImpl", "Client not found");
            return Observable.error(throwableMapper.map(new AppThrowable(Error.CLIENTS_DATA_ACCESS_ERROR)));
        }
        Log.i("FetchClientUsecaseImpl", "Client id: " + clientId.toString());
        return userRepository.getById(clientId)
                .onErrorResumeNext(throwable -> Observable.error(throwableMapper.map(throwable)));
    }
}
