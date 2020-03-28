package com.pistudiosofficial.myclass.view;

import com.pistudiosofficial.myclass.objects.AssignmentObject;

import java.util.ArrayList;
import java.util.HashMap;

public interface AssignmentViewerInfoFragView {

    void downloadSuccess(AssignmentObject assignmentObject, HashMap<String,String> meta_data,ArrayList<String> name_meta_data);
    void downloadFailed();

}
