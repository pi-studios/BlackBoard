package com.pistudiosofficial.myclass.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pistudiosofficial.myclass.view.MainActivityView;

import java.util.HashMap;

import static com.pistudiosofficial.myclass.Common.CHECK_NEW_COMMENT;
import static com.pistudiosofficial.myclass.Common.CHECK_NEW_COMMENT_POST;
import static com.pistudiosofficial.myclass.Common.CURRENT_CLASS_ID_LIST;
import static com.pistudiosofficial.myclass.Common.LOG;
import static com.pistudiosofficial.myclass.Common.mREF_classList;

public class LiveMainModel {

    MainActivityView mainActivityView;

    public LiveMainModel(MainActivityView mainActivityView) {
        this.mainActivityView = mainActivityView;
    }

    public void performCheckRead(String admin_type, String UID){
        if (admin_type.equals("admin")){
            adminCheck(UID);
        }
        if (admin_type.equals("user")){
            userCheck(UID);
        }
    }
    private void adminCheck(String UID){
            mREF_classList.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean flag;
                    for (String s:CURRENT_CLASS_ID_LIST){
                        flag = false;
                        for (DataSnapshot d:dataSnapshot.child(s).child("admin_index").child(UID).getChildren()){
                                CHECK_NEW_COMMENT.put(d.getKey(),true);
                                flag=true;
                        }
                        if (flag){CHECK_NEW_COMMENT_POST.add(2);}
                        else{CHECK_NEW_COMMENT_POST.add(0);}
                    }
                    mainActivityView.notifNewComment();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

    }
    private void userCheck(String UID){
        mREF_classList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean flag;

                for (String s:CURRENT_CLASS_ID_LIST){
                    flag = false;
                    for (DataSnapshot p:dataSnapshot.child(s).child("student_index").child(UID).getChildren()){
                         if (!p.getKey().equals("new_post")){
                            CHECK_NEW_COMMENT.put(p.getKey(),true);
                            Log.i("TAG",p.getKey());
                            flag = true;
                         }
                    }
                    if (dataSnapshot.child(s).child("student_index").child(UID).child("new_post")
                            .getValue().toString().equals("true")){
                        CHECK_NEW_COMMENT_POST.add(1);
                        Log.i("TAG",1+"");
                    }
                    else {
                        if (flag){CHECK_NEW_COMMENT_POST.add(2);Log.i("TAG",2+"");}
                        else {CHECK_NEW_COMMENT_POST.add(0);}
                    }
                }
                mainActivityView.notifNewComment();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
