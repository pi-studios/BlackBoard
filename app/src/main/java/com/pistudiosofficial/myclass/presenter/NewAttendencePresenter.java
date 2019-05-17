package com.pistudiosofficial.myclass.presenter;

import com.pistudiosofficial.myclass.model.NewAttendenceModel;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.NewAttendencePresenterInterface;
import com.pistudiosofficial.myclass.view.NewAttendenceView;

public class NewAttendencePresenter implements NewAttendencePresenterInterface {

    NewAttendenceView view;
    NewAttendenceModel model;

    public NewAttendencePresenter(NewAttendenceView view) {
        this.view = view;
        this.model =new  NewAttendenceModel(this);
    }

    public void performAttendenceUpload(){
        model.performAttendenceUpload();
    }

    @Override
    public void success() {
        view.uploadSuccess();
    }

    @Override
    public void failed() {
        view.uploadFailed();
    }
}
