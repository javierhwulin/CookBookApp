package edu.ub.pis2324.projecte.presentation.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import edu.ub.pis2324.projecte.R;
import edu.ub.pis2324.projecte.databinding.ActivityConfigBinding;
import edu.ub.pis2324.projecte.presentation.viewmodel.ConfigViewModel;

public class ConfigActivity extends AppCompatActivity {

    private ConfigViewModel configViewModel;

    private ActivityConfigBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        binding = ActivityConfigBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(true){
            binding.button4.setText("Fes-te premium");
        }
        else{
            binding.button4.setText("Gestionar premium");
        }
        initWidgetListeners();
        initViewModel();


    }

    private void initWidgetListeners(){
        binding.changeUsernameBtn.setOnClickListener(ignoredView -> {
            Intent intent = new Intent(this, ChangeUsernameActivity.class);
            startActivity(intent);
        });
        binding.changePasswordBtn.setOnClickListener(ignoredView -> {
            //Intent intent = new Intent(this, ChangePasswordActivity.class);
            //startActivity(intent);
        });

        binding.changePfpBtn.setOnClickListener(ignoredView -> {
            //Intent intent = new Intent(this, ChangePfpActivity.class);
            //startActivity(intent);
        });
    }

    private void initViewModel(){
        configViewModel = new ViewModelProvider(
                this
        ).get(ConfigViewModel.class);
    }

}

