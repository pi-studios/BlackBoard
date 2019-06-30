package com.pistudiosofficial.myclass.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pistudiosofficial.myclass.adapters.AdapterCourseItem;
import com.pistudiosofficial.myclass.Common;
import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.presenter.ProfilePresenter;
import com.pistudiosofficial.myclass.view.ProfileActivityView;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ProfileActivity extends AppCompatActivity implements ProfileActivityView {

    private static final String Tag=" ProfileActivity";
    ImageButton backButton;
    private ArrayList<String> mCourseNames=new ArrayList<>();
    private ArrayList<String> mInstructorNames=new ArrayList<>();

    ProfilePresenter presenter;
    TextView addHOD;
    EditText hodEmail;
    Button add;
    private ArrayList<String> mAttendancePercentage=new ArrayList<>();
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        courseAndInstructorNames();

        backButton=findViewById(R.id.backButton);
//        AttendancePercentage();
        if(Common.CURRENT_USER.AdminLevel.equals("admin")) {
            addHOD = findViewById(R.id.tv_add_hod);
            addHOD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog = new Dialog(ProfileActivity.this);
                    dialog.setContentView(R.layout.add_hod_admin);
                    hodEmail = dialog.findViewById(R.id.et_addFaculty_email);
                    add = dialog.findViewById(R.id.bt_add_new_class_student_popup);
                    dialog.show();
                    add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            presenter.addHOD(hodEmail.getText().toString());
                        }
                    });
                }
            });
        }
        else {
            addHOD = findViewById(R.id.tv_add_hod);
            addHOD.setVisibility(View.GONE);
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,MainActivity.class));
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter = new ProfilePresenter(this);
        if(Common.CURRENT_USER.AdminLevel.equals("admin")){
            presenter.downloadProfileData();
        }

    }

    @Override
    public void hodAddSuccess() {
        dialog.dismiss();
        Toast.makeText(ProfileActivity.this,"Success",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hodAddFailed() {
        Toast.makeText(ProfileActivity.this,"Failed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hodNameLoad(String text) {
        addHOD.setText(text);
    }

    public void courseAndInstructorNames()
    {
        mCourseNames.add("Graphics");
        mInstructorNames.add("Badal Soni");

        mCourseNames.add("Maths");
        mInstructorNames.add("K.N.Das");

        mCourseNames.add("FLAT");
        mInstructorNames.add("Shyampada Mukherjee");

        mCourseNames.add("Signals And System");
        mInstructorNames.add("Anish KUmar");

        initRecyclerViewForCourse();
    }
//    public void AttendancePercentage()
//    {
//        mCourseNames.add("Graphics");
//        mAttendancePercentage.add("89");
//
//        mCourseNames.add("Maths");
//        mAttendancePercentage.add("91.5");
//
//        mCourseNames.add("FLAT");
//        mAttendancePercentage.add("79");
//
//        mCourseNames.add("Signals And System");
//        mAttendancePercentage.add("72");
//
//        initRecyclerViewForAttendance();
//    }
    private void initRecyclerViewForCourse(){
        Log.d(TAG,"Recycler View Initialized");

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView=findViewById(R.id.course_recyclerview);
        recyclerView.setLayoutManager(linearLayoutManager);
        AdapterCourseItem adapterCourseItem= new AdapterCourseItem(mCourseNames,mInstructorNames,this);
        recyclerView.setAdapter(adapterCourseItem);
    }
//    private void initRecyclerViewForAttendance(){
//        Log.d(TAG,"Recycler View Initialized");
//
//        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
//        RecyclerView recyclerView=findViewById(R.id.attendance_recyclerview);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        AdapterCourseItem adapterCourseItem= new AdapterCourseItem(mCourseNames,mAttendancePercentage,this);
//        recyclerView.setAdapter(adapterCourseItem);
//    }
}
