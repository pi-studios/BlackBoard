package com.pistudiosofficial.myclass.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pistudiosofficial.myclass.AdapterCheckAttendanceList;
import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.activities.NewAttendenceAcitivity;
import com.pistudiosofficial.myclass.presenter.CheckAttendancePresenter;
import com.pistudiosofficial.myclass.view.CheckAttendanceFragView;;
import java.util.ArrayList;

import static com.pistudiosofficial.myclass.Common.CURRENT_CLASS_ID_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_INDEX;
import static com.pistudiosofficial.myclass.Common.SHARED_PREFERENCES;

public class AdminCheckAttendanceFragment extends Fragment implements CheckAttendanceFragView {
    ProgressDialog progressDialog;
    Button bt_newAttendance;
    RecyclerView recyclerView;
    CheckAttendancePresenter presenter;
    public AdminCheckAttendanceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v=inflater.inflate(R.layout.admin_check_attendance, container, false);

        recyclerView = v.findViewById(R.id.recyclerView_admin_checkAttendance);
        presenter = new CheckAttendancePresenter(this);
        presenter.performAdminAttendanceDataDownload();
        progressDialog = ProgressDialog.show(getContext(), "",
                "Loading. Please wait...", true);
        bt_newAttendance = v.findViewById(R.id.bt_newAttendance);
        bt_newAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_attendance_taken = SHARED_PREFERENCES.getString(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX),"");
                if(!new_attendance_taken.equals("true")){
                    Intent intent = new Intent(v.getContext(), NewAttendenceAcitivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                else{
                    Toast.makeText(v.getContext(),"Attendance cannot be taken twice",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }


    @SuppressLint("WrongConstant")
    @Override
    public void success(ArrayList<Double> attendancePercentageList) {
        AdapterCheckAttendanceList adapter = new AdapterCheckAttendanceList(attendancePercentageList);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        progressDialog.dismiss();

    }

    @Override
    public void failed() {
        new AlertDialog.Builder(getContext())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Server Unreachable")
                .setMessage("Try Again after Some time")
                .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                }).show();
    }
}
