package edu.ub.pis2324.projecte.domain.usecases.implementation;

import androidx.lifecycle.LiveData;

import java.util.HashMap;
import java.util.Objects;

import edu.ub.pis2324.projecte.data.UserRepository;
import edu.ub.pis2324.projecte.data.services.RecipeListService;
import edu.ub.pis2324.projecte.utils.livedata.StateLiveData;
import io.reactivex.rxjava3.core.*;

import edu.ub.pis2324.projecte.domain.model.entities.Recipe;
import edu.ub.pis2324.projecte.domain.model.entities.User;
import edu.ub.pis2324.projecte.domain.model.values.Record;
import edu.ub.pis2324.projecte.domain.exceptions.UserException.UserNotFoundException;
import edu.ub.pis2324.projecte.domain.exceptions.RecipeException;
import edu.ub.pis2324.projecte.domain.exceptions.ExceptionType;

public class HistorialUsecase {
    private final UserRepository userRepository;
    private final RecipeListService recipeListService;
    private final StateLiveData<User> userStateLiveData;
    private final StateLiveData<Recipe> recipeStateLiveData;

    private HistorialUsecase(UserRepository userRepository, RecipeListService recipeListService) {
        this.userRepository = userRepository;
        this.recipeListService = recipeListService;
        this.userStateLiveData = new StateLiveData<>();
        this.recipeStateLiveData = new StateLiveData<>();
    }

    private static final class RecordsHolder {
        static final HashMap<User, Record> records = new HashMap<>();
    }

    public static HistorialUsecase getInstance() {
        return new HistorialUsecase(new UserRepository(), new RecipeListService());
    }

    public StateLiveData<User> getUserStateLiveData() {
        return userStateLiveData;
    }

    public StateLiveData<Recipe> getRecipeStateLiveData() {
        return recipeStateLiveData;
    }

    public interface OnGetRecipeListener {
        void OnGetRecipeSuccess(Recipe recipe);
        void OnGetRecipeError(Throwable throwable);
    }

    public void addRecordtoUser(String username, String password){
        userStateLiveData.postLoading();
        userRepository.getUser(username, password, new UserRepository.OnGetUserListener() {
            @Override
            public void OnGetUserSuccess(User user) {
                if(user != null){
                    RecordsHolder.records.put(user, new Record());
                    userStateLiveData.postSuccess(user);
                }else {
                    userStateLiveData.postError(new UserNotFoundException(ExceptionType.USER_NOT_FOUND.getMessage()));
                }
                userStateLiveData.postComplete();
            }
            @Override
            public void OnGetUserError(Throwable throwable) {
                userStateLiveData.postError(throwable);
                userStateLiveData.postComplete();
            }
        });
    }

    public void addRecipe(String username, String password, String recipeId) {
        recipeStateLiveData.postLoading();
        userRepository.getUser(username, password, new UserRepository.OnGetUserListener() {
            @Override
            public void OnGetUserSuccess(User user) {
                if (user != null) {
                    Record record = RecordsHolder.records.get(user);
                    if (record != null) {
                        recipeListService.getRecipe(recipeId, new RecipeListService.OnGetRecipeListener() {
                            @Override
                            public void OnGetRecipeSuccess(Recipe recipe) {
                                if (recipe != null) {
                                    record.addRecipe(recipe);
                                    recipeStateLiveData.postSuccess(recipe);
                                } else {
                                    recipeStateLiveData.postError(new RecipeException.RecipeNotFoundException(ExceptionType.RECIPE_NOT_FOUND.getMessage()));
                                }
                                recipeStateLiveData.postComplete();
                            }

                            @Override
                            public void OnGetRecipeError(Throwable throwable) {
                                recipeStateLiveData.postError(throwable);
                                recipeStateLiveData.postComplete();
                            }
                        });
                    } else {
                        recipeStateLiveData.postError(new NullPointerException("Record is null"));
                        recipeStateLiveData.postComplete();
                    }
                } else {
                    recipeStateLiveData.postError(new NullPointerException("User is null"));
                    recipeStateLiveData.postComplete();
                }
            }
            @Override
            public void OnGetUserError(Throwable throwable) {
                recipeStateLiveData.postError(throwable);
                recipeStateLiveData.postComplete();
            }
        });
    }

    public void getRecipe(String username, String password, String recipeId, OnGetRecipeListener listener){
        userRepository.getUser(username, password, new UserRepository.OnGetUserListener() {
            @Override
            public void OnGetUserSuccess(User user) {
                Recipe recipe = Objects.requireNonNull(RecordsHolder.records.get(user)).getRecipe(recipeId);
                listener.OnGetRecipeSuccess(recipe);
            }

            @Override
            public void OnGetUserError(Throwable throwable) {
                listener.OnGetRecipeError(throwable);
            }
        });
    }

    public void removeRecipe(String username, String password, String recipeId) {
        recipeStateLiveData.postLoading();
        userRepository.getUser(username, password, new UserRepository.OnGetUserListener() {
            @Override
            public void OnGetUserSuccess(User user) {
                Record record = RecordsHolder.records.get(user);
                if(record == null) {
                    recipeStateLiveData.postError(new NullPointerException("Record is null"));
                }else {
                    record.removeRecipe(recipeId);
                    recipeStateLiveData.postSuccess(null);
                }
                recipeStateLiveData.postComplete();
            }

            @Override
            public void OnGetUserError(Throwable throwable) {
                recipeStateLiveData.postError(throwable);
                recipeStateLiveData.postComplete();
            }
        });
    }

    public void clearRecord(String username, String password) {
        recipeStateLiveData.postLoading();
        userRepository.getUser(username, password, new UserRepository.OnGetUserListener() {
            @Override
            public void OnGetUserSuccess(User user) {
                Record record = RecordsHolder.records.get(user);
                if(record == null) {
                    recipeStateLiveData.postError(new NullPointerException("Record is null"));
                }else {
                    record.clear();
                    recipeStateLiveData.postSuccess(null);
                }
                recipeStateLiveData.postComplete();
            }
            @Override
            public void OnGetUserError(Throwable throwable) {
                recipeStateLiveData.postError(throwable);
                recipeStateLiveData.postComplete();
            }
        });
    }
}
