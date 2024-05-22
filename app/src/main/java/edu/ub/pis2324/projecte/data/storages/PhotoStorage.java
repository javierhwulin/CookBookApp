package edu.ub.pis2324.projecte.data.storages;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.ref.Reference;

import edu.ub.pis2324.projecte.domain.exceptions.AppThrowable;
import edu.ub.pis2324.projecte.domain.model.repositories.IUserRepository;
import edu.ub.pis2324.projecte.domain.model.storages.IPhotoStorage;
import io.reactivex.rxjava3.core.Observable;

public class PhotoStorage implements IPhotoStorage {
    private static final String PHOTO_REFERENCE_NAME = "images/";

    private final FirebaseStorage storage;

    public PhotoStorage() {
        this.storage = FirebaseStorage.getInstance();
    }

    public Observable<Boolean> changePhoto(String mail, Uri uri){
        return Observable.create(emitter -> {
            storage.getReference().child(PHOTO_REFERENCE_NAME + mail).putFile(uri)
                    .addOnFailureListener(exception -> {
                        emitter.onError(new AppThrowable(IUserRepository.Error.UPDATE_UNKNOWN_ERROR));
                    })
                    .addOnSuccessListener(ignored -> {
                        emitter.onNext(true);
                        emitter.onComplete();
                    });
        });
    }

    public Observable<Uri> getPhoto(String mail) {
        {
            return Observable.create(emitter -> {
                Log.e("UserRepositoryPhoto", "Mail: " + mail);
                storage.getReference().child(PHOTO_REFERENCE_NAME + mail).getDownloadUrl()
                        .addOnFailureListener(exception -> {
                            emitter.onError(new AppThrowable(IUserRepository.Error.ADD_UNKNOWN_ERROR));
                        })
                        .addOnSuccessListener(uri -> {
                            if(uri != null){
                                Log.e("UserRepositoryPhoto", "Uri: " + uri.toString());
                            } else {
                                Log.e("UserRepositoryPhoto", "Uri not found");
                            }
                            emitter.onNext(uri);
                            emitter.onComplete();
                        });
            });
        }
    }

    public StorageReference  getReference() {
        return storage.getReference();
    }

    public StorageReference getReferenceFromUrl(String toString) {
        return storage.getReferenceFromUrl(toString);
    }




}
