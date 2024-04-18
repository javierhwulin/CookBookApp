package edu.ub.pis2324.projecte.presentation.viewmodel;

import androidx.lifecycle.ViewModel;

import edu.ub.pis2324.projecte.data.UserRepository;
import edu.ub.pis2324.projecte.domain.model.entities.User;
import edu.ub.pis2324.projecte.utils.livedata.StateLiveData;

public class LogInViewModel extends ViewModel {
    /* Attributes */
    private final UserRepository userRepository;
    /* LiveData */
    private final StateLiveData<User> logInState;

    /* Constructor */
    public LogInViewModel() {
        userRepository = new UserRepository();
        logInState = new StateLiveData<>();
    }
    /**
     * Returns the state of the log-in
     * @return the state of the log-in
     */
    public StateLiveData<User> getLogInState() {
        return logInState;
    }

    /**
     * Logs in the user
     * @param username the username
     * @param password the password
     */
    public void logIn(String username, String password) {
        userRepository.getUser(
                username,
                password,
                new UserRepository.OnGetUserListener() {
                    @Override
                    public void OnGetUserSuccess(User user) {
                        logInState.postSuccess(user);
                    }
                    @Override
                    public void OnGetUserError(Throwable throwable) {
                        logInState.postError(throwable);
                    }
                }
        );
    }
}