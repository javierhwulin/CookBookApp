package edu.ub.pis2324.projecte.presentation.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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


    }
}