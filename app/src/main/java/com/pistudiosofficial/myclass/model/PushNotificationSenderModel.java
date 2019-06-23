package com.pistudiosofficial.myclass.model;

// This model Sends Notification to all student user

import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pistudiosofficial.myclass.NotificationStoreObj;
import com.pistudiosofficial.myclass.StudentClassObject;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.CheckAttendancePresenterInterface;

import java.util.ArrayList;

import static com.pistudiosofficial.myclass.Common.CURRENT_CLASS_ID_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_INDEX;
import static com.pistudiosofficial.myclass.Common.mREF_classList;
import static com.pistudiosofficial.myclass.Common.mREF_student_classList;
import static com.pistudiosofficial.myclass.Common.mREF_users;

public class PushNotificationSenderModel {

    ArrayList<String> studentUID;
    String className, creationTime, classID,simpleTime;

    DatabaseReference mRefNotification;
    CheckAttendancePresenterInterface presenter;
    ValueEventListener valueEventListener;
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

    //Create Poll
    public PushNotificationSenderModel(String broadcastTitle, String broadcastBody,
                                       String className, String creationTime, String classID,
                                       String simpleTime) {
        this.broadcastTitle = broadcastTitle;
        this.broadcastBody = broadcastBody;
        this.className = className;
        this.creationTime = creationTime;
        this.classID = classID;
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
                if(databaseError==null){
                    fcmMasterNotificationList(obj);
                    if (presenter != null){
                        presenter.broadcastSuccess();
                    }
                    return;}
                else{
                    if(presenter != null){
                        presenter.broadcastFailed();
                    }
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
                if(databaseError == null){
                    fcmMasterNotificationList(obj);
                    presenter.broadcastSuccess();
                    return;
                }
                else{
                    presenter.broadcastFailed();
                }
            }
        });

    }

    public void fcmMasterNotificationList(NotificationStoreObj obj){

        studentUID = new ArrayList<>();
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()){
                    studentUID.add(s.getKey());
                }
                finishUpload(obj);
                return;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mREF_classList.child(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX))
                .child("student_index").addValueEventListener(valueEventListener);
    }

    public void finishUpload(NotificationStoreObj obj){
        mREF_classList.removeEventListener(valueEventListener);
        for (String str : studentUID){
            mREF_users.child(str).child("notification").push().setValue(obj);
        }
        return;
    }
}
