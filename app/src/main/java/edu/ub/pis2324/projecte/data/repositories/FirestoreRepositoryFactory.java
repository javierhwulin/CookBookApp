package edu.ub.pis2324.projecte.data.repositories;

import edu.ub.pis2324.projecte.domain.model.repositories.AbstractRepositoryFactory;

public class FirestoreRepositoryFactory implements AbstractRepositoryFactory {

    @Override
    public UserRepository createUserRepository() {
        return new UserRepository();
    }

    @Override
    public RecipeRepository createRecipeRepository() {
        return new RecipeRepository();
    }
}
