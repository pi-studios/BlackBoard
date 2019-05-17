package com.pistudiosofficial.myclass.presenter;

import com.pistudiosofficial.myclass.ClassObject;
import com.pistudiosofficial.myclass.Common;
import com.pistudiosofficial.myclass.StudentClassObject;
import com.pistudiosofficial.myclass.model.MainModel;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.MainPresenterInterface;
import com.pistudiosofficial.myclass.view.MainActivityView;

import java.util.ArrayList;

public class MainPresenter implements MainPresenterInterface {

    MainActivityView view;
    MainModel model;
    ArrayList<ClassObject> classList;
    public MainPresenter(MainActivityView view) {
        this.view = view;
        model = new MainModel(this);
    }

    public void performDataDownload(){
        model.performDataDownload();
    }

    public void performAdminClassListDownload(){
        model.performAdminClassListDownload();
    }

    public void performUserClassListDownload() {model.performUserClassListDownload();}

    public void performAdminAddClass(ClassObject classObject){
        model.performAdminAddClass(classObject);
    }

    public void endSession(int index){
        model.performEndSession(index);
    }

    public void performUserAddClass(StudentClassObject studentClassObject){
        model.performUserAddClass(studentClassObject);
    }

    @Override
    public void downloadDataSuccess() {
        view.downloadDataSuccess();
    }

    @Override
    public void downloadDataFailed() {
        view.downloadDataFailed();
    }

    @Override
    public void addAdminClassSuccess() {
        view.addAdminClassSuccess();
    }

    @Override
    public void addAdminClassFailed() {
        view.addAdminClassFailed();
    }

    @Override
    public void adminClassListDownloadSuccess(ArrayList<ClassObject> classObjectArrayList) {
        classList = new ArrayList<>();
        if(classObjectArrayList != null){
            for(int i = 0; i<classObjectArrayList.size(); i++) {
               if (classObjectArrayList.get(i).facultyUID.equals(Common.CURRENT_USER.UID)){
                   classList.add(classObjectArrayList.get(i));
               }
            }
            if(classList == null){
                view.downloadDataFailed();
            }
            else{
                view.loadAdminClassList(classList);
            }
        }

    }

    @Override
    public void userClassListDownloadSuccess(ArrayList<ClassObject> classObjectArrayList,
                                             ArrayList<String> userPercentageList) {
        view.loadUserClassList(classObjectArrayList, userPercentageList);
    }

    @Override
    public void addUserClassSuccess() {
        view.addUserClassSuccess();
    }

    @Override
    public void addUserClassFailed() {
        view.addUserClassFailed();
    }

}
