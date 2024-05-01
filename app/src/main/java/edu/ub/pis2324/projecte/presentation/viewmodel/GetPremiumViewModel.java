package edu.ub.pis2324.projecte.presentation.viewmodel;

import androidx.lifecycle.ViewModel;

import edu.ub.pis2324.projecte.data.repositories.UserRepository;
import edu.ub.pis2324.projecte.domain.model.entities.User;
import edu.ub.pis2324.projecte.utils.livedata.StateLiveData;

public class GetPremiumViewModel extends ViewModel {
    /* Attributes */
    private final UserRepository userRepository;
    /* LiveData */
    private final StateLiveData<User> premiumState;

    /* Constructor */
    public GetPremiumViewModel() {
        userRepository = new UserRepository();
        premiumState = new StateLiveData<>();
    }

    public StateLiveData<User> getPremiumState() {
        return premiumState;
    }

    public void getPremium(String username) {

        userRepository.updateUser(
                username,
                new UserRepository.OnUpdateUserListener() {
                    @Override
                    public void OnUpdateUserSuccess() {
                        premiumState.postSuccess(null);
                    }
                    @Override
                    public void OnUpdateUserError(Throwable throwable) {
                        premiumState.postError(throwable);
                    }
                }
        );
    }
}