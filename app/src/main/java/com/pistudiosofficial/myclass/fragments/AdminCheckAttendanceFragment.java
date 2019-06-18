package com.pistudiosofficial.myclass.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.pistudiosofficial.myclass.AdapterCheckAttendanceList;
import com.pistudiosofficial.myclass.AdapterConnectionList;
import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.activities.CreatePollActivity;
import com.pistudiosofficial.myclass.activities.NewAttendenceAcitivity;
import com.pistudiosofficial.myclass.presenter.CheckAttendancePresenter;
import com.pistudiosofficial.myclass.view.CheckAttendanceFragView;;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.pistudiosofficial.myclass.Common.CURRENT_CLASS_ID_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_INDEX;
import static com.pistudiosofficial.myclass.Common.SHARED_PREFERENCES;

public class AdminCheckAttendanceFragment extends Fragment implements CheckAttendanceFragView {
    ProgressDialog progressDialog;
    CheckAttendancePresenter presenter;
    Dialog dialog,dialogAttendancePercent;
    ArrayList<Double> admin_attendance_percent_list;
    String type = "";
    FloatingActionButton fab_exportcsv,fab_create_poll,fab_new_attendance,fab_show_attendace_percent,fab_notify;
    public AdminCheckAttendanceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v=inflater.inflate(R.layout.admin_check_attendance, container, false);

        fab_create_poll = v.findViewById(R.id.fab_create_poll);
        fab_exportcsv = v.findViewById(R.id.fab_export_csv);
        fab_new_attendance = v.findViewById(R.id.fab_new_attendance);
        fab_notify = v.findViewById(R.id.fab_notify);
        fab_show_attendace_percent = v.findViewById(R.id.fab_show_attendnace_list);
        presenter = new CheckAttendancePresenter(this);
        presenter.performAdminAttendanceDataDownload();
        progressDialog = ProgressDialog.show(getContext(), "",
                "Loading. Please wait...", true);
        fab_new_attendance.setOnClickListener(new View.OnClickListener() {
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
        fab_exportcsv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.performExportCSV();
            }
        });
        fab_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.admin_notify_dialog);
                final EditText broadcastTitle,broadcast,date01,date02;
                RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup_admin_notify);
                broadcast = dialog.findViewById(R.id.et_broadcast_message);
                broadcastTitle = dialog.findViewById(R.id.et_broadcastTitle);
                date01 = dialog.findViewById(R.id.et_date01);
                date02 = dialog.findViewById(R.id.et_date02);
                sessionDatePick(date01);
                sessionDatePick(date02);

                Button bt_done = dialog.findViewById(R.id.bt_notify_broadcast);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        int id = radioGroup.getCheckedRadioButtonId();
                        switch (id){
                            case R.id.rb_broadcast:
                                type = "Broadcast";
                                broadcastTitle.setVisibility(View.VISIBLE);
                                broadcast.setVisibility(View.VISIBLE);
                                date01.setVisibility(View.INVISIBLE);
                                date02.setVisibility(View.INVISIBLE);
                                break;
                            case R.id.rb_classCancel:
                                type = "Class_Cancel";
                                broadcastTitle.setVisibility(View.INVISIBLE);
                                date01.setVisibility(View.VISIBLE);
                                date02.setVisibility(View.VISIBLE);
                                broadcast.setVisibility(View.INVISIBLE);
                                break;
                            case R.id.rb_classShift:
                                type = "Class_Shifted";
                                broadcastTitle.setVisibility(View.INVISIBLE);
                                date01.setVisibility(View.VISIBLE);
                                date02.setVisibility(View.VISIBLE);
                                broadcast.setVisibility(View.INVISIBLE);
                                break;
                            default:
                                type = "Broadcast";
                                broadcastTitle.setVisibility(View.VISIBLE);
                                broadcast.setVisibility(View.VISIBLE);
                                date01.setVisibility(View.INVISIBLE);
                                date02.setVisibility(View.INVISIBLE);
                                break;
                        }

                    }
                });
                bt_done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        presenter.performBroadcast(type,broadcastTitle.getText().toString(),
                                broadcast.getText().toString(),
                                date01.getText().toString(),date02.getText().toString());
                    }
                });
                dialog.show();
            }
        });
        fab_create_poll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreatePollActivity.class);
                v.getContext().startActivity(intent);
            }
        });
        fab_show_attendace_percent.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                dialogAttendancePercent = new Dialog(getContext());
                dialogAttendancePercent.setContentView(R.layout.connection_list_dialog);
                LinearLayoutManager llm = new LinearLayoutManager(getContext());
                AdapterCheckAttendanceList adapter = new AdapterCheckAttendanceList(admin_attendance_percent_list);
                RecyclerView recyclerView = dialogAttendancePercent.findViewById(R.id.recycler_connection_list);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(llm);
                recyclerView.setAdapter(adapter);
                dialogAttendancePercent.show();
            }
        });

        return v;
    }


    @SuppressLint("WrongConstant")
    @Override
    public void success(ArrayList<Double> attendancePercentageList) {
        admin_attendance_percent_list = attendancePercentageList;
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

    @Override
    public void exportCsvSuccess() {
        Toast.makeText(getContext(),"Export CSV DONE",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void exportCsvFailed() {
        Toast.makeText(getContext(),"Export CSV FAILED",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void notifySuccess() {
        Toast.makeText(getActivity(),"Success", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    @Override
    public void notifyFailed() {
        Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    private void sessionDatePick(final EditText editText){
        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                editText.setText(sdf.format(myCalendar.getTime()));
            }

        };

        editText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

}
