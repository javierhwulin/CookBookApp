package edu.ub.pis2324.projecte.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.domain.model.values.RecipeId;
import edu.ub.pis2324.projecte.domain.usecases.HistorialUsecase;
import edu.ub.pis2324.projecte.domain.model.values.Record;
import edu.ub.pis2324.projecte.utils.livedata.StateLiveData;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RecentRecipesViewModel extends ViewModel {
    private final HistorialUsecase historialUsecase;
    private final CompositeDisposable compositeDisposable;
    /* LiveData */
    private final StateLiveData<Record> recipesState;  // products' list
    private final MutableLiveData<Integer> hiddenRecipeState;

    /* Constructor */
    public RecentRecipesViewModel(HistorialUsecase historialUsecase) {
        super();
        this.historialUsecase = historialUsecase;
        recipesState = new StateLiveData<>();
        hiddenRecipeState = new MutableLiveData<>();
        compositeDisposable = new CompositeDisposable();
    }

    /**
     * Returns the state of the products being fetched
     * @return the state of the products being fetched
     */
    public StateLiveData<Record> getRecipesState() {
        return recipesState;
    }

    /**
     * Returns the state of the product being hidden
     * @return
     */
    public LiveData<Integer> getHiddenRecipeState() {
        return hiddenRecipeState;
    }

    /**
     * Fetches the products from a data store
     */
    public void fetchRecentRecipes(String username) {
        ClientId id = new ClientId(username);
        Disposable d = historialUsecase.get(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        recipes -> handleFetchRecentRecipesSuccess(recipes),
                        throwable -> handleFetchRecentRecipesError(throwable)
                );
        compositeDisposable.add(d);
    }

    public void handleFetchRecentRecipesSuccess(Record recipes) {
        recipesState.postSuccess(recipes);
    }

    public void handleFetchRecentRecipesError(Throwable throwable) {
        recipesState.postError(throwable);
    }

    public void removeFromHistorial(String username, String recipe) {
        ClientId clientId = new ClientId(username);
        RecipeId recipeId = new RecipeId(recipe);
        Disposable d = historialUsecase.remove(clientId, recipeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ignored -> handleRemoveFromHistorialSuccess(),
                        throwable -> handleRemoveFromHistorialError(throwable)
                );
        compositeDisposable.add(d);
    }

    public void handleRemoveFromHistorialSuccess() {
        hiddenRecipeState.postValue(1);
    }

    public void handleRemoveFromHistorialError(Throwable throwable) {
        recipesState.postError(throwable);
    }
}
