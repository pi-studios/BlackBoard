package com.pistudiosofficial.myclass.presenter;


import com.pistudiosofficial.myclass.objects.ClassObject;
import com.pistudiosofficial.myclass.objects.StudentClassObject;
import com.pistudiosofficial.myclass.objects.UserObject;
import com.pistudiosofficial.myclass.model.MainModel;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.MainPresenterInterface;
import com.pistudiosofficial.myclass.view.MainActivityView;

import java.util.ArrayList;

import static com.pistudiosofficial.myclass.Common.CURRENT_ADMIN_CLASS_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_CLASS_ID_LIST;

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
        CURRENT_ADMIN_CLASS_LIST.clear();
        CURRENT_CLASS_ID_LIST.clear();
        model.performAdminClassListDownload();
    }

    public void performUserClassListDownload() {model.performUserClassListDownload();}

    public void performAdminAddClass(ClassObject classObject){
        model.performAdminAddClass(classObject);
    }

    public void endSession(int index){
        model.performEndSession(index);
    }

    public void addCollab(int index, String email,boolean isTransfer){
        model.addCollab(index,email,isTransfer);
    }

    public void transferClass(int index, String email){
        model.transferClass(index, email);
    }

    public void performUserAddClass(StudentClassObject studentClassObject){
        model.performUserAddClass(studentClassObject);
    }

    public void performConnectionDownload(){
        model.performConnectionDownload();
    }

    public void performPostLoad(){
        model.performPostLoad();
    }

    public void loadHelloRequest(){
        model.loadHelloRequest();
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
                   classList.add(classObjectArrayList.get(i));
            }
            if(classList == null){
                view.downloadDataFailed();
            }
            else{
                view.loadAdminClassList(classList);
            }
        }
        else{
            view.downloadDataFailed();
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

    @Override
    public void transferCollabActionFailed() {
        view.transferClassFailed();
    }

    @Override
    public void connectionListDownloadSuccess(ArrayList<UserObject> userList) {
        view.connectionListDownloadSuccess(userList);
    }

    @Override
    public void connectionListDownloadFailed() {
        view.connectionListDownloadFailed();
    }

    @Override
    public void loadHelloSuccess() {
        view.loadHelloSuccess();
    }

    @Override
    public void loadHelloFailed() {
        view.loadHelloFailed();
    }

}
