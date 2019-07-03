package com.pistudiosofficial.myclass.objects;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatListMasterObject {

    public HashMap<String, ChatListObject> chatHashMap;
    public ArrayList<UserObject> userObjects;
    public ArrayList<String> chatIndex;
    public ArrayList<String>chatCounts;

    public ChatListMasterObject(HashMap<String, ChatListObject> chatHashMap
            , ArrayList<UserObject> userObjects, ArrayList<String> chatIndex, ArrayList<String> chatCounts) {
        this.chatHashMap = chatHashMap;
        this.userObjects = userObjects;
        this.chatIndex = chatIndex;
        this.chatCounts = chatCounts;
    }

    public ChatListMasterObject() {
    }
}
