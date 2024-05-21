package edu.ub.pis2324.projecte.presentation.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import edu.ub.pis2324.projecte.App;
import edu.ub.pis2324.projecte.AppContainer;
import edu.ub.pis2324.projecte.R;
import edu.ub.pis2324.projecte.databinding.ActivityConfigBinding;
import edu.ub.pis2324.projecte.presentation.viewmodel.ConfigViewModel;
import edu.ub.pis2324.projecte.presentation.viewmodel.SharedViewModel;

import androidx.navigation.NavController;

import com.squareup.picasso.Picasso;

public class ConfigFragment extends Fragment {

    private ConfigViewModel configViewModel;
    private AppContainer appContainer;
    private ActivityConfigBinding binding;

    private SharedViewModel sharedViewModel;

    private NavController navController;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityConfigBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appContainer = ((App) getActivity().getApplication()).getAppContainer();
        navController = Navigation.findNavController(view);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        boolean premium = sharedViewModel.getIsPremium().getValue();


        Picasso.get().load(sharedViewModel.getPhotoUrl().getValue()).into(binding.ivProfileImage);
        binding.nomCLient.setText(sharedViewModel.getClientName().getValue());

        if(!premium){
            binding.changePremium.setText("Fes-te premium");
            binding.TextUsuari.setText("Usuari Normal");
        }
        else{
            binding.changePremium.setText("Gestionar premium");
            binding.TextUsuari.setText("Usuari Premium");
        }

        initWidgetListeners();
        initViewModel();
    }


    private void initWidgetListeners() {
        binding.changeUsernameBtn.setOnClickListener(ignoredView -> {
            navController.navigate(R.id.action_configFragment_to_changeUsernameFragment);
        });
        binding.changePasswordBtn.setOnClickListener(ignoredView -> {
            navController.navigate(R.id.action_configFragment_to_changePasswordFragment);
        });

        binding.changePfpBtn.setOnClickListener(ignoredView -> {
            navController.navigate(R.id.action_configFragment_to_changePhotoFragment);
        });

        binding.changePremium.setOnClickListener(ignoredView -> {
            navController.navigate(R.id.action_configFragment_to_changePremiumFragment);
        });

        binding.logOut.setOnClickListener(ignoredView -> {
            navController.navigate(R.id.action_configFragment_to_logInFragment);
        });
    }

    private void initViewModel() {
        configViewModel = new ViewModelProvider(
                this
        ).get(ConfigViewModel.class);
    }
}