package edu.ub.pis2324.projecte.presentation.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Toast;

import edu.ub.pis2324.projecte.R;
import edu.ub.pis2324.projecte.data.repositories.UserRepository;
import edu.ub.pis2324.projecte.databinding.ActivityChangeUsernameBinding;
import edu.ub.pis2324.projecte.domain.model.entities.User;
import edu.ub.pis2324.projecte.presentation.viewmodel.ChangeUsernameViewModel;


public class ChangeUsernameActivity extends AppCompatActivity {

    private ChangeUsernameViewModel ChangeUsernameViewModel;

    private ActivityChangeUsernameBinding binding;

    private User usuari; //TEMPORAL
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);
        binding = ActivityChangeUsernameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //TODO: Usuari MOCK, canviar per usuari real amb Fragments, per parametre o global.
        usuari = new User("Marc", "admin", "Aaaa");


        initWidgetListeners();
        initViewModel();
        initObservers();

    }
    private void initWidgetListeners() {
        binding.TextUsername.setText(usuari.getUsername());
        binding.Changebtn.setOnClickListener(ignoredView -> {
            ChangeUsernameViewModel.ChangeUsername(usuari, binding.TextNewUsername.getText().toString());
        });
    }
    private void initViewModel() {
        ChangeUsernameViewModel = new ViewModelProvider(
                this
        ).get(ChangeUsernameViewModel.class);
    }

    private void initObservers(){
        ChangeUsernameViewModel.getChangeUsernameState().observe(this, state -> {
            switch (state.getStatus()) {
                case SUCCESS:
                    finish();
                    break;
                case ERROR:
                    Toast.makeText(this, state.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}