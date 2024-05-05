package edu.ub.pis2324.projecte.presentation.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import edu.ub.pis2324.projecte.data.repositories.UserRepository;
import edu.ub.pis2324.projecte.domain.exceptions.AppError;
import edu.ub.pis2324.projecte.domain.exceptions.AppThrowable;
import edu.ub.pis2324.projecte.domain.model.entities.User;
import edu.ub.pis2324.projecte.domain.model.repositories.IUserRepository;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.domain.usecases.LogInUsecase;
import edu.ub.pis2324.projecte.domain.usecases.SignUpUsecase;
import edu.ub.pis2324.projecte.utils.livedata.StateLiveData;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LogInViewModel extends ViewModel {
    /* Attributes */
    private final LogInUsecase logInUsecase;
    /* LiveData */
    private final StateLiveData<User> logInState;

    CompositeDisposable compositeDisposable;

    /* Constructor */
    public LogInViewModel(LogInUsecase logInUsecase){
        logInState = new StateLiveData<>();
        this.logInUsecase = logInUsecase;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onCleared(){
        super.onCleared();
        compositeDisposable.dispose();
    }
    /**
     * Returns the state of the log-in
     * @return the state of the log-in
     */
    public StateLiveData<User> getLogInState() {
        return logInState;
    }

    /**
     * Logs in the user
     * @param username the username
     * @param password the password
     */

    public void logIn(String username, String password) {
        logInState.postLoading();

        ClientId clientId = new ClientId(username);

        Disposable d = logInUsecase.execute(clientId, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::handleLogInSuccess,
                        throwable -> handleLogInError((AppThrowable) throwable)
                );

        compositeDisposable.add(d);
    }

    private void handleLogInSuccess(User user){
        logInState.postSuccess(user);
    }

    private void handleLogInError(AppThrowable throwable){
        String message;
        AppError error = throwable.getError();
        if (error == LogInUsecase.Error.USERNAME_EMPTY)
            message = "Username is empty";
        else if (error == LogInUsecase.Error.PASSWORD_EMPTY)
            message = "Password is empty";
        else if (error == LogInUsecase.Error.CLIENT_NOT_FOUND)
            message = "Client not found";
        else if (error == LogInUsecase.Error.PASSWORD_INCORRECT)
            message = "Password is incorrect";
        else if (error == LogInUsecase.Error.CLIENTS_DATA_ACCESS_ERROR)
            message = "Clients' data access error";
        else
            message = "Unknown error";

        logInState.postError(new Throwable(message));
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private final LogInUsecase logInUseCase;

        public Factory(LogInUsecase logInUseCase) {
            this.logInUseCase = logInUseCase;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new LogInViewModel(logInUseCase);
        }
    }
}
