package com.pistudiosofficial.myclass.model;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.pistudiosofficial.myclass.objects.UserObject;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.SignUpPresenterInterface;

import static com.pistudiosofficial.myclass.Common.FIREBASE_USER;
import static com.pistudiosofficial.myclass.Common.mAUTH;
import static com.pistudiosofficial.myclass.Common.mREF_users;

public class SignUpModel {

    SignUpPresenterInterface presenter;

    public SignUpModel(SignUpPresenterInterface presenter) {
        this.presenter = presenter;
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
                            mREF_users.child(userObject.UID).child("lower_name").setValue(userObject.Name.toLowerCase());
                        } else {
                            presenter.signupFailed();
                        }
                    }
                });
    }
}
