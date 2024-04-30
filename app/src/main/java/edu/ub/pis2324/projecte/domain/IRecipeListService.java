package edu.ub.pis2324.projecte.domain;

import edu.ub.pis2324.projecte.data.services.RecipeListService;
import edu.ub.pis2324.projecte.domain.model.entities.Recipe;

public interface IRecipeListService {
    void getRecipe(String recipe, RecipeListService.OnGetRecipeListener listener);
    void getAllRecipe(RecipeListService.OnGetAllRecipesListener listener);
}
