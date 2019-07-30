package com.pistudiosofficial.myclass.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.adapters.AdapterCheckAttendanceList;
import com.pistudiosofficial.myclass.objects.ClassObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.pistudiosofficial.myclass.Common.SHOW_ATTENDANCE_PERCENTAGE;

public class ShowAttendanceActivity extends AppCompatActivity {
    ArrayList<Double> admin_attendance_percent_list;
    TextView fromDate,toDate;
    final Calendar myCalendar = Calendar.getInstance();
    ClassObject classObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        admin_attendance_percent_list = SHOW_ATTENDANCE_PERCENTAGE;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_attendance);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Class");
        fromDate=findViewById(R.id.class_from);
        toDate=findViewById(R.id.class_to);
        setCurrentDate(fromDate);
        setCurrentDate(toDate);
        datePick(fromDate);
        datePick(toDate);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        AdapterCheckAttendanceList adapter = new AdapterCheckAttendanceList(admin_attendance_percent_list);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_show_attendance);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
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
                datePickerDialog.show();;
            }
        });

    }

    private  void setCurrentDate(TextView textView){
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        textView.setText(date);
    }

}
