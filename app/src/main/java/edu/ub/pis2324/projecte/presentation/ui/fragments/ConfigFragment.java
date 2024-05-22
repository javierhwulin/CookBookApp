package edu.ub.pis2324.projecte.presentation.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import edu.ub.pis2324.projecte.domain.usecases.implementation.ChangePhotoUseCaseImpl;
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
    

    private static final int PICK_IMAGE = 1;
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
        initObservers();
    }


    private void initWidgetListeners() {
        binding.changeUsernameBtn.setOnClickListener(ignoredView -> {
            navController.navigate(R.id.action_configFragment_to_changeUsernameFragment);
        });
        binding.changePasswordBtn.setOnClickListener(ignoredView -> {
            navController.navigate(R.id.action_configFragment_to_changePasswordFragment);
        });

        binding.changePfpBtn.setOnClickListener(ignoredView -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        });


        binding.changePremium.setOnClickListener(ignoredView -> {
            navController.navigate(R.id.action_configFragment_to_changePremiumFragment);
        });

        binding.logOut.setOnClickListener(ignoredView -> {
            navController.navigate(R.id.action_configFragment_to_logInFragment);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            // Use the Uri object to display the selected image
            Picasso.get().load(selectedImage).into(binding.ivProfileImage);
            configViewModel.ChangePhoto(sharedViewModel.getEmail().getValue(), selectedImage);
        }
    }

    private void initViewModel() {
        configViewModel = new ViewModelProvider(
                this, new ConfigViewModel.Factory(appContainer.changePhotoUseCase)
        ).get(ConfigViewModel.class);
    }

    private void initObservers() {
        configViewModel.getChangePhotoState().observe(getViewLifecycleOwner(), stateData -> {
            switch (stateData.getStatus()) {
                case SUCCESS:
                    //Convert Uri to String
                    sharedViewModel.setPhotoUrl(stateData.getData().toString());
                    break;
                case ERROR:
                    // Handle error
                    break;
            }
        });
    }
}