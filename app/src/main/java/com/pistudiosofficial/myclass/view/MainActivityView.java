package com.pistudiosofficial.myclass.view;

import com.pistudiosofficial.myclass.ClassObject;
import com.pistudiosofficial.myclass.StudentClassObject;

import java.util.ArrayList;

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

}
