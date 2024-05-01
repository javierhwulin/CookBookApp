package edu.ub.pis2324.projecte.domain.model.repositories;

import edu.ub.pis2324.projecte.data.services.RecipeListService;

public interface IRecipeListService {
    void getRecipe(String recipe, RecipeListService.OnGetRecipeListener listener);
    void getAll(RecipeListService.OnFetchRecipesListener listener);
}
