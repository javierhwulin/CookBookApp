package edu.ub.pis2324.projecte.presentation.adapters;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.ub.pis2324.projecte.domain.model.entities.Recipe;
import edu.ub.pis2324.projecte.R;



public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.RecipeViewHolder> {
  /* Attributes */

  /* Product list to show in the recycler view */
  private List<Recipe> recipeList;
  /* Listener to callback the activity */
  private final OnRecipeClickListener onRecipeClickListener;



  /* EXERCICI 2 */
  // ...

  /**
   * Interface to callback the activity when a product is clicked.
   */
  public interface OnRecipeClickListener {
    void onRecipeClick(Recipe recipeModel);
  }



  /* EXERCICI 2 */
  // ...

  /**
   * Constructor
   * @param onRecipeClickListener: Listener to callback the activity.
   */
  public RecipeRecyclerViewAdapter(
      OnRecipeClickListener onRecipeClickListener
  ) {
    super();
    this.onRecipeClickListener = onRecipeClickListener;
  }

  public void removeRecipe(int position) {
    recipeList.remove(position);
    notifyItemRemoved(position);
  }

  /**
   * Set the reference to the data displayed in the recycler view.
   * @param recipeModelList: The list of products to be displayed.
   */
  @SuppressLint("NotifyDataSetChanged")
  public void setRecipesData(List<Recipe> recipeModelList) {
    this.recipeList = recipeModelList; // Note that this is a reference, not a copy. It is
                                    // instead modified by the ViewModel directly
    notifyDataSetChanged(); // Reflect the changes in the UI
  }

  /* EXERCICI 2 */
  // ...

  /**
   * Create a ViewHolder object for each data element.
   * @param parent: The parent ViewGroup. In this case, the RecyclerView.
   * @param viewType: The type of the view. In this case, it is not used.
   */
  @NonNull
  @Override
  public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    /* Create a ViewHolder object for this data element */
    View view = LayoutInflater
        .from(parent.getContext())
        .inflate(R.layout.item_rv_recipe, parent, false);

    /* Return the ViewHolder object */
    return new RecipeViewHolder(view,
      position -> onRecipeClickListener.onRecipeClick(recipeList.get(position))
    );
  }



  /**
   * Bind the actual data of the Product to the ViewHolder object created in
   * onCreateViewHolder.
   * @param holder The ViewHolder which should be updated to represent the contents of the
   *        item at the given position in the data set.
   * @param position The position of the item within the adapter's data set.
   */
  @Override
  public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
    /* Get the data from the data element */
    Recipe r = recipeList.get(position);
    String name = r.getName();
    String duration = Integer.toString(r.getDuration());
    String calories = r.getNutritionInfo();
    boolean isPremium = r.isPremium();
    String imageUrl = r.getImageUrl();

    /* Set the data to the ViewHolder */
    holder.tvRecipeName.setText(name);
    holder.tvRecipeTime.setText(duration);
    holder.tvRecipeCalories.setText(calories);
    if (isPremium) holder.tvRecipePremium.setVisibility(View.VISIBLE);
    else holder.tvRecipePremium.setVisibility(View.INVISIBLE);

    Picasso.get().load(imageUrl).into(holder.ivRecipeImage); // Internet URL image -> ImageView
  }

  /**
   * Get the number of data elements. Used internally by the RecyclerView.
   * @return The number of data elements.
   */
  @Override
  public int getItemCount() {
    return (recipeList == null) ? 0 : recipeList.size();
  }

  /**
   * ViewHolder class definition.
   */
  public static class RecipeViewHolder extends RecyclerView.ViewHolder {
    private final ImageView ivRecipeImage;
    private final TextView tvRecipeName;
    private final TextView tvRecipeTime;
    private final TextView tvRecipeCalories;
    private final TextView tvRecipePremium;


    /* EXERCICI 2 */

    /**
     * Interface to callback the adapter when a product
     * at some position is clicked.
     */
    public interface OnItemPositionClickListener {
      void onItemPositionClick(int position);
    }



    /**
     * Constructor
     * @param itemView: The view of the ViewHolder.
     * @param onItemPositionClickListener: Listener to callback the adapter.
     */
    public RecipeViewHolder(
      @NonNull View itemView,
      OnItemPositionClickListener onItemPositionClickListener
      /* EXERCICI 2 */
    ) {
      super(itemView);

      ivRecipeImage = itemView.findViewById(R.id.ivRecipeImage);
      tvRecipeName = itemView.findViewById(R.id.tvRecipeName);
      tvRecipeTime = itemView.findViewById(R.id.tvRecipeTime);
      tvRecipeCalories = itemView.findViewById(R.id.tvRecipeCalories);
      tvRecipePremium = itemView.findViewById(R.id.tvIsPremium);


      itemView.setOnClickListener(v ->
        onItemPositionClickListener.onItemPositionClick(getAdapterPosition())
      );

    }
  }
}
