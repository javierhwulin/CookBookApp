package edu.ub.pis2324.projecte.domain.usecases;

import edu.ub.pis2324.projecte.domain.usecases.implementation.HistorialUsecaseImpl;

public interface HistorialUsecase {
    void addRecordtoUser(String username, String password);
    void addRecipe(String username, String password, String recipeId);
    void getRecipe(String username, String password, String recipeId, HistorialUsecaseImpl.OnGetRecipeListener listener);
    void removeRecipe(String username, String password, String recipeId);
    void clearRecord(String username, String password);
}

