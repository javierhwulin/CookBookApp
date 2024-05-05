package edu.ub.pis2324.projecte.domain.usecases.implementation;

import edu.ub.pis2324.projecte.data.repositories.UserRepository;
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
        return userRepository.getById(clientId)
                .onErrorResumeNext(throwable -> Observable.error(throwableMapper.map(throwable)));
    }
}
