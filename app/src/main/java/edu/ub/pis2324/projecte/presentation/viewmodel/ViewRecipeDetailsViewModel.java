package edu.ub.pis2324.projecte.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import edu.ub.pis2324.projecte.domain.model.entities.Recipe;


public class ViewRecipeDetailsViewModel extends ViewModel {

    /* EXERCICI 1 */

    private final MutableLiveData<Integer> startState;

    /* Constructor */
    public ViewRecipeDetailsViewModel() {
        super();
        startState = new MutableLiveData<>(0);
    }

    public LiveData<Integer> getStartState() {
        return startState;
    }

    public void startRecipe(Recipe recipe, boolean premium) {
        if(!recipe.isPremium()) startState.postValue(1);
        else{
            if(premium) startState.postValue(1);
            else startState.postValue(2);
        }
    }

    public void updateStartState(int state){
        startState.postValue(state);
    }
}
