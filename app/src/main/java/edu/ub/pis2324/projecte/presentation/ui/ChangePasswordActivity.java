package edu.ub.pis2324.projecte.presentation.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Toast;

import edu.ub.pis2324.projecte.App;
import edu.ub.pis2324.projecte.AppContainer;
import edu.ub.pis2324.projecte.R;
import edu.ub.pis2324.projecte.databinding.ActivityChangePasswordBinding;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.domain.usecases.ChangePasswordUseCase;
import edu.ub.pis2324.projecte.presentation.viewmodel.ChangePasswordViewModel;

public class ChangePasswordActivity extends AppCompatActivity {
    
    private ChangePasswordViewModel changePasswordViewModel;
    
    private AppContainer appContainer;
    
    private ActivityChangePasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        appContainer = ((App) getApplication()).getAppContainer();
        setContentView(binding.getRoot());
        
        initWidgetListeners();
        initViewModel();
        
    }

    private void initWidgetListeners() {
        //TODO: USERNAME HARDCODED. FRAGMENT?
        binding.Changebtn.setOnClickListener(ignoredView -> {
            changePasswordViewModel.ChangePassword(
                    new ClientId("Maston"),
                    String.valueOf(binding.TextPassword.getText()),
                    String.valueOf(binding.TextNewPassword.getText()),
                    String.valueOf(binding.TextRepeatnewPassword.getText())
            );
        });

    }
    private void initViewModel() {
            changePasswordViewModel = new ViewModelProvider(
                    this, new ChangePasswordViewModel.Factory(appContainer.changePasswordUseCase)
            ).get(ChangePasswordViewModel.class);

            initObservers();
    }

    private void initObservers() {
        changePasswordViewModel.getChangePasswordState().observe(this, changeState -> {
            switch (changeState.getStatus()) {
                case SUCCESS:
                    Toast.makeText(this, "Password changed", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case ERROR:
                    Toast.makeText(this, changeState.getError().getMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }


}