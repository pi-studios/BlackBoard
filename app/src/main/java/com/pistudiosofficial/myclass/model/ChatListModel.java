package com.pistudiosofficial.myclass.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pistudiosofficial.myclass.activities.MainActivity;
import com.pistudiosofficial.myclass.objects.ChatListMasterObject;
import com.pistudiosofficial.myclass.objects.ChatListObject;
import com.pistudiosofficial.myclass.objects.ChatObject;
import com.pistudiosofficial.myclass.objects.UserObject;
import com.pistudiosofficial.myclass.view.ChatListView;
import com.pistudiosofficial.myclass.view.MainActivityView;

import java.util.ArrayList;
import java.util.HashMap;

import static com.pistudiosofficial.myclass.Common.mREF_chat;
import static com.pistudiosofficial.myclass.Common.mREF_users;

public class ChatListModel {

    MainActivityView mainActivityView;
    ChatListMasterObject chatListMasterObject;
    int p;
    boolean flag = false;
    public ChatListModel(MainActivityView mainActivityView, ChatListMasterObject chatListMasterObject) {
        this.mainActivityView = mainActivityView;
        this.chatListMasterObject = chatListMasterObject;
    }

    public void performChatListLoad(String UID){
        mREF_users.child(UID).child("chat_index").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s: dataSnapshot.getChildren()){
                    if (s.getValue() != null){
                        chatListMasterObject.chatIndex.add(s.getKey());
                        chatListMasterObject.chatCounts.add(s.getValue().toString());
                    }
                }
                chatUserObject(UID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void chatLastText(){
        p=0;
        for (String key: chatListMasterObject.chatIndex) {
            mREF_chat.child(key).orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        if (dataSnapshot.getValue() != null) {
                            ChatObject chatObject = s.getValue(ChatObject.class);
                            ChatListObject chatListObject =
                                    new ChatListObject(chatObject.message, chatListMasterObject.chatCounts.get(p));
                            p++;
                            chatListMasterObject.chatHashMap.put(key, chatListObject);
                        }
                    }
                    checkNewMessage();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //Handle possible errors.
                }
            });
        }

    }
    private void chatUserObject(String UID){

        mREF_users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    for (String s: chatListMasterObject.chatIndex){
                        String recieverUID = s.replace(UID,"");
                        recieverUID = recieverUID.replace(":","");
                        chatListMasterObject.userObjects.add(dataSnapshot.child(recieverUID).getValue(UserObject.class));
                    }
                    chatLastText();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public ChatListMasterObject getChatListMasterObject(){
        return this.chatListMasterObject;
    }

    private void checkNewMessage(){
        for(String s : chatListMasterObject.chatCounts){
            if (!s.equals("0")){
                flag = true;
            }
        }
        if (flag){
            mainActivityView.newChatNotif(chatListMasterObject);
            flag = false;
        }
    }
}
