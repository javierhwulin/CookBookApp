package edu.ub.pis2324.projecte.domain.usecases.implementation;

import edu.ub.pis2324.projecte.data.repositories.UserRepository;
import edu.ub.pis2324.projecte.domain.exceptions.AppThrowable;
import edu.ub.pis2324.projecte.domain.exceptions.AppThrowableMapper;
import edu.ub.pis2324.projecte.domain.model.entities.User;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.domain.usecases.FetchClientUsecase;
import edu.ub.pis2324.projecte.domain.usecases.LogInUsecase;
import io.reactivex.rxjava3.core.Observable;

public class LogInUsecaseImpl implements LogInUsecase {
    private final AppThrowableMapper throwableMapper;
    private final FetchClientUsecase fetchClientUseCase;

    public LogInUsecaseImpl(UserRepository userRepository, FetchClientUsecase fetchClientUseCase){
        this.fetchClientUseCase = fetchClientUseCase;
        this.throwableMapper = new AppThrowableMapper();
        throwableMapper.add(UserRepository.Error.USER_NOT_FOUND, Error.CLIENT_NOT_FOUND);
    }

    @Override
    public Observable<User> execute(ClientId clientId, String enteredPassword) {
        return checkUsernameNotEmpty(clientId)
                .concatMap(ignored -> checkPasswordNotEmpty(enteredPassword))
                .concatMap(ignored -> fetchClientUseCase.execute(clientId))
                .concatMap(fetchedClient -> checkPasswordIsCorrect(fetchedClient, enteredPassword)
                        .concatMap(ignored -> Observable.just(fetchedClient))
                )
                .onErrorResumeNext(throwable -> Observable.error(throwableMapper.map(throwable)));
    }

    private Observable<Boolean> checkUsernameNotEmpty(ClientId clientId) {
        return clientId.toString().isEmpty() ? Observable.error(new AppThrowable(Error.USERNAME_EMPTY)) : Observable.just(true);
    }

    private Observable<Boolean> checkPasswordNotEmpty(String enteredPassword) {
        return enteredPassword.isEmpty() ? Observable.error(new AppThrowable(Error.PASSWORD_EMPTY)) : Observable.just(true);
    }

    private Observable<Boolean> checkPasswordIsCorrect(User user, String enteredPassword) {
        return user.getPassword().equals(enteredPassword) ? Observable.just(true) : Observable.error(new AppThrowable(Error.PASSWORD_INCORRECT));
    }
}
