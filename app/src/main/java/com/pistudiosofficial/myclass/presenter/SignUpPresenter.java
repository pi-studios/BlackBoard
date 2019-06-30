package com.pistudiosofficial.myclass.presenter;

import com.pistudiosofficial.myclass.objects.UserObject;
import com.pistudiosofficial.myclass.model.SignUpModel;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.SignUpPresenterInterface;
import com.pistudiosofficial.myclass.view.SignUpView;

public class SignUpPresenter implements SignUpPresenterInterface {

    SignUpView view;
    SignUpModel signUpModel;
    public SignUpPresenter(SignUpView view) {
        this.view = view;
        signUpModel = new SignUpModel(this);
    }

    public void performSignUp(String getEmailSign, String getPasswordSign, String getPhoneSign, String getNameSign, String getAdminLevelSign, String getRollSign){
        UserObject userObject = new UserObject(getEmailSign, getPhoneSign, getNameSign, getAdminLevelSign, getRollSign);
        signUpModel.signup(getEmailSign,getPasswordSign,userObject);
    }


    @Override
    public void signupSuccess() {
        view.successSignup();
    }

    @Override
    public void signupFailed() {
        view.showErrorFailed();
    }
}
