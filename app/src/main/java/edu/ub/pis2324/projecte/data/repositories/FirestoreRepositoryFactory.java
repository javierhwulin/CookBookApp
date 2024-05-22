package edu.ub.pis2324.projecte.data.repositories;

import edu.ub.pis2324.projecte.data.storages.PhotoStorage;
import edu.ub.pis2324.projecte.domain.model.repositories.AbstractRepositoryFactory;
import edu.ub.pis2324.projecte.domain.model.repositories.IRecipeRepository;
import edu.ub.pis2324.projecte.domain.model.storages.IPhotoStorage;

public class FirestoreRepositoryFactory implements AbstractRepositoryFactory {

    @Override
    public UserRepository createUserRepository(IPhotoStorage photoStorage) {
        return new UserRepository(photoStorage);
    }

    @Override
    public RecipeRepository createRecipeRepository() {
        return new RecipeRepository();
    }

    @Override
    public HistoryRepository createHistoryRepository(IRecipeRepository recipeRepository) {
        return new HistoryRepository(recipeRepository);
    }
}
