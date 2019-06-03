package com.pistudiosofficial.myclass.presenter.presenter_interfaces;

import com.pistudiosofficial.myclass.NotificationStoreObj;

import java.util.ArrayList;

public interface NotificationHistoryPresenterInterface {

    void notificationDownloadSuccess(ArrayList<NotificationStoreObj> notifobj);
    void notificationDownloadFailed();

}
