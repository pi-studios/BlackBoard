package com.pistudiosofficial.myclass.view;

import com.pistudiosofficial.myclass.objects.AssignmentSubmissionObject;

import java.util.ArrayList;

public interface AssignmentViewerSubmissionFragView {
    void downloadSuccessStudent(ArrayList<AssignmentSubmissionObject> objectArrayList);
    void downloadFailed();
    void downloadSuccessSubmissionStudentList(ArrayList<String> roll_list, ArrayList<String> name_list);
}
