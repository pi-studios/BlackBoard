package com.pistudiosofficial.myclass.model;



import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pistudiosofficial.myclass.NotificationStoreObj;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.NotificationHistoryPresenterInterface;

import java.util.ArrayList;

import static com.pistudiosofficial.myclass.Common.CURRENT_CLASS_ID_LIST;
import static com.pistudiosofficial.myclass.Common.mREF_classList;

public class NotificationHistoryModel {

    NotificationHistoryPresenterInterface presenter;
    NotificationStoreObj obj;
    ArrayList<NotificationStoreObj> notifobjList;
    ValueEventListener valueEventListener;

    public NotificationHistoryModel(NotificationHistoryPresenterInterface presenter) {
        this.presenter = presenter;
        notifobjList = new ArrayList<>();
    }

    public void downloadNotificationHistory(){
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (String s : CURRENT_CLASS_ID_LIST){
                    for (DataSnapshot snapshot:dataSnapshot.child(s).child("notification").getChildren()){
                        obj = snapshot.getValue(NotificationStoreObj.class);
                        notifobjList.add(obj);
                    }
                }
                if (notifobjList != null && notifobjList.size()>0){
                    presenter.notificationDownloadSuccess(notifobjList);
                    done();
                }
                else{
                    presenter.notificationDownloadFailed();
                    done();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                presenter.notificationDownloadFailed();
                done();
            }
        };
        mREF_classList.addValueEventListener(valueEventListener);
    }
    public void done(){
        mREF_classList.removeEventListener(valueEventListener);
    }

}
