package edu.ub.pis2324.projecte.presentation.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.ub.pis2324.projecte.databinding.ActivityRecentRecipesBinding;
import edu.ub.pis2324.projecte.domain.model.entities.Recipe;
import edu.ub.pis2324.projecte.presentation.adapters.RecipeRecyclerViewAdapter;
import edu.ub.pis2324.projecte.presentation.viewmodel.RecentRecipesViewModel;

public class RecentRecipesActivity extends AppCompatActivity {
/* Constants */
private static final String RECYCLER_VIEW_STATE = "recycler_view_state";

/* Attributes */

/* This activity's view model */
private RecentRecipesViewModel recipeViewModel;

/* View binding */
private ActivityRecentRecipesBinding binding;
/* Client id */
private String clientId;
/* Adapter for the recycler view of products */
private RecipeRecyclerViewAdapter rvRecipesAdapter;
/* LayoutManager for the recycler view of products */
private RecyclerView.LayoutManager rvLayoutManager;
private Parcelable rvStateParcelable; // to save state of the rv's layout manager (scroll)

/**
 * Called when the activity is being created.
 * @param savedInstanceState
 */
@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Set view binding */
        binding = ActivityRecentRecipesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /* Get client id from intent */
        clientId = getIntent().getStringExtra("CLIENT_ID");

        /* Initializations */
        initWidgetListeners();
        initRecyclerView();
        initViewModel();

        recipeViewModel.fetchRecentRecipes(clientId);
        }

/**
 * Lifecycle method called when the activity is being resumed.
 */
protected void onResume() {
        super.onResume();
        if (rvStateParcelable != null) {
        rvLayoutManager.onRestoreInstanceState(rvStateParcelable);
        }
        }

/**
 * Lifecycle method called when the activity is being paused.
 * @param state the bundle to save the state of the activity
 */
protected void onSaveInstanceState(@NonNull Bundle state) {
        super.onSaveInstanceState(state);
        rvStateParcelable = rvLayoutManager.onSaveInstanceState();
        state.putParcelable(RECYCLER_VIEW_STATE, rvStateParcelable);
        }

/**
 * Lifecycle method called when the activity is being restored.
 * @param state the bundle to restore the state of the activity
 */
protected void onRestoreInstanceState(@NonNull Bundle state) {
        super.onRestoreInstanceState(state);
        rvStateParcelable = state.getParcelable(RECYCLER_VIEW_STATE);
        }

/**
 * Initialize the listeners of the widgets.
 */
private void initWidgetListeners() {
        /* Search view */
        binding.svRecipes.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
@Override
public boolean onQueryTextSubmit(String queryText) {
        recipeViewModel.fetchRecentRecipes(queryText);
        return true;
        }
@Override
public boolean onQueryTextChange(String queryText) {
        /*
         * "Hack" per suplir OnCloseListener. Més info:
         *  See: https://stackoverflow.com/questions/9327826/
         *   searchviews-oncloselistener-doesnt-work
         * Altrament fariem simplement: return false.
         */
        if (!queryText.isEmpty()) return false;
        recipeViewModel.fetchRecentRecipes(clientId);
        return true;
        }
        });
        }

/**
 * Initialize the recycler view.
 */
private void initRecyclerView() {
        rvLayoutManager = new LinearLayoutManager(this);
        binding.rvRecipes.setLayoutManager(rvLayoutManager);

        initRecyclerViewAdapter();
        }

/**
 * Initialize the recycler view adapter.
 */
private void initRecyclerViewAdapter() {
        rvRecipesAdapter = new RecipeRecyclerViewAdapter(
        recipe -> startViewRecipeDetailsActivity(recipe)
        );
        binding.rvRecipes.setAdapter(rvRecipesAdapter);
        }

        /**
 * Initialize the view model.
 */
private void initViewModel() {
        /*
         * Get the view model from the ViewModelProvider. If we where to use "new ShoppingViewModel()"
         * the view model would not be aware of the activity's lifecycle. So every time the activity
         * is destroyed and recreated, the view model would be a new one, and we would lose the
         * data that we had in the previous one, when to keep the data is its very own purpose.
         */
        recipeViewModel = new ViewModelProvider(this).get(RecentRecipesViewModel.class);
        initObservers();
        }

/**
 * Initialize the observers of the view model.
 */
private void initObservers() {
        /* Observe the state of the products' catalog */
        recipeViewModel.getRecipesState().observe(this, state -> {
        switch (state.getStatus()) {
        case SUCCESS:
        assert state.getData() != null;
        showNoRecipesAvailable(state.getData().isEmpty());
        rvRecipesAdapter.setRecipesData((state.getData().getAllRecipes()));
        break;
        case ERROR:
        showNoRecipesAvailable(true);
        break;
default:
        break;
        }


        });

        /* Observe the state of the product being hidden */
        recipeViewModel.getHiddenRecipeState().observe(this, hiddenProductPosition -> {
        rvRecipesAdapter.removeRecipe(hiddenProductPosition);
        });
        }

private void showNoRecipesAvailable(boolean mustShow) {
        binding.clNoRecipesAvailable.setVisibility(
        mustShow ? View.VISIBLE : View.GONE
        );
        }

/**
 * Starts the ViewProductDetailsActivity.
 * @param recipe the recipe to be shown
 */
private void startViewRecipeDetailsActivity(Recipe recipe) {
        Intent intent;
        intent = new Intent(this, ViewRecipeDetailsActivity.class);
        boolean premium = getIntent().getBooleanExtra("PREMIUM", false);
        intent.putExtra("CLIENT_ID", clientId);
        intent.putExtra("PREMIUM", premium);
        intent.putExtra("RECIPE", recipe); // Product class implements Parcelable
        startActivity(intent);
        }
}
