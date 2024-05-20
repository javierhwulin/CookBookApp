package edu.ub.pis2324.projecte.presentation.ui.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import edu.ub.pis2324.projecte.App;
import edu.ub.pis2324.projecte.AppContainer;
import edu.ub.pis2324.projecte.databinding.ActivityRecentRecipesBinding;
import edu.ub.pis2324.projecte.presentation.adapters.RecipeRecyclerViewAdapter;
import edu.ub.pis2324.projecte.presentation.viewmodel.RecentRecipesViewModel;
import edu.ub.pis2324.projecte.presentation.viewmodel.SharedViewModel;

import androidx.navigation.NavController;
public class RecentRecipesFragment extends Fragment {

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

    private AppContainer appContainer;

    private NavController navController;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityRecentRecipesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appContainer = ((App) getActivity().getApplication()).getAppContainer();
        navController = Navigation.findNavController(view);

        //initWidgetListeners();
        initViewModel();
    }

    private void initViewModel() {
        recipeViewModel = new ViewModelProvider(this).get(RecentRecipesViewModel.class);
        //initObservers();
    }


    // TODO: FALTA ADAPTAR EL RESTO DEL CÓDIGO DE LA ACTIVITY. NO LO HE HECHO AÚN PORQUE NO SÉ SI FUNCIONA

}