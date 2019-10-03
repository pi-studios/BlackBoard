package com.pistudiosofficial.myclass.view;

import com.google.android.gms.common.util.Strings;

import java.util.ArrayList;

public interface ShowAttendanceView {
    void totalClassDownloadSuccess(long totalClass, ArrayList<String> dateList);
    void indivAttendanceDownload(ArrayList<String> data);
}
