package com.pistudiosofficial.myclass.view;

import com.pistudiosofficial.myclass.objects.ChatListMasterObject;
import com.pistudiosofficial.myclass.objects.ChatListObject;
import com.pistudiosofficial.myclass.objects.ClassObject;
import com.pistudiosofficial.myclass.objects.UserObject;

import java.util.ArrayList;
import java.util.HashMap;

public interface MainActivityView {


    void downloadDataSuccess();
    void downloadDataFailed();

    void addAdminClassSuccess();
    void addAdminClassFailed();

    void endSession(int index);

    void loadAdminClassList(ArrayList<ClassObject> classObjectArrayList);
    void loadUserClassList(ArrayList<ClassObject> classObjects, ArrayList<String> userAttendanceList);

    void addUserClassSuccess();
    void addUserClassFailed();

    void addCollab(int index, String email);
    void transferClass(int index, String email);
    void transferClassFailed();

    void connectionListDownloadSuccess(ArrayList<UserObject> userList);
    void connectionListDownloadFailed();

    void loadHelloSuccess();
    void loadHelloFailed();

    void newChatNotif(ChatListMasterObject chatListMasterObject);

    void notifNewComment();
}
