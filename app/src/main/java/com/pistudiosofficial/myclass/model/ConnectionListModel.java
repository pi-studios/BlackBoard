package com.pistudiosofficial.myclass.model;

import com.pistudiosofficial.myclass.UserObject;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.ConnectionListPresenterInterface;

public class ConnectionListModel {

    ConnectionListPresenterInterface presenter;

    public ConnectionListModel(ConnectionListPresenterInterface presenter) {
        this.presenter = presenter;
    }

    public void downloadData(UserObject userObject){

    }

}
