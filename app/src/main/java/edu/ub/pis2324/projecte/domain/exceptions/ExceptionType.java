package edu.ub.pis2324.projecte.domain.exceptions;

public enum ExceptionType {
    USER_NOT_FOUND("User not found"),
    RECIPE_NOT_FOUND("Recipe not found"),
    RECORD_NOT_FOUND("Record not found"),
    RECORD_IS_EMPTY("Record is empty"),
    RECORD_ALREADY_EXISTS("User already has a record");

    private final String message;

    ExceptionType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
