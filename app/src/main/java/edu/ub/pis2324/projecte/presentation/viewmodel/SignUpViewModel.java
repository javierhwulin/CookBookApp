package edu.ub.pis2324.projecte.presentation.viewmodel;

import androidx.lifecycle.ViewModel;

import edu.ub.pis2324.projecte.data.UserRepository;
import edu.ub.pis2324.projecte.domain.model.entities.User;
import edu.ub.pis2324.projecte.utils.livedata.StateLiveData;

public class SignUpViewModel extends ViewModel {


    private final UserRepository userRepository;
    private final StateLiveData<Void> signUpState;

    public SignUpViewModel (){
        super();
        signUpState = new StateLiveData<>();
        userRepository = new UserRepository();
    }


    public StateLiveData<Void> getSignUpState (){
        return signUpState;
    }

    public void SignUp (String username, String email ,String password, String passwordConfirmation){
        if (!password.equals(passwordConfirmation)){
            signUpState.postError(new Throwable("Contrasenyes no iguals"));
            return;
        }
        User usuari = new User(username, password, email);
        userRepository.addUser(usuari, new UserRepository.OnAddUserListener() {
            @Override
            public void OnAddUserSuccess() {
                signUpState.postSuccess(null);
            }

            @Override
            public void OnAddUserError(Throwable throwable) {
                signUpState.postError(throwable);
            }
        });

    }
}
