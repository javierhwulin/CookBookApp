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
import edu.ub.pis2324.projecte.databinding.ActivityChangePhotoBinding;
import edu.ub.pis2324.projecte.domain.exceptions.AppThrowable;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.presentation.viewmodel.ChangePhotoViewModel;
import edu.ub.pis2324.projecte.presentation.viewmodel.SharedViewModel;

import androidx.navigation.NavController;
public class ChangePhotoFragment extends Fragment {

    private ChangePhotoViewModel changePhotoViewModel;
    private AppContainer appContainer;
    private ActivityChangePhotoBinding binding;

    private SharedViewModel sharedViewModel;

    private boolean Photo = false;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityChangePhotoBinding.inflate(inflater, container, false);
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
            changePhotoViewModel.ChangePhoto(
                    new ClientId(sharedViewModel.getClientName().getValue()),
                    String.valueOf(binding.TextNewPhoto.getText())
            );
        });

    }
    private void initViewModel() {
        changePhotoViewModel = new ViewModelProvider(
                this, new ChangePhotoViewModel.Factory(appContainer.changePhotoUseCase)
        ).get(ChangePhotoViewModel.class);

        initObservers();
    }

    private void initObservers() {
        changePhotoViewModel.getChangePhotoState().observe(getViewLifecycleOwner(), changeState -> {
            switch (changeState.getStatus()) {
                case SUCCESS:
                    Toast.makeText(getContext(), "Photo changed", Toast.LENGTH_SHORT).show();
                    sharedViewModel.setPhotoUrl(String.valueOf(binding.TextNewPhoto.getText()));
                    navController.navigateUp();
                    break;
                case ERROR:
                    Toast.makeText(getContext(), ((AppThrowable) changeState.getError()).getErrorName(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }
}