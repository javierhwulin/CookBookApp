package edu.ub.pis2324.projecte.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import edu.ub.pis2324.projecte.data.repositories.RecipeRepository;
import edu.ub.pis2324.projecte.domain.model.entities.Recipe;
import edu.ub.pis2324.projecte.domain.usecases.RecipeViewUsecase;
import edu.ub.pis2324.projecte.utils.livedata.StateLiveData;
import io.reactivex.rxjava3.disposables.CompositeDisposable;


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

    /**
     * Fetches the products from a data store
     */
    public void fetchRecipesCatalog() {
        compositeDisposable.add(recipeView.getRecipes()
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

    public void hideRecipe(Recipe recipe) {
        /* EXERCICI 2: NO TOCAR */
        int position = recipes.indexOf(recipe);
        recipes.remove(position);
        hiddenRecipeState.postValue(position);
    }
}
