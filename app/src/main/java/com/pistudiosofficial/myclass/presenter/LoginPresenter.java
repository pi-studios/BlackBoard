package com.pistudiosofficial.myclass.presenter;

import com.pistudiosofficial.myclass.UserObject;
import com.pistudiosofficial.myclass.model.LoginModel;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.LoginPresenterInterface;
import com.pistudiosofficial.myclass.view.LoginActivityView;

public class LoginPresenter implements LoginPresenterInterface {

    LoginActivityView view;
    LoginModel loginModel;
    public LoginPresenter(LoginActivityView view) {
        this.view = view;
        loginModel = new LoginModel(this);
    }

    public void performSignUp(String getEmailSign, String getPasswordSign, String getPhoneSign, String getNameSign, String getAdminLevelSign, String getRollSign){
        UserObject userObject = new UserObject(getEmailSign, getPhoneSign, getNameSign, getAdminLevelSign, getRollSign);
        loginModel.signup(getEmailSign,getPasswordSign,userObject);
    }

    public void performLogin(String getEmailLogin,String getPasswordLogin){
        loginModel.login(getEmailLogin,getPasswordLogin);
    }


    @Override
    public void loginSuccess() {
        view.successLogin();
    }

    @Override
    public void loginFailed() {
        view.showErrorFailed();
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
