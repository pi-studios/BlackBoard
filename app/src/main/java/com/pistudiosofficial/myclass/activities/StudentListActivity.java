package com.pistudiosofficial.myclass.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.adapters.AdapterNotificationHistory;
import com.pistudiosofficial.myclass.adapters.AdapterStudentList;
import com.pistudiosofficial.myclass.model.StudentListModel;
import com.pistudiosofficial.myclass.objects.AccountListObject;
import com.pistudiosofficial.myclass.view.StudentListView;

import java.util.ArrayList;
import java.util.HashMap;

import static com.pistudiosofficial.myclass.Common.CURRENT_CLASS_ID_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_INDEX;

public class StudentListActivity extends AppCompatActivity implements StudentListView {
    StudentListModel model;
    Spinner spinnerYear, spinnerDept;
    ArrayAdapter yearAdapter,deptAdapter;
    HashMap<String, ArrayList<String>> spinnerMap;
    ArrayList<String> arrayListYear,arrayListDept;
    HashMap<String,AccountListObject> selected_student;
    Button bt_add;
    RecyclerView recyclerView;
    String year,dept;
    AdapterStudentList adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        model = new StudentListModel(this);
        spinnerDept = findViewById(R.id.spinner_studentList_dept);
        spinnerYear = findViewById(R.id.spinner_studentList_year);
        bt_add = findViewById(R.id.bt_admin_add_student);
        recyclerView = findViewById(R.id.rv_studentList);
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0){
                    year = arrayListYear.get(position);
                    arrayListDept = spinnerMap.get(arrayListYear.get(position));
                    deptAdapter = new ArrayAdapter(StudentListActivity.this,android.R.layout.simple_spinner_item,arrayListDept);
                    deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerDept.setAdapter(deptAdapter);                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerDept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (arrayListDept != null && position != 0){
                    dept = arrayListDept.get(position);
                    model.performStudentListDownload(year,dept);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        model.performSpinnerDownload();
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected_student != null && selected_student.size()>0){
                    ArrayList<AccountListObject> accountListObjects = new ArrayList<>();
                    for (String s:selected_student.keySet()){
                        accountListObjects.add(selected_student.get(s));
                    }
                    model.performStudentAdd(accountListObjects,CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX));
                }
            }
        });

    }

    @Override
    public void spinnerLoadSuccess(HashMap<String, ArrayList<String>> spinnerMap) {
        this.spinnerMap = spinnerMap;
        arrayListYear = new ArrayList<>();
        arrayListYear.add("Pick Year");
        for (String s:spinnerMap.keySet()){
            arrayListYear.add(s);
        }
        yearAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arrayListYear);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(yearAdapter);
    }

    @Override
    public void spinnerLoadFailed() {
        Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void listDownloadSuccess(ArrayList<AccountListObject> objectArrayList) {
        selected_student = new HashMap<>();
        adapter = new AdapterStudentList(objectArrayList,this,selected_student);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void listDownloadFailed() {
        Toast.makeText(this,"Error: Please Restart the App",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void studentAddSuccess() {
        Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void studentAddFail() {
        Toast.makeText(this,"Error: Try Again",Toast.LENGTH_SHORT).show();
    }
}
