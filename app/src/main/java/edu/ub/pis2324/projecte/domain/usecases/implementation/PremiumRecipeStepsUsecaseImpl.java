package edu.ub.pis2324.projecte.domain.usecases.implementation;

import edu.ub.pis2324.projecte.data.repositories.RecipeRepository;
import edu.ub.pis2324.projecte.domain.model.entities.Recipe;
import edu.ub.pis2324.projecte.domain.usecases.RecipeStepsUsecase;

public class PremiumRecipeStepsUsecaseImpl implements RecipeStepsUsecase {
    private final Recipe recipe;
    private final RecipeRepository recipeRepository;

    public PremiumRecipeStepsUsecaseImpl(Recipe recipe, RecipeRepository recipeRepository) {
        this.recipe = recipe;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void getSteps(RecipeRepository.OnGetRecipeListener listener){
        recipeRepository.getRecipe(recipe.getName(), listener);
    }
}
