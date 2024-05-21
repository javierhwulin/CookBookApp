package edu.ub.pis2324.projecte.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import edu.ub.pis2324.projecte.domain.model.entities.Recipe;
import edu.ub.pis2324.projecte.domain.usecases.RecipeViewUsecase;
import edu.ub.pis2324.projecte.utils.livedata.StateLiveData;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class RecipesListViewModel extends ViewModel {
    /* LiveData */
    private final List<Recipe> recipes = new ArrayList<>();
    private final StateLiveData<List<Recipe>> recipesState;  // products' list
    private final MutableLiveData<Integer> hiddenRecipeState;
    private final RecipeViewUsecase recipeView;
    private final CompositeDisposable compositeDisposable;

    /* Constructor */
    public RecipesListViewModel(RecipeViewUsecase recipeView) {
        super();
        recipesState = new StateLiveData<>();
        hiddenRecipeState = new MutableLiveData<>();
        this.recipeView = recipeView;
        compositeDisposable = new CompositeDisposable();
    }

    /**
     * Returns the state of the products being fetched
     * @return the state of the products being fetched
     */
    public StateLiveData<List<Recipe>> getRecipesState() {
        return recipesState;
    }

    /**
     * Returns the state of the product being hidden
     * @return
     */
    public LiveData<Integer> getHiddenRecipeState() {
        return hiddenRecipeState;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
    /**
     * Fetches the products from a data store
     */
    public void fetchRecipesCatalog() {
        compositeDisposable.add(recipeView.getRecipes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        recipes -> handleFetchRecipesSuccess(recipes),
                        throwable -> handleFetchRecipesError(throwable)
                ));
    }

    /**
     * Fetches the products using the use case
     */
    public void fetchRecipesByName(String name) {
        compositeDisposable.add(recipeView.getRecipeByName(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        recipes -> handleFetchRecipesSuccess(recipes),
                        throwable -> handleFetchRecipesError(throwable)
                ));
    }

    public void handleFetchRecipesSuccess(List<Recipe> recipes) {
        this.recipes.clear();
        this.recipes.addAll(recipes);
        recipesState.postSuccess(recipes);
    }

    public void handleFetchRecipesError(Throwable throwable) {
        recipesState.postError(throwable);
    }


    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private final RecipeViewUsecase recipeView;

        public Factory(RecipeViewUsecase recipeView) {
            this.recipeView = recipeView;
        }
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if(modelClass.isAssignableFrom(RecipesListViewModel.class)){
                return (T) new RecipesListViewModel(recipeView);
            }else{
                throw new IllegalArgumentException("ViewModel Not Found");
            }
        }
    }
}


