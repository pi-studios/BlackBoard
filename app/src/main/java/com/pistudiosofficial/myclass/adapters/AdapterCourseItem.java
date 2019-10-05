package com.pistudiosofficial.myclass.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.activities.ProfileNewActivity;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.pistudiosofficial.myclass.Common.ATTD_PERCENTAGE_LIST;

public class AdapterCourseItem extends RecyclerView.Adapter<AdapterCourseItem.ViewHolder> {

    private static final String Tag="Recycler View Adapter";

    private ArrayList<String> mCourseNames=new ArrayList<>();
    private ArrayList<String> mInstructorNames=new ArrayList<>();
    private Context mContext;

    PieChart pieChart;
    public AdapterCourseItem(ArrayList<String> mCourseNames, ArrayList<String> mInstructorNames, Context mContext) {
        this.mCourseNames = mCourseNames;
        this.mInstructorNames = mInstructorNames;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG,"OnCreateView Holder Called");

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.course_listitem,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)  {

        Log.d(TAG,"OnBindView Holder Called");
        holder.CourseName.setText(mCourseNames.get(position));
        holder.InstructorName.setText(mInstructorNames.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float att=Float.parseFloat(ATTD_PERCENTAGE_LIST.get(position));
                Log.d("Dick","Dick");
                ProfileNewActivity profileNewActivity=new ProfileNewActivity();
                ArrayList<PieEntry> yValues=new ArrayList<>();
               yValues.add(new PieEntry(att,"Present",0));
               yValues.add(new PieEntry((100-att),"Absent",1));
               profileNewActivity.yValues=yValues;
               profileNewActivity.pieChart.notifyDataSetChanged();
               profileNewActivity.pieChart.invalidate();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCourseNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView CourseName,InstructorName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            CourseName=itemView.findViewById(R.id.courseName);
            InstructorName=itemView.findViewById(R.id.instructorName);
        }

    }
}
