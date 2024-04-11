package edu.ub.pis2324.projecte.data;

import com.google.firebase.firestore.FirebaseFirestore;

import edu.ub.pis2324.projecte.domain.IUserRepository;
import edu.ub.pis2324.projecte.domain.User;


public class UserRepository implements IUserRepository {

    private final FirebaseFirestore db;

    public UserRepository() {
        super();
        db = FirebaseFirestore.getInstance();
    }

    public void addUser(User user) {
        // Add user to database
        db.collection("users").add(user).addOnSuccessListener(documentReference -> {
            // User added successfully
        }).addOnFailureListener(e -> {
            // User not added
        });
    }

}
