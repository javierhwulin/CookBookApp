package edu.ub.pis2324.projecte.domain.usecases.implementation;

import java.util.List;

import edu.ub.pis2324.projecte.domain.exceptions.AppThrowableMapper;
import edu.ub.pis2324.projecte.domain.model.entities.Recipe;
import edu.ub.pis2324.projecte.domain.model.repositories.IHistoryRepository;
import edu.ub.pis2324.projecte.domain.model.repositories.IRecipeRepository;
import edu.ub.pis2324.projecte.domain.model.repositories.IUserRepository;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.domain.model.values.RecipeId;
import edu.ub.pis2324.projecte.domain.usecases.HistorialUsecase;

import io.reactivex.rxjava3.core.Observable;

public class HistorialUsecaseImpl implements HistorialUsecase {
    private final IUserRepository userRepository;
    private final IRecipeRepository recipeRepository;
    private final IHistoryRepository historyRepository;
    private final AppThrowableMapper throwableMapper;

    public HistorialUsecaseImpl(IUserRepository userRepository, IRecipeRepository recipeRepository, IHistoryRepository historyRepository) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.historyRepository = historyRepository;
        this.throwableMapper = new AppThrowableMapper();
    }


    @Override
    public Observable<Boolean> add(ClientId clientId, RecipeId recipeId) {
        return historyRepository.add(clientId, recipeId)
                .onErrorResumeNext(throwable -> Observable.error(throwableMapper.map(throwable)));
    }

    @Override
    public Observable<Boolean> remove(ClientId clientId, RecipeId recipeId){
        return null;
    }


    @Override
    public Observable<Boolean> clear(ClientId clientId) {
        return null;
    }

    @Override
    public Observable<List<Recipe>> get(ClientId clientId) {
        return historyRepository.getAll(clientId)
                .onErrorResumeNext(throwable -> Observable.error(throwableMapper.map(throwable)));
    }

    @Override
    public Observable<List<Recipe>> getRecipesByName(ClientId clientId, String recipeName) {
        return historyRepository.getRecipesByName(clientId, recipeName)
                .onErrorResumeNext(throwable -> Observable.error(throwableMapper.map(throwable)));
    }
}
