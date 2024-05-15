package edu.ub.pis2324.projecte.presentation.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import dagger.internal.Factory;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.domain.usecases.ChangePasswordUseCase;
import edu.ub.pis2324.projecte.domain.usecases.ChangeUsernameUseCase;
import edu.ub.pis2324.projecte.presentation.ui.ChangePasswordActivity;
import edu.ub.pis2324.projecte.utils.livedata.StateLiveData;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChangePasswordViewModel extends ViewModel {
        private final ChangePasswordUseCase changePasswordUseCase;

        private final StateLiveData<Void> changePasswordState;

        CompositeDisposable compositeDisposable;

        public ChangePasswordViewModel(ChangePasswordUseCase changePasswordUseCase) {
            this.changePasswordState = new StateLiveData<>();
            this.changePasswordUseCase = changePasswordUseCase;
            this.compositeDisposable = new CompositeDisposable();
        }

        public void onCleared(){
            super.onCleared();
            compositeDisposable.dispose();
        }

        public StateLiveData<Void> getChangePasswordState() {
            return changePasswordState;
        }

        public void ChangePassword(ClientId clientId, String oldPassword, String newPassword, String confirmPassword) {
            changePasswordState.postLoading();
            compositeDisposable.add(changePasswordUseCase.execute(clientId, oldPassword, newPassword, confirmPassword)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            ignored -> changePasswordState.postSuccess(null),
                            throwable -> changePasswordState.postError(throwable)
                    ));
        }
    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private final ChangePasswordUseCase changePasswordUseCase;

        public Factory(ChangePasswordUseCase changePasswordUseCase) {
            this.changePasswordUseCase = changePasswordUseCase;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(ChangePasswordUseCase.class)) {
                return (T) new ChangePasswordViewModel(changePasswordUseCase);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

}
