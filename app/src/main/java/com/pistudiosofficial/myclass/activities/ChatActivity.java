package com.pistudiosofficial.myclass.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.adapters.AdapterChat;
import com.pistudiosofficial.myclass.model.ChatModel;
import com.pistudiosofficial.myclass.objects.ChatObject;
import com.pistudiosofficial.myclass.objects.UserObject;
import com.pistudiosofficial.myclass.view.ChatView;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.pistudiosofficial.myclass.Common.CURRENT_USER;
import static com.pistudiosofficial.myclass.Common.SELECTED_CHAT_UID;
import static com.pistudiosofficial.myclass.Common.SELECTED_PROFILE_UID;

public class ChatActivity extends AppCompatActivity implements ChatView {

    EditText et_chatBox;
    Button bt_chatSend;
    ChatModel model;
    String node;
    ArrayList<ChatObject> chatObjects;
    RecyclerView recyclerView;
    LinearLayoutManager llm;
    AdapterChat adapterChat;
    UserObject chatUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        et_chatBox = findViewById(R.id.et_chatbox);
        bt_chatSend = findViewById(R.id.bt_chatbox_send);
        bt_chatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!et_chatBox.getText().toString().isEmpty()){
                    if (!et_chatBox.getText().toString().equals("")){
                        String time = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
                        int compare = CURRENT_USER.UID.compareTo(SELECTED_CHAT_UID);
                        String node = "";
                        if (compare<0){
                            node = CURRENT_USER.UID+":"+SELECTED_CHAT_UID;
                        }
                        else {
                            node = SELECTED_CHAT_UID+":"+CURRENT_USER.UID;
                        }
                        ChatObject chatObject = new ChatObject(
                                et_chatBox.getText().toString(),
                                CURRENT_USER.UID,
                                SELECTED_CHAT_UID,
                                time);
                        model.performMessageSent(chatObject,node,SELECTED_CHAT_UID);
                        et_chatBox.setText("");
                    }
                }
            }
        });


    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onStart() {
        super.onStart();
        int compare = CURRENT_USER.UID.compareTo(SELECTED_CHAT_UID);
        node = "";
        if (compare<0){
            node = CURRENT_USER.UID+":"+SELECTED_PROFILE_UID;
        }
        else {
            node = SELECTED_CHAT_UID +":"+CURRENT_USER.UID;
        }
        model = new ChatModel(this);
        model.performChatUserDataLoad(SELECTED_CHAT_UID);
        chatObjects = new ArrayList<>();
        recyclerView = findViewById(R.id.reyclerview_message_list);
        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        model.performChatLoad(node,chatObjects);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        model.removeListener();
    }

    @Override
    public void chatLoadSuccess(ArrayList<ChatObject> chatObjects) {
        adapterChat = new AdapterChat(chatObjects,chatUser);
        recyclerView.setAdapter(adapterChat);
    }

    @Override
    public void chatUpdate() {
        llm.scrollToPosition(chatObjects.size()-1);
        adapterChat.notifyDataSetChanged();
    }

    @Override
    public void sendSuccess() {

    }

    @Override
    public void chatUserDataLoad(UserObject userObject) {
        setTitle(userObject.Name);
        chatUser = userObject;
    }
}
