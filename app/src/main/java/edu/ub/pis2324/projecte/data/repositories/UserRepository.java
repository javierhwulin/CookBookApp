package edu.ub.pis2324.projecte.data.repositories;

import com.google.firebase.firestore.FirebaseFirestore;

import edu.ub.pis2324.projecte.domain.model.repositories.IUserRepository;
import edu.ub.pis2324.projecte.domain.model.entities.User;


public class UserRepository implements IUserRepository {
    private static final String CLIENTS_COLLECTION_NAME = "users";
    private final FirebaseFirestore db;

    public UserRepository() {
        super();
        db = FirebaseFirestore.getInstance();
    }

    public interface OnAddUserListener {
        void OnAddUserSuccess();
        void OnAddUserError(Throwable throwable);
    }

    public interface OnGetUserListener {
        void OnGetUserSuccess(User user);
        void OnGetUserError(Throwable throwable);
    }

    public interface OnUpdateUserListener {
        void OnUpdateUserSuccess();
        void OnUpdateUserError(Throwable throwable);
    }

    public interface OnChangeUsernameListener {
        void OnChangeUsernameSuccess(User user);
        void OnChangeUsernameError(Throwable throwable);
    }

    public interface OnChangePasswordListener {
        void OnChangePasswordSuccess();
        void OnChangePasswordError(Throwable throwable);
    }

    public void addUser(User user, OnAddUserListener listener){
        // Add user to database)
        if (user.getUsername().isEmpty()){
            listener.OnAddUserError(new Throwable("Username cannot be empty"));
        }
        else if(user.getEmail().isEmpty()){
            listener.OnAddUserError(new Throwable("Email cannot be empty"));
        }
        else if(user.getPassword().isEmpty()){
            listener.OnAddUserError(new Throwable("Password cannot be empty"));
        }

        db.collection(CLIENTS_COLLECTION_NAME).document(user.getUsername()).set(user)
            .addOnSuccessListener(aVoid -> listener.OnAddUserSuccess())
            .addOnFailureListener(e -> listener.OnAddUserError(e));
    }


    public void getUser(String username, String password, OnGetUserListener listener){
        // Get user from database
        if (username.isEmpty()){
            listener.OnGetUserError(new Throwable("Username cannot be empty"));
        }
        else if(password.isEmpty()){
            listener.OnGetUserError(new Throwable("Password cannot be empty"));
        }

        db.collection(CLIENTS_COLLECTION_NAME).document(username).get()
                .addOnFailureListener(e -> listener.OnGetUserError(e))
                .addOnSuccessListener(usr -> {
                    if (!usr.exists()){
                        listener.OnGetUserError(new Throwable("Username does not exist"));
                    } else {
                        User user = usr.toObject(User.class);
                        if (!user.getPassword().equals(password)){
                            listener.OnGetUserError(new Throwable("Incorrect password"));
                        } else {
                            listener.OnGetUserSuccess(user);
                        }
                    }
                });
    }

    public void updateUser(String username, boolean isPremium, OnUpdateUserListener listener){

        // Update user to database
        db.collection(CLIENTS_COLLECTION_NAME).document(username)
                .update("isPremium", isPremium)
                .addOnSuccessListener(aVoid -> listener.OnUpdateUserSuccess())
                .addOnFailureListener(e -> listener.OnUpdateUserError(e));
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
