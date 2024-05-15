package edu.ub.pis2324.projecte.domain.usecases.implementation;

import java.util.List;

import edu.ub.pis2324.projecte.domain.exceptions.AppThrowableMapper;
import edu.ub.pis2324.projecte.domain.model.entities.Recipe;
import edu.ub.pis2324.projecte.domain.model.repositories.IRecipeRepository;
import edu.ub.pis2324.projecte.domain.usecases.RecipeViewUsecase;
import io.reactivex.rxjava3.core.Observable;

public class RecipeViewUsecaseImpl implements RecipeViewUsecase {
    private final IRecipeRepository recipeRepository;
    private final AppThrowableMapper throwableMapper;

    public RecipeViewUsecaseImpl(IRecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
        this.throwableMapper = new AppThrowableMapper();
        throwableMapper.add(IRecipeRepository.Error.GETALL_UNKNOWN_ERROR, Error.GET_RECIPES_UNKNOWN_ERROR);
        throwableMapper.add(IRecipeRepository.Error.GETBYNAME_UNKNOWN_ERROR, Error.GET_RECIPE_UNKNOWN_ERROR);
    }
    @Override
    public Observable<List<Recipe>> getRecipes() {
        return recipeRepository.getAll()
                .onErrorResumeNext(throwable -> Observable.error(throwableMapper.map(throwable)));
    }

    @Override
    public Observable<List<Recipe>> getRecipeByName(String name) {
        return recipeRepository.getByName(name)
                .onErrorResumeNext(throwable -> Observable.error(throwableMapper.map(throwable)));
    }
}
