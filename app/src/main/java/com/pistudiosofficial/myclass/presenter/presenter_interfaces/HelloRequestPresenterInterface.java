package com.pistudiosofficial.myclass.presenter.presenter_interfaces;

public interface HelloRequestPresenterInterface {


    void requestAccepted(String UID);
    void requestRejected(String UID);
    void requestActionFailed();

}
