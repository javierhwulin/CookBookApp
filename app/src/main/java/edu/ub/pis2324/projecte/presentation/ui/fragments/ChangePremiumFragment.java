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
import edu.ub.pis2324.projecte.databinding.ActivityChangePremiumBinding;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.presentation.viewmodel.ChangePremiumViewModel;
import edu.ub.pis2324.projecte.presentation.viewmodel.SharedViewModel;

import androidx.navigation.NavController;
public class ChangePremiumFragment extends Fragment {

    private ChangePremiumViewModel changePremiumViewModel;
    private AppContainer appContainer;
    private ActivityChangePremiumBinding binding;

    private SharedViewModel sharedViewModel;

    private boolean premium = false;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityChangePremiumBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appContainer = ((App) getActivity().getApplication()).getAppContainer();
        navController = Navigation.findNavController(view);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        premium = sharedViewModel.getIsPremium().getValue();

        if(premium){
            binding.getPremiumBtn.setText("Aconsegueix ser Premium!");
        }
        else{
            binding.getPremiumBtn.setText("Deixa de ser Premium");
        }
        initWidgetListeners();
        initViewModel();
    }

    private void initWidgetListeners() {
        binding.getPremiumBtn.setOnClickListener(ignoredView -> {
            changePremiumViewModel.ChangePremium(
                    new ClientId(sharedViewModel.getClientName().getValue()),
                    !premium
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
        changePremiumViewModel.getChangePremiumState().observe(getViewLifecycleOwner(), changeState -> {
            switch (changeState.getStatus()) {
                case SUCCESS:
                    Toast.makeText(getContext(), "Premium changed", Toast.LENGTH_SHORT).show();
                    premium = !premium;
                    sharedViewModel.setIsPremium(premium);
                    navController.navigateUp();
                    break;
                case ERROR:
                    Toast.makeText(getContext(), changeState.getError().getMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }
}
