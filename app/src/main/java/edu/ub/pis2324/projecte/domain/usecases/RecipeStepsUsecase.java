package edu.ub.pis2324.projecte.domain.usecases;

import edu.ub.pis2324.projecte.data.repositories.RecipeRepository;

public interface RecipeStepsUsecase{
    void getSteps(RecipeRepository.OnGetRecipeListener listener);
}
