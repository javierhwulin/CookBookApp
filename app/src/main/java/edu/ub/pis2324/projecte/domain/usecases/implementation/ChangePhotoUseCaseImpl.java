package edu.ub.pis2324.projecte.domain.usecases.implementation;

import android.net.Uri;

import edu.ub.pis2324.projecte.data.repositories.UserRepository;
import edu.ub.pis2324.projecte.data.storages.PhotoStorage;
import edu.ub.pis2324.projecte.domain.exceptions.AppThrowable;
import edu.ub.pis2324.projecte.domain.exceptions.AppThrowableMapper;
import edu.ub.pis2324.projecte.domain.usecases.ChangePhotoUseCase;
import edu.ub.pis2324.projecte.domain.usecases.FetchClientUsecase;
import io.reactivex.rxjava3.core.Observable;

public class ChangePhotoUseCaseImpl implements ChangePhotoUseCase{
    
    private final AppThrowableMapper throwableMapper;

    private final PhotoStorage photoStorage;

    public ChangePhotoUseCaseImpl(PhotoStorage photoStorage) {
        throwableMapper = new AppThrowableMapper();
        throwableMapper.add(UserRepository.Error.USER_NOT_FOUND, ChangePhotoUseCase.Error.CLIENTS_DATA_ACCESS_ERROR);
        this.photoStorage = photoStorage;
    }

    @Override
    public Observable<Boolean> execute(String mail, Uri uri) {

        return checkMailEmpty(mail)
                .concatMap(ignored -> changePhoto(mail, uri))
                .onErrorResumeNext(throwable -> Observable.error(throwableMapper.map(throwable)));

    }

    private Observable<Boolean> changePhoto(String mail, Uri uri) {
        return photoStorage.changePhoto(mail, uri);
    }
    private Observable<Boolean> checkMailEmpty(String mail) {
        if (mail == null || mail.toString().isEmpty()) {
            return Observable.error(new AppThrowable(ChangePhotoUseCase.Error.CLIENTS_DATA_ACCESS_ERROR));
        }
        return Observable.just(true);
    }


}
