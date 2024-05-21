package edu.ub.pis2324.projecte.presentation.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import edu.ub.pis2324.projecte.domain.model.entities.Recipe;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.domain.model.values.RecipeId;
import edu.ub.pis2324.projecte.domain.usecases.HistorialUsecase;
import edu.ub.pis2324.projecte.utils.livedata.StateLiveData;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RecentRecipesViewModel extends ViewModel {
    private final HistorialUsecase historialUsecase;
    private final CompositeDisposable compositeDisposable;
    /* LiveData */
    private final StateLiveData<List<Recipe>> historyState;

    /* Constructor */
    public RecentRecipesViewModel(HistorialUsecase historialUsecase) {
        super();
        this.historialUsecase = historialUsecase;
        historyState = new StateLiveData<>();
        compositeDisposable = new CompositeDisposable();
    }

    /**
     * Returns the state of the products being fetched
     * @return the state of the products being fetched
     */
    public StateLiveData<List<Recipe>> getRecipesState() {
        return historyState;
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

    public void handleFetchRecentRecipesSuccess(List<Recipe> recipes) {
        historyState.postSuccess(recipes);
    }

    public void handleFetchRecentRecipesError(Throwable throwable) {
        historyState.postError(throwable);
    }

    public void removeFromHistorial(String username, String recipe) {
        ClientId clientId = new ClientId(username);
        RecipeId recipeId = new RecipeId(recipe);
        Disposable d = historialUsecase.remove(clientId, recipeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::handleRemoveFromHistorialSuccess,
                        this::handleRemoveFromHistorialError
                );
        compositeDisposable.add(d);
    }

    public void handleRemoveFromHistorialSuccess(Boolean success) {
        historyState.postSuccess(null);
    }

    public void handleRemoveFromHistorialError(Throwable throwable) {
        historyState.postError(throwable);
    }

   public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private final HistorialUsecase historialUsecase;

        public Factory(HistorialUsecase historialUsecase) {
            this.historialUsecase = historialUsecase;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if(modelClass.isAssignableFrom(RecentRecipesViewModel.class)){
                return (T) new RecentRecipesViewModel(historialUsecase);
            }else{
                throw new IllegalArgumentException("ViewModel Not Found");
            }
        }
    }
}
