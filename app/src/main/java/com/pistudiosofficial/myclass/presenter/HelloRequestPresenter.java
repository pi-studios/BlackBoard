package com.pistudiosofficial.myclass.presenter;

import com.pistudiosofficial.myclass.model.HelloRequestModel;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.HelloRequestPresenterInterface;
import com.pistudiosofficial.myclass.view.HelloRequestView;

public class HelloRequestPresenter implements HelloRequestPresenterInterface {
    HelloRequestView view;
    HelloRequestModel model;
    public HelloRequestPresenter(HelloRequestView view) {
        this.view = view;
        model = new HelloRequestModel(this);
    }

    public void performHelloRequestRespond(String UID, boolean hello){
        model.performHelloRequestRespond(UID,hello);
    }

    @Override
    public void requestAccepted(String UID) {
        view.requestAccepted(UID);
    }

    @Override
    public void requestRejected(String UID) {
        view.requestRejected(UID);
    }

    @Override
    public void requestActionFailed() {
        view.requestActionFailed();
    }
}
