package edu.ub.pis2324.projecte.presentation.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;

public class ViewRecipeDetailsActivity extends AppCompatActivity {
  /* Attributes */

  /* ViewModel */
  private ViewRecipeDetailsViewModel viewProductDetailsViewModel;
  /* View binding */
  private ActivityRecipeDetailsBinding binding;

  /**
   * Called when the activity is being created.
   * @param savedInstanceState
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    /* Set view binding */
    binding = ActivityViewProductDetailsBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    Product product = (Product) getIntent()
        .getParcelableExtra("PRODUCT");

    /* Initializations */
    assert product != null;
    initWidgets(product);
    initWidgetListeners(product);
    initViewModel();
  }

  /**
   * Initialize the values of the widgets
   * @param product The product model whose details are being shown.
   */
  private void initWidgets(Product product) {
    Picasso.get().load(product.getImageUrl()).into(binding.ivDetailsProductImage);
    binding.tvDetailsProductName.setText(product.getName());
    binding.tvDialogProductDescription.setText(product.getDescription());
    binding.tvDetailsProductPrice.setText(product.getPrice().toString());
    binding.ibtnDecreaseItemQuantity.setVisibility(View.GONE);
  }

  /**
   * Initialize the listeners of the widgets
   * @param product The product model whose details are being shown.
   */
  private void initWidgetListeners(Product product) {
    binding.ibtnIncreaseItemQuantity.setOnClickListener(v -> {
      viewProductDetailsViewModel.increaseQuantity();
    });
    binding.ibtnDecreaseItemQuantity.setOnClickListener(v -> {
      viewProductDetailsViewModel.decreaseQuantity();
    });
    binding.btnBuy.setOnClickListener(v -> {
      viewProductDetailsViewModel.buyProduct();
    });
  }

  /**
   * Initialize the view model
   */
  private void initViewModel() {
    viewProductDetailsViewModel = new ViewModelProvider(this)
        .get(ViewProductDetailsViewModel.class);
    initObservers();
  }

  /**
   * Initialize the observers of the view model
   */
  private void initObservers() {
    viewProductDetailsViewModel.getQuantityState().observe(this, quantity -> {
      binding.tvProductQuantity.setText(String.valueOf(quantity));
        if (quantity > 1) {
            binding.ibtnDecreaseItemQuantity.setVisibility(View.VISIBLE);
        } else {
            binding.ibtnDecreaseItemQuantity.setVisibility(View.GONE);
        }
    });
    viewProductDetailsViewModel.getBuyState().observe(this, buyState -> {
      if (buyState) {
        Toast.makeText(this, "Product bought!", Toast.LENGTH_SHORT).show();
        finish();
      }
    });
  }
}