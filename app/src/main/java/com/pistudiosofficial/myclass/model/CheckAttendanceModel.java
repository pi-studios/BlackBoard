package com.pistudiosofficial.myclass.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pistudiosofficial.myclass.Common;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.CheckAttendancePresenterInterface;

import java.util.ArrayList;

import static com.pistudiosofficial.myclass.Common.CURRENT_CLASS_ID_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_INDEX;
import static com.pistudiosofficial.myclass.Common.ROLL_LIST;
import static com.pistudiosofficial.myclass.Common.TEMP01_LIST;
import static com.pistudiosofficial.myclass.Common.mREF_classList;

public class CheckAttendanceModel {
    CheckAttendancePresenterInterface presenter;
    ArrayList<Double> checkAttendanceList;
    ValueEventListener valueEventListener;
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
}
