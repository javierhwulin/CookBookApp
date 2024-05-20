package edu.ub.pis2324.projecte.presentation.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import edu.ub.pis2324.projecte.App;
import edu.ub.pis2324.projecte.R;
import edu.ub.pis2324.projecte.AppContainer;
import edu.ub.pis2324.projecte.databinding.ActivityLogInBinding;
import edu.ub.pis2324.projecte.presentation.ui.RecipesListActivity;
import edu.ub.pis2324.projecte.presentation.ui.SignUpActivity;
import edu.ub.pis2324.projecte.presentation.viewmodel.LogInViewModel;
import edu.ub.pis2324.projecte.presentation.viewmodel.SharedViewModel;

import androidx.navigation.NavController;
public class LogInFragment extends Fragment {

    private LogInViewModel logInViewModel;
    private AppContainer appContainer;
    private ActivityLogInBinding binding;

    private SharedViewModel sharedViewModel;

    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityLogInBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appContainer = ((App) getActivity().getApplication()).getAppContainer();
        navController = Navigation.findNavController(view);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        initWidgetListeners();
        initViewModel();
    }

    private void initWidgetListeners() {
        binding.logInBtn.setOnClickListener(ignoredView -> {
            // Delegate the log-in logic to the viewmodel
            logInViewModel.logIn(
                    String.valueOf(binding.UsernameText.getText()),
                    String.valueOf(binding.PasswordText.getText())
            );
        });

        binding.signUpBtn.setOnClickListener(ignoredView -> {
            // Navigate to the SignUpFragment
            NavHostFragment.findNavController(this).navigate(R.id.action_logInFragment_to_signUpFragment);
        });
    }

    private void initViewModel() {
        /* Init viewmodel */
        logInViewModel = new ViewModelProvider(
                this, new LogInViewModel.Factory(appContainer.logInUsecase)
        ).get(LogInViewModel.class);

        initObservers();
    }

    private void initObservers() {
        /* Observe the login state */
        logInViewModel.getLogInState().observe(getViewLifecycleOwner(), logInState -> {
            // Whenever there's a change in the login state of the viewmodel
            switch (logInState.getStatus()) {
                case LOADING:
                    binding.logInBtn.setEnabled(false);
                    break;
                case SUCCESS:
                    assert logInState.getData() != null;
                    Log.i("LogInFragment", "Log in success");

                    sharedViewModel.setClientName(logInState.getData().getUsername());
                    sharedViewModel.setIsPremium(logInState.getData().isPremium());
                    navController.navigate(R.id.action_logInFragment_to_recipesListFragment);
                    break;
                case ERROR:
                    assert logInState.getError() != null;
                    String errorMessage = logInState.getError().getMessage();
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                    binding.logInBtn.setEnabled(true);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + logInState.getStatus());
            }
        });
    }
}