package edu.ub.pis2324.projecte.domain.model.repositories;

import edu.ub.pis2324.projecte.data.repositories.UserRepository;
import edu.ub.pis2324.projecte.domain.model.entities.User;

public interface IUserRepository {
    /* Interface for UserRepository */
    void addUser(User user, UserRepository.OnAddUserListener listener);
    void getUser(String username, String password, UserRepository.OnGetUserListener listener);

    void changeUsername(User user, String newUsername, UserRepository.OnChangeUsernameListener listener);
}
