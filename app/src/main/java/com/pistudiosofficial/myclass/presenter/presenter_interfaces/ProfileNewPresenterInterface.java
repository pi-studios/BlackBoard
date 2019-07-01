package com.pistudiosofficial.myclass.presenter.presenter_interfaces;

public interface ProfileNewPresenterInterface {

    void profilePicUploadSuccess();
    void profilePicUploadFailed();

    void helloSendSuccess();
    void helloSendFailed();

    void profilePictureLoadSuccess(String link);
    void profilePictureLoadFailed();

    void helloStatusCheckSuccess(int hello);


}
