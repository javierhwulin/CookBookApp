package edu.ub.pis2324.projecte.domain.usecases.implementation;

import edu.ub.pis2324.projecte.data.repositories.UserRepository;
import edu.ub.pis2324.projecte.domain.exceptions.AppThrowable;
import edu.ub.pis2324.projecte.domain.exceptions.AppThrowableMapper;
import edu.ub.pis2324.projecte.domain.model.entities.User;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.domain.services.CheckClientExistsService;
import edu.ub.pis2324.projecte.domain.usecases.SignUpUsecase;
import io.reactivex.rxjava3.core.Observable;

public class SignUpUsecaseImpl implements SignUpUsecase {
    private final UserRepository userRepository;
    private final AppThrowableMapper throwableMapper;
    private final CheckClientExistsService checkClientExistsService;

    public SignUpUsecaseImpl(CheckClientExistsService checkClientExistsService, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.checkClientExistsService = checkClientExistsService;
        this.throwableMapper = new AppThrowableMapper();
        throwableMapper.add(UserRepository.Error.ADD_UNKNOWN_ERROR, Error.CLIENTS_DATA_ACCESS_ERROR);

    }

    @Override
    public Observable<Boolean> execute(ClientId clientId, String username, String email, String password, String passwordConfirmation) {
        return checkUsernameNotEmpty(clientId)
                .concatMap(ignored -> checkEmailNotEmpty(email))
                .concatMap(ignored -> checkPasswordNotEmpty(password))
                .concatMap(ignored -> checkPasswordConfirmationNotEmpty(passwordConfirmation))
                .concatMap(ignored -> checkPasswordAndConfirmationMatch(password, passwordConfirmation))
                .concatMap(ignored -> checkClientExistsService.execute(clientId))
                .concatMap(clientExists -> {
                    if (clientExists) {
                        return Observable.error(new AppThrowable(Error.CLIENT_ALREADY_EXISTS));
                    } else {
                        User user = new User(clientId, username, password, email);
                        return userRepository.add(user);
                    }
                })
                .onErrorResumeNext(throwable -> Observable.error(throwableMapper.map(throwable)));
    }

    private Observable<Boolean> checkUsernameNotEmpty(ClientId clientId) {
        return clientId.toString().isEmpty() ? Observable.error(new AppThrowable(Error.USERNAME_EMPTY)) : Observable.just(true);
    }

    private Observable<Boolean> checkEmailNotEmpty(String email) {
        return email.isEmpty() ? Observable.error(new AppThrowable(Error.EMAIL_EMPTY)) : Observable.just(true);
    }

    private Observable<Boolean> checkPasswordNotEmpty(String password) {
        return password.isEmpty() ? Observable.error(new AppThrowable(Error.PASSWORD_EMPTY)) : Observable.just(true);
    }

    private Observable<Boolean> checkPasswordConfirmationNotEmpty(String passwordConfirmation) {
        return passwordConfirmation.isEmpty() ? Observable.error(new AppThrowable(Error.PASSWORD_CONFIRMATION_EMPTY)) : Observable.just(true);
    }

    private Observable<Boolean> checkPasswordAndConfirmationMatch(String password, String passwordConfirmation) {
        return password.equals(passwordConfirmation) ? Observable.just(true) : Observable.error(new AppThrowable(Error.PASSWORD_AND_CONFIRMATION_MISMATCH));
    }
}
