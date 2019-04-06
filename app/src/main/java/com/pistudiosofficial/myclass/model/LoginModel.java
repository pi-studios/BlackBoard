package com.pistudiosofficial.myclass.model;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.pistudiosofficial.myclass.UserObject;
import com.pistudiosofficial.myclass.presenter.LoginActivityPresenterInterface;

import androidx.annotation.NonNull;

import static com.pistudiosofficial.myclass.Common.FIREBASE_USER;
import static com.pistudiosofficial.myclass.Common.mAUTH;
import static com.pistudiosofficial.myclass.Common.mREF_users;

public class LoginModel {
    LoginActivityPresenterInterface presenter;

    public LoginModel(LoginActivityPresenterInterface presenter) {
        this.presenter = presenter;
    }

    public void login(String email, String password){
        mAUTH.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FIREBASE_USER = mAUTH.getCurrentUser();
                            presenter.loginSuccess();
                        } else {
                            presenter.loginFailed();
                        }
                    }
                });
    }
    public void signup(String getEmailSign, String getPasswordSign, final UserObject userObject){
        mAUTH.createUserWithEmailAndPassword(getEmailSign, getPasswordSign)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FIREBASE_USER = mAUTH.getCurrentUser();
                            presenter.signupSuccess();
                            userObject.UID = mAUTH.getUid();
                            mREF_users.child(userObject.UID).setValue(userObject);
                        } else {
                            presenter.signupFailed();
                        }
                    }
                });
    }

}
