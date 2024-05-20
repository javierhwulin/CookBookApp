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
import edu.ub.pis2324.projecte.databinding.ActivityChangeUsernameBinding;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.presentation.viewmodel.ChangeUsernameViewModel;
import edu.ub.pis2324.projecte.presentation.viewmodel.SharedViewModel;

import androidx.navigation.NavController;
public class ChangeUsernameFragment extends Fragment {

    private ChangeUsernameViewModel changeUsernameViewModel;
    private AppContainer appContainer;
    private ActivityChangeUsernameBinding binding;

    private SharedViewModel sharedViewModel;

    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityChangeUsernameBinding.inflate(inflater, container, false);
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
        changeUsernameViewModel.getChangeUsernameState().observe(getViewLifecycleOwner(), changeState -> {
            switch (changeState.getStatus()) {
                case SUCCESS:
                    Toast.makeText(getContext(), "Username changed", Toast.LENGTH_SHORT).show();
                    sharedViewModel.setClientName(String.valueOf(binding.TextNewUsername.getText()));
                    navController.navigateUp();
                    break;
                case ERROR:
                    Toast.makeText(getContext(), changeState.getError().getMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }
}