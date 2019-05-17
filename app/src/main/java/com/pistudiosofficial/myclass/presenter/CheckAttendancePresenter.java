package com.pistudiosofficial.myclass.presenter;

import com.pistudiosofficial.myclass.model.CheckAttendanceModel;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.CheckAttendancePresenterInterface;
import com.pistudiosofficial.myclass.view.CheckAttendanceFragView;

import java.util.ArrayList;

public class CheckAttendancePresenter implements CheckAttendancePresenterInterface {

    CheckAttendanceFragView view;
    CheckAttendanceModel model;
    public CheckAttendancePresenter(CheckAttendanceFragView view) {
        this.view = view;
        model = new CheckAttendanceModel(this);
    }

    public void performAdminAttendanceDataDownload(){
        model.performCheckAttendanceDownload();
    }

    @Override
    public void adminCheckAttendanceDataDownloadSuccess(ArrayList<Double> arrayList) {
        view.success(arrayList);
    }

    @Override
    public void adminCheckAttendanceDataDownloadFailed() {
        view.failed();
    }
}
