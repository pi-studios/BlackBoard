package com.pistudiosofficial.myclass.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pistudiosofficial.myclass.objects.UserObject;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.HelloListPresenterInterface;
import com.pistudiosofficial.myclass.view.HelloListView;

import java.util.ArrayList;

import static com.pistudiosofficial.myclass.Common.CURRENT_USER;
import static com.pistudiosofficial.myclass.Common.mREF_users;

public class HelloListModel  {
    HelloListPresenterInterface presenter;

    public HelloListModel(HelloListPresenterInterface presenter) {
        this.presenter = presenter;
    }

    public void performHelloListDownload(){
        ArrayList<String> uid = new ArrayList<>();
        ArrayList<UserObject> userObjects = new ArrayList<>();
        ValueEventListener valueEventListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (String s: uid){
                    userObjects.add(dataSnapshot.child(s).getValue(UserObject.class));
                }
                presenter.helloListLoadSuccess(userObjects);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()){
                   // Log.i("TAG",s.getValue().toString());
                    if(s.getValue().toString().equals("2")){
                        uid.add(s.getKey());
                    }
                }
                mREF_users.addListenerForSingleValueEvent(valueEventListener1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mREF_users.child(CURRENT_USER.UID).child("hello").addListenerForSingleValueEvent(valueEventListener);
    }


}
