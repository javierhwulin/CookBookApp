package edu.ub.pis2324.projecte.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import edu.ub.pis2324.projecte.domain.model.entities.Recipe;
import edu.ub.pis2324.projecte.presentation.ui.StepsRecipeActivity;
import edu.ub.pis2324.projecte.presentation.ui.ViewRecipeDetailsActivity;


public class StepsRecipeViewModel extends ViewModel {

    /* EXERCICI 1 */

    private final MutableLiveData<Integer> stepsState;
    private final StepsRecipeActivity stepsRecipeActivity;

    /* Constructor */
    public StepsRecipeViewModel() {
        super();
        stepsState = new MutableLiveData<>(0);
        stepsRecipeActivity = new StepsRecipeActivity();
    }

    public LiveData<Integer> getStepsState() {
        return stepsState;
    }

    public void nextStep(String [] steps){
        stepsState.postValue(stepsState.getValue()+1);
    }

    public void previousStep(String [] steps){
        stepsState.postValue(stepsState.getValue()-1);
    }
}
