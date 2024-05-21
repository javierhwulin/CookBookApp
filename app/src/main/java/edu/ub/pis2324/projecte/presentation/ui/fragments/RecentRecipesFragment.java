package edu.ub.pis2324.projecte.presentation.ui.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.ub.pis2324.projecte.App;
import edu.ub.pis2324.projecte.AppContainer;
import edu.ub.pis2324.projecte.R;
import edu.ub.pis2324.projecte.databinding.ActivityRecentRecipesBinding;
import edu.ub.pis2324.projecte.domain.model.entities.Recipe;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.presentation.adapters.RecipeRecyclerViewAdapter;
import edu.ub.pis2324.projecte.presentation.viewmodel.RecentRecipesViewModel;
import edu.ub.pis2324.projecte.presentation.viewmodel.SharedViewModel;

import androidx.navigation.NavController;
public class RecentRecipesFragment extends Fragment {

    /* Attributes */
    private static final String RECYCLER_VIEW_STATE = "recycler_view_state";

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

    private AppContainer appContainer;

    private SharedViewModel sharedViewModel;
    private NavController navController;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityRecentRecipesBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appContainer = ((App) getActivity().getApplication()).getAppContainer();
        navController = Navigation.findNavController(view);

        // clientId will be the username of the logged user
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        clientId = sharedViewModel.getClientName().getValue();
        Log.i("RecentRecipesFragment", "clientId: " + clientId);

        initRecyclerView();
        initViewModel();

        recipeViewModel.fetchRecentRecipes(clientId);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.searchView);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setQueryHint(getString(R.string.shop_search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                recipeViewModel.fetchRecipesByName(new ClientId(clientId), query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    recipeViewModel.fetchRecipesByName(new ClientId(clientId), newText);
                }
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        recipeViewModel.fetchRecentRecipes(clientId);
    }

    public void onSaveInstanceState(@NonNull Bundle state) {
        super.onSaveInstanceState(state);
        rvStateParcelable = rvLayoutManager.onSaveInstanceState();
        state.putParcelable(RECYCLER_VIEW_STATE, rvStateParcelable);
    }



    /**
     * Initialize the recycler view.
     */
    private void initRecyclerView() {
        rvLayoutManager = new LinearLayoutManager(getContext());
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
        Log.i("RecentRecipesActivity", "initViewModel");
        Log.i("RecentRecipesActivity", "clientId: " + clientId);
        recipeViewModel = new ViewModelProvider(this, new RecentRecipesViewModel.Factory(appContainer.historialUsecase)).get(RecentRecipesViewModel.class);
        initObservers();
    }

    /**
     * Initialize the observers of the view model.
     */
    private void initObservers() {
        /* Observe the state of the products' catalog */
        recipeViewModel.getRecipesState().observe(getViewLifecycleOwner(), state -> {
            switch (state.getStatus()) {
                case SUCCESS:
                    assert state.getData() != null;
                    showNoRecipesAvailable(state.getData().isEmpty());
                    rvRecipesAdapter.setRecipesData((state.getData()));
                    break;
                case ERROR:
                    showNoRecipesAvailable(true);
                    break;
                default:
                    break;
            }
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
    private void startViewRecipeDetailsFragment(Recipe recipe) {
        sharedViewModel.setRecipe(recipe);
        navController.navigate(R.id.action_recentRecipesFragment_to_viewRecipeDetailsFragment);
    }
}