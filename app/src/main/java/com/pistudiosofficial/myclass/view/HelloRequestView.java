package com.pistudiosofficial.myclass.view;

public interface HelloRequestView {

    void requestAccepted(String UID);
    void requestRejected(String UID);
    void requestActionFailed();

}
