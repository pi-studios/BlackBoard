package com.pistudiosofficial.myclass.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.adapters.AdapterCheckAttendanceList;
import com.pistudiosofficial.myclass.model.CheckAttendanceModel;
import com.pistudiosofficial.myclass.objects.ClassObject;
import com.pistudiosofficial.myclass.view.ShowAttendanceView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.pistudiosofficial.myclass.Common.CURRENT_ADMIN_CLASS_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_CLASS_ID_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_INDEX;
import static com.pistudiosofficial.myclass.Common.ROLL_LIST;
import static com.pistudiosofficial.myclass.Common.SHOW_ATTENDANCE_PERCENTAGE;
import static com.pistudiosofficial.myclass.Common.mREF_classList;

public class ShowAttendanceActivity extends AppCompatActivity implements ShowAttendanceView {
    TextView fromDate,toDate;
    CheckAttendanceModel checkAttendanceModel;
    final Calendar myCalendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_attendance);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(CURRENT_ADMIN_CLASS_LIST.get(CURRENT_INDEX).className);
       // fromDate=findViewById(R.id.class_from);
        //toDate=findViewById(R.id.class_to);
       // setCurrentDate(fromDate);
        //setCurrentDate(toDate);
        //datePick(fromDate);
        //datePick(toDate);
        DatabaseReference databaseReference = mREF_classList.child(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX))
                .child("attendance");
        checkAttendanceModel = new CheckAttendanceModel(this);
        checkAttendanceModel.downloadTotalClass(databaseReference);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    private void datePick(final TextView textView){

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                /*SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String selectedDate = formatter.format(myCalendar.getTime());*/
                textView.setText(sdf.format(myCalendar.getTime()));
            }

        };

        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=
                new DatePickerDialog(ShowAttendanceActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Date todayDate = Calendar.getInstance().getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String todayString = formatter.format(todayDate);
                textView.setText(todayString);
                checkAttendanceModel.downloadTotalClass(mREF_classList
                                .child(CURRENT_CLASS_ID_LIST
                                .get(CURRENT_INDEX))
                                .child("attendance"));
                return true;
            }
        });

    }

    private  void setCurrentDate(TextView textView){
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        textView.setText(date);
    }

    @Override
    public void totalClassDownloadSuccess(long totalClass) {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        AdapterCheckAttendanceList adapter = new AdapterCheckAttendanceList(SHOW_ATTENDANCE_PERCENTAGE,totalClass,null);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_show_attendance);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
    }

/*    @Override
    public void indivAttendanceDownload(ArrayList<String> data) {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        AdapterCheckAttendanceList adapter = new AdapterCheckAttendanceList(null,0,data);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_show_attendance);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
    }*/
}
