package com.pistudiosofficial.myclass.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pistudiosofficial.myclass.Common;
import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.presenter.ProfilePresenter;
import com.pistudiosofficial.myclass.view.ProfileActivityView;

public class ProfileActivity extends AppCompatActivity implements ProfileActivityView {

    ProfilePresenter presenter;
    TextView addHOD;
    EditText hodEmail;
    Button add;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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


}
