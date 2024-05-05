package edu.ub.pis2324.projecte.domain.usecases;

import java.util.List;

import edu.ub.pis2324.projecte.domain.exceptions.AppError;
import edu.ub.pis2324.projecte.domain.model.entities.Recipe;
import io.reactivex.rxjava3.core.Observable;

public interface RecipeViewUsecase {
    Observable<List<Recipe>> getRecipes();
    Observable<List<Recipe>> getRecipeByName(String name);

    enum Error implements AppError {
        GET_RECIPES_UNKNOWN_ERROR,
        GET_RECIPE_UNKNOWN_ERROR;
    }
}
