package edu.ub.pis2324.projecte.domain.usecases.implementation;

import android.net.Uri;
import android.provider.ContactsContract;

import edu.ub.pis2324.projecte.data.repositories.UserRepository;
import edu.ub.pis2324.projecte.data.storages.PhotoStorage;
import edu.ub.pis2324.projecte.domain.exceptions.AppThrowableMapper;
import edu.ub.pis2324.projecte.domain.model.repositories.IUserRepository;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.domain.usecases.FetchProfileImageUseCase;
import io.reactivex.rxjava3.core.Observable;

public class FetchProfileImageUseCaseImpl implements FetchProfileImageUseCase {
    private final PhotoStorage photoStorage;

    private final AppThrowableMapper throwableMapper;

    public FetchProfileImageUseCaseImpl(PhotoStorage photoStorage) {
        this.photoStorage = photoStorage;
        this.throwableMapper = new AppThrowableMapper();
        throwableMapper.add(UserRepository.Error.USER_NOT_FOUND, Error.PROFILE_IMAGE_NOT_FOUND);
        throwableMapper.add(IUserRepository.Error.GETBYID_UNKNOWN_ERROR, Error.PROFILE_IMAGE_DATA_ACCESS_ERROR);
    }

    @Override
    public Observable<Uri> execute(String mail) {
        return photoStorage.getPhoto(mail)
                .onErrorResumeNext(throwable -> Observable.error(throwableMapper.map(throwable)));
    }
}
