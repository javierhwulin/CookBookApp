package edu.ub.pis2324.projecte.domain.usecases.implementation;

import android.util.Log;

import edu.ub.pis2324.projecte.data.repositories.UserRepository;
import edu.ub.pis2324.projecte.domain.exceptions.AppThrowable;
import edu.ub.pis2324.projecte.domain.exceptions.AppThrowableMapper;
import edu.ub.pis2324.projecte.domain.model.entities.User;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.domain.usecases.ChangeUsernameUseCase;
import edu.ub.pis2324.projecte.domain.usecases.FetchClientUsecase;
import edu.ub.pis2324.projecte.domain.usecases.LogInUsecase;
import io.reactivex.rxjava3.core.Observable;

public class ChangeUsernameUseCaseImpl implements ChangeUsernameUseCase{

    private final AppThrowableMapper throwableMapper;
    private final FetchClientUsecase fetchClientUseCase;

    private final UserRepository userRepository;
    public ChangeUsernameUseCaseImpl(FetchClientUsecase fetchClientUseCase) {
        this.fetchClientUseCase = fetchClientUseCase;
        this.throwableMapper = new AppThrowableMapper();
        throwableMapper.add(UserRepository.Error.USER_NOT_FOUND, LogInUsecase.Error.CLIENT_NOT_FOUND);
        userRepository = new UserRepository();
    }

    @Override
    public Observable<User> execute(ClientId clientId, String newUsername) {
        Log.e("UsernameUseCaseImpl", "execute");
        if(clientId == null || clientId.toString().isEmpty()) {
            Log.e("UsernameUseCaseImpl", "Client id is empty");
        }else if(newUsername == null || newUsername.isEmpty()){
            Log.e("UsernameUseCaseImpl", "Username is empty");
        }
        return checkIdEmpty(clientId)
                .concatMap(ignored -> checkUsernameNotEmpty(newUsername))
                .concatMap(ignored -> fetchClientUseCase.execute(clientId))
                .concatMap(fetchedClient -> changeUsername(fetchedClient, newUsername)
                        .concatMap(ignored -> Observable.just(fetchedClient))
                )
                .onErrorResumeNext(throwable -> Observable.error(throwableMapper.map(throwable)));
    }

    private Observable<User> changeUsername(User fetchedClient, String newUsername) {
        Log.i("UsernameUseCaseImpl", "changeUsername");
        //TODO: DO WITH ID NOT USERNAME??
        return userRepository.changeUsername(new ClientId(fetchedClient.getUsername()), newUsername);
    }

    private Observable<Boolean> checkIdEmpty(ClientId clientId) {
        Log.e("UsernameUseCaseImpl", "checkUsernameNotEmpty");
        if (clientId == null || clientId.toString().isEmpty()) {
            return Observable.error(new AppThrowable(Error.USERNAME_EMPTY));
        }
        return Observable.just(true);
    }

    private Observable<Boolean> checkUsernameNotEmpty(String newUsername) {
        Log.e("UsernameUseCaseImpl", "checkUsernameNotEmpty");
        if (newUsername == null || newUsername.isEmpty()) {
            return Observable.error(new AppThrowable(Error.USERNAME_EMPTY));
        }
        return Observable.just(true);
    }
}
