package edu.ub.pis2324.projecte.data;

import com.google.firebase.firestore.FirebaseFirestore;

import edu.ub.pis2324.projecte.domain.IUserRepository;
import edu.ub.pis2324.projecte.domain.model.entities.User;


public class UserRepository implements IUserRepository {

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

        db.collection("users").document(user.getUsername()).set(user)
            .addOnSuccessListener(aVoid -> listener.OnAddUserSuccess())
            .addOnFailureListener(e -> listener.OnAddUserError(e));
    }


    public void getUser(String username, String password, OnGetUserListener listener){
        // Add user to database)
        if (username.isEmpty()){
            listener.OnGetUserError(new Throwable("Username cannot be empty"));
        }
        else if(password.isEmpty()){
            listener.OnGetUserError(new Throwable("Password cannot be empty"));
        }

        db.collection("users").document(username).get()
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
}
