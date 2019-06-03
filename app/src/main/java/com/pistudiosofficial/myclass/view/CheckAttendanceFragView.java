package com.pistudiosofficial.myclass.view;

import java.util.ArrayList;

public interface CheckAttendanceFragView {

    void success(ArrayList<Double> attendancePercentageList);
    void failed();

    void exportCsvSuccess();
    void exportCsvFailed();

    void notifySuccess();
    void notifyFailed();

}
