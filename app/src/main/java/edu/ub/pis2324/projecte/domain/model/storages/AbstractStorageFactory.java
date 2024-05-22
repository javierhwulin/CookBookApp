package edu.ub.pis2324.projecte.domain.model.storages;

import edu.ub.pis2324.projecte.data.storages.PhotoStorage;

public interface AbstractStorageFactory {
    PhotoStorage createPhotoStorage();
}
