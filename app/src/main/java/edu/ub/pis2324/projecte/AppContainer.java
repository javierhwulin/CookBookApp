package edu.ub.pis2324.projecte;

import edu.ub.pis2324.projecte.data.repositories.FirestoreRepositoryFactory;
import edu.ub.pis2324.projecte.data.repositories.RecipeRepository;
import edu.ub.pis2324.projecte.data.repositories.UserRepository;
import edu.ub.pis2324.projecte.domain.model.repositories.AbstractRepositoryFactory;
import edu.ub.pis2324.projecte.domain.services.CheckClientExistsService;
import edu.ub.pis2324.projecte.domain.services.CheckClientIsPremiumService;
import edu.ub.pis2324.projecte.domain.services.implementation.CheckClientExistsServiceImpl;
import edu.ub.pis2324.projecte.domain.services.implementation.CheckClientIsPremiumServiceImpl;
import edu.ub.pis2324.projecte.domain.usecases.ChangePasswordUseCase;
import edu.ub.pis2324.projecte.domain.usecases.ChangePremiumUseCase;
import edu.ub.pis2324.projecte.domain.usecases.ChangeUsernameUseCase;
import edu.ub.pis2324.projecte.domain.usecases.FetchClientUsecase;
import edu.ub.pis2324.projecte.domain.usecases.HistorialUsecase;
import edu.ub.pis2324.projecte.domain.usecases.LogInUsecase;
import edu.ub.pis2324.projecte.domain.usecases.RecipeDescriptionUsecase;
import edu.ub.pis2324.projecte.domain.usecases.RecipeStepsUsecase;
import edu.ub.pis2324.projecte.domain.usecases.RecipeViewUsecase;
import edu.ub.pis2324.projecte.domain.usecases.SettingsUsecase;
import edu.ub.pis2324.projecte.domain.usecases.SignUpUsecase;
import edu.ub.pis2324.projecte.domain.usecases.implementation.ChangePasswordUseCaseImpl;
import edu.ub.pis2324.projecte.domain.usecases.implementation.ChangePremiumUseCaseImpl;
import edu.ub.pis2324.projecte.domain.usecases.implementation.ChangeUsernameUseCaseImpl;
import edu.ub.pis2324.projecte.domain.usecases.implementation.FetchClientUsecaseImpl;
import edu.ub.pis2324.projecte.domain.usecases.implementation.HistorialUsecaseImpl;
import edu.ub.pis2324.projecte.domain.usecases.implementation.LogInUsecaseImpl;
import edu.ub.pis2324.projecte.domain.usecases.implementation.NormalRecipeStepsUsecaseImpl;
import edu.ub.pis2324.projecte.domain.usecases.implementation.RecipeDescriptionUsecaseImpl;
import edu.ub.pis2324.projecte.domain.usecases.implementation.RecipeViewUsecaseImpl;
import edu.ub.pis2324.projecte.domain.usecases.implementation.SettingsUsecaseImpl;
import edu.ub.pis2324.projecte.domain.usecases.implementation.SignUpUsecaseImpl;

public class AppContainer {
    /* Repositories */
    public final AbstractRepositoryFactory repositoryFactory = new FirestoreRepositoryFactory();
    public final UserRepository userRepository = repositoryFactory.createUserRepository();
    public final RecipeRepository recipeRepository = repositoryFactory.createRecipeRepository();
    /* Domain Application Services */
    public final CheckClientExistsService checkUserExistsService = new CheckClientExistsServiceImpl(userRepository);
    public final CheckClientIsPremiumService checkClientIsPremiumService = new CheckClientIsPremiumServiceImpl(userRepository);
    /* Use cases */
    public final FetchClientUsecase fetchClientUseCase = new FetchClientUsecaseImpl(userRepository);
    public final HistorialUsecase historialUsecase = new HistorialUsecaseImpl(userRepository, recipeRepository);
    public final LogInUsecase logInUsecase = new LogInUsecaseImpl(fetchClientUseCase);
    public final RecipeDescriptionUsecase recipeDescriptionUsecase = new RecipeDescriptionUsecaseImpl();
    public final RecipeViewUsecase recipeViewUsecase = new RecipeViewUsecaseImpl(recipeRepository);
    public final SettingsUsecase settingsUsecase = new SettingsUsecaseImpl();
    public final SignUpUsecase signUpUsecase = new SignUpUsecaseImpl(checkUserExistsService, userRepository);

    public final ChangeUsernameUseCase changeUsernameUseCase = new ChangeUsernameUseCaseImpl(fetchClientUseCase);

    public final ChangePasswordUseCase changePasswordUseCase = new ChangePasswordUseCaseImpl(fetchClientUseCase);

    public final ChangePremiumUseCase changePremiumUseCase = new ChangePremiumUseCaseImpl(fetchClientUseCase);
}
