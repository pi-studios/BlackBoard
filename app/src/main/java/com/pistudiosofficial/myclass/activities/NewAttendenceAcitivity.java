package com.pistudiosofficial.myclass.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pistudiosofficial.myclass.AdapterNewAttendenceList;
import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.presenter.NewAttendencePresenter;
import com.pistudiosofficial.myclass.view.NewAttendenceView;

import static com.pistudiosofficial.myclass.Common.TEMP01_LIST;

public class NewAttendenceAcitivity extends AppCompatActivity implements AdapterNewAttendenceList.OnItemListener, NewAttendenceView {

    RecyclerView newAttendenceRecycler;
    AdapterNewAttendenceList adapterNewAttendenceList;
    NewAttendencePresenter presenter;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_new_attendence);



        newAttendenceRecycler = findViewById(R.id.recyclerView_newAttendence);
        adapterNewAttendenceList = new AdapterNewAttendenceList(this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        newAttendenceRecycler.setLayoutManager(llm);
        newAttendenceRecycler.setAdapter(adapterNewAttendenceList);
        Button bt_done = findViewById(R.id.bt_new_attendence_done);
        bt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(NewAttendenceAcitivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are You Sure?")
                        .setMessage("Once Attendance taken CANNOT be changed")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                presenter.performAttendenceUpload();
                            }
                        }).setNegativeButton("No",null).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter = new NewAttendencePresenter(this);
    }

    @Override
    public void onItemClick(int position) {
        if(TEMP01_LIST.get(position).equals("ABSENT")){
            TEMP01_LIST.set(position,"PRESENT");
        }else {
            TEMP01_LIST.set(position,"ABSENT");
        }
        adapterNewAttendenceList.notifyDataSetChanged();
    }

    @Override
    public void uploadSuccess() {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void uploadFailed() {
        Toast.makeText(NewAttendenceAcitivity.this,"Upload Failed",Toast.LENGTH_LONG).show();
    }
}
