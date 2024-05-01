package edu.ub.pis2324.projecte.domain.usecases.implementation;

import java.util.HashMap;
import java.util.Objects;

import edu.ub.pis2324.projecte.data.repositories.UserRepository;
import edu.ub.pis2324.projecte.data.repositories.RecipeRepository;
import edu.ub.pis2324.projecte.domain.model.repositories.IRecipeRepository;
import edu.ub.pis2324.projecte.domain.model.repositories.IUserRepository;
import edu.ub.pis2324.projecte.utils.livedata.StateLiveData;

import edu.ub.pis2324.projecte.domain.model.entities.Recipe;
import edu.ub.pis2324.projecte.domain.model.entities.User;
import edu.ub.pis2324.projecte.domain.model.values.Record;

public class HistorialUsecaseImpl {
    private final IUserRepository userRepository;
    private final IRecipeRepository recipeListService;
    private final StateLiveData<User> userStateLiveData;
    private final StateLiveData<Recipe> recipeStateLiveData;

    public HistorialUsecaseImpl(IUserRepository userRepository, IRecipeRepository recipeListService) {
        this.userRepository = userRepository;
        this.recipeListService = recipeListService;
        this.userStateLiveData = new StateLiveData<>();
        this.recipeStateLiveData = new StateLiveData<>();
    }

    private static final class RecordsHolder {
        static final HashMap<User, Record> records = new HashMap<>();
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
                    userStateLiveData.postError(new NullPointerException("User is null"));
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
                        recipeListService.getRecipe(recipeId, new RecipeRepository.OnGetRecipeListener() {
                            @Override
                            public void OnGetRecipeSuccess(Recipe recipe) {
                                if (recipe != null) {
                                    record.addRecipe(recipe);
                                    recipeStateLiveData.postSuccess(recipe);
                                } else {
                                    recipeStateLiveData.postError(new NullPointerException("Recipe is null"));
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
