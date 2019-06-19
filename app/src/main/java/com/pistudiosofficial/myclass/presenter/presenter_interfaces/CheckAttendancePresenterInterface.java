package com.pistudiosofficial.myclass.presenter.presenter_interfaces;

import com.pistudiosofficial.myclass.PostObject;

import java.util.ArrayList;

public interface CheckAttendancePresenterInterface {

    void adminCheckAttendanceDataDownloadSuccess(ArrayList<Double> arrayList);
    void adminCheckAttendanceDataDownloadFailed();

    void exportCsvSuccess();
    void exportCsvFailed();

    void broadcastSuccess();
    void broadcastFailed();

    void postLoadSuccess(ArrayList<PostObject> postObjectArrayList);
    void postLoadFailed();
    void postingSuccess();
    void postingFailed();

}
