package com.pistudiosofficial.myclass.presenter;

import android.net.Uri;

import com.pistudiosofficial.myclass.model.HelloRequestModel;
import com.pistudiosofficial.myclass.model.ProfileNewModel;
import com.pistudiosofficial.myclass.objects.UserObject;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.ProfileNewPresenterInterface;
import com.pistudiosofficial.myclass.view.ProfileNewView;

public class ProfileNewPresenter implements ProfileNewPresenterInterface {
    ProfileNewView view;
    ProfileNewModel model;
    public ProfileNewPresenter(ProfileNewView view) {
        this.view = view;
        model = new ProfileNewModel(this);
    }

    public void performHelloRequestRespond(String UID, boolean hello){
        HelloRequestModel helloRequestModel = new HelloRequestModel(null);
        helloRequestModel.performHelloRequestRespond(UID,hello);
    }

    public void performImageUpload(Uri imgURI, String uploadType, String ext){
        model.performImageUpload(imgURI,uploadType,ext);
    }

    public void performSendHello(String UID){
        model.performSendHello(UID);
    }

    public void performProfilePictureLoad(String UID){
        model.performProfilePictureLoad(UID);
    }

    public void performHelloStatusCheck(String UID){
        model.performHelloStatusCheck(UID);
    }

    @Override
    public void profilePicUploadSuccess() {
        view.profilePicUploadSuccess();
    }

    @Override
    public void profilePicUploadFailed() {
        view.profilePicUploadFailed();
    }

    @Override
    public void helloSendSuccess() {
        view.helloSendSuccess();
    }

    @Override
    public void helloSendFailed() {
        view.helloSendFailed();
    }

    @Override
    public void profileLoadSuccess(UserObject userObject) {
        view.profileLoadSuccess(userObject);
    }

    @Override
    public void profilePictureLoadFailed() {
        view.profilePictureLoadFailed();
    }

    @Override
    public void helloStatusCheckSuccess(int hello) {
        view.helloStatusCheckSuccess(hello);
    }


}
