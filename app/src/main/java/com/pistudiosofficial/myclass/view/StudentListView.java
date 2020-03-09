package com.pistudiosofficial.myclass.view;

import com.pistudiosofficial.myclass.objects.AccountListObject;

import java.util.ArrayList;
import java.util.HashMap;

public interface StudentListView {

    void spinnerLoadSuccess(HashMap<String,ArrayList<String>> spinnerMap);
    void spinnerLoadFailed();

    void listDownloadSuccess(ArrayList<AccountListObject> objectArrayList);
    void listDownloadFailed();

    void studentAddSuccess();
    void studentAddFail();

}
