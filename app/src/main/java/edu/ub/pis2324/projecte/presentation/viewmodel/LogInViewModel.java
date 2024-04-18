package edu.ub.pis2324.projecte.presentation.viewmodel;

import androidx.lifecycle.ViewModel;

import edu.ub.pis2324.projecte.data.services.AuthenticationService;
import edu.ub.pis2324.projecte.domain.model.entities.User;
import edu.ub.pis2324.projecte.utils.livedata.StateLiveData;

public class LogInViewModel extends ViewModel {
    /* Attributes */
    private final AuthenticationService authenticationService;
    /* LiveData */
    private final StateLiveData<User> logInState;

    /* Constructor */
    public LogInViewModel() {
        authenticationService = new AuthenticationService();
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
        authenticationService.logIn(
                username,
                password,
                new AuthenticationService.OnLogInListener() {
                    @Override
                    public void onLogInSuccess(User user) {
                        logInState.postSuccess(user);
                    }
                    @Override
                    public void onLogInError(Throwable throwable) {
                        logInState.postError(throwable);
                    }
                }
        );
    }
}