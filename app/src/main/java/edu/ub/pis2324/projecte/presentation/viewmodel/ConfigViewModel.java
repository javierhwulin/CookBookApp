package edu.ub.pis2324.projecte.presentation.viewmodel;

import android.net.Uri;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.domain.usecases.ChangePhotoUseCase;
import edu.ub.pis2324.projecte.utils.livedata.StateLiveData;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class ConfigViewModel extends ViewModel {
    private final ChangePhotoUseCase changePhotoUseCase;
    private final StateLiveData<Uri> changePhotoState;
    CompositeDisposable compositeDisposable;

    public ConfigViewModel(ChangePhotoUseCase changePhotoUseCase) {
        this.changePhotoState = new StateLiveData<>();
        this.changePhotoUseCase = changePhotoUseCase;
        this.compositeDisposable = new CompositeDisposable();
    }


    public void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

    public StateLiveData<Uri> getChangePhotoState() {
        return changePhotoState;
    }

    public void ChangePhoto(String mail,Uri photoPath) {
        changePhotoState.postLoading();
        compositeDisposable.add(changePhotoUseCase.execute(mail, photoPath)
                .subscribe(
                        photo -> changePhotoState.postSuccess(photoPath),
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
            if (modelClass.isAssignableFrom(ConfigViewModel.class)) {
                return (T) new ConfigViewModel(changePhotoUseCase);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }


}
