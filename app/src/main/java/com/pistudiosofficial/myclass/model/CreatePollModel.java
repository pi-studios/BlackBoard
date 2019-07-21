package com.pistudiosofficial.myclass.model;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pistudiosofficial.myclass.Common;
import com.pistudiosofficial.myclass.objects.PostObject;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.CreatePollPresenterInterface;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.pistudiosofficial.myclass.Common.CURRENT_CLASS_ID_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_INDEX;
import static com.pistudiosofficial.myclass.Common.CURRENT_USER;
import static com.pistudiosofficial.myclass.Common.mREF_classList;

public class CreatePollModel {

    CreatePollPresenterInterface presenter;
    String key;
    public CreatePollModel(CreatePollPresenterInterface presenter) {
        this.presenter = presenter;
    }

    public void performCreatePoll(String title, ArrayList<String> optionList){
        key = Common.mREF_classList.child(Common.CURRENT_CLASS_ID_LIST.get(Common.CURRENT_INDEX))
                .child("post").push().getKey();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a MMM d, ''yy");
        String simpleTime = simpleDateFormat.format(new Date());
        PostObject postObject = new PostObject(Common.CURRENT_USER.Name,
                simpleTime,title,"admin_poll",CURRENT_USER.UID,CURRENT_USER.profilePicLink,key);
        Common.mREF_classList.child(Common.CURRENT_CLASS_ID_LIST.get(Common.CURRENT_INDEX))
                .child("post").child(key).setValue(postObject);
        for (int i = 0; i<optionList.size(); i++){
            Common.mREF_classList.child(Common.CURRENT_CLASS_ID_LIST.get(Common.CURRENT_INDEX))
                    .child("post").child(key).child("options").child(optionList.get(i)).setValue(0);
        }
        pushNotification();
        presenter.createPollSuccess();
        postReadIndex(key);
    }

    private void postReadIndex(String key){
        ArrayList<String> studentUID = new ArrayList<>();
        mREF_classList.child(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX)).child("student_index")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot s :dataSnapshot.getChildren()){
                            studentUID.add(s.getKey());
                        }
                        for (String id :studentUID){
                            mREF_classList.child(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX))
                                    .child("student_index").child(id).child("new_post").setValue(true);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void pushNotification(){

        String currentTime = DateFormat.getDateTimeInstance().format(new Date());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a MMM d, ''yy");
        String simpleTime = simpleDateFormat.format(new Date());
        PushNotificationSenderModel notifModel = new PushNotificationSenderModel("Poll",
                "Respond To The Poll Soon!",Common.CURRENT_ADMIN_CLASS_LIST.get(Common.CURRENT_INDEX).className,
                currentTime,Common.CURRENT_CLASS_ID_LIST.get(Common.CURRENT_INDEX),simpleTime);
        notifModel.performBroadcast();

    }
}
