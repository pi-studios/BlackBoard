package com.pistudiosofficial.myclass.view;

import com.pistudiosofficial.myclass.PostObject;

import java.util.ArrayList;

public interface CheckAttendanceFragView {

    void success(ArrayList<Double> attendancePercentageList);
    void failed();

    void exportCsvSuccess();
    void exportCsvFailed();

    void notifySuccess();
    void notifyFailed();

    void postLoadSuccess(ArrayList<PostObject> postObjectArrayList);
    void postLoadFailed();

    void postingSuccess();
    void postingFailed();

}
