package com.pistudiosofficial.myclass.view;

import com.pistudiosofficial.myclass.objects.AssignmentObject;

import java.util.ArrayList;

public interface AssignmentHomeView {

    void assignmentDownloadSuccess(ArrayList<AssignmentObject> assignmentObjects);
    void assignmentDownloadFailed();

}
