package edu.ub.pis2324.projecte.presentation.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import edu.ub.pis2324.projecte.databinding.ActivityGetPremiumBinding;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.presentation.viewmodel.GetPremiumViewModel;

public class GetPremiumActivity extends AppCompatActivity {

    private GetPremiumViewModel getPremiumViewModel;

    private ActivityGetPremiumBinding binding;

    private String clientId;
    private boolean premiumStatus;

    /**
     * Called when the activity is being created.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Set view binding */
        binding = ActivityGetPremiumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        clientId = getIntent().getStringExtra("CLIENT_ID");
        premiumStatus = getIntent().getBooleanExtra("PREMIUM_STATUS", false);

        /* Initializations */
        initWidgetListeners();
        initViewModel();
    }

    /**
     * Initialize the listeners of the widgets.
     */
    private void initWidgetListeners() {
        binding.getPremiumBtn.setOnClickListener(ignoredView -> {
            // Delegate the log-in logic to the viewmodel
            getPremiumViewModel.getPremium(clientId);
            premiumStatus = !premiumStatus;
        });
    }

    /**
     * Initialize the viewmodel and its observers.
     */
    private void initViewModel() {
        /* Init viewmodel */
        getPremiumViewModel = new ViewModelProvider(
               this
        ).get(GetPremiumViewModel.class);

        initObservers();
    }

    /**
     * Initialize the observers of the viewmodel.
     */
    private void initObservers() {
        /* Observe the login state */
        getPremiumViewModel.getPremiumState().observe(this, premiumState -> {
            // Whenever there's a change in the login state of the viewmodel
            switch (premiumState.getStatus()) {
                case LOADING:
                    binding.getPremiumBtn.setEnabled(false);
                    break;
                case SUCCESS:
                    finish();
                    break;
                case ERROR:
                    assert premiumState.getError() != null;
                    String errorMessage = premiumState.getError().getMessage();
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                    binding.getPremiumBtn.setEnabled(true);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + premiumState.getStatus());
            }
        });
    }

}
