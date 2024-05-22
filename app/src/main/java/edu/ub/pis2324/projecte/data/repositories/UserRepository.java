package edu.ub.pis2324.projecte.data.repositories;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.IOException;

import edu.ub.pis2324.projecte.domain.exceptions.AppThrowable;
import edu.ub.pis2324.projecte.domain.model.repositories.IUserRepository;
import edu.ub.pis2324.projecte.domain.model.entities.User;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import io.reactivex.rxjava3.core.Observable;

public class UserRepository implements IUserRepository {
    private static final String CLIENTS_COLLECTION_NAME = "users";
    private final FirebaseFirestore db;
    private final FirebaseStorage storage;

    public UserRepository() {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public Observable<Boolean> add(User user){
        return Observable.create(emitter -> {
            // Get the reference to the default image
            storage.getReference().child("images/default.jpg").getDownloadUrl()
                    .addOnFailureListener(exception -> {
                        emitter.onError(new AppThrowable(Error.ADD_UNKNOWN_ERROR));
                    })
                    .addOnSuccessListener(uri -> {
                        // Download the default image to a local file
                        File localFile = null;
                        try {
                            localFile = File.createTempFile("images", "jpg");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        File finalLocalFile = localFile;
                        storage.getReferenceFromUrl(uri.toString()).getFile(localFile)
                                .addOnSuccessListener(taskSnapshot -> {
                                    // Upload the local file to the new location
                                    Uri fileUri = Uri.fromFile(finalLocalFile);
                                    storage.getReference().child("images/" + user.getId().toString()).putFile(fileUri)
                                            .addOnSuccessListener(uploadTaskSnapshot -> {
                                                // Set the user's photoUrl to the download URL of the uploaded file
                                                uploadTaskSnapshot.getStorage().getDownloadUrl()
                                                        .addOnSuccessListener(downloadUri -> {
                                                            user.setPhotoUrl(downloadUri.toString());
                                                            // Add the user to the Firestore database
                                                            db.collection(CLIENTS_COLLECTION_NAME).document(user.getUsername()).set(user)
                                                                    .addOnFailureListener(exception -> {
                                                                        emitter.onError(new AppThrowable(Error.ADD_UNKNOWN_ERROR));
                                                                    })
                                                                    .addOnSuccessListener(ignored -> {
                                                                        emitter.onNext(true);
                                                                        emitter.onComplete();
                                                                    });
                                                        });
                                            })
                                            .addOnFailureListener(exception -> {
                                                emitter.onError(new AppThrowable(Error.ADD_UNKNOWN_ERROR));
                                            });
                                })
                                .addOnFailureListener(exception -> {
                                    emitter.onError(new AppThrowable(Error.ADD_UNKNOWN_ERROR));
                                });
                    });
        });
    }

    public Observable<User> getById(ClientId id){
        Log.e("UserRepository", "execute");
        return Observable.create(emitter -> {
            db.collection(CLIENTS_COLLECTION_NAME)
                .document(id.toString())
                .get()
                .addOnFailureListener(exception -> {
                    Log.e("UserRepository", "Error getting user by id");
                    emitter.onError(new AppThrowable(Error.GETBYID_UNKNOWN_ERROR));
                })
                .addOnSuccessListener(ds -> {
                    if (ds.exists()) {
                        Log.i("UserRepository", "User found");
                        User user = ds.toObject(User.class);
                        Log.i("UserRepository", "User: " + user.getUsername());
                        emitter.onNext(user);
                        emitter.onComplete();
                    } else {
                        Log.e("UserRepository", "User not found");
                        emitter.onError(new AppThrowable(Error.USER_NOT_FOUND));
                    }
                });
        });
    }

    public Observable<Boolean> update(ClientId id, OnUpdateListener onUpdateListener) {
        return Observable.create(emitter -> {
            db.runTransaction(transaction -> {
                try {
                    DocumentReference docRef = db
                            .collection(CLIENTS_COLLECTION_NAME)
                            .document(id.toString());
                    DocumentSnapshot ds = transaction.get(docRef);
                    if (!ds.exists()) {
                        return false;
                    } else {
                        User client = ds.toObject(User.class);
                        if(client == null){
                            throw new AppThrowable(Error.USER_NOT_FOUND);
                        }
                        onUpdateListener.onUpdate(client);
                        transaction.set(docRef, client);
                        return true;
                    }
                } catch (Throwable e) {
                    try {
                        throw e;
                    } catch (AppThrowable ex) {
                        throw new RuntimeException(ex);
                    }
                }
            })
            .addOnFailureListener(e -> {
                emitter.onError(new AppThrowable(Error.UPDATE_UNKNOWN_ERROR));
            })
            .addOnSuccessListener(success -> {
                if (!success) {
                    emitter.onError(new AppThrowable(Error.USER_NOT_FOUND));
                } else {
                    emitter.onNext(true);
                    emitter.onComplete();
                }
            });
        });
    }

    public Observable<Boolean> remove(ClientId id){
        return Observable.create(emitter -> {
            db.collection(CLIENTS_COLLECTION_NAME)
                .document(id.toString())
                .delete()
                .addOnFailureListener(exception -> {
                    emitter.onError(new AppThrowable(Error.REMOVE_UNKNOWN_ERROR));
                })
                .addOnSuccessListener(ignored -> {
                    emitter.onNext(true);
                    emitter.onComplete();
                });
        });
    }

    public Observable<User> changeUsername(ClientId id, String newUsername){
        Log.i("Change Username", "ID es: " + id + " Username: " + newUsername);
        return Observable.create(emitter -> {
            // Check if new username already exists
            db.collection(CLIENTS_COLLECTION_NAME)
                    .document(newUsername)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // If new username already exists, emit an error
                                emitter.onError(new AppThrowable(Error.USER_ALREADY_EXISTS));
                            } else {
                                // If new username does not exist, proceed with changing the username
                                db.collection(CLIENTS_COLLECTION_NAME)
                                        .document(id.toString())
                                        .get()
                                        .addOnFailureListener(exception -> {
                                            emitter.onError(new AppThrowable(Error.GETBYID_UNKNOWN_ERROR));
                                        })
                                        .addOnSuccessListener(ds -> {
                                            if (ds.exists()) {
                                                User user = ds.toObject(User.class);
                                                user.setUsername(newUsername);
                                                db.collection(CLIENTS_COLLECTION_NAME)
                                                        .document(newUsername)
                                                        .set(user)
                                                        .addOnFailureListener(exception -> {
                                                            emitter.onError(new AppThrowable(Error.UPDATE_UNKNOWN_ERROR));
                                                        })
                                                        .addOnSuccessListener(ignored -> {
                                                            db.collection(CLIENTS_COLLECTION_NAME)
                                                                    .document(id.toString())
                                                                    .delete()
                                                                    .addOnFailureListener(exception -> {
                                                                        emitter.onError(new AppThrowable(Error.REMOVE_UNKNOWN_ERROR));
                                                                    })
                                                                    .addOnSuccessListener(ignored2 -> {
                                                                        emitter.onNext(user);
                                                                        emitter.onComplete();
                                                                    });
                                                        });
                                            } else {
                                                emitter.onError(new AppThrowable(Error.USER_NOT_FOUND));
                                            }
                                        });
                            }
                        } else {
                            emitter.onError(task.getException());
                        }
                    });
        });
    }
    @Override
    public Observable<Boolean> changePassword(ClientId id, String newPassword) {
        return Observable.create(emitter -> {
            db.collection(CLIENTS_COLLECTION_NAME)
                    .document(id.toString())
                    .update("password", newPassword)
                    .addOnFailureListener(exception -> {
                        emitter.onError(new AppThrowable(Error.UPDATE_UNKNOWN_ERROR));
                    })
                    .addOnSuccessListener(ignored -> {
                        emitter.onNext(true);
                        emitter.onComplete();
                    });
        });
    }

    public Observable<Boolean> changePremium(ClientId id, boolean isPremium){
        return Observable.create(emitter -> {
            db.collection(CLIENTS_COLLECTION_NAME)
                    .document(id.toString())
                    .update("premium", isPremium)
                    .addOnFailureListener(exception -> {
                        emitter.onError(new AppThrowable(Error.UPDATE_UNKNOWN_ERROR));
                    })
                    .addOnSuccessListener(ignored -> {
                        emitter.onNext(true);
                        emitter.onComplete();
                    });
        });
    }

    public Observable<Boolean> changePhoto(ClientId id, Uri uri){
        return Observable.create(emitter -> {
            storage.getReference().child("images/" + id.toString()).putFile(uri)
                    .addOnFailureListener(exception -> {
                        emitter.onError(new AppThrowable(Error.UPDATE_UNKNOWN_ERROR));
                    })
                    .addOnSuccessListener(ignored -> {
                        emitter.onNext(true);
                        emitter.onComplete();
                    });
        });
    }

    public Observable<Uri> getPhoto(ClientId id) {
        {
            return Observable.create(emitter -> {
                storage.getReference().child("images/" + id.toString()).getDownloadUrl()
                        .addOnFailureListener(exception -> {
                            emitter.onError(new AppThrowable(Error.ADD_UNKNOWN_ERROR));
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

}
