package edu.ub.pis2324.projecte.presentation.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import edu.ub.pis2324.projecte.App;
import edu.ub.pis2324.projecte.AppContainer;
import edu.ub.pis2324.projecte.R;
import edu.ub.pis2324.projecte.databinding.ActivityChangePasswordBinding;
import edu.ub.pis2324.projecte.databinding.ActivityChangePremiumBinding;
import edu.ub.pis2324.projecte.databinding.ActivityChangePasswordBinding;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.presentation.viewmodel.ChangePasswordViewModel;
import edu.ub.pis2324.projecte.presentation.viewmodel.ChangePremiumViewModel;

public class ChangePremiumActivity extends AppCompatActivity {

    private ChangePremiumViewModel changePremiumViewModel;

    private AppContainer appContainer;

    private ActivityChangePremiumBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_premium);
        binding = ActivityChangePremiumBinding.inflate(getLayoutInflater());
        appContainer = ((App) getApplication()).getAppContainer();
        setContentView(binding.getRoot());

        initWidgetListeners();
        initViewModel();

    }

    private void initWidgetListeners() {
        //TODO: USERNAME HARDCODED. FRAGMENT?
        binding.getPremiumBtn.setOnClickListener(ignoredView -> {
            changePremiumViewModel.ChangePremium(
                    new ClientId("Maston"),
                    true    // luego dar opción de quitar Premium
            );
        });

    }
    private void initViewModel() {
        changePremiumViewModel = new ViewModelProvider(
                this, new ChangePremiumViewModel.Factory(appContainer.changePremiumUseCase)
        ).get(ChangePremiumViewModel.class);

        initObservers();
    }

    private void initObservers() {
        changePremiumViewModel.getChangePremiumState().observe(this, changeState -> {
            switch (changeState.getStatus()) {
                case SUCCESS:
                    Toast.makeText(this, "Premium changed", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case ERROR:
                    Toast.makeText(this, changeState.getError().getMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

}
