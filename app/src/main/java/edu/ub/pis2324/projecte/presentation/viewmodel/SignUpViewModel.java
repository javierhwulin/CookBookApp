package edu.ub.pis2324.projecte.presentation.viewmodel;

import androidx.lifecycle.ViewModel;

import edu.ub.pis2324.projecte.livedata.StateLiveData;

public class SignUpViewModel extends ViewModel {

    private final StateLiveData<Void> signUpState;

    public SignUpViewModel (){
        super();
        signUpState = new StateLiveData<>();
    }


    public StateLiveData<Void> getSignUpState (){
        return signUpState;
    }


}
