package com.pistudiosofficial.myclass.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.adapters.AdapterAssignmentViewerInfoFrag;
import com.pistudiosofficial.myclass.model.AssignmentModel;
import com.pistudiosofficial.myclass.objects.AssignmentSubmissionObject;
import com.pistudiosofficial.myclass.view.AssignmentViewerSubmissionFragView;

import java.util.ArrayList;

import static com.pistudiosofficial.myclass.Common.CURRENT_CLASS_ID_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_INDEX;
import static com.pistudiosofficial.myclass.Common.SELECTED_ASSIGNMENT_ID;

public class FacultyCheckIndivStudentSubmissionActivity extends AppCompatActivity implements AssignmentViewerSubmissionFragView {
    private GridView gridView;
    AssignmentModel assignmentModel;
    private AdapterAssignmentViewerInfoFrag adapterAssignmentViewerInfoFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_check_indiv_student_submission);
        gridView = findViewById(R.id.gridViewAssignmentViewerFacultyCheckSubmission);
        assignmentModel = new AssignmentModel(this);
        String roll = getIntent().getStringExtra("roll");
        assignmentModel.downloadAssignmentSubmissionCheckFaculty(SELECTED_ASSIGNMENT_ID, CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX),roll);
    }

    @Override
    public void downloadSuccessStudent(ArrayList<AssignmentSubmissionObject> objectArrayList) {
        adapterAssignmentViewerInfoFrag = new AdapterAssignmentViewerInfoFrag(objectArrayList,this);
        gridView.setAdapter(adapterAssignmentViewerInfoFrag);
    }

    @Override
    public void downloadFailed() {
        Toast.makeText(this,"Try Again",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void downloadSuccessSubmissionStudentList(ArrayList<String> roll_list, ArrayList<String> name_list) {

    }
}
