package edu.ub.pis2324.projecte.domain.model.repositories;

import edu.ub.pis2324.projecte.data.repositories.RecipeRepository;

public interface IRecipeRepository {
    void getRecipe(String recipe, RecipeRepository.OnGetRecipeListener listener);
    void getAll(RecipeRepository.OnFetchRecipesListener listener);
}
