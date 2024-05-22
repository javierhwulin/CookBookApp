package edu.ub.pis2324.projecte.domain.model.storages;

import android.net.Uri;

import com.google.firebase.storage.StorageReference;

import edu.ub.pis2324.projecte.domain.exceptions.AppError;
import io.reactivex.rxjava3.core.Observable;

public interface IPhotoStorage {
    Observable<Boolean> changePhoto(String mail, Uri uri);
    Observable<Uri> getPhoto(String mail);

    StorageReference getReference();

    StorageReference getReferenceFromUrl(String toString);

    enum Error implements AppError {
        CHANGE_PHOTO_UNKNOWN_ERROR,
        GET_PHOTO_UNKNOWN_ERROR
    }
}
