package edu.ub.pis2324.projecte.domain.model.repositories;

import edu.ub.pis2324.projecte.data.repositories.HistoryRepository;
import edu.ub.pis2324.projecte.data.repositories.RecipeRepository;
import edu.ub.pis2324.projecte.data.repositories.UserRepository;
import edu.ub.pis2324.projecte.data.storages.PhotoStorage;
import edu.ub.pis2324.projecte.domain.model.storages.IPhotoStorage;

public interface AbstractRepositoryFactory {
    UserRepository createUserRepository(IPhotoStorage photoStorage);
    RecipeRepository createRecipeRepository();
    HistoryRepository createHistoryRepository(IRecipeRepository recipeRepository);
}
