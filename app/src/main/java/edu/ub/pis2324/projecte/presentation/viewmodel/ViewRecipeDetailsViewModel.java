package edu.ub.pis2324.projecte.presentation.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import edu.ub.pis2324.projecte.domain.model.entities.Recipe;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.domain.model.values.RecipeId;
import edu.ub.pis2324.projecte.domain.usecases.HistorialUsecase;


public class ViewRecipeDetailsViewModel extends ViewModel {

    /* EXERCICI 1 */
    private final HistorialUsecase historialUsecase;

    private final MutableLiveData<Integer> startState;

    /* Constructor */
    public ViewRecipeDetailsViewModel(HistorialUsecase historialUsecase) {
        super();
        startState = new MutableLiveData<>(0);
        this.historialUsecase = historialUsecase;
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

    public void addHistorial(ClientId clientId, RecipeId recipeId){
        Log.i("ViewRecipeDetailsViewModel", "addHistorial: " + clientId + " " + recipeId);
        historialUsecase.add(clientId, recipeId)
                .subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
                .observeOn(io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(
                        success -> Log.i("ViewRecipeDetailsViewModel", "addHistorial: Success"),
                        error -> Log.e("ViewRecipeDetailsViewModel", "addHistorial: Error", error)
                );
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private final HistorialUsecase historialUsecase;

        public Factory(HistorialUsecase historialUsecase) {
            this.historialUsecase = historialUsecase;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(ViewRecipeDetailsViewModel.class)) {
                return (T) new ViewRecipeDetailsViewModel(historialUsecase);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
