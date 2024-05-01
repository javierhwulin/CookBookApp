package edu.ub.pis2324.projecte.domain.exceptions;

public class AppThrowable extends Throwable{
    private final AppError error;

    public AppThrowable(AppError error) {
        this.error = error;
    }

    public AppError getError() {
        return error;
    }
}