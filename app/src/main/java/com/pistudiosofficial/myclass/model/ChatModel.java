package com.pistudiosofficial.myclass.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pistudiosofficial.myclass.objects.ChatObject;
import com.pistudiosofficial.myclass.objects.UserObject;
import com.pistudiosofficial.myclass.view.ChatView;

import java.util.ArrayList;

import static com.pistudiosofficial.myclass.Common.mREF_chat;
import static com.pistudiosofficial.myclass.Common.mREF_users;

public class ChatModel {
    ChatView view;
    boolean flag = false;
    public ChatModel(ChatView view) {
        this.view = view;
    }

    public void performMessageSent(ChatObject chatObject, String node){
        mREF_chat.child(node).push().setValue(chatObject);
    }

    public void performChatLoad(String node,ArrayList<ChatObject> chatObjects){
        mREF_chat.child(node).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getValue() != null){
                    chatObjects.add(dataSnapshot.getValue(ChatObject.class));
                    if (flag){
                        view.chatUpdate();
                    }
                }
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
        });

        mREF_chat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                view.chatLoadSuccess(chatObjects);
                flag = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void performChatUserDataLoad(String UID){
        mREF_users.child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                view.chatUserDataLoad(dataSnapshot.getValue(UserObject.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
