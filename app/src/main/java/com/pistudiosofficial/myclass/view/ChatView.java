package com.pistudiosofficial.myclass.view;

import com.pistudiosofficial.myclass.objects.ChatObject;
import com.pistudiosofficial.myclass.objects.UserObject;

import java.util.ArrayList;

public interface ChatView {

    void chatLoadSuccess(ArrayList<ChatObject> chatObjects);
    void chatUpdate();
    void sendSuccess();

    void chatUserDataLoad(UserObject userObject);

}
