package com.pistudiosofficial.myclass.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.util.Strings;
import com.google.firebase.database.DatabaseReference;
import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.adapters.AdapterCheckAttendanceList;
import com.pistudiosofficial.myclass.model.CheckAttendanceModel;
import com.pistudiosofficial.myclass.objects.ClassObject;
import com.pistudiosofficial.myclass.view.ShowAttendanceView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import static com.pistudiosofficial.myclass.Common.CURRENT_ADMIN_CLASS_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_CLASS_ID_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_INDEX;
import static com.pistudiosofficial.myclass.Common.EDIT_ATTENDANCE_DATE;
import static com.pistudiosofficial.myclass.Common.INDIV_ATTENDANCE_ORIGNAL;
import static com.pistudiosofficial.myclass.Common.NEW_ATTENDANCE;
import static com.pistudiosofficial.myclass.Common.ROLL_LIST;
import static com.pistudiosofficial.myclass.Common.SHOW_ATTENDANCE_PERCENTAGE;
import static com.pistudiosofficial.myclass.Common.TEMP01_LIST;
import static com.pistudiosofficial.myclass.Common.TOTAL_CLASSES;
import static com.pistudiosofficial.myclass.Common.mREF_classList;

public class ShowAttendanceActivity extends AppCompatActivity implements ShowAttendanceView, AdapterView.OnItemSelectedListener {
    Spinner spinner;
    ArrayAdapter spnAdapter;
    Context context;
    CheckAttendanceModel checkAttendanceModel;
    ArrayList<String> dateListMain;
    RecyclerView recyclerView;
    Button editAttendance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_attendance);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(CURRENT_ADMIN_CLASS_LIST.get(CURRENT_INDEX).className);
        spinner = findViewById(R.id.spn_show_attendance);
        recyclerView = findViewById(R.id.recycler_view_show_attendance);
        DatabaseReference databaseReference = mREF_classList.child(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX))
                .child("attendance");
        editAttendance = findViewById(R.id.bt_edit_attendance);
        checkAttendanceModel = new CheckAttendanceModel(this);
        checkAttendanceModel.downloadTotalClass(databaseReference);
        spinner.setOnItemSelectedListener(this);
        context = this;
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void totalClassDownloadSuccess(long totalClass, ArrayList<String> dateList) {
        dateListMain = dateList;
        TOTAL_CLASSES = totalClass;
        LinearLayoutManager llm = new LinearLayoutManager(this);
        AdapterCheckAttendanceList adapter = new AdapterCheckAttendanceList(SHOW_ATTENDANCE_PERCENTAGE,totalClass,null);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
        Collections.reverse(dateListMain);
        spnAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,dateListMain);
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spnAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i != 0) {
            DatabaseReference reference = mREF_classList.child(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX))
                    .child("attendance").child(dateListMain.get(i));
            checkAttendanceModel.downloadIndivAttendance(reference);
            editAttendance.setVisibility(View.VISIBLE);
            editAttendance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NEW_ATTENDANCE = false;
                    EDIT_ATTENDANCE_DATE = dateListMain.get(i);
                    Intent intent = new Intent(getApplicationContext(),NewAttendenceAcitivity.class);
                    startActivity(intent);
                }
            });
        }
        if (i == 0){
            editAttendance.setVisibility(View.INVISIBLE);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            AdapterCheckAttendanceList adapter = new AdapterCheckAttendanceList(SHOW_ATTENDANCE_PERCENTAGE,TOTAL_CLASSES,null);
            recyclerView.setLayoutManager(llm);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void indivAttendanceDownload(ArrayList<String> data) {
        TEMP01_LIST = data;
        INDIV_ATTENDANCE_ORIGNAL  = new ArrayList<>();
        for (String s:data){
            INDIV_ATTENDANCE_ORIGNAL.add(s);
        }
        LinearLayoutManager llm = new LinearLayoutManager(this);
        AdapterCheckAttendanceList adapter = new AdapterCheckAttendanceList(null,0,data);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
    }
}
