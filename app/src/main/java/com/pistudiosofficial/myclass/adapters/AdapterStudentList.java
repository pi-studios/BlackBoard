package com.pistudiosofficial.myclass.adapters;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.objects.AccountListObject;
import com.pistudiosofficial.myclass.view.StudentListView;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterStudentList extends RecyclerView.Adapter<AdapterStudentList.MyViewHolder> {

    ArrayList<AccountListObject> accountListObjects;
    StudentListView view;
    HashMap<String,AccountListObject> hashMap;
    public AdapterStudentList(ArrayList<AccountListObject> accountListObjects, StudentListView view, HashMap<String,AccountListObject> hashMap) {
        this.accountListObjects = accountListObjects;
        this.view = view;
        this.hashMap = hashMap;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.admin_add_student_row,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tv_roll.setText(accountListObjects.get(i).Roll);
        myViewHolder.tv_name.setText(accountListObjects.get(i).Name);
        myViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    hashMap.put(accountListObjects.get(i).UID,accountListObjects.get(i));
                }
                else{
                    hashMap.remove(accountListObjects.get(i).UID);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return accountListObjects.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name,tv_roll;
        CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name_add_student_List);
            tv_roll = itemView.findViewById(R.id.tv_roll_add_student_list);
            checkBox = itemView.findViewById(R.id.cb_add_student_list);
        }
    }

}
