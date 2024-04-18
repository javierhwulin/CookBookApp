package edu.ub.pis2324.projecte.domain.exceptions;

public class RecipeException {
    public static class RecordNotFoundException extends Exception {
        public RecordNotFoundException(String message) {
            super(message);
        }
    }

    public static class RecipeNotFoundException extends Exception {
        public RecipeNotFoundException(String message) {
            super(message);
        }
    }
}
