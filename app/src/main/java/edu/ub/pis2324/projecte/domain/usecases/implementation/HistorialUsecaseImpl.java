package edu.ub.pis2324.projecte.domain.usecases.implementation;

import java.util.HashMap;
import java.util.Objects;

import edu.ub.pis2324.projecte.data.repositories.UserRepository;
import edu.ub.pis2324.projecte.data.repositories.RecipeRepository;
import edu.ub.pis2324.projecte.domain.exceptions.AppThrowableMapper;
import edu.ub.pis2324.projecte.domain.model.repositories.IRecipeRepository;
import edu.ub.pis2324.projecte.domain.model.repositories.IUserRepository;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.domain.model.values.RecipeId;
import edu.ub.pis2324.projecte.domain.usecases.HistorialUsecase;
import edu.ub.pis2324.projecte.utils.livedata.StateLiveData;

import edu.ub.pis2324.projecte.domain.model.entities.Recipe;
import edu.ub.pis2324.projecte.domain.model.entities.User;
import edu.ub.pis2324.projecte.domain.model.values.Record;
import io.reactivex.rxjava3.core.Observable;

public class HistorialUsecaseImpl implements HistorialUsecase {
    private final IUserRepository userRepository;
    private final IRecipeRepository recipeRepository;
    private final AppThrowableMapper throwableMapper;

    public HistorialUsecaseImpl(IUserRepository userRepository, IRecipeRepository recipeRepository) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.throwableMapper = new AppThrowableMapper();
    }


    @Override
    public Observable<Boolean> add(ClientId clientId) {
        return null;
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
    public Observable<Record> get(ClientId clientId) {
        return null;
    }
}
