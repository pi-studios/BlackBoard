package com.pistudiosofficial.myclass.model;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pistudiosofficial.myclass.Common;

public class PostInteractionModel {

    public PostInteractionModel() {
    }

    public void likeClicked(String postId, String classId){
        ValueEventListener valueEventListener = new ValueEventListener() {
            int likecount;
            int flag = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s:dataSnapshot.getChildren()){
                    if(s.getKey().equals("like_count")){flag=1;}
                }
                if (flag == 0){
                    likecount = 1;
                }else {
                    likecount = dataSnapshot.child("like_count").getValue(Integer.class);
                    likecount++;
                }
                Common.mREF_classList.child(classId).child("post").child(postId)
                        .child("like").child("like_count").setValue(likecount);
                Common.mREF_classList.child(classId).child("post").child(postId)
                        .child("like").child(Common.CURRENT_USER.UID).setValue("true");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ValueEventListener valueEventListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot != null) {
                    int flag = 0;
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        if(s.getValue()!=null) {
                            if (Common.CURRENT_USER.UID.equals(s.getKey())) {
                                flag = 1;
                            }
                        }
                    }
                    if(flag == 0){
                        Common.mREF_classList.child(classId).child("post").child(postId)
                                .child("like").addListenerForSingleValueEvent(valueEventListener);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        Common.mREF_classList.child(classId).child("post").child(postId)
                .child("like").addListenerForSingleValueEvent(valueEventListener1);
    }

    public void pollClicked(String option, String classId, String postId){
        ValueEventListener valueEventListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count;
                if (dataSnapshot.child(option).getValue() != null){
                    count = dataSnapshot.child(option).getValue(Integer.class);
                    count++;
                }
                else{
                    count = 1;
                }
                Common.mREF_classList.child(classId).child("post")
                        .child(postId).child("options").child(option).setValue(count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ValueEventListener valueEventListener = new ValueEventListener() {
            int flag = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("poll_clicked_user").exists()){
                    for (DataSnapshot s: dataSnapshot.child("poll_clicked_user").getChildren()){
                        if (s.getKey().equals(Common.CURRENT_USER.UID)){
                            flag = 1;
                        }
                    }
                }
                if(flag == 0){
                    Common.mREF_classList.child(classId).child("post")
                            .child(postId).child("options").child("poll_clicked_user")
                            .child(Common.CURRENT_USER.UID).setValue("true");
                    Common.mREF_classList.child(classId).child("post")
                            .child(postId).child("options")
                            .addListenerForSingleValueEvent(valueEventListener1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        Common.mREF_classList.child(classId).child("post")
                .child(postId).child("options").addListenerForSingleValueEvent(valueEventListener);
    }
}