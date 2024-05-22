package edu.ub.pis2324.projecte.domain.usecases.implementation;



import edu.ub.pis2324.projecte.data.repositories.UserRepository;
import edu.ub.pis2324.projecte.domain.exceptions.AppThrowable;
import edu.ub.pis2324.projecte.domain.exceptions.AppThrowableMapper;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.domain.usecases.ChangePasswordUseCase;
import edu.ub.pis2324.projecte.domain.usecases.FetchClientUsecase;
import io.reactivex.rxjava3.core.Observable;

public class ChangePasswordUseCaseImpl implements ChangePasswordUseCase {
    private final AppThrowableMapper throwableMapper;
    private final FetchClientUsecase fetchClientUseCase;

    private final UserRepository userRepository;

    public ChangePasswordUseCaseImpl(FetchClientUsecase fetchClientUseCase, UserRepository userRepository) {
        this.fetchClientUseCase = fetchClientUseCase;
        throwableMapper = new AppThrowableMapper();
        throwableMapper.add(UserRepository.Error.USER_NOT_FOUND, ChangePasswordUseCase.Error.CLIENTS_DATA_ACCESS_ERROR);
        this.userRepository = userRepository;
    }

    @Override
    public Observable<Boolean> execute(ClientId clientId,String oldPassword ,String newPassword, String confirmPassword) {

        return checkIdEmpty(clientId)
                .concatMap(ignored -> checkPasswordNotEmpty(newPassword))
                .concatMap(ignored -> checkPasswordNotEmpty(oldPassword))
                .concatMap(ignored -> checkPasswordIsCorrect(clientId, oldPassword))
                .concatMap(ignored -> checkPasswordAndConfirmationMatch(newPassword, confirmPassword))
                .concatMap(ignored -> changePassword(clientId, newPassword))
                .onErrorResumeNext(throwable -> Observable.error(throwableMapper.map(throwable)));

    }

    private Observable<Boolean> changePassword(ClientId clientId,String newPassword) {
        return userRepository.changePassword(clientId, newPassword);
    }
    private Observable<Boolean> checkIdEmpty(ClientId clientId) {
        if (clientId == null || clientId.toString().isEmpty()) {
            return Observable.error(new AppThrowable(Error.PASSWORD_EMPTY));
        }
        return Observable.just(true);
    }

    private Observable<Boolean> checkPasswordNotEmpty(String newPassword) {
        if (newPassword == null || newPassword.isEmpty()) {
            return Observable.error(new AppThrowable(Error.PASSWORD_EMPTY));
        }
        return Observable.just(true);
    }

    private Observable<Boolean> checkPasswordIsCorrect(ClientId clientId, String oldPassword) {
        return userRepository.getById(clientId)
                .map(user -> user.getPassword().equals(oldPassword))
                .flatMap(isCorrect -> {
                    if (!isCorrect) {
                        return Observable.error(new AppThrowable(Error.PASSWORD_INCORRECT));
                    }
                    return Observable.just(true);
                });
    }

    private Observable<Boolean> checkPasswordAndConfirmationMatch(String newPassword, String confirmPassword) {
        return newPassword.equals(confirmPassword) ? Observable.just(true) : Observable.error(new AppThrowable(Error.PASSWORD_DONT_MATCH));
    }
}
