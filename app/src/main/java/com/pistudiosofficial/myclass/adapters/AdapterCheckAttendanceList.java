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
    public AdapterCheckAttendanceList( ArrayList<Double> attendanceList, long totalClasses) {
        this.attendanceList = attendanceList;
        //total classes is number of child int classList->{UID}->attendance
        this.totalClasses = totalClasses;
        presentday = new ArrayList<>();

        for(double d:attendanceList){
            double p = (d/100.0)*totalClasses;
            presentday.add(p);
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
        myViewHolder.textView_roll.setText(ROLL_LIST.get(i));
        myViewHolder.textView_name.setText("Attendance");
        totalPresentDays=presentday.get(i);
        myViewHolder.total_days.setText(totalPresentDays+"/"+totalClasses);
        myViewHolder.textView_percent.setText(String.format("%.1f",attendanceList.get(i)));
        //
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
            //
        }
    }
    public  static long dateDiffernce(String startDate,String FinalDate){
        long dayDifference=0;
        try {
            //Dates to compare
            Date date1;
            Date date2;

            SimpleDateFormat dates = new SimpleDateFormat("dd/MM/yyyy");

            //Setting dates
            date1 = dates.parse(startDate);
            date2 = dates.parse(FinalDate);

            //Comparing dates
            long difference = Math.abs(date1.getTime() - date2.getTime());
             dayDifference = difference / (24 * 60 * 60 * 1000);

            //Convert long to String
            Log.e("HERE","HERE: " + dayDifference);

        } catch (Exception exception) {
            Log.e("DIDN'T WORK", "exception " + exception);
        }
        return dayDifference;
    }

}
