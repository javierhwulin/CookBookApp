package edu.ub.pis2324.projecte.domain.model.repositories;

import edu.ub.pis2324.projecte.data.repositories.UserRepository;
import edu.ub.pis2324.projecte.domain.model.entities.User;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.domain.exceptions.AppError;

public interface IUserRepository {
    /* Interface for UserRepository */
    interface OnUpdateListener{
        void onUpdate(User user);
        void onError(Throwable throwable);
    }

    Observable<Boolean> add(User user);
    Observable<User> getById(ClientId id);
    Observable<Boolean> update(ClientId id, OnUpdateListener onUpdateListener);
    Observable<Boolean> remove(ClientId id);

    Observable<User> changeUsername(ClientId id, String newUsername);

    enum Error implements AppError{
        USER_NOT_FOUND,
        ADD_UNKNOWN_ERROR,
        GETBYID_UNKNOWN_ERROR,
        UPDATE_UNKNOWN_ERROR,
        REMOVE_UNKNOWN_ERROR;
    }
    void addUser(User user, UserRepository.OnAddUserListener listener);
    void getUser(String username, String password, UserRepository.OnGetUserListener listener);

    void changeUsername(User user, String newUsername, UserRepository.OnChangeUsernameListener listener);
}
