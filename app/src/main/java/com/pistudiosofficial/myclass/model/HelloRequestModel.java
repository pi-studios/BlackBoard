package com.pistudiosofficial.myclass.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.HelloRequestPresenterInterface;

import static com.pistudiosofficial.myclass.Common.CURRENT_USER;
import static com.pistudiosofficial.myclass.Common.mREF_users;

public class HelloRequestModel {

    HelloRequestPresenterInterface presenter;

    public HelloRequestModel(HelloRequestPresenterInterface presenter) {
        this.presenter = presenter;
    }

    public void performHelloRequestRespond(String UID, boolean hello){
        if(hello){

            mREF_users.child(UID).child("hello").child(CURRENT_USER.UID).setValue("2",
                    new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError == null){
                                mREF_users.child(CURRENT_USER.UID).child("hello").child(UID).setValue("2");
                                if (presenter != null){
                                    presenter.requestAccepted(UID);
                                }
                            }
                            else {
                                if (presenter != null){
                                    presenter.requestActionFailed();
                                }
                            }
                        }
                    });
        }
        else{
            mREF_users.child(CURRENT_USER.UID).child("hello").child(UID).removeValue();
            mREF_users.child(UID).child("hello").child(CURRENT_USER.UID).removeValue();
            if (presenter != null){
                presenter.requestRejected(UID);
            }
        }
    }
}
