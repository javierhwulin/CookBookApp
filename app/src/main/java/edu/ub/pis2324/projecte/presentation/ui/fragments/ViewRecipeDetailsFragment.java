package edu.ub.pis2324.projecte.presentation.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;

import edu.ub.pis2324.projecte.App;
import edu.ub.pis2324.projecte.R;
import edu.ub.pis2324.projecte.AppContainer;
import edu.ub.pis2324.projecte.databinding.ActivityViewRecipeDetailsBinding;
import edu.ub.pis2324.projecte.domain.model.entities.Recipe;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.presentation.viewmodel.SharedViewModel;
import edu.ub.pis2324.projecte.presentation.viewmodel.ViewRecipeDetailsViewModel;

public class ViewRecipeDetailsFragment extends Fragment {

    private ViewRecipeDetailsViewModel viewRecipeDetailsViewModel;
    private AppContainer appContainer;
    private ActivityViewRecipeDetailsBinding binding;
    private NavController navController;
    private SharedViewModel sharedViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityViewRecipeDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appContainer = ((App) getActivity().getApplication()).getAppContainer();
        navController = Navigation.findNavController(view);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        Recipe recipe = sharedViewModel.getRecipe().getValue();
        boolean premium = sharedViewModel.getIsPremium().getValue();

        /* Initializations */
        assert recipe != null;
        initWidgets(recipe);
        initWidgetListeners(recipe,premium);
        initViewModel();
    }

    /**
     * Initialize the values of the widgets
     * @param recipe The product model whose details are being shown.
     */
    private void initWidgets(Recipe recipe) {
        Picasso.get().load(recipe.getImageUrl()).into(binding.ivDetailsRecipeImage);
        binding.tvDetailsRecipeName.setText(recipe.getName());
        binding.tvDialogRecipeDescription.setText(recipe.getIngredients());
        binding.tvDetailsRecipeDuration.setText(String.valueOf(recipe.getDuration()) + " min");
        binding.tvDetailsRecipeCalories.setText(String.valueOf(recipe.getNutritionInfo()));
    }

    /**
     * Initialize the listeners of the widgets
     * @param recipe The product model whose details are being shown.
     */
    private void initWidgetListeners(Recipe recipe, boolean premium) {
        binding.btnStart.setOnClickListener(v -> {
            viewRecipeDetailsViewModel.startRecipe(recipe, premium);
            viewRecipeDetailsViewModel.addHistorial(new ClientId(sharedViewModel.getClientID().getValue()), recipe.getId());
        });
    }
    private void initViewModel() {
        viewRecipeDetailsViewModel = new ViewModelProvider(this,
                new ViewRecipeDetailsViewModel.Factory(appContainer.historialUsecase))
                .get(ViewRecipeDetailsViewModel.class);
        initObservers();
    }

    private void initObservers() {
        viewRecipeDetailsViewModel.getStartState().observe(getActivity(), startState -> {
            if (startState.intValue() == 1){
                navController.navigate(R.id.action_viewRecipeDetailsFragment_to_stepsRecipeFragment);
                viewRecipeDetailsViewModel.updateStartState(0);
            } else if (startState.intValue() == 2){
                Toast.makeText(getActivity(), "Hazte premium para acceder a esta receta", Toast.LENGTH_SHORT).show();
            }
        });
    }
}