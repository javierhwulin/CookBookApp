package edu.ub.pis2324.projecte.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import edu.ub.pis2324.recyclerandsearchviewexample.presentation.ui.ViewProductDetailsActivity;

public class ViewRecipeDetailsViewModel extends ViewModel {
  /* LiveData */
  private final MutableLiveData<Integer> quantityState;
  private final MutableLiveData<Boolean> buyState;
  private final ViewRecipeDetailsActivity viewRecipeDetailsActivity;

  /* Constructor */
  public ViewRecipeDetailsViewModel() {
    super();
    quantityState = new MutableLiveData<>(1);
    buyState = new MutableLiveData<>(false);
    viewRecipeDetailsActivity = new ViewProductDetailsActivity();
  }

  /* EXERCICI 1 */
  public LiveData<Integer> getQuantityState() {
    return quantityState;
  }

    public LiveData<Boolean> getBuyState() {
        return buyState;
    }

  public void increaseQuantity() {
    quantityState.postValue(quantityState.getValue() + 1);

  }

    public void decreaseQuantity() {
        if (quantityState.getValue() > 1) {
        quantityState.postValue(quantityState.getValue() - 1);
        }
    }

    public void buyProduct() {
        buyState.postValue(true);
    }


}
