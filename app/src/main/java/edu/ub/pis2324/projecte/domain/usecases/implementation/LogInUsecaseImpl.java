package edu.ub.pis2324.projecte.domain.usecases.implementation;

import android.util.Log;

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

    public LogInUsecaseImpl(FetchClientUsecase fetchClientUseCase){
        this.fetchClientUseCase = fetchClientUseCase;
        this.throwableMapper = new AppThrowableMapper();
        throwableMapper.add(UserRepository.Error.USER_NOT_FOUND, Error.CLIENT_NOT_FOUND);
    }

    @Override
    public Observable<User> execute(ClientId clientId, String enteredPassword) {
        Log.e("LogInUsecaseImpl", "execute");
        if(clientId == null || clientId.toString().isEmpty()) {
            Log.e("LogInUsecaseImpl", "Client id is empty");
        }else if(enteredPassword == null || enteredPassword.isEmpty()){
            Log.e("LogInUsecaseImpl", "Password is empty");
        }
        Log.i("LogInUsecaseImpl", "Client id: " + clientId.toString() + " Password: " + enteredPassword);
        return checkUsernameNotEmpty(clientId)
                .concatMap(ignored -> checkPasswordNotEmpty(enteredPassword))
                .concatMap(ignored -> fetchClientUseCase.execute(clientId))
                .concatMap(fetchedClient -> checkPasswordIsCorrect(fetchedClient, enteredPassword)
                        .concatMap(ignored -> Observable.just(fetchedClient))
                )
                .onErrorResumeNext(throwable -> Observable.error(throwableMapper.map(throwable)));
    }

    private Observable<Boolean> checkUsernameNotEmpty(ClientId clientId) {
        Log.e("LogInUsecaseImpl", "checkUsernameNotEmpty");
        if (clientId == null || clientId.toString().isEmpty()) {
            return Observable.error(new AppThrowable(Error.USERNAME_EMPTY));
        }
        return Observable.just(true);
    }

    private Observable<Boolean> checkPasswordNotEmpty(String enteredPassword) {
        Log.e("LogInUsecaseImpl", "checkPasswordNotEmpty");
        if (enteredPassword == null || enteredPassword.isEmpty()) {
            return Observable.error(new AppThrowable(Error.PASSWORD_EMPTY));
        }
        return Observable.just(true);
    }

    private Observable<Boolean> checkPasswordIsCorrect(User user, String enteredPassword) {
        Log.i("LogInUsecaseImpl", "checkPasswordIsCorrect");
        if (user == null || user.getPassword() == null || !user.getPassword().equals(enteredPassword)) {
            Log.e("LogInUsecaseImpl", "Password is incorrect");
            return Observable.error(new AppThrowable(Error.PASSWORD_INCORRECT));
        }
        Log.i("LogInUsecaseImpl", "Password is correct");
        Log.i("LogInUsecaseImpl", "User: " + user.getUsername());
        return Observable.just(true);
    }
}
