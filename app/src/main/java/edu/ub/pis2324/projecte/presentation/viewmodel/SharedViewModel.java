package edu.ub.pis2324.projecte.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import edu.ub.pis2324.projecte.domain.model.entities.Recipe;

public class SharedViewModel extends ViewModel {

    private final MutableLiveData<String> clientID = new MutableLiveData<>();
    private final MutableLiveData<String> clientName = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isPremium = new MutableLiveData<>();
    private final MutableLiveData<Recipe> recipe = new MutableLiveData<>();

    private final MutableLiveData<String> photoUrl = new MutableLiveData<>();

    public void setClientName(String name) {
        clientName.setValue(name);
    }

    public LiveData<String> getClientName() {
        return clientName;
    }

    public void setIsPremium(Boolean premium) {
        isPremium.setValue(premium);
    }

    public LiveData<Boolean> getIsPremium() {
        return isPremium;
    }

    public void setRecipe(Recipe name) {
        recipe.setValue(name);
    }

    public LiveData<Recipe> getRecipe() {
        return recipe;
    }

    public void setPhotoUrl(String url) {
        photoUrl.setValue(url);
    }

    public LiveData<String> getPhotoUrl() {
        return photoUrl;
    }

    public void setClientID(String id) {
        clientID.setValue(id);
    }
    public LiveData<String> getClientID() {
        return clientID;
    }
}
