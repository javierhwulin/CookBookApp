package edu.ub.pis2324.projecte.presentation.viewmodel;

import androidx.lifecycle.ViewModel;

import edu.ub.pis2324.projecte.domain.exceptions.AppThrowable;
import edu.ub.pis2324.projecte.domain.model.repositories.IUserRepository;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.domain.services.CheckClientIsPremiumService;
import edu.ub.pis2324.projecte.utils.livedata.StateLiveData;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GetPremiumViewModel extends ViewModel {
    /* Attributes */
    private final CheckClientIsPremiumService checkClientIsPremiumService;
    /* LiveData */
    private final StateLiveData<Boolean> premiumState;

    CompositeDisposable compositeDisposable;
    /* Constructor */
    public GetPremiumViewModel(CheckClientIsPremiumService checkClientIsPremiumService) {
        premiumState = new StateLiveData<>();
        this.checkClientIsPremiumService = checkClientIsPremiumService;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

    public StateLiveData<Boolean> getPremiumState() {
        return premiumState;
    }

    public void getPremium(String username) {
        premiumState.postLoading();

        ClientId id = new ClientId(username);

        Disposable d = checkClientIsPremiumService.execute(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                user -> handleGetPremiumSuccess(),
                throwable -> handleGetPremiumError(throwable)
            );
        compositeDisposable.add(d);
    }

    public void handleGetPremiumSuccess() {
        premiumState.postSuccess(true);
    }

    public void handleGetPremiumError(Throwable throwable) {
        if (throwable instanceof AppThrowable) {
            AppThrowable appThrowable = (AppThrowable) throwable;
            handleAppError(appThrowable);
        } else {
            premiumState.postError(new Throwable("Unknown error"));
        }
    }

    private void handleAppError(AppThrowable appThrowable) {
        if (appThrowable.getError() == IUserRepository.Error.USER_NOT_FOUND) {
            premiumState.postError(new Throwable("Client not found"));
        } else if (appThrowable.getError() == CheckClientIsPremiumService.Error.CLIENT_IS_NOT_PREMIUM) {
            premiumState.postError(new Throwable("Client not premium"));
        } else {
            premiumState.postError(new Throwable("Unknown error"));
        }
    }
}