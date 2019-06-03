package com.pistudiosofficial.myclass.view;

import java.util.ArrayList;

public interface NotificationHistoryView {

    void loadRecyclerSuccess(ArrayList<String> title, ArrayList<String> body);
    void loadRecyclerFailed();

}
