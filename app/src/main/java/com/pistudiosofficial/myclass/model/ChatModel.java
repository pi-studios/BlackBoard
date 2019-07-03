package com.pistudiosofficial.myclass.model;

import android.content.Intent;

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

import static com.pistudiosofficial.myclass.Common.CURRENT_USER;
import static com.pistudiosofficial.myclass.Common.mREF_chat;
import static com.pistudiosofficial.myclass.Common.mREF_users;

public class ChatModel {
    ChatView view;
    boolean flag = false;
    ChildEventListener childEventListener;
    String nodeString;
    public ChatModel(ChatView view) {
        this.view = view;
    }

    public void performMessageSent(ChatObject chatObject, String node, String recieverUID){
        String newid = mREF_chat.child(node).push().getKey();
        mREF_chat.child(node).child(newid).setValue(chatObject);
        mREF_chat.child(node).child(newid).child("id").setValue(newid);
        mREF_users.child(CURRENT_USER.UID).child("chat_index").child(node).setValue("0");
        mREF_users.child(recieverUID).child("chat_index").child(node).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    int x = Integer.parseInt(dataSnapshot.getValue().toString());
                    x++;
                    mREF_users.child(recieverUID).child("chat_index").child(node).setValue(x);
                }
                else{
                    mREF_users.child(recieverUID).child("chat_index").child(node).setValue("1");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void performChatLoad(String node,ArrayList<ChatObject> chatObjects){
        this.nodeString = node;
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getValue() != null){
                    try {
                        chatObjects.add(dataSnapshot.getValue(ChatObject.class));
                        if (flag){
                            view.chatUpdate();
                            mREF_users.child(CURRENT_USER.UID).child("chat_index").child(node).setValue("0");                        }
                    }catch (Exception e){
                        e.printStackTrace();
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
        };
        mREF_chat.child(node).addChildEventListener(childEventListener);
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

    public void removeListener(){
        mREF_chat.child(nodeString).removeEventListener(childEventListener);
    }

}
