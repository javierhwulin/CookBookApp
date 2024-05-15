package edu.ub.pis2324.projecte.presentation.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import edu.ub.pis2324.projecte.domain.exceptions.AppError;
import edu.ub.pis2324.projecte.domain.exceptions.AppThrowable;
import edu.ub.pis2324.projecte.domain.model.entities.User;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.domain.usecases.ChangeUsernameUseCase;
import edu.ub.pis2324.projecte.utils.livedata.StateData;
import edu.ub.pis2324.projecte.utils.livedata.StateLiveData;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChangeUsernameViewModel extends ViewModel {

        private final ChangeUsernameUseCase changeUsernameUseCase;

        private final StateLiveData<User> changeUsernameState;
        CompositeDisposable compositeDisposable;


    public ChangeUsernameViewModel(ChangeUsernameUseCase changeUsernameUseCase) {
        this.changeUsernameState = new StateLiveData<>();
        this.changeUsernameUseCase = changeUsernameUseCase;
        this.compositeDisposable = new CompositeDisposable();
    }

    public void onCleared(){
        super.onCleared();
        compositeDisposable.dispose();
    }
    public StateLiveData<User> getChangeUsernameState() {
        return changeUsernameState;
    }


    public void ChangeUsername(ClientId clientId, String newUsername) {
        changeUsernameState.postLoading();
        compositeDisposable.add(changeUsernameUseCase.execute(clientId, newUsername)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> changeUsernameState.postSuccess(user),
                        throwable -> {
                            if (throwable instanceof AppThrowable) {
                                AppThrowable error = (AppThrowable) throwable;
                                if (error.getError() == ChangeUsernameUseCase.Error.USERNAME_EMPTY) {
                                    changeUsernameState.postError(new Throwable("El nom d'usuari no pot estar buit"));
                                } else if (error.getError() == ChangeUsernameUseCase.Error.CLIENTS_DATA_ACCESS_ERROR) {
                                    changeUsernameState.postError(new Throwable("Error al canviar el nom d'usuari"));

                                } else {
                                    changeUsernameState.postError(throwable);
                                }
                            }
                        }));

    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private final ChangeUsernameUseCase changeUsernameUseCase;

        public Factory(ChangeUsernameUseCase changeUsernameUseCase) {
            this.changeUsernameUseCase = changeUsernameUseCase;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(ChangeUsernameViewModel.class)) {
                return (T) new ChangeUsernameViewModel(changeUsernameUseCase);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
