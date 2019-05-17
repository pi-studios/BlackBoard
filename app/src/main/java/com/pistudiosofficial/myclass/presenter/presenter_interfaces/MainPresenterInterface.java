package com.pistudiosofficial.myclass.presenter.presenter_interfaces;

import com.pistudiosofficial.myclass.ClassObject;
import com.pistudiosofficial.myclass.StudentClassObject;

import java.util.ArrayList;

public interface MainPresenterInterface {

    void downloadDataSuccess();
    void downloadDataFailed();

    void addAdminClassSuccess();
    void addAdminClassFailed();

    void adminClassListDownloadSuccess(ArrayList<ClassObject> classObjectArrayList);
    void userClassListDownloadSuccess(ArrayList<ClassObject> classObjectArrayList,
                                      ArrayList<String> userPercentageList);

    void addUserClassSuccess();
    void addUserClassFailed();

}
