package edu.ub.pis2324.projecte.data.storages;

import edu.ub.pis2324.projecte.domain.model.storages.AbstractStorageFactory;

public class FirestoreStorageFactory implements AbstractStorageFactory {

        @Override
        public PhotoStorage createPhotoStorage() {
            return new PhotoStorage();
        }
}
