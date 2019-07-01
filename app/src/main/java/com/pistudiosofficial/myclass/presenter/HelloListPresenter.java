package com.pistudiosofficial.myclass.presenter;

import com.pistudiosofficial.myclass.model.HelloListModel;
import com.pistudiosofficial.myclass.objects.UserObject;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.HelloListPresenterInterface;
import com.pistudiosofficial.myclass.view.HelloListView;

import java.util.ArrayList;


public class HelloListPresenter implements HelloListPresenterInterface {
    HelloListView view;
    HelloListModel model;
    public HelloListPresenter(HelloListView view) {
        this.view = view;
        model = new HelloListModel(this);
    }

    public void performHelloListDownload(){
        model.performHelloListDownload();
    }

    @Override
    public void helloListLoadSuccess(ArrayList<UserObject> userObjects) {
        view.helloListLoadSuccess(userObjects);
    }

    @Override
    public void helloListLoadFailed() {
        view.helloListLoadFailed();
    }
}
