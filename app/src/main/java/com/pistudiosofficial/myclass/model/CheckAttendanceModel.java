package com.pistudiosofficial.myclass.model;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pistudiosofficial.myclass.Common;
import com.pistudiosofficial.myclass.PostObject;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.CheckAttendancePresenterInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.pistudiosofficial.myclass.Common.ATTD_PERCENTAGE_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_CLASS_ID_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_INDEX;
import static com.pistudiosofficial.myclass.Common.POST_OBJECT_LIST;
import static com.pistudiosofficial.myclass.Common.ROLL_LIST;
import static com.pistudiosofficial.myclass.Common.TEMP01_LIST;
import static com.pistudiosofficial.myclass.Common.mREF_classList;

public class CheckAttendanceModel {
    CheckAttendancePresenterInterface presenter;
    ArrayList<Double> checkAttendanceList;
    ValueEventListener valueEventListener;
    String retrivedDate;
    public CheckAttendanceModel(CheckAttendancePresenterInterface presenter) {
        this.presenter = presenter;
    }

    public void performCheckAttendanceDownload(){
        int startRoll = Integer.parseInt(Common.CURRENT_ADMIN_CLASS_LIST.get(Common.CURRENT_INDEX).startRoll);
        int endRoll = Integer.parseInt(Common.CURRENT_ADMIN_CLASS_LIST.get(Common.CURRENT_INDEX).endRoll);
        for (int i = startRoll; i<=endRoll; i++){
            ROLL_LIST.add(Integer.toString(i));
            TEMP01_LIST.add("ABSENT");
        }
        checkAttendanceList = new ArrayList<>();
        valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(int i = 0; i<dataSnapshot.getChildrenCount(); i++){
                            checkAttendanceList.add(dataSnapshot.child(ROLL_LIST.get(i))
                                    .getValue(Double.class));
                        }
                        mREF_classList.child(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX)).child("attendance_percentage")
                                .removeEventListener(valueEventListener);
                        for (int i =0; i<checkAttendanceList.size(); i++){
                            ATTD_PERCENTAGE_LIST.add(checkAttendanceList.get(i).toString());
                        }
                        presenter.adminCheckAttendanceDataDownloadSuccess(checkAttendanceList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        presenter.adminCheckAttendanceDataDownloadFailed();
                    }
        };
        mREF_classList.child(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX)).child("attendance_percentage")
                .addValueEventListener(valueEventListener);
    }

    public void performPosting(PostObject postObject){
        mREF_classList.child(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX))
                .child("post").push().setValue(postObject, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError != null){
                    presenter.postingSuccess();
                }
                else {
                    presenter.postingFailed();
                }
            }
        });

    }

    public void checkMultipleAttendance(){
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String todayString = formatter.format(todayDate);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                retrivedDate = dataSnapshot.getValue(String.class);
                if(retrivedDate != null) {
                    if (retrivedDate.equals(todayString)) {
                        presenter.checkMultipleAttendanceReturn(false);
                    }
                    else {
                        presenter.checkMultipleAttendanceReturn(true);
                    }
                }
                else {
                    presenter.checkMultipleAttendanceReturn(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mREF_classList.child(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX))
                .child("last_attendance_date").addListenerForSingleValueEvent(valueEventListener);
    }

}
