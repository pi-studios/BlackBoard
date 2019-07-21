package com.pistudiosofficial.myclass.presenter.presenter_interfaces;

import com.pistudiosofficial.myclass.objects.UserObject;

public interface ProfileNewPresenterInterface {

    void profilePicUploadSuccess();
    void profilePicUploadFailed();

    void helloSendSuccess();
    void helloSendFailed();

    void profileLoadSuccess(UserObject object);
    void profilePictureLoadFailed();

    void helloStatusCheckSuccess(int hello);


}
