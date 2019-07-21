package com.pistudiosofficial.myclass.presenter;

import android.net.Uri;

import com.pistudiosofficial.myclass.objects.PollOptionValueLikeObject;
import com.pistudiosofficial.myclass.objects.PostObject;
import com.pistudiosofficial.myclass.model.CheckAttendanceModel;
import com.pistudiosofficial.myclass.model.ExportCSVModel;
import com.pistudiosofficial.myclass.model.PushNotificationSenderModel;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.CheckAttendancePresenterInterface;
import com.pistudiosofficial.myclass.view.CheckAttendanceFragView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.pistudiosofficial.myclass.Common.CURRENT_ADMIN_CLASS_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_CLASS_ID_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_INDEX;

public class CheckAttendancePresenter implements CheckAttendancePresenterInterface {

    CheckAttendanceFragView view;
    CheckAttendanceModel model;
    ExportCSVModel exportModel;
    PushNotificationSenderModel broadcastSendModel;
    String type;
    public CheckAttendancePresenter(CheckAttendanceFragView view) {
        this.view = view;
        model = new CheckAttendanceModel(this);
    }

    public void performMultipleAttendanceCheck(){
        model.checkMultipleAttendance();
    }

    public void performLoadPost(String classID){
        model.performLoadPost(classID);
    }

    public void performDeleteRead(String classid){
        model.deleteClassRead(classid);
    }

    public void performPostFileUpload(Uri fileURI, String extension){
        model.performPostFileUpload(fileURI,extension);
    }

    public void performPosting(PostObject postObject, ArrayList<Uri> imgURI, ArrayList<String> extensionList){
        model.performPosting(postObject,imgURI,extensionList);
    }

    public void performBroadcast(String type,String broadcastTitle, String broadcastMessage, String date01, String date02){
        String currentTime = DateFormat.getDateTimeInstance().format(new Date());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a MMM d, ''yy");
        String simpleTime = simpleDateFormat.format(new Date());
        this.type = type;
        if (type.equals("Broadcast")){
            broadcastSendModel = new PushNotificationSenderModel(broadcastTitle,broadcastMessage,
                    CURRENT_ADMIN_CLASS_LIST.get(CURRENT_INDEX).className,
                    currentTime, CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX),this,simpleTime);
        }else{
            broadcastSendModel = new PushNotificationSenderModel(date01, date02, type,
                    CURRENT_ADMIN_CLASS_LIST.get(CURRENT_INDEX).className, currentTime,
                    CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX),this,simpleTime);
        }
        broadcastFunctionPerform();
    }

    public void performExportCSV(){
        exportModel = new ExportCSVModel(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX),this);
        exportModel.exportAttendanceData();
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

    @Override
    public void exportCsvSuccess() {
        view.exportCsvSuccess();
    }

    @Override
    public void exportCsvFailed() {
        view.exportCsvFailed();
    }

    @Override
    public void broadcastSuccess() {
        view.notifySuccess();
    }

    @Override
    public void broadcastFailed() {
        view.notifyFailed();
    }

    @Override
    public void postingSuccess() {
        view.postingSuccess();
    }

    @Override
    public void postingFailed() {
        view.postingFailed();
    }

    @Override
    public void checkMultipleAttendanceReturn(boolean b) {
        view.checkAttendanceReturn(b);
    }

    @Override
    public void fileUploadLink(String link) {
        view.fileUploadDone(link);
    }

    @Override
    public void loadPostSuccess(ArrayList<PostObject> postObjects, HashMap<String,
            PollOptionValueLikeObject> post_poll_option, ArrayList<String> post_like_list,
                                HashMap<String, ArrayList<String>> post_url_list,
                                ArrayList<String> comment_count) {
        view.loadPostSuccess(postObjects,post_poll_option,post_like_list,post_url_list,comment_count);
    }

    public void broadcastFunctionPerform(){
        if (type.equals("Broadcast")){
            broadcastSendModel.performBroadcast();
        }else{
            broadcastSendModel.performClassShiftCancel();
        }
    }
}
