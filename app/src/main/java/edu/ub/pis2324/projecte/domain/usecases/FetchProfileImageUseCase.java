package edu.ub.pis2324.projecte.domain.usecases;

import io.reactivex.rxjava3.core.Observable;
import android.net.Uri;

import edu.ub.pis2324.projecte.domain.exceptions.AppError;

public interface FetchProfileImageUseCase {
Observable<Uri> execute(String username);

enum Error implements AppError {
    PROFILE_IMAGE_NOT_FOUND,
    PROFILE_IMAGE_DATA_ACCESS_ERROR;
}

}
