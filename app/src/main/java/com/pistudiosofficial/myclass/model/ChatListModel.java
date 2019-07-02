package com.pistudiosofficial.myclass.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pistudiosofficial.myclass.objects.ChatListObject;
import com.pistudiosofficial.myclass.objects.ChatObject;
import com.pistudiosofficial.myclass.objects.UserObject;
import com.pistudiosofficial.myclass.view.ChatListView;

import java.util.ArrayList;
import java.util.HashMap;

import static com.pistudiosofficial.myclass.Common.mREF_chat;
import static com.pistudiosofficial.myclass.Common.mREF_users;

public class ChatListModel {

    ChatListView view;
    ArrayList<String> chatIndexes, chatcounts;
    HashMap<String, ChatListObject> chatHashMap;
    ArrayList<UserObject> userObjects;
    boolean flag = false;
    public ChatListModel(ChatListView view, ArrayList<UserObject> userObjects,
                         HashMap<String, ChatListObject> objectHashMap ) {
        this.view = view;
        this.chatHashMap = objectHashMap;
        this.userObjects = userObjects;
        this.chatcounts = new ArrayList<>();
        this.chatIndexes = new ArrayList<>();
    }

    public void performChatListLoad(String UID){
        mREF_users.child(UID).child("chat_index").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s: dataSnapshot.getChildren()){
                    if (s.child("counts_"+UID).getValue() != null&& s.getValue() != null){
                        chatIndexes.add(s.getKey());
                        chatcounts.add(s.child("counts_"+UID).getValue().toString());
                        mREF_users.child(UID).child("chat_index").child("counts_"+UID).setValue("0");
                    }
                }
                for (int i = 0; i<chatIndexes.size(); i++){
                   chatLastText(chatIndexes.get(i),chatcounts.get(i));
                   chatUserObject(chatIndexes.get(i),UID);
                }
                flag = true;

                view.ChatLoaded(chatHashMap,userObjects);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void chatLastText(String key,String count){

        // Need to count chat in list
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ChatObject chatObject = dataSnapshot.getValue(ChatObject.class);
                ChatListObject chatListObject= new ChatListObject(chatObject.message,count);
                chatHashMap.put(key,chatListObject);
                if (flag){view.chatUpdated();}
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Handle possible errors.
            }
        };
        Query lastQuery = mREF_chat.child(key).orderByKey().limitToLast(1);
        lastQuery.addListenerForSingleValueEvent(valueEventListener);
    }
    private void chatUserObject(String key, String UID){
        String recieverUID = key.replace(UID,"");
        Log.i("TAG",recieverUID);
        mREF_users.child(recieverUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    userObjects.add(dataSnapshot.getValue(UserObject.class));
                    if (flag){view.chatUpdated();}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
