package edu.ub.pis2324.projecte.domain.exceptions;

import java.util.HashMap;
import java.util.Map;

public class AppThrowableMapper {
    private final Map<AppError, AppError> errorMap;

    public AppThrowableMapper() {
        errorMap = new HashMap<>();
    }

    public void add(AppError xError, AppError xMappedError) {
        errorMap.put(xError, xMappedError);
    }

    public Throwable map(Throwable throwable) {
        return (throwable instanceof AppThrowable)
            ? map((AppThrowable) throwable)
            : throwable;
    }

    public AppThrowable map(AppThrowable appThrowable) {
        AppError error = appThrowable.getError();
        return (errorMap.containsKey(error))
            ? new AppThrowable(errorMap.get(error))
            : appThrowable;
    }
}
