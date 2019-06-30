package com.pistudiosofficial.myclass.presenter;

import android.net.Uri;

import com.pistudiosofficial.myclass.model.ProfileNewModel;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.ProfileNewPresenterInterface;
import com.pistudiosofficial.myclass.view.ProfileNewView;

public class ProfileNewPresenter implements ProfileNewPresenterInterface {
    ProfileNewView view;
    ProfileNewModel model;
    public ProfileNewPresenter(ProfileNewView view) {
        this.view = view;
        model = new ProfileNewModel(this);
    }

    public void performImageUpload(Uri imgURI, String uploadType, String ext){
        model.performImageUpload(imgURI,uploadType,ext);
    }

    public void performSendHello(String UID){
        model.performSendHello(UID);
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


}
