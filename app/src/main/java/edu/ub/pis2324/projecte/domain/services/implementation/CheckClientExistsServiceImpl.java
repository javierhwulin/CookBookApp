package edu.ub.pis2324.projecte.domain.services.implementation;

import edu.ub.pis2324.projecte.data.repositories.UserRepository;
import edu.ub.pis2324.projecte.domain.exceptions.AppError;
import edu.ub.pis2324.projecte.domain.exceptions.AppThrowable;
import edu.ub.pis2324.projecte.domain.exceptions.AppThrowableMapper;
import edu.ub.pis2324.projecte.domain.model.repositories.IUserRepository;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.domain.services.CheckClientExistsService;
import io.reactivex.rxjava3.core.Observable;

public class CheckClientExistsServiceImpl implements CheckClientExistsService{
    private final UserRepository userRepository;
    private final AppThrowableMapper throwableMapper;

    public CheckClientExistsServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
        this.throwableMapper = new AppThrowableMapper();
        throwableMapper.add(IUserRepository.Error.GETBYID_UNKNOWN_ERROR, Error.CLIENTS_DATA_ACCESS_ERROR);
    }

    @Override
    public Observable<Boolean> execute(ClientId clientId) {
        return userRepository.getById(clientId)
            .concatMap(ignored -> Observable.just(true))
            .onErrorResumeNext(throwable -> {
                if (throwable instanceof AppThrowable) {
                    AppError xError = ((AppThrowable) throwable).getError();
                    if (xError == UserRepository.Error.USER_NOT_FOUND)
                        return Observable.just(false);
                    else
                        return Observable.error(throwableMapper.map(throwable));
                } else {
                    return Observable.error(throwable);
                }
            });
    }
}

