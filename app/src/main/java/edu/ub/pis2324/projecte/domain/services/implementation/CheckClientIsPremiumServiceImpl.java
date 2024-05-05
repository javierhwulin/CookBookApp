package edu.ub.pis2324.projecte.domain.services.implementation;

import edu.ub.pis2324.projecte.data.repositories.UserRepository;
import edu.ub.pis2324.projecte.domain.exceptions.AppError;
import edu.ub.pis2324.projecte.domain.exceptions.AppThrowable;
import edu.ub.pis2324.projecte.domain.exceptions.AppThrowableMapper;
import edu.ub.pis2324.projecte.domain.model.repositories.IUserRepository;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.domain.services.CheckClientIsPremiumService;
import io.reactivex.rxjava3.core.Observable;

public class CheckClientIsPremiumServiceImpl implements CheckClientIsPremiumService {
    private final UserRepository userRepository;
    private final AppThrowableMapper throwableMapper;

    public CheckClientIsPremiumServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.throwableMapper = new AppThrowableMapper();
        throwableMapper.add(IUserRepository.Error.GETBYID_UNKNOWN_ERROR, Error.CLIENTS_DATA_ACCESS_ERROR);
    }

    @Override
    public Observable<Boolean> execute(ClientId clientId) {
        return userRepository.getById(clientId)
            .concatMap(user -> Observable.just(user.isPremium())
            .onErrorResumeNext(throwable -> {
                if (throwable instanceof AppThrowable) {
                    AppError error = ((AppThrowable) throwable).getError();
                    if (error == UserRepository.Error.USER_NOT_FOUND)
                        return Observable.just(false);
                    else
                        return Observable.error(throwableMapper.map(throwable));
                } else {
                    return Observable.error(throwable);
                }
            }));
    }
}
