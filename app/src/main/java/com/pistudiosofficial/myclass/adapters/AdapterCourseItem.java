package com.pistudiosofficial.myclass.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pistudiosofficial.myclass.R;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AdapterCourseItem extends RecyclerView.Adapter<AdapterCourseItem.ViewHolder>{

    private static final String Tag="Recycler View Adapter";

    private ArrayList<String> mCourseNames=new ArrayList<>();
    private ArrayList<String> mInstructorNames=new ArrayList<>();
    private Context mContext;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Log.d(TAG,"OnBindView Holder Called");
        holder.CourseName.setText(mCourseNames.get(position));
        holder.InstructorName.setText(mInstructorNames.get(position));
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
