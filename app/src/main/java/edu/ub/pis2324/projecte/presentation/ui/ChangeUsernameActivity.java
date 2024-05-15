package edu.ub.pis2324.projecte.presentation.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import edu.ub.pis2324.projecte.App;
import edu.ub.pis2324.projecte.AppContainer;
import edu.ub.pis2324.projecte.R;
import edu.ub.pis2324.projecte.databinding.ActivityChangeUsernameBinding;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.presentation.viewmodel.ChangeUsernameViewModel;
import edu.ub.pis2324.projecte.presentation.viewmodel.LogInViewModel;

public class ChangeUsernameActivity extends AppCompatActivity {

    private ChangeUsernameViewModel changeUsernameViewModel;

    private AppContainer appContainer;

    private ActivityChangeUsernameBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);
        binding = ActivityChangeUsernameBinding.inflate(getLayoutInflater());
        appContainer = ((App) getApplication()).getAppContainer();
        setContentView(binding.getRoot());


        initWidgetListeners();
        initViewModel();
        binding.TextUsername.setText("Juan");
    }



    private void initWidgetListeners() {
        //TODO: USERNAME HARDCODED. FRAGMENT?
        binding.Changebtn.setOnClickListener(ignoredView -> {
            changeUsernameViewModel.ChangeUsername(
                    new ClientId(String.valueOf(binding.TextUsername.getText())),
                    String.valueOf(binding.TextNewUsername.getText())
            );
        });
    }

    private void initViewModel() {
        changeUsernameViewModel = new ViewModelProvider(
                this, new ChangeUsernameViewModel.Factory(appContainer.changeUsernameUseCase)
        ).get(ChangeUsernameViewModel.class);

        initObservers();
    }

    private void initObservers() {
        changeUsernameViewModel.getChangeUsernameState().observe(this, userState -> {
            switch (userState.getStatus()) {
                case LOADING:
                    binding.Changebtn.setEnabled(false);
                    break;
                case SUCCESS:
                    Log.i("ChangeUsernameActivity", "Change username success");
                    finish();
                    break;
                case ERROR:
                    String errorMessage = userState.getError().getMessage();
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                    binding.Changebtn.setEnabled(true);
                    break;
            }
        });
    }
}