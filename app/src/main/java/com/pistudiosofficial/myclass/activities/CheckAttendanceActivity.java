package com.pistudiosofficial.myclass.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.os.Bundle;
import android.widget.Button;

import com.pistudiosofficial.myclass.Common;
import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.fragments.AdminCheckAttendanceFragment;
import com.pistudiosofficial.myclass.fragments.UserCheckAttendanceFragment;

public class CheckAttendanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_attendance);

        Fragment fragment;

        if(Common.CURRENT_USER.AdminLevel.equals("admin")){
            fragment = new AdminCheckAttendanceFragment();
            getFragmentManager().beginTransaction().replace(R.id.frame_layout_CheckAttendance,fragment).commit();
        }
        if(Common.CURRENT_USER.AdminLevel.equals("user")){
            fragment = new UserCheckAttendanceFragment();
            getFragmentManager().beginTransaction().replace(R.id.frame_layout_CheckAttendance,fragment).commit();
        }
    }
}
