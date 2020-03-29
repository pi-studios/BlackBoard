package com.pistudiosofficial.myclass.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.adapters.AdapterAssignmentHome;
import com.pistudiosofficial.myclass.model.AssignmentModel;
import com.pistudiosofficial.myclass.objects.AssignmentObject;
import com.pistudiosofficial.myclass.view.AssignmentHomeView;

import java.util.ArrayList;

import static com.pistudiosofficial.myclass.Common.CURRENT_CLASS_ID_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_INDEX;
import static com.pistudiosofficial.myclass.Common.CURRENT_USER;

public class AssignmentHome extends AppCompatActivity implements AssignmentHomeView {

    FloatingActionButton fab_new_assignmnents;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_home_admin);
        final ActionBar abar = getSupportActionBar();
        if(abar!=null) {
            abar.setDisplayHomeAsUpEnabled(true);
            abar.setTitle("Assignments");
        }
        recyclerView=findViewById(R.id.rv_assignmentHome_admin);

        fab_new_assignmnents = findViewById(R.id.fab_new_assignments_admin);
        if (CURRENT_USER.AdminLevel.equals("user")){
            fab_new_assignmnents.setVisibility(View.GONE);
        }
        fab_new_assignmnents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AssignmentCreationActivity.class);
                intent.putExtra("status","create");
                startActivity(intent);
            }
        });
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return  false;
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
        Toast.makeText(this,"Try Again",Toast.LENGTH_SHORT).show();
    }
}
