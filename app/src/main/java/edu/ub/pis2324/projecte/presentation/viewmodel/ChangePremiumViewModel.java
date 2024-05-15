package edu.ub.pis2324.projecte.presentation.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import edu.ub.pis2324.projecte.domain.exceptions.AppThrowable;
import edu.ub.pis2324.projecte.domain.model.repositories.IUserRepository;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.domain.services.CheckClientIsPremiumService;
import edu.ub.pis2324.projecte.domain.usecases.ChangePasswordUseCase;
import edu.ub.pis2324.projecte.domain.usecases.ChangePremiumUseCase;
import edu.ub.pis2324.projecte.presentation.ui.ChangePremiumActivity;
import edu.ub.pis2324.projecte.utils.livedata.StateLiveData;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChangePremiumViewModel extends ViewModel {
    private final ChangePremiumUseCase changePremiumUseCase;

    private final StateLiveData<Void> changePremiumState;

    CompositeDisposable compositeDisposable;

    public ChangePremiumViewModel(ChangePremiumUseCase changePremiumUseCase) {
        this.changePremiumState = new StateLiveData<>();
        this.changePremiumUseCase = changePremiumUseCase;
        this.compositeDisposable = new CompositeDisposable();
    }

    public void onCleared(){
        super.onCleared();
        compositeDisposable.dispose();
    }

    public StateLiveData<Void> getChangePremiumState() {
        return changePremiumState;
    }

    public void ChangePremium(ClientId clientId, boolean isPremium) {
        changePremiumState.postLoading();
        compositeDisposable.add(changePremiumUseCase.execute(clientId, isPremium)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ignored -> changePremiumState.postSuccess(null),
                        //TODO: DISPLAY ERRORS
                        throwable -> changePremiumState.postError(throwable)
                ));
    }
    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private final ChangePremiumUseCase changePremiumUseCase;

        public Factory(ChangePremiumUseCase changePremiumUseCase) {
            this.changePremiumUseCase = changePremiumUseCase;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(ChangePremiumViewModel.class)) {
                return (T) new ChangePremiumViewModel(changePremiumUseCase);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}