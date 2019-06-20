package com.pistudiosofficial.myclass;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pistudiosofficial.myclass.activities.CheckAttendanceActivity;
import com.pistudiosofficial.myclass.view.MainActivityView;

import java.util.ArrayList;

import static com.pistudiosofficial.myclass.Common.CURRENT_USER;

public class AdapterClassList extends RecyclerView.Adapter<AdapterClassList.MyViewHolder> {

    ArrayList<ClassObject> classObjectArrayList;
    ArrayList<String> percentageList;
    MainActivityView mainActivityView;
    int adminLevel;
    public AdapterClassList(ArrayList<ClassObject> classObjectArrayList,
            ArrayList<String> percentageList, MainActivityView view) {
        this.classObjectArrayList = classObjectArrayList;
        this.percentageList = percentageList;
        this.mainActivityView = view;
        if(CURRENT_USER.AdminLevel.equals("admin")){
            adminLevel = 1;
        }
        if(CURRENT_USER.AdminLevel.equals("user")){
            adminLevel = 0;
        }
        if(CURRENT_USER.AdminLevel.equals("master_admin")){
            adminLevel = 2;
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = null;
        if(adminLevel == 1) {
            view = inflater.inflate(R.layout.admin_classlist_row, viewGroup, false);
        }
        if(adminLevel == 0) {
            view = inflater.inflate(R.layout.user_class_list_row, viewGroup,false);
        }
        else{

        }
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        if(adminLevel == 1){
            myViewHolder.tvclassName.setText(classObjectArrayList.get(i).className);
            myViewHolder.session.
                    setText(classObjectArrayList.get(i).sessionStart + " - " + classObjectArrayList.get(i).sessionEnd);
            myViewHolder.roll.
                    setText(classObjectArrayList.get(i).startRoll + " - " + classObjectArrayList.get(i).endRoll);
        }
        if(adminLevel == 0){
            myViewHolder.tvclassName.setText(classObjectArrayList.get(i).className);
            myViewHolder.faculty.setText("Prof : "+classObjectArrayList.get(i).facultyName);
            myViewHolder.attendance.setText(percentageList.get(i));
            myViewHolder.session.setText(classObjectArrayList.get(i).
                    sessionStart+" - "+classObjectArrayList.get(i).sessionEnd);
        }

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.CURRENT_INDEX = i;
                Intent intent = new Intent(view.getContext(), CheckAttendanceActivity.class);
                view.getContext().startActivity(intent);
            }
        });
        myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.end_session_dialog);
                Button button = dialog.findViewById(R.id.bt_faculty_end_session);
                Button addCollabButton = dialog.findViewById(R.id.bt_faculty_add_collab);
                Button transferClass = dialog.findViewById(R.id.bt_faculty_transfer);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                if(CURRENT_USER.AdminLevel.equals("user")){
                    addCollabButton.setVisibility(View.GONE);
                    transferClass.setVisibility(View.GONE);
                }

                dialog.show();
                final Dialog subDialog = new Dialog(view.getContext());
                addCollabButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        subDialog.setContentView(R.layout.add_hod_admin);
                        subDialog.show();
                        final EditText editText = subDialog.findViewById(R.id.et_addFaculty_email);
                        Button newButton = subDialog.findViewById(R.id.bt_add_new_class_student_popup);
                        newButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mainActivityView.addCollab(i,editText.getText().toString());
                                subDialog.dismiss();
                            }
                        });
                    }
                });
                transferClass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        subDialog.setContentView(R.layout.add_hod_admin);
                        subDialog.show();
                        final EditText editText = subDialog.findViewById(R.id.et_addFaculty_email);
                        Button newButton = subDialog.findViewById(R.id.bt_add_new_class_student_popup);
                        newButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mainActivityView.transferClass(i,editText.getText().toString());
                                subDialog.dismiss();
                            }
                        });
                    }
                });
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        new AlertDialog.Builder(view.getContext())
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Are You Sure?")
                                .setMessage("Once Session End, it CANNOT be reversed")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                     mainActivityView.endSession(i);
                                    }
                                }).setNegativeButton("No",null).show();
                        dialog.dismiss();
                    }
                });
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return classObjectArrayList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvclassName, session, roll,attendance,faculty;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            if(adminLevel == 1){
                tvclassName = itemView.findViewById(R.id.tv_name_faculty_main_recycler);
                session = itemView.findViewById(R.id.tv_session_faculty_main_recycler);
                roll = itemView.findViewById(R.id.tv_student_roll_faculty_main_recycler);
            }
            if(adminLevel == 0){
                tvclassName = itemView.findViewById(R.id.tv_user_row_className);
                session = itemView.findViewById(R.id.tv_session_student_main_recycler_row);
                attendance = itemView.findViewById(R.id.tv_attendance_student_main_recycler_row);
                faculty = itemView.findViewById(R.id.tv_facultyname_student_main_recycler_row);
            }
            else{

            }
        }
    }


}
