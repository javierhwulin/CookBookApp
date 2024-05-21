package edu.ub.pis2324.projecte.presentation.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.domain.usecases.ChangePhotoUseCase;
import edu.ub.pis2324.projecte.utils.livedata.StateLiveData;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChangePhotoViewModel extends ViewModel {
    private final ChangePhotoUseCase changePhotoUseCase;

    private final StateLiveData<Void> changePhotoState;

    CompositeDisposable compositeDisposable;

    public ChangePhotoViewModel(ChangePhotoUseCase changePhotoUseCase) {
        this.changePhotoState = new StateLiveData<>();
        this.changePhotoUseCase = changePhotoUseCase;
        this.compositeDisposable = new CompositeDisposable();
    }

    public void onCleared(){
        super.onCleared();
        compositeDisposable.dispose();
    }

    public StateLiveData<Void> getChangePhotoState() {
        return changePhotoState;
    }

    public void ChangePhoto(ClientId clientId, String url) {
        changePhotoState.postLoading();
        compositeDisposable.add(changePhotoUseCase.execute(clientId, url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ignored -> changePhotoState.postSuccess(null),
                        //TODO: DISPLAY ERRORS
                        throwable -> changePhotoState.postError(throwable)
                ));
    }
    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private final ChangePhotoUseCase changePhotoUseCase;

        public Factory(ChangePhotoUseCase changePhotoUseCase) {
            this.changePhotoUseCase = changePhotoUseCase;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(ChangePhotoViewModel.class)) {
                return (T) new ChangePhotoViewModel(changePhotoUseCase);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}