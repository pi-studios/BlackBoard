package com.pistudiosofficial.myclass.adapters;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pistudiosofficial.myclass.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.function.LongToIntFunction;

import static com.pistudiosofficial.myclass.Common.ROLL_LIST;
import static com.pistudiosofficial.myclass.Common.TOTAL_CLASSES;
import static com.pistudiosofficial.myclass.Common.sessionStartDate;

public class AdapterCheckAttendanceList extends RecyclerView.Adapter<AdapterCheckAttendanceList.MyViewHolder> {
    private static double totalPresentDays;

    ArrayList<Double> attendanceList;
    ArrayList<Double> presentday;
    long totalClasses;
    ArrayList<String> indivAttendance;
    public AdapterCheckAttendanceList( ArrayList<Double> attendanceList, long totalClasses, ArrayList<String> indivAttendance) {
        this.attendanceList = attendanceList;
        //total classes is number of child int classList->{UID}->attendance
        this.totalClasses = totalClasses;
        presentday = new ArrayList<>();
        this.indivAttendance = indivAttendance;
        if (indivAttendance == null) {
            for (double d : attendanceList) {
                double p = (d / 100.0) * totalClasses;
                presentday.add(p);
            }
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.check_attendance_row,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override

    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        //Change the design of  layout to show name and text, so add two text view
        if (indivAttendance == null) {
            myViewHolder.textView_roll.setText(ROLL_LIST.get(i));
            myViewHolder.textView_name.setText("Attendance");
            totalPresentDays = presentday.get(i);
            myViewHolder.total_days.setText((int)Math.round(totalPresentDays) + "/" + totalClasses);
            myViewHolder.textView_percent.setText(String.format("%.1f", attendanceList.get(i)));
        }
        else{
            myViewHolder.textView_roll.setText(ROLL_LIST.get(i));
            myViewHolder.textView_name.setText(indivAttendance.get(i));
            myViewHolder.total_days.setVisibility(View.INVISIBLE);
            myViewHolder.textView_percent.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return ROLL_LIST.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView_roll,textView_name,textView_percent,total_days;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //Change the design of  layout to show name and text, so add two text view
            textView_roll = itemView.findViewById(R.id.tv_check_attendance);
            textView_name=itemView.findViewById(R.id.tv_check_attendance_name);
            textView_percent=itemView.findViewById(R.id.tv_check_attendance_percent);
            total_days=itemView.findViewById(R.id.present_days);

        }
    }

}
