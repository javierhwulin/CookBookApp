package edu.ub.pis2324.projecte.domain.model.repositories;

import edu.ub.pis2324.projecte.data.repositories.RecipeRepository;
import edu.ub.pis2324.projecte.data.repositories.UserRepository;

public interface AbstractRepositoryFactory {
    UserRepository createUserRepository();
    RecipeRepository createRecipeRepository();
}
