package com.pistudiosofficial.myclass.presenter.presenter_interfaces;

import com.pistudiosofficial.myclass.objects.UserObject;

import java.util.ArrayList;

public interface HelloListPresenterInterface {
    void helloListLoadSuccess(ArrayList<UserObject> userObjects);
    void helloListLoadFailed();
}
