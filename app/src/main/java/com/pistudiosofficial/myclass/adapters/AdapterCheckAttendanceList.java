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
        myViewHolder.textView.setText(ROLL_LIST.get(i)+"  -----  "+attendanceList.get(i));
    }

    @Override
    public int getItemCount() {
        return ROLL_LIST.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_new_attendance);
        }
    }

}
