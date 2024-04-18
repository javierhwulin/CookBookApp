package edu.ub.pis2324.projecte.data.services;


/**
 * Cloud Firestore implementation of the data store.
 */
public class AuthenticationService {
    /* Attributes */
    private static MockClientsHashMap clients;
    static { clients = new MockClientsHashMap(); }

    public interface OnLogInListener {
        void onLogInSuccess(User user);
        void onLogInError(Throwable throwable);
    }

    public interface OnSignUpListener {
        void onSignUpSuccess();
        void onSignUpError(Throwable throwable);
    }

    /**
     * Empty constructor
     */
    @SuppressWarnings("unused")
    public AuthenticationService() { }

    /**
     * Log in client with username and password.
     * @param username the username
     * @param enteredPassword the password
     * @param listener the listener
     */
    public void logIn(
            String username,
            String enteredPassword,
            OnLogInListener listener
    ) {
        if (username.isEmpty())
            listener.onLogInError(new Throwable("Username cannot be empty"));
        else if (enteredPassword.isEmpty())
            listener.onLogInError(new Throwable("Password cannot be empty"));
        else if (!clients.containsKey(username))
            listener.onLogInError(new Throwable("Username does not exist"));
        else {
            Client client = clients.get(username);
            if (!client.getPassword().equals(enteredPassword))
                listener.onLogInError(new Throwable("Incorrect password"));
            else
                listener.onLogInSuccess(client);
        }
    }

    /**
     * Sign up a new client.
     * @param username the username
     * @param password the password
     * @param passwordConfirmation the password confirmation
     * @param listener the listener
     */
    public void signUp(
            String username,
            String password,
            String passwordConfirmation,
            OnSignUpListener listener
    ) {
        if (username.isEmpty())
            listener.onSignUpError(new Throwable("Username cannot be empty"));
        else if (clients.containsKey(username))
            listener.onSignUpError(new Throwable("Username already exists"));
        else if (password.isEmpty())
            listener.onSignUpError(new Throwable("Password cannot be empty"));
        else if (passwordConfirmation.isEmpty())
            listener.onSignUpError(new Throwable("Password confirmation cannot be empty"));
        else if (!password.equals(passwordConfirmation))
            listener.onSignUpError(new Throwable("Passwords do not match"));
        else {
            clients.put(username, new Client(username, username, password));
            listener.onSignUpSuccess();
        }
    }
}
