package edu.ub.pis2324.projecte.data.repositories;

import com.google.firebase.firestore.FirebaseFirestore;

import edu.ub.pis2324.projecte.domain.model.repositories.IUserRepository;
import edu.ub.pis2324.projecte.domain.model.entities.User;


public class UserRepository implements IUserRepository {
    private static final String CLIENTS_COLLECTION_NAME = "users";
    private final FirebaseFirestore db;

    public UserRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public Observable<Boolean> add(User user){
        return Observable.create(emitter -> {
            db.collection(CLIENTS_COLLECTION_NAME).document(user.getUsername()).set(user)
                .addOnFailureListener(exception -> {
                    emitter.onError(new AppThrowable(Error.ADD_UNKNOWN_ERROR));
                })
                .addOnSuccessListener(ignored -> {
                    emitter.onNext(true);
                    emitter.onComplete();
                });
        });
    }


    public void getUser(String username, String password, OnGetUserListener listener){
        // Get user from database
        if (username.isEmpty()){
            listener.OnGetUserError(new Throwable("Username cannot be empty"));
        }
        else if(password.isEmpty()){
            listener.OnGetUserError(new Throwable("Password cannot be empty"));
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

    public void changeUsername(User user, String newUsername,OnChangeUsernameListener listener){
        // Change username in database

        db.collection(CLIENTS_COLLECTION_NAME).document(user.getUsername()).get()
                .addOnFailureListener(e -> listener.OnChangeUsernameError(e))
                .addOnSuccessListener(usr -> {
                    if (!usr.exists()){
                        listener.OnChangeUsernameError(new Throwable("Username does not exist"));
                    } else {
                        User usuari = usr.toObject(User.class);
                        usuari.setUsername(newUsername);
                        if (!user.getPassword().equals(usuari.getPassword())){
                            listener.OnChangeUsernameError(new Throwable("Incorrect password"));
                        } else {
                            db.collection(CLIENTS_COLLECTION_NAME).document(newUsername).set(usuari)
                                    .addOnSuccessListener(aVoid -> {
                                        db.collection(CLIENTS_COLLECTION_NAME).document(user.getUsername()).delete()
                                                .addOnSuccessListener(aVoid1 -> listener.OnChangeUsernameSuccess(usuari))
                                                .addOnFailureListener(e -> listener.OnChangeUsernameError(e));
                                    })
                                    .addOnFailureListener(e -> listener.OnChangeUsernameError(e));
                        }
                    }
                });
    }
}
