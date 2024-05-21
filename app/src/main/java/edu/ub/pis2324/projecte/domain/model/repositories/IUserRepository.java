package edu.ub.pis2324.projecte.domain.model.repositories;


import io.reactivex.rxjava3.core.Observable;

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

    Observable<Boolean> changePassword(ClientId id, String newPassword);

    enum Error implements AppError{
        USER_NOT_FOUND,
        USER_ALREADY_EXISTS,
        ADD_UNKNOWN_ERROR,
        GETBYID_UNKNOWN_ERROR,
        UPDATE_UNKNOWN_ERROR,
        REMOVE_UNKNOWN_ERROR;
    }
}
