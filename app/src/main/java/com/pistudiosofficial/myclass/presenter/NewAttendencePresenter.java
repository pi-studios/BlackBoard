package com.pistudiosofficial.myclass.presenter;

import android.util.Log;

import com.pistudiosofficial.myclass.model.NewAttendenceModel;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.NewAttendencePresenterInterface;
import com.pistudiosofficial.myclass.view.NewAttendenceView;

import java.util.ArrayList;

import static com.pistudiosofficial.myclass.Common.EDIT_ATTENDANCE_DATE;
import static com.pistudiosofficial.myclass.Common.INDIV_ATTENDANCE_ORIGNAL;
import static com.pistudiosofficial.myclass.Common.ROLL_LIST;
import static com.pistudiosofficial.myclass.Common.TEMP01_LIST;
import static com.pistudiosofficial.myclass.Common.TOTAL_CLASSES;

public class NewAttendencePresenter implements NewAttendencePresenterInterface {

    NewAttendenceView view;
    NewAttendenceModel model;

    public NewAttendencePresenter(NewAttendenceView view) {
        this.view = view;
        this.model =new  NewAttendenceModel(this);
    }

    public void performAttendenceUpload(){
        model.performAttendenceUpload();
    }

    public void performEditUpload(){
        model.performAttendancePercent();
    }

    @Override
    public void success() {
        view.uploadSuccess();
    }

    @Override
    public void failed() {
        view.uploadFailed();
    }

    @Override
    public void percentListDownloaded(ArrayList<Double> percentList) {
        for (int i = 0; i<TEMP01_LIST.size(); i++){
            Log.i("TAG",TEMP01_LIST.get(i)+"  "+INDIV_ATTENDANCE_ORIGNAL.get(i));
            if (TEMP01_LIST.get(i).equals("PRESENT")&&INDIV_ATTENDANCE_ORIGNAL.get(i).equals("ABSENT")){
                // A->P
                double percent = percentList.get(i)/100.0;
                double presentDays = ((double)TOTAL_CLASSES)*percent;
                presentDays += 1;
                percent = presentDays/(double)TOTAL_CLASSES;
                percentList.set(i,percent*100);
            }
            if (TEMP01_LIST.get(i).equals("ABSENT")&&INDIV_ATTENDANCE_ORIGNAL.get(i).equals("PRESENT")){
                // P->A
                double percent = percentList.get(i)/100.0;
                double presentDays = ((double)TOTAL_CLASSES)*percent;
                presentDays -= 1;
                percent = presentDays/(double)TOTAL_CLASSES;
                percentList.set(i,percent);
            }
        }
        model.updateEditedAttendance(ROLL_LIST, TEMP01_LIST, percentList, EDIT_ATTENDANCE_DATE);
    }

    @Override
    public void editSuccess() {
        view.editSuccess();
    }

    @Override
    public void editFailed() {
        view.editFailed();
    }
}
