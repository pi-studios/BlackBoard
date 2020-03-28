package com.pistudiosofficial.myclass.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.adapters.AdapterAssignmentHome;
import com.pistudiosofficial.myclass.adapters.AdapterAssignmentViewerInfoFrag;
import com.pistudiosofficial.myclass.model.AssignmentModel;
import com.pistudiosofficial.myclass.objects.AssignmentSubmissionObject;
import com.pistudiosofficial.myclass.view.AssignmentViewerSubmissionFragView;

import java.util.ArrayList;

import static com.pistudiosofficial.myclass.Common.CURRENT_CLASS_ID_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_INDEX;
import static com.pistudiosofficial.myclass.Common.CURRENT_USER;
import static com.pistudiosofficial.myclass.Common.SELECTED_ASSIGNMENT_ID;

public class AssignmentViewerSubmissionFragment extends Fragment implements AssignmentViewerSubmissionFragView {

    private GridView gridView;
    private RecyclerView recyclerView;
    private AdapterAssignmentViewerInfoFrag adapterAssignmentViewerInfoFrag;
    private AssignmentModel assignmentModel;
    LinearLayoutManager layoutManager;
    public AssignmentViewerSubmissionFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assignment_submission,container,false);
        assignmentModel = new AssignmentModel(this);
        recyclerView = view.findViewById(R.id.rv_assignmentViewerSubmissionFrag);
        gridView = view.findViewById(R.id.gridViewAssignmentViewerISubmission);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        if (CURRENT_USER.AdminLevel.equals("user")) {
            recyclerView.setVisibility(View.GONE);
            assignmentModel.downloadAssignmentSubmissionStudent(SELECTED_ASSIGNMENT_ID, CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX));
        }
        else{
            gridView.setVisibility(View.GONE);
            assignmentModel.downloadAssignmentSubmissionStudentList(SELECTED_ASSIGNMENT_ID,CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX));
        }
        return view;
    }

    @Override
    public void downloadSuccessStudent(ArrayList<AssignmentSubmissionObject> objectArrayList) {
        adapterAssignmentViewerInfoFrag = new AdapterAssignmentViewerInfoFrag(objectArrayList,getContext());
        gridView.setAdapter(adapterAssignmentViewerInfoFrag);
    }

    @Override
    public void downloadFailed() {
        Toast.makeText(getContext(),"Try Again",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void downloadSuccessSubmissionStudentList(ArrayList<String> roll_list, ArrayList<String> name_list) {
        AdapterAssignmentHome adapterAssignmentHome = new AdapterAssignmentHome(getContext(),roll_list,name_list);
        recyclerView.setAdapter(adapterAssignmentHome);
    }
}
