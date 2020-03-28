package com.pistudiosofficial.myclass.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.pistudiosofficial.myclass.R;

public class AssignmentHomeAdmin extends AppCompatActivity {

    FloatingActionButton fab_new_assignmnents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_home_admin);

        fab_new_assignmnents = findViewById(R.id.fab_new_assignments_admin);
        fab_new_assignmnents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AssignmentCreationActivity.class));
            }
        });



    }
}
