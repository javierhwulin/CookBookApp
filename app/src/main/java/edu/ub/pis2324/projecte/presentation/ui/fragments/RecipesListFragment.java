package edu.ub.pis2324.projecte.presentation.ui.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.ub.pis2324.projecte.App;
import edu.ub.pis2324.projecte.AppContainer;
import edu.ub.pis2324.projecte.R;
import edu.ub.pis2324.projecte.databinding.ActivityRecipesListBinding;
import edu.ub.pis2324.projecte.domain.model.entities.Recipe;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.presentation.adapters.RecipeRecyclerViewAdapter;
import edu.ub.pis2324.projecte.presentation.viewmodel.RecipesListViewModel;
import edu.ub.pis2324.projecte.presentation.viewmodel.SharedViewModel;

import androidx.navigation.NavController;
public class RecipesListFragment extends Fragment {
    /* Constants */
    private static final String RECYCLER_VIEW_STATE = "recycler_view_state";

    /* Attributes */
    private AppContainer appContainer;
    /* This activity's view model */
    private RecipesListViewModel recipeViewModel;
    /* View binding */
    private ActivityRecipesListBinding binding;
    /* Client id */
    private ClientId clientId;
    /* Adapter for the recycler view of products */
    private RecipeRecyclerViewAdapter rvRecipesAdapter;
    /* LayoutManager for the recycler view of products */
    private RecyclerView.LayoutManager rvLayoutManager;
    private Parcelable rvStateParcelable; // to save state of the rv's layout manager (scroll)

    private SharedViewModel sharedViewModel;

    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityRecipesListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appContainer = ((App) getActivity().getApplication()).getAppContainer();
        navController = Navigation.findNavController(view);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        initWidgetListeners();
        initRecyclerView();
        initViewModel();
    }

    /**
     * Lifecycle method called when the activity is being resumed.
     */
    public void onResume() {
        super.onResume();
        if (rvStateParcelable != null) {
            rvLayoutManager.onRestoreInstanceState(rvStateParcelable);
        }
    }

    /**
     * Lifecycle method called when the activity is being paused.
     * @param state the bundle to save the state of the activity
     */
    public void onSaveInstanceState(@NonNull Bundle state) {
        super.onSaveInstanceState(state);
        rvStateParcelable = rvLayoutManager.onSaveInstanceState();
        state.putParcelable(RECYCLER_VIEW_STATE, rvStateParcelable);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            rvStateParcelable = savedInstanceState.getParcelable(RECYCLER_VIEW_STATE);
        }
    }

    /**
     * Initialize the listeners of the widgets.
     */
    private void initWidgetListeners() {
        /* Search view */
        binding.svRecipes.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryText) {
                recipeViewModel.fetchRecipesByName(queryText);
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
                recipeViewModel.fetchRecipesCatalog();
                return true;
            }
        });
    }

    /**
     * Initialize the recycler view.
     */
    private void initRecyclerView() {
        rvLayoutManager = new LinearLayoutManager(getActivity());
        binding.rvRecipes.setLayoutManager(rvLayoutManager);

        initRecyclerViewAdapter();
    }

    /**
     * Initialize the recycler view adapter.
     */
    private void initRecyclerViewAdapter() {
        rvRecipesAdapter = new RecipeRecyclerViewAdapter(
                recipe -> startViewRecipeDetailsFragment(recipe)
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
        Log.i("RecipesListActivity", "initViewModel");

        recipeViewModel = new ViewModelProvider(
                this, new RecipesListViewModel.Factory(appContainer.recipeViewUsecase)
        ).get(RecipesListViewModel.class.getName(), RecipesListViewModel.class);

        recipeViewModel.fetchRecipesCatalog();

        initObservers();
    }

    /**
     * Initialize the observers of the view model.
     */
    private void initObservers() {
        /* Observe the state of the products' catalog */
        recipeViewModel.getRecipesState().observe(getActivity(), state -> {
            switch (state.getStatus()) {
                case SUCCESS:
                    assert state.getData() != null;
                    showNoRecipesAvailable(state.getData().isEmpty());
                    rvRecipesAdapter.setRecipesData(state.getData());
                    break;
                case ERROR:
                    showNoRecipesAvailable(true);
                    break;
                default:
                    break;
            }


        });

        /* Observe the state of the product being hidden */
        recipeViewModel.getHiddenRecipeState().observe(getActivity(), hiddenProductPosition -> {
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
     */
    private void startViewRecipeDetailsFragment(Recipe recipe) {
        sharedViewModel.setRecipe(recipe);
        navController.navigate(R.id.action_recipesListFragment_to_viewRecipeDetailsFragment);
    }
}