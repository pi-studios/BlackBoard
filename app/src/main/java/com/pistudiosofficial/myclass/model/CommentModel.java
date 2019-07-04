package com.pistudiosofficial.myclass.model;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pistudiosofficial.myclass.objects.CommentObject;
import com.pistudiosofficial.myclass.objects.PostObject;
import com.pistudiosofficial.myclass.view.CommentView;

import java.util.ArrayList;

public class CommentModel {
    ChildEventListener childEventListener;
    DatabaseReference postRef;
    CommentView view;
    boolean flag = false;
    public CommentModel(CommentView view) {
        this.view = view;
    }

    public void performCommentLoad(DatabaseReference postRef, PostObject postObject, ArrayList<CommentObject>commentObjects){
        this.postRef = postRef;
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getValue()!=null && !dataSnapshot.getKey().equals("comment_count")) {
                    CommentObject commentObject = dataSnapshot.getValue(CommentObject.class);
                    commentObjects.add(commentObject);
                }
                if (flag){view.commentNotify();}
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                view.commmentLoaded(commentObjects);
                flag = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        postRef.addChildEventListener(childEventListener);
    }

    public void performCommentPost(CommentObject commentObject, DatabaseReference commentRef){
        String id = commentRef.push().getKey();
        commentRef.child(id).setValue(commentObject);
        commentRef.child("comment_count").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    int x = Integer.parseInt(dataSnapshot.getValue().toString());
                    x++;
                    commentRef.child("comment_count").setValue(x);
                }else{
                    commentRef.child("comment_count").setValue("1");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void removeListener(){
        postRef.removeEventListener(childEventListener);
    }
}
