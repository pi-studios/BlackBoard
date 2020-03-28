package com.pistudiosofficial.myclass.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.adapters.AdapterAssignmentHome;
import com.pistudiosofficial.myclass.model.AssignmentModel;
import com.pistudiosofficial.myclass.objects.AssignmentObject;
import com.pistudiosofficial.myclass.view.AssignmentHomeView;

import java.util.ArrayList;

import static com.pistudiosofficial.myclass.Common.CURRENT_CLASS_ID_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_INDEX;

public class AssignmentHome extends AppCompatActivity implements AssignmentHomeView {

    FloatingActionButton fab_new_assignmnents;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_home_admin);
        setTitle("Assignment");
        recyclerView=findViewById(R.id.rv_assignmentHome_admin);
        fab_new_assignmnents = findViewById(R.id.fab_new_assignments_admin);
        fab_new_assignmnents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AssignmentCreationActivity.class));
            }
        });
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        AssignmentModel model = new AssignmentModel(this);
        model.downloadAssignmentList(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX));
    }

    @Override
    public void assignmentDownloadSuccess(ArrayList<AssignmentObject> assignmentObjects) {
        AdapterAssignmentHome adapterAssignmentHome = new AdapterAssignmentHome(assignmentObjects,this);
        recyclerView.setAdapter(adapterAssignmentHome);
    }

    @Override
    public void assignmentDownloadFailed() {

    }
}
