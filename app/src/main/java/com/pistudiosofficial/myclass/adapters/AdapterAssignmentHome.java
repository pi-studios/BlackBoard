package com.pistudiosofficial.myclass.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.objects.AssignmentObject;

import java.util.ArrayList;
import java.util.Collections;

public class AdapterAssignmentHome extends RecyclerView.Adapter<AdapterAssignmentHome.MyViewHolder> {

    ArrayList<AssignmentObject> assignmentObjects;
    Context context;

    public AdapterAssignmentHome(ArrayList<AssignmentObject> assignmentObjects,Context context) {
        this.assignmentObjects = assignmentObjects;
        this.context = context;
        Collections.reverse(assignmentObjects);
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
        myViewHolder.textViewTitle.setText(assignmentObjects.get(i).getTitle());
        myViewHolder.textViewBody.setText(assignmentObjects.get(i).getTimePosted());
    }

    @Override
    public int getItemCount() {
        return assignmentObjects.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle,textViewBody;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tv_assignmenthome_title);
            textViewBody = itemView.findViewById(R.id.tv_assignmentHome_due_date);
        }
    }

}
