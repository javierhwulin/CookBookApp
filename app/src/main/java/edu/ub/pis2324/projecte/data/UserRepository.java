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

    public void addUser(User user, OnAddUserListener listener){
        // Add user to database){
        db.collection("users").document(user.getUsername()).set(user)
            .addOnSuccessListener(aVoid -> listener.OnAddUserSuccess())
            .addOnFailureListener(e -> listener.OnAddUserError(e));
    }

}
