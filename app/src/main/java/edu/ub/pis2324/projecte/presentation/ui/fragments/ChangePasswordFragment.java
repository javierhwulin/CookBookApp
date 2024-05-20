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
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import edu.ub.pis2324.projecte.App;
import edu.ub.pis2324.projecte.AppContainer;
import edu.ub.pis2324.projecte.databinding.ActivityChangePasswordBinding;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.presentation.viewmodel.ChangePasswordViewModel;
import edu.ub.pis2324.projecte.presentation.viewmodel.SharedViewModel;

import androidx.navigation.NavController;
public class ChangePasswordFragment extends Fragment {

    private ChangePasswordViewModel changePasswordViewModel;
    private AppContainer appContainer;
    private ActivityChangePasswordBinding binding;

    private SharedViewModel sharedViewModel;

    private NavController navController;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityChangePasswordBinding.inflate(inflater, container, false);
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
        binding.Changebtn.setOnClickListener(ignoredView -> {
            changePasswordViewModel.ChangePassword(
                    new ClientId(sharedViewModel.getClientName().getValue()),
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
        changePasswordViewModel.getChangePasswordState().observe(getViewLifecycleOwner(), changeState -> {
            switch (changeState.getStatus()) {
                case SUCCESS:
                    Toast.makeText(getContext(), "Password changed", Toast.LENGTH_SHORT).show();
                    navController.navigateUp();
                    break;
                case ERROR:
                    Toast.makeText(getContext(), changeState.getError().getMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }
}