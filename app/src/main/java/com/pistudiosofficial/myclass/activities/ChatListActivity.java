package com.pistudiosofficial.myclass.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.adapters.AdapterChatList;
import com.pistudiosofficial.myclass.model.ChatListModel;
import com.pistudiosofficial.myclass.objects.ChatListObject;
import com.pistudiosofficial.myclass.objects.UserObject;
import com.pistudiosofficial.myclass.view.ChatListView;

import java.util.ArrayList;
import java.util.HashMap;

import static com.pistudiosofficial.myclass.Common.CURRENT_USER;

public class ChatListActivity extends AppCompatActivity implements ChatListView {
    ArrayList<UserObject> userObjects;
    HashMap<String, ChatListObject> objectHashMap;
    ChatListModel model;
    RecyclerView recyclerView;
    LinearLayoutManager llm;
    AdapterChatList adapterChatList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);


    }

    @Override
    protected void onStart() {
        super.onStart();

        userObjects = new ArrayList<>();
        objectHashMap = new HashMap<>();
        model = new ChatListModel(this,userObjects,objectHashMap);
        model.performChatListLoad(CURRENT_USER.UID);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void ChatLoaded(HashMap<String, ChatListObject> chatHaskMap, ArrayList<UserObject> userObjects) {
        adapterChatList = new AdapterChatList(chatHaskMap,userObjects,this);
        llm = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recyclerView_chatList);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

    }

    @Override
    public void chatUpdated() {
        recyclerView.setAdapter(adapterChatList);
        adapterChatList.notifyDataSetChanged();
    }
}
