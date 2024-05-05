package edu.ub.pis2324.projecte.presentation.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import edu.ub.pis2324.projecte.domain.exceptions.AppError;
import edu.ub.pis2324.projecte.domain.exceptions.AppThrowable;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.domain.usecases.SignUpUsecase;
import edu.ub.pis2324.projecte.utils.livedata.StateLiveData;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class SignUpViewModel extends ViewModel {
    private final SignUpUsecase signUpUseCase;
    private final StateLiveData<Void> signUpState;

    CompositeDisposable compositeDisposable;

    public SignUpViewModel (SignUpUsecase signUpUseCase){
        super();
        this.signUpUseCase = signUpUseCase;
        signUpState = new StateLiveData<>();
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onCleared(){
        super.onCleared();
        compositeDisposable.dispose();
    }

    public StateLiveData<Void> getSignUpState (){
        return signUpState;
    }

    public void SignUp (String username, String email ,String password, String passwordConfirmation){
        ClientId clientId = new ClientId(username);
        Disposable d = signUpUseCase.execute(clientId, username, email, password, passwordConfirmation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    ignored -> signUpState.postComplete(),
                    throwable -> handleSignUpError(throwable)
                );
        compositeDisposable.add(d);
    }

    public void handleSignUpError(Throwable throwable){
        if(throwable instanceof AppThrowable){
            AppThrowable appThrowable = (AppThrowable) throwable;
            handleAppError(appThrowable);
        } else {
            signUpState.postError(new Throwable("Unknown error"));
        }
    }

    public void handleAppError(AppThrowable appThrowable){
        String message;
        AppError error = appThrowable.getError();
        if(error == SignUpUsecase.Error.USERNAME_EMPTY)
            message ="Username cannot be empty";
        if(error == SignUpUsecase.Error.EMAIL_EMPTY)
            message = "Email cannot be empty";
        if(error == SignUpUsecase.Error.PASSWORD_EMPTY)
            message = "Password cannot be empty";
        if(error == SignUpUsecase.Error.PASSWORD_CONFIRMATION_EMPTY)
            message = "Password confirmation cannot be empty";
        if(error == SignUpUsecase.Error.PASSWORD_AND_CONFIRMATION_MISMATCH)
            message = "Passwords do not match";
        if(error == SignUpUsecase.Error.CLIENT_ALREADY_EXISTS)
            message = "User already exists";
        if(error == SignUpUsecase.Error.CLIENTS_DATA_ACCESS_ERROR)
            message = "Error accessing data";
        else
            message = "Unknown error";
        signUpState.postError(new Throwable(message));
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory{
        private final SignUpUsecase signUpUseCase;

        public Factory(SignUpUsecase signUpUseCase){
            this.signUpUseCase = signUpUseCase;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(Class<T> modelClass){
            return (T) new SignUpViewModel(signUpUseCase);
        }
    }
}
