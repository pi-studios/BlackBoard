package com.pistudiosofficial.myclass.presenter;


import com.pistudiosofficial.myclass.UserObject;
import com.pistudiosofficial.myclass.model.ConnectionListModel;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.ConnectionListPresenterInterface;
import com.pistudiosofficial.myclass.view.ConnectionListActivityView;

public class ConnectionListPresenter implements ConnectionListPresenterInterface {

    ConnectionListActivityView view;
    ConnectionListModel model;
    public ConnectionListPresenter(ConnectionListActivityView view) {
        this.view = view;
        model = new ConnectionListModel(this);
    }

    public void downloadData(UserObject userObject){
        model.downloadData(userObject);
    }

    @Override
    public void downloadDataSuccess() {

    }

    @Override
    public void downloadDataFailed() {

    }
}
