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

}
