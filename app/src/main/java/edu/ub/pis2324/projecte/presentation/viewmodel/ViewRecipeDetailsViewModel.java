package edu.ub.pis2324.projecte.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import edu.ub.pis2324.projecte.presentation.ui.ViewRecipeDetailsActivity;


public class ViewRecipeDetailsViewModel extends ViewModel {

    /* EXERCICI 1 */

    private final MutableLiveData<Boolean> startState;
    private final ViewRecipeDetailsActivity viewRecipeDetailsActivity;

    /* Constructor */
    public ViewRecipeDetailsViewModel() {
        super();
        startState = new MutableLiveData<>(false);
        viewRecipeDetailsActivity = new ViewRecipeDetailsActivity();
    }



    public LiveData<Boolean> getStartState() {
        return startState;
    }



    public void startRecipe() {
        startState.postValue(true);
    }



}
