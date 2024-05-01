package edu.ub.pis2324.projecte.presentation.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;

import edu.ub.pis2324.projecte.databinding.ActivityViewRecipeDetailsBinding;
import edu.ub.pis2324.projecte.domain.model.entities.Recipe;
import edu.ub.pis2324.projecte.presentation.viewmodel.ViewRecipeDetailsViewModel;

public class ViewRecipeDetailsActivity extends AppCompatActivity {
    /* Attributes */

    /* ViewModel */
    private ViewRecipeDetailsViewModel viewRecipeDetailsViewModel;
    /* View binding */
    private ActivityViewRecipeDetailsBinding binding;

    /**
     * Called when the activity is being created.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Set view binding */
        binding = ActivityViewRecipeDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Recipe recipe = (Recipe) getIntent()
                .getParcelableExtra("RECIPE");
        boolean premium = getIntent().getBooleanExtra("PREMIUM", false);

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
        binding.tvDetailsRecipeDuration.setText(String.valueOf(recipe.getDuration()));
        binding.tvDetailsRecipeCalories.setText(String.valueOf(recipe.getNutritionInfo()));
    }

    /**
     * Initialize the listeners of the widgets
     * @param recipe The product model whose details are being shown.
     */
    private void initWidgetListeners(Recipe recipe, boolean premium) {

        binding.btnStart.setOnClickListener(v -> {
            viewRecipeDetailsViewModel.startRecipe(recipe, premium);
        });
    }

    /**
     * Initialize the view model
     */
    private void initViewModel() {
        viewRecipeDetailsViewModel = new ViewModelProvider(this)
                .get(ViewRecipeDetailsViewModel.class);
        initObservers();
    }

    /**
     * Initialize the observers of the view model
     */
    private void initObservers() {
        viewRecipeDetailsViewModel.getStartState().observe(this, startState -> {
            if (startState.intValue() == 1){
                Toast.makeText(this, "Button clicked", Toast.LENGTH_SHORT).show();
                //Hacer un intent a la pantalla del paso por paso
            } else if (startState.intValue() == 2){
                Toast.makeText(this, "Hazte premium para acceder a esta receta", Toast.LENGTH_SHORT).show();
            }
        });
    }

}