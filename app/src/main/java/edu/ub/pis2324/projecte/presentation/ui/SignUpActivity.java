package edu.ub.pis2324.projecte.presentation.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Toast;

import edu.ub.pis2324.projecte.R;

import edu.ub.pis2324.projecte.databinding.ActivitySignUpBinding;

import edu.ub.pis2324.projecte.presentation.viewmodel.SignUpViewModel;

public class SignUpActivity extends AppCompatActivity {

    private SignUpViewModel SignUpViewModel;

    private ActivitySignUpBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initWidgetListeners();
        initViewModel();
    }

    private void initWidgetListeners(){
        binding.SignUpBtn.setOnClickListener(ignoredView -> {
            SignUpViewModel.SignUp(
                    String.valueOf(binding.UsernameText.getText()),
                    String.valueOf(binding.emailText.getText()),
                    String.valueOf(binding.PasswordText.getText()),
                    String.valueOf(binding.RPasswordText.getText())
            );
        });

    }
    private void initViewModel(){
        SignUpViewModel = new ViewModelProvider(
                this
        ).get(SignUpViewModel.class);

        initObservers();
    }

    private void initObservers(){
        SignUpViewModel.getSignUpState().observe(this, state -> {
            switch (state.getStatus()) {
                case SUCCESS:
                    Toast.makeText(this, "bien", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case ERROR:
                    Toast.makeText(this, "mal", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }


}