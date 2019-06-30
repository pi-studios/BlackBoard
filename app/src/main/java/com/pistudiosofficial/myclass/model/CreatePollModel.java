package com.pistudiosofficial.myclass.model;

import com.pistudiosofficial.myclass.Common;
import com.pistudiosofficial.myclass.objects.PostObject;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.CreatePollPresenterInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CreatePollModel {

    CreatePollPresenterInterface presenter;
    String key;
    public CreatePollModel(CreatePollPresenterInterface presenter) {
        this.presenter = presenter;
    }

    public void performCreatePoll(String title, ArrayList<String> optionList){
        key = Common.mREF_classList.child(Common.CURRENT_CLASS_ID_LIST.get(Common.CURRENT_INDEX))
                .child("post").push().getKey();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        String simpleTime = simpleDateFormat.format(new Date());
        PostObject postObject = new PostObject(Common.CURRENT_USER.Name,
                simpleTime,title,null,"admin_poll");
        Common.mREF_classList.child(Common.CURRENT_CLASS_ID_LIST.get(Common.CURRENT_INDEX))
                .child("post").child(key).setValue(postObject);
        for (int i = 0; i<optionList.size(); i++){
            Common.mREF_classList.child(Common.CURRENT_CLASS_ID_LIST.get(Common.CURRENT_INDEX))
                    .child("post").child(key).child("options").child(optionList.get(i)).setValue(0);
        }
        pushNotification();
        presenter.createPollSuccess();
    }

    private void pushNotification(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        String simpleTime = simpleDateFormat.format(new Date());
        PushNotificationSenderModel notifModel = new PushNotificationSenderModel("Poll",
                "Respond To The Poll Soon!",Common.CURRENT_ADMIN_CLASS_LIST.get(Common.CURRENT_INDEX).className,
                simpleTime,Common.CURRENT_CLASS_ID_LIST.get(Common.CURRENT_INDEX),simpleTime);
        notifModel.performBroadcast();

    }

}
