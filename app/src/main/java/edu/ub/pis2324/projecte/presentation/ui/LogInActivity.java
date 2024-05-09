package edu.ub.pis2324.projecte.presentation.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import edu.ub.pis2324.projecte.AppContainer;
import edu.ub.pis2324.projecte.databinding.ActivityLogInBinding;
import edu.ub.pis2324.projecte.presentation.viewmodel.LogInViewModel;
import edu.ub.pis2324.projecte.App;

public class LogInActivity extends AppCompatActivity {

    private LogInViewModel logInViewModel;

    private AppContainer appContainer;

    private ActivityLogInBinding binding;

    /**
     * Called when the activity is being created.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("LogInActirevity", "onCreate");
        super.onCreate(savedInstanceState);
        /* Set view binding */
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        App app = (App) getApplication();
        appContainer = app.getAppContainer();
        setContentView(binding.getRoot());

        /* Initializations */
        initWidgetListeners();
        initViewModel();
    }

    /**
     * Initialize the listeners of the widgets.
     */
    private void initWidgetListeners() {
        binding.logInBtn.setOnClickListener(ignoredView -> {
            // Delegate the log-in logic to the viewmodel
            logInViewModel.logIn(
                    String.valueOf(binding.UsernameText.getText()),
                    String.valueOf(binding.PasswordText.getText())
            );
        });

        binding.signUpBtn.setOnClickListener(ignoredView -> {
            // Start the sign-up activity
            Log.i("LogInActivity", "Starting SignUpActivity");
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Initialize the viewmodel and its observers.
     */
    private void initViewModel() {
        /* Init viewmodel */
        logInViewModel = new ViewModelProvider(
               this, new LogInViewModel.Factory(appContainer.logInUsecase)
        ).get(LogInViewModel.class);

        initObservers();
    }

    /**
     * Initialize the observers of the viewmodel.
     */
    private void initObservers() {
        /* Observe the login state */
        logInViewModel.getLogInState().observe(this, logInState -> {
            // Whenever there's a change in the login state of the viewmodel
            switch (logInState.getStatus()) {
                case LOADING:
                    binding.logInBtn.setEnabled(false);
                    break;
                case SUCCESS:
                    assert logInState.getData() != null;
                    Log.i("LogInActivity", "Log in success");
                    Intent intent = new Intent(this, RecipesListActivity.class);
                    intent.putExtra("CLIENT_ID", logInState.getData().getUsername());
                    intent.putExtra("PREMIUM", logInState.getData().isPremium());
                    startActivity(intent);
                    finish();
                    break;
                case ERROR:
                    assert logInState.getError() != null;
                    String errorMessage = logInState.getError().getMessage();
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                    binding.logInBtn.setEnabled(true);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + logInState.getStatus());
            }
        });
    }

}
