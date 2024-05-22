package edu.ub.pis2324.projecte.presentation.ui.fragments;

import android.content.Intent;
import android.net.Uri;
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
import edu.ub.pis2324.projecte.domain.exceptions.AppThrowable;
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
        Log.e("LogInFragmentPhoto2", "IRL: "+ sharedViewModel.getPhotoUrl());

        logInViewModel.getLogInState().observe(getViewLifecycleOwner(), logInState -> {
            // Whenever there's a change in the login state of the viewmodel
            switch (logInState.getStatus()) {
                case LOADING:
                    binding.logInBtn.setEnabled(false);
                    break;
                case SUCCESS:
                    assert logInState.getData() != null;

                    sharedViewModel.setClientID(logInState.getData().getId().getId());
                    sharedViewModel.setClientName(logInState.getData().getUsername());
                    sharedViewModel.setIsPremium(logInState.getData().isPremium());
                    sharedViewModel.setEmail(logInState.getData().getEmail());
                    logInViewModel.fetchProfileImage(logInState.getData().getEmail());
                    break;
                case ERROR:
                    assert logInState.getError() != null;
                    Toast.makeText(getContext(), logInState.getError().getMessage(), Toast.LENGTH_SHORT).show();
                    binding.logInBtn.setEnabled(true);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + logInState.getStatus());
            }
        });
        logInViewModel.getProfileImageState().observe(getViewLifecycleOwner(), state -> {
            switch (state.getStatus()) {
                case LOADING:
                    // Show a loading spinner
                    break;
                case SUCCESS:
                    // Set the profile image URL in the shared view model
                    sharedViewModel.setPhotoUrl(state.getData().toString());
                    Log.e("LogInFragment", "Profile image URL: " + state.getData().toString());

                    // Navigate to the recipesListFragment after the profile image URL is successfully fetched
                    navController.navigate(R.id.action_logInFragment_to_recipesListFragment);
                    break;
                case ERROR:
                    Log.e("ErrorLogInFragment", "Profile image URL: " + state.getData().toString());
                    break;
            }
        });
    }


}