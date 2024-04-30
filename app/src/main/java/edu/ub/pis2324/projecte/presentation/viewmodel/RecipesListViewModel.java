package edu.ub.pis2324.projecte.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import edu.ub.pis2324.projecte.data.services.RecipeListService;
import edu.ub.pis2324.projecte.domain.model.entities.Recipe;
import edu.ub.pis2324.projecte.utils.livedata.StateLiveData;


public class RecipesListViewModel {
    RecipeListService recipeListService;
    private final List<Recipe> recipes;
    /* LiveData */
    private final StateLiveData<List<Recipe>> recipesState;  // products' list
    private final MutableLiveData<Integer> hiddenRecipeState;

    /* Constructor */
    public RecipesListViewModel() {
        super();
        recipeListService = new RecipeListService();
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
    public void fetchProductsCatalog() {
        recipeListService.getAll(new RecipeListService.OnGetAllRecipesListener() {
            @Override
            public void onFetchProducts(List<Recipe> gottenRecipes) {
                recipes.clear();
                recipes.addAll(gottenRecipes);
                recipesState.postSuccess(recipes);
            }
        });

    }

    /**
     * Fetches the products using the use case
     */
    public void fetchRecipesByName(String name) {
        recipeListService.getByName(name, new ProductStoreService.OnFetchProductsListener() {
            @Override
            public void onFetchProducts(List<Product> gottenProducts) {
                recipes.clear();
                recipes.addAll(gottenProducts);
                recipesState.postSuccess(recipes);
            }
        });
    }

    /**
     * Hides a product in the products' list
     * @param product the product to be hidden
     */
    public void hideProduct(Product product) {
        /* EXERCICI 2: NO TOCAR */
        int position = recipes.indexOf(product);
        recipes.remove(position);
        hiddenRecipeState.postValue(position);
    }
}
