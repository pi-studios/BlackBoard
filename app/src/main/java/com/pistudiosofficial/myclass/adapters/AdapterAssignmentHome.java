package com.pistudiosofficial.myclass.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.activities.AssignmentViewerActivity;
import com.pistudiosofficial.myclass.activities.FacultyCheckIndivStudentSubmissionActivity;
import com.pistudiosofficial.myclass.objects.AssignmentObject;

import java.util.ArrayList;
import java.util.Collections;

public class AdapterAssignmentHome extends RecyclerView.Adapter<AdapterAssignmentHome.MyViewHolder> {

    ArrayList<AssignmentObject> assignmentObjects;
    Context context;

    //For AssignmentViewerSubmissionFrag
    ArrayList<String> roll_list;
    ArrayList<String> name_list;
    public AdapterAssignmentHome(ArrayList<AssignmentObject> assignmentObjects,Context context) {
        this.assignmentObjects = assignmentObjects;
        this.context = context;
        Collections.reverse(assignmentObjects);
    }

    public AdapterAssignmentHome(Context context, ArrayList<String> roll_list, ArrayList<String> name) {
        this.context = context;
        this.roll_list = roll_list;
        this.name_list = name;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.assignment_row,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        if (roll_list == null) {
            myViewHolder.textViewTitle.setText(assignmentObjects.get(i).getTitle());
            myViewHolder.textViewBody.setText(assignmentObjects.get(i).getTimePosted());
            myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AssignmentViewerActivity.class);
                    intent.putExtra("assignment_id", assignmentObjects.get(i).getAssignmentid());
                    context.startActivity(intent);
                }
            });
        }
        else{
            myViewHolder.textViewTitle.setText(roll_list.get(i));
            myViewHolder.textViewBody.setText(name_list.get(i));
            myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FacultyCheckIndivStudentSubmissionActivity.class);
                    intent.putExtra("roll",roll_list.get(i));
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (roll_list == null) {
            return assignmentObjects.size();
        }
        else{
            return roll_list.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle,textViewBody;
        LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.assignment_row_linearLayout);
            textViewTitle = itemView.findViewById(R.id.tv_assignmenthome_title);
            textViewBody = itemView.findViewById(R.id.tv_assignmentHome_due_date);
        }
    }

}
