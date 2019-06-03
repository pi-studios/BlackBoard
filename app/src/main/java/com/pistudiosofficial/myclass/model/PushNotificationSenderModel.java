package com.pistudiosofficial.myclass.model;

// This model Sends Notification to all student user

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.pistudiosofficial.myclass.NotificationStoreObj;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.CheckAttendancePresenterInterface;

import static com.pistudiosofficial.myclass.Common.mREF_classList;

public class PushNotificationSenderModel {

    String className, creationTime, classID,simpleTime;

    DatabaseReference mRefNotification;
    CheckAttendancePresenterInterface presenter;

    //Class cancel or shift
    String date01,date02,type;

    public PushNotificationSenderModel(String date01, String date02, String type,
                                       String className, String creationTime, String classID,
                                       CheckAttendancePresenterInterface presenter,String simpleTime) {
        this.date01 = date01;
        this.date02 = date02;
        this.type = type;
        this.className = className;
        this.creationTime = creationTime;
        this.classID = classID;
        this.presenter = presenter;
        this.simpleTime = simpleTime;

    }

    // Broadcasting
    String broadcastTitle, broadcastBody;

    public PushNotificationSenderModel(String broadcastTitle, String broadcastBody,
                                       String className, String creationTime, String classID,
                                       CheckAttendancePresenterInterface presenter,String simpleTime) {
        this.broadcastTitle = broadcastTitle;
        this.broadcastBody = broadcastBody;
        this.className = className;
        this.creationTime = creationTime;
        this.classID = classID;
        this.presenter = presenter;
        this.simpleTime = simpleTime;

    }

    public void performBroadcast(){
        broadcastTitle = broadcastTitle+"("+className+")";
        broadcastBody = broadcastBody+"- Created: "+creationTime;
        NotificationStoreObj obj = new NotificationStoreObj(broadcastTitle,broadcastBody,simpleTime);
        mRefNotification = mREF_classList.child(classID).child("notification");
        mRefNotification.push().setValue(obj, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError==null){presenter.broadcastSuccess();}
                else{
                    presenter.broadcastFailed();
                }
            }
        });
    }

    public void performClassShiftCancel(){
        String title = "";
        String body = "";
        title = type+"("+className+")";
        if(date02.equals("")){
            body = type+" : "+date01+"  - Created: "+creationTime;
        }else{
            body = type+" : "+date01+"->"+date02+"  Created: "+creationTime;
        }
        NotificationStoreObj obj = new NotificationStoreObj(title,body,simpleTime);
        mRefNotification = mREF_classList.child(classID).child("notification");
        mRefNotification.push().setValue(obj, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError == null){presenter.broadcastSuccess();}
                else{
                    presenter.broadcastFailed();
                }
            }
        });

    }

}
