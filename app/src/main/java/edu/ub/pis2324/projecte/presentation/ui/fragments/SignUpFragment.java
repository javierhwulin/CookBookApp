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
import androidx.navigation.fragment.NavHostFragment;

import edu.ub.pis2324.projecte.App;
import edu.ub.pis2324.projecte.AppContainer;
import edu.ub.pis2324.projecte.databinding.ActivitySignUpBinding;
import edu.ub.pis2324.projecte.domain.model.entities.Recipe;
import edu.ub.pis2324.projecte.presentation.viewmodel.SharedViewModel;
import edu.ub.pis2324.projecte.presentation.viewmodel.SignUpViewModel;

public class SignUpFragment extends Fragment {

    private SignUpViewModel SignUpViewModel;
    private AppContainer appContainer;
    private ActivitySignUpBinding binding;

    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivitySignUpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appContainer = ((App) getActivity().getApplication()).getAppContainer();
        navController = Navigation.findNavController(view);

        initWidgetListeners();
        initViewModel();

    }

    private void initWidgetListeners() {
        binding.SignUpBtn.setOnClickListener(ignoredView -> {
            SignUpViewModel.SignUp(
                    String.valueOf(binding.UsernameText.getText()),
                    String.valueOf(binding.emailText.getText()),
                    String.valueOf(binding.PasswordText.getText()),
                    String.valueOf(binding.RPasswordText.getText())
            );
        });
    }

    private void initViewModel() {
        SignUpViewModel = new ViewModelProvider(
                this,
                new SignUpViewModel.Factory(appContainer.signUpUsecase)
        ).get(SignUpViewModel.class);

        initObservers();
    }

    private void initObservers() {
        SignUpViewModel.getSignUpState().observe(getViewLifecycleOwner(), state -> {
            switch (state.getStatus()) {
                case COMPLETE:
                    Toast.makeText(getActivity(), "Usuari Creat", Toast.LENGTH_SHORT).show();
                    navController.navigateUp();
                    break;
                case ERROR:
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }
}