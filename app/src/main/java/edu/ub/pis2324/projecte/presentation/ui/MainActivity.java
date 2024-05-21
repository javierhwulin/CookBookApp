package edu.ub.pis2324.projecte.presentation.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.ub.pis2324.projecte.R;
import edu.ub.pis2324.projecte.databinding.ActivityMainBinding;
import edu.ub.pis2324.projecte.presentation.ui.fragments.ConfigFragment;
import edu.ub.pis2324.projecte.presentation.ui.fragments.RecentRecipesFragment;
import edu.ub.pis2324.projecte.presentation.ui.fragments.RecipesListFragment;

public class MainActivity extends AppCompatActivity {
  /* Attributes */
  private NavController navController;
  AppBarConfiguration appBarConfiguration;
  private ActivityMainBinding binding;





  /**
   * Called when the activity is being created.
   * @param savedInstanceState The saved instance state.
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    setSupportActionBar(binding.toolbar);

    /* Initializations */
    initNavigation();
  }

  /**
   * Initialize the navigation.
   */
  private void initNavigation() {
    /* Set up the navigation controller */
    navController = ( (NavHostFragment) getSupportFragmentManager()
            .findFragmentById(R.id.nav_host_fragment_main) )
            .getNavController();

    /*
      Set up the bottom navigation, indicating the fragments
      that are part of the bottom navigation.
    */
    appBarConfiguration = new AppBarConfiguration.Builder(
            R.id.recipesListFragment,
            R.id.recentRecipesFragment,
            R.id.configFragment
    ).build();

    /* Set up navigation with both the action bar and the bottom navigation view */
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);

    /*
      Set up the visibility of the bottom navigation view and the toolbar,
      so we only show the action bar and bottom navigation view when showing
      the fragments that are part of the bottom navigation.
     */
    navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
      setBottomNavigationViewVisibility(destination);
      setToolbarVisibility(destination);
    });
  }

  /**
   * Set the visibility of the bottom navigation view.
   * @param destination
   */
  private void setBottomNavigationViewVisibility(NavDestination destination) {
    /* If the destination is the log in or sign up fragment, hide the bottom navigation view */
    if (destination.getId() == R.id.logInFragment || destination.getId() == R.id.signUpFragment) {
      binding.bottomNavigationView.setVisibility(View.GONE);
    } else {
      binding.bottomNavigationView.setVisibility(View.VISIBLE);
    }
  }

  /**
   * Set the visibility of the toolbar.
   * @param destination
   */
  private void setToolbarVisibility(NavDestination destination) {
    /* If the destination is the log in or sign up fragment, hide the bottom navigation view */
    if (destination.getId() == R.id.logInFragment  || destination.getId() == R.id.signUpFragment) {
      binding.toolbar.setVisibility(View.GONE);
    } else {
      binding.toolbar.setVisibility(View.VISIBLE);
    }
  }

  /**
   * Handle the up navigation.
   * @return
   */
  @Override
  public boolean onSupportNavigateUp() {
    /* Enable the up navigation: the button shown in the left of the action bar */
    return NavigationUI.navigateUp(navController, appBarConfiguration)
            || super.onSupportNavigateUp();
  }
  @Override
  public void onBackPressed() {
    int currentDestinationId = navController.getCurrentDestination().getId();

    if (currentDestinationId == R.id.configFragment || currentDestinationId == R.id.recentRecipesFragment) {
      // Navigate to the recipes list fragment
      navController.navigate(R.id.recipesListFragment);
    } else if (currentDestinationId == R.id.recipesListFragment) {
      // Exit the app
      finish();
    } else {
      // Let the system handle the back press
      super.onBackPressed();
    }
  }
}