package com.pistudiosofficial.myclass.view;


import com.pistudiosofficial.myclass.objects.ChatListObject;
import com.pistudiosofficial.myclass.objects.UserObject;

import java.util.ArrayList;
import java.util.HashMap;

public interface ChatListView {

    void ChatLoaded(HashMap<String, ChatListObject> chatHaskMap,ArrayList<UserObject>userObjects);
    void chatUpdated();

}
