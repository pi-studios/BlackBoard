package com.pistudiosofficial.myclass.model;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.NewAttendencePresenterInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.pistudiosofficial.myclass.Common.CURRENT_CLASS_ID_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_INDEX;
import static com.pistudiosofficial.myclass.Common.ROLL_LIST;
import static com.pistudiosofficial.myclass.Common.SHARED_PREFERENCES;
import static com.pistudiosofficial.myclass.Common.TEMP01_LIST;
import static com.pistudiosofficial.myclass.Common.mREF_classList;

public class NewAttendenceModel {

    NewAttendencePresenterInterface presenter;
    int  totalLectureCount;
    double temp01, temp02;
    DatabaseReference refAttendance, refAttdPercentage;
    ValueEventListener valueEventListener;
    ArrayList<String> templist;String todayString;
    public NewAttendenceModel(NewAttendencePresenterInterface presenter) {
        this.presenter = presenter;
    }

    public void performAttendenceUpload(){
        refAttendance = mREF_classList.child(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX))
                .child("attendance");
        refAttdPercentage = mREF_classList.child(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX))
                .child("attendance_percentage");
        // Individual Attendance Upload
        performIndividualAttendanceUpload();
    }

    public void performIndividualAttendanceUpload(){
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        todayString = formatter.format(todayDate);
        for (int i = 0; i<ROLL_LIST.size(); i++){
            refAttendance.child(todayString).child(ROLL_LIST.get(i)).setValue(TEMP01_LIST.get(i));
        }
        // Update Percentage List
        performAttendancePercentUpload();
    }


    private void tempFunction(){
        refAttdPercentage.removeEventListener(valueEventListener);
        for (int i = 0; i<templist.size(); i++){
            temp01 = Double.parseDouble(templist.get(i))/100;
            if(TEMP01_LIST.get(i).equals("PRESENT")){
                temp02 = 1.0;
            }
            else{
                temp02 = 0.0;
            }
            temp01 = (temp01*(totalLectureCount-1) + temp02)/totalLectureCount;
            double roundOff = Math.round(temp01 * 100.0) / 100.0;
            refAttdPercentage.child(ROLL_LIST.get(i)).setValue(roundOff * 100);
        }
            SHARED_PREFERENCES.edit().putString(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX), "true").apply();
            SHARED_PREFERENCES.edit().putString("day", todayString).apply();
            presenter.success();

    }
    void performAttendancePercentUpload(){
        refAttendance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalLectureCount = (int)dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        templist = new ArrayList<>();
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(int i = 0; i<dataSnapshot.getChildrenCount();i++){
                    templist.add(dataSnapshot.child(ROLL_LIST.get(i)).getValue().toString());
                }
                tempFunction();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                presenter.failed();
            }
        };
        refAttdPercentage.addValueEventListener(valueEventListener);
    }

}
