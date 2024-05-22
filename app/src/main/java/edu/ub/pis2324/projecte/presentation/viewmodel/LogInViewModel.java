package edu.ub.pis2324.projecte.presentation.viewmodel;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import dagger.hilt.android.lifecycle.HiltViewModel;
import edu.ub.pis2324.projecte.domain.exceptions.AppError;
import edu.ub.pis2324.projecte.domain.exceptions.AppThrowable;
import edu.ub.pis2324.projecte.domain.model.entities.User;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.domain.usecases.LogInUsecase;
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

    private final StateLiveData<Uri> profileImageState;



    public StateLiveData<Uri> getProfileImageState() {
        return profileImageState;
    }

    public void fetchProfileImage(String username) {
        profileImageState.postLoading();
        Disposable d = logInUsecase.fetchProfileImage(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::handleFetchProfileImageSuccess,
                        throwable -> {
                            if(throwable instanceof AppThrowable) {
                                Log.e("LogInViewModel1", "Fetch profile image error: " + ((AppThrowable) throwable).getError().toString());
                                handleFetchProfileImageError((AppThrowable) throwable);
                            } else{
                                Log.e("LogInViewModel2", "Fetch profile image error: " + throwable.getMessage());
                                profileImageState.postError(throwable);
                            }
                        }
                );
        compositeDisposable.add(d);
    }

    /* Constructor */
    public LogInViewModel(LogInUsecase logInUsecase) {
        logInState = new StateLiveData<>();
        profileImageState = new StateLiveData<>();
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
                        throwable -> {
                            if(throwable instanceof AppThrowable) {
                                Log.e("LogInViewModel", "Log in error: " + ((AppThrowable) throwable).getError().toString());
                                handleLogInError((AppThrowable) throwable);
                            } else{
                                Log.e("LogInViewModel", "Log in error: " + throwable.getMessage());
                                logInState.postError(throwable);
                            }
                        }
                );
        compositeDisposable.add(d);
    }

    private void handleLogInSuccess(User user){
        Log.i("LogInViewModel", "Log in success");
        logInState.postSuccess(user);
    }

    private void handleFetchProfileImageSuccess(Uri uri){
        Log.i("LogInViewModel", "Fetch profile image success");
        profileImageState.postSuccess(uri);
    }

    private void handleFetchProfileImageError(AppThrowable throwable){
        Log.e("LogInViewModel", "Fetch profile image error: " + throwable.getError().toString());

        profileImageState.postError(new Throwable(throwable.getMessage()));
    }

    private void handleLogInError(AppThrowable throwable){
        Log.e("LogInViewModel", "Log in error: " + throwable.getError().toString());
        String message;
        AppError error = throwable.getError();
        if (error == LogInUsecase.Error.USERNAME_EMPTY)
            message = "Username is empty";
        else if (error == LogInUsecase.Error.PASSWORD_EMPTY)
            message = "Password is empty";
        else if (error == LogInUsecase.Error.CLIENT_NOT_FOUND)
            message = "User not found";
        else if (error == LogInUsecase.Error.PASSWORD_INCORRECT)
            message = "Password is incorrect";
        else if (error == LogInUsecase.Error.CLIENTS_DATA_ACCESS_ERROR)
            message = "Users' data access error";
        else
            message = "Unknown error: " + throwable.getMessage();

        logInState.postError(new Throwable(message));
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private final LogInUsecase logInUseCase;

        public Factory(LogInUsecase logInUseCase) {
            this.logInUseCase = logInUseCase;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if(modelClass.isAssignableFrom(LogInViewModel.class)){
                return (T) new LogInViewModel(logInUseCase);
            }else{
                throw new IllegalArgumentException("ViewModel Not Found");
            }
        }
    }
}
