package com.pistudiosofficial.myclass.presenter.presenter_interfaces;

import com.pistudiosofficial.myclass.objects.PollOptionValueLikeObject;
import com.pistudiosofficial.myclass.objects.PostObject;

import java.util.ArrayList;
import java.util.HashMap;

public interface CheckAttendancePresenterInterface {

    void adminCheckAttendanceDataDownloadSuccess(ArrayList<Double> arrayList);
    void adminCheckAttendanceDataDownloadFailed();

    void exportCsvSuccess();
    void exportCsvFailed();

    void broadcastSuccess();
    void broadcastFailed();

    void postingSuccess();
    void postingFailed();

    void checkMultipleAttendanceReturn(boolean b);

    void fileUploadLink(String link);

    void loadPostSuccess(ArrayList<PostObject> postObjects ,
            HashMap<String, PollOptionValueLikeObject>post_poll_option ,
            ArrayList<String> post_like_list ,
            HashMap<String, ArrayList<String>> post_url_list,ArrayList<String> comment_count);
}
