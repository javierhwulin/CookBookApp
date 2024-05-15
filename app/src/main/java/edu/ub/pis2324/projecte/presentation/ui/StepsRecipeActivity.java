package edu.ub.pis2324.projecte.presentation.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import edu.ub.pis2324.projecte.databinding.ActivityStepsRecipeBinding;
import edu.ub.pis2324.projecte.domain.model.entities.Recipe;
import edu.ub.pis2324.projecte.presentation.viewmodel.StepsRecipeViewModel;

public class StepsRecipeActivity extends AppCompatActivity {
    /* Attributes */

    /* ViewModel */
    private StepsRecipeViewModel stepsRecipeViewModel;
    /* View binding */
    private ActivityStepsRecipeBinding binding;


    /**
     * Called when the activity is being created.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Set view binding */
        binding = ActivityStepsRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Recipe recipe = (Recipe) getIntent()
                .getParcelableExtra("RECIPE");

        String[] stepsArray = recipe.getSteps().split("-");


        /* Initializations */
        assert recipe != null;
        initWidgets(stepsArray);
        initWidgetListeners(stepsArray);
        initViewModel();
    }

    /**
     * Initialize the values of the widgets
     * @param steps The steps of the recipe
     */
    private void initWidgets(String[] steps) {
        binding.tvStepNumber.setText("Step 1");
        binding.tvStepDescription.setText(steps[0]);
        binding.btnPrevious.setEnabled(false);
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
    private void initViewModel() {
        stepsRecipeViewModel = new ViewModelProvider(this)
                .get(StepsRecipeViewModel.class);
        initObservers();
    }

    /**
     * Initialize the observers of the view model
     */
    private void initObservers() {
        stepsRecipeViewModel.getStepsState().observe(this, stepsState -> {
            binding.tvStepNumber.setText("Step " + stepsState.intValue());
            binding.tvStepDescription.setText(stepsState.toString());

            if (stepsState.intValue() == 0){
                binding.btnPrevious.setEnabled(false);
            } else {
                binding.btnPrevious.setEnabled(true);
            }

            if (stepsState.intValue() == stepsState.toString().length()){
                binding.btnNext.setEnabled(false);
            } else {
                binding.btnNext.setEnabled(true);
            }
        });
    }
}
