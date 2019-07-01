package com.pistudiosofficial.myclass.view;

public interface ProfileNewView {

    void profilePicUploadSuccess();
    void profilePicUploadFailed();

    void helloSendSuccess();
    void helloSendFailed();

    void profilePictureLoadSuccess(String link);
    void profilePictureLoadFailed();

    void helloStatusCheckSuccess(int hello);
}
