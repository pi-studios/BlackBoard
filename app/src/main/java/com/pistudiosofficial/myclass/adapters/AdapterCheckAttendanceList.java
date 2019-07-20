package com.pistudiosofficial.myclass.adapters;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pistudiosofficial.myclass.R;

import java.util.ArrayList;

import static com.pistudiosofficial.myclass.Common.ROLL_LIST;

public class AdapterCheckAttendanceList extends RecyclerView.Adapter<AdapterCheckAttendanceList.MyViewHolder> {

    ArrayList<Double> attendanceList;

    public AdapterCheckAttendanceList( ArrayList<Double> attendanceList) {
        this.attendanceList = attendanceList;
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
        Log.i("TAG","ROLL lIST: "+ROLL_LIST.size()+"  attendanceList "+attendanceList.size());
        //Change the design of  layout to show name and text, so add two text view
        myViewHolder.textView_roll.setText(ROLL_LIST.get(i));
        myViewHolder.textView_name.setText("Vivek");
        myViewHolder.textView_percent.setText(String.format("%.2f",attendanceList.get(i)));
        //
    }

    @Override
    public int getItemCount() {
        return ROLL_LIST.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView_roll,textView_name,textView_percent;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //Change the design of  layout to show name and text, so add two text view
            textView_roll = itemView.findViewById(R.id.tv_check_attendance);
            textView_name=itemView.findViewById(R.id.tv_check_attendance_name);
            textView_percent=itemView.findViewById(R.id.tv_check_attendance_percent);
            //
        }
    }

}
