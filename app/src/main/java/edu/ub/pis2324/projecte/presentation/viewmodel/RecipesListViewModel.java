package edu.ub.pis2324.projecte.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import edu.ub.pis2324.projecte.data.repositories.RecipeRepository;
import edu.ub.pis2324.projecte.domain.model.entities.Recipe;
import edu.ub.pis2324.projecte.utils.livedata.StateLiveData;


public class RecipesListViewModel extends ViewModel {
    RecipeRepository recipeRepository;
    private final List<Recipe> recipes;
    /* LiveData */
    private final StateLiveData<List<Recipe>> recipesState;  // products' list
    private final MutableLiveData<Integer> hiddenRecipeState;

    /* Constructor */
    public RecipesListViewModel() {
        super();
        recipeRepository = new RecipeRepository();
        recipes = new ArrayList<>();
        recipesState = new StateLiveData<>();
        hiddenRecipeState = new MutableLiveData<>();
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
        recipeRepository.getAll(new RecipeRepository.OnFetchRecipesListener() {
            @Override
            public void OnFetchRecipes(List<Recipe> gottenRecipes) {
                recipes.clear();
                recipes.addAll(gottenRecipes);
                recipesState.postSuccess(recipes);
            }

            @Override
            public void OnFetchRecipes(Throwable throwable) {
                recipesState.postError(throwable);
            }
        });

    }

    /**
     * Fetches the products using the use case
     */
    public void fetchRecipesByName(String name) {
        recipeRepository.getByName(name, new RecipeRepository.OnFetchRecipesListener() {
            @Override
            public void OnFetchRecipes(List<Recipe> gottenRecipes) {
                recipes.clear();
                recipes.addAll(gottenRecipes);
                recipesState.postSuccess(recipes);
            }

            @Override
            public void OnFetchRecipes(Throwable throwable) {
                recipesState.postError(throwable);
            }
        });
    }

    public void hideRecipe(Recipe recipe) {
        /* EXERCICI 2: NO TOCAR */
        int position = recipes.indexOf(recipe);
        recipes.remove(position);
        hiddenRecipeState.postValue(position);
    }

}
