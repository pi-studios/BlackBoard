package com.pistudiosofficial.myclass.presenter;

import android.os.Build;

import com.pistudiosofficial.myclass.objects.NotificationStoreObj;
import com.pistudiosofficial.myclass.model.NotificationHistoryModel;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.NotificationHistoryPresenterInterface;
import com.pistudiosofficial.myclass.view.NotificationHistoryView;

import java.util.ArrayList;

public class NotificationHistoryPresenter implements NotificationHistoryPresenterInterface {
    NotificationHistoryView view;
    NotificationHistoryModel model;
    ArrayList<String> title,body;
    public NotificationHistoryPresenter(NotificationHistoryView view) {
        this.view = view;
        body = new ArrayList<>();
        title = new ArrayList<>();
        model = new NotificationHistoryModel(this);
    }

    public void performNotificationHistoryDownload(){
        model.downloadNotificationHistory();
    }

    @Override
        public void notificationDownloadSuccess(ArrayList<NotificationStoreObj> objArrayList) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            objArrayList.sort((o1, o2) -> o1.dateCreated.compareTo(o2.dateCreated));
        }


        for (NotificationStoreObj obj : objArrayList){
                title.add(obj.Title);
                body.add(obj.Body);
            }
          view.loadRecyclerSuccess(title,body);
        }

    @Override
    public void notificationDownloadFailed() {
        view.loadRecyclerFailed();
    }
}
