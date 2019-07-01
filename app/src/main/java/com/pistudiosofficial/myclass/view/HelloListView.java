package com.pistudiosofficial.myclass.view;

import com.pistudiosofficial.myclass.objects.UserObject;

import java.util.ArrayList;

public interface HelloListView {

    void helloListLoadSuccess(ArrayList<UserObject> userObjects);
    void helloListLoadFailed();

}
