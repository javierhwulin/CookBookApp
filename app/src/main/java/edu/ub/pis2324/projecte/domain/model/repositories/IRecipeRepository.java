package edu.ub.pis2324.projecte.domain.model.repositories;

import java.util.List;

import edu.ub.pis2324.projecte.domain.exceptions.AppError;
import edu.ub.pis2324.projecte.domain.model.entities.Recipe;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.domain.model.values.RecipeId;
import io.reactivex.rxjava3.core.Observable;

public interface IRecipeRepository {
    Observable<Recipe> getById(RecipeId id);
    Observable<List<Recipe>> getAll();
    Observable<List<Recipe>> getByName(String recipeName);
    enum Error implements AppError{
        RECIPE_NOT_FOUND,
        GETBYID_UNKNOWN_ERROR,
        GETALL_UNKNOWN_ERROR,
        GETBYNAME_UNKNOWN_ERROR
    }
}
