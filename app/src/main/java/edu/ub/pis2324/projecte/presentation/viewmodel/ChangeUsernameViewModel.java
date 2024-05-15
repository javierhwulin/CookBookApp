package edu.ub.pis2324.projecte.presentation.viewmodel;

import androidx.lifecycle.ViewModel;

import edu.ub.pis2324.projecte.data.repositories.UserRepository;
import edu.ub.pis2324.projecte.domain.model.entities.User;
import edu.ub.pis2324.projecte.utils.livedata.StateLiveData;

public class ChangeUsernameViewModel extends ViewModel {
    private final UserRepository userRepository;

    private final StateLiveData<User> changeUsernameState;

    public ChangeUsernameViewModel() {
        super();
        this.userRepository = new UserRepository();
        this.changeUsernameState = new StateLiveData<>();

    }

    public StateLiveData<User> getChangeUsernameState() {
        return changeUsernameState;
    }

    public void ChangeUsername(User user, String newUsername){
        userRepository.changeUsername(user, newUsername,new UserRepository.OnChangeUsernameListener() {
            @Override
            public void OnChangeUsernameSuccess(User user) {
                changeUsernameState.postSuccess(user);
            }

            @Override
            public void OnChangeUsernameError(Throwable throwable) {
                changeUsernameState.postError(throwable);
            }
        });
    }
}
