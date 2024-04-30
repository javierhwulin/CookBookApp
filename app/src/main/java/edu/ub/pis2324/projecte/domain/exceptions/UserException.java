package edu.ub.pis2324.projecte.domain.exceptions;

public class UserException {
    public static class UserNotFoundException extends Exception {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
}
