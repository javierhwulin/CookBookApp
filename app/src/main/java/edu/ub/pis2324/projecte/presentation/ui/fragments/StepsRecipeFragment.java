package edu.ub.pis2324.projecte.presentation.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import edu.ub.pis2324.projecte.App;
import edu.ub.pis2324.projecte.AppContainer;
import edu.ub.pis2324.projecte.databinding.ActivityStepsRecipeBinding;
import edu.ub.pis2324.projecte.domain.model.entities.Recipe;
import edu.ub.pis2324.projecte.presentation.viewmodel.SharedViewModel;
import edu.ub.pis2324.projecte.presentation.viewmodel.StepsRecipeViewModel;

public class StepsRecipeFragment extends Fragment {

    private StepsRecipeViewModel stepsRecipeViewModel;

    private SharedViewModel sharedViewModel;
    private AppContainer appContainer;
    private ActivityStepsRecipeBinding binding;

    private NavController navController;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityStepsRecipeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appContainer = ((App) getActivity().getApplication()).getAppContainer();
        navController = Navigation.findNavController(view);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        Recipe recipe = sharedViewModel.getRecipe().getValue();
        String[] stepsArray = recipe.getSteps().split("-");

        assert recipe != null;
        initWidgets(stepsArray);
        initWidgetListeners(stepsArray);
        initViewModel(stepsArray);
    }

    /**
     * Initialize the values of the widgets
     * @param steps The steps of the recipe
     */
    private void initWidgets(String[] steps) {
        binding.tvStepNumber.setText("Step 1");
        binding.tvStepDescription.setText(steps[0]);
        binding.btnPrevious.setVisibility(View.GONE);
    }

    /**
     * Initialize the listeners of the widgets
     * @param steps The steps of the recipe
     */
    private void initWidgetListeners(String[] steps) {

        binding.btnNext.setOnClickListener(v -> {
            stepsRecipeViewModel.nextStep(steps);
        });
        binding.btnPrevious.setOnClickListener(v -> {
            stepsRecipeViewModel.previousStep(steps);
        });
    }

    /**
     * Initialize the view model
     */
    private void initViewModel(String[] steps) {
        stepsRecipeViewModel = new ViewModelProvider(this)
                .get(StepsRecipeViewModel.class);
        initObservers(steps);
    }

    /**
     * Initialize the observers of the view model
     */
    private void initObservers(String[] steps) {
        stepsRecipeViewModel.getStepsState().observe(getActivity(), stepsState -> {
            binding.tvStepNumber.setText("Paso " + (stepsState.intValue()+1));
            binding.tvStepDescription.setText(steps[stepsState.intValue()]);

            if (stepsState.intValue() == 0){
                binding.btnPrevious.setVisibility(View.GONE);
            } else {
                binding.btnPrevious.setVisibility(View.VISIBLE);
            }

            if (stepsState.intValue() == steps.length-1){
                binding.btnNext.setVisibility(View.GONE);
            } else {
                binding.btnNext.setVisibility(View.VISIBLE);
            }
        });
    }
}