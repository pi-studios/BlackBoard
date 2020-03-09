package com.pistudiosofficial.myclass.model;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pistudiosofficial.myclass.objects.AccountListObject;
import com.pistudiosofficial.myclass.objects.StudentClassObject;
import com.pistudiosofficial.myclass.view.StudentListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static com.pistudiosofficial.myclass.Common.CURRENT_USER;
import static com.pistudiosofficial.myclass.Common.FIREBASE_DATABASE;
import static com.pistudiosofficial.myclass.Common.mAUTH;
import static com.pistudiosofficial.myclass.Common.mREF_ACCOUNT_LIST;
import static com.pistudiosofficial.myclass.Common.mREF_classList;
import static com.pistudiosofficial.myclass.Common.mREF_student_classList;
import static com.pistudiosofficial.myclass.Common.mREF_users;

public class StudentListModel {
    private ArrayList<AccountListObject> account_list;
    HashMap<String,ArrayList<String>> spinnerMap;
    StudentListView view;
    public StudentListModel(StudentListView view) {
        account_list = new ArrayList<>();
        spinnerMap = new HashMap<>();
        this.view = view;
    }

    public void performStudentListDownload(String year, String dept){
        mREF_ACCOUNT_LIST.child(year).child(dept).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()>0) {
                    account_list.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        AccountListObject accountListObject = new AccountListObject();
                        accountListObject.UID = snapshot.getKey();
                        accountListObject.Name = snapshot.child("name").getValue(String.class);
                        accountListObject.Roll = snapshot.child("roll").getValue(String.class);
                        account_list.add(accountListObject);
                    }
                    view.listDownloadSuccess(account_list);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                view.listDownloadFailed();
            }
        });

    }

    public void performSpinnerDownload(){
        mREF_ACCOUNT_LIST.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    ArrayList<String> temp = new ArrayList<>();
                    temp.add("Pick Branch");
                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                        temp.add(snapshot1.getKey());
                    }
                    spinnerMap.put(snapshot.getKey(),temp);
                }
                view.spinnerLoadSuccess(spinnerMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                view.spinnerLoadFailed();
            }
        });



    }

    public void performStudentAdd(ArrayList<AccountListObject> accountListObjects,String classKey){
        for (AccountListObject a:accountListObjects) {
            StudentClassObject studentClassObject = new StudentClassObject();
            studentClassObject.classKey = classKey;
            studentClassObject.facultyEmail = CURRENT_USER.Email;
            studentClassObject.roll = a.Roll;
            studentClassObject.studentUID = a.UID;

            mREF_users.child(studentClassObject.studentUID).child("class_list").child(studentClassObject.classKey).setValue(studentClassObject, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if (databaseError == null) {
                        mREF_classList.child(studentClassObject.classKey)
                                .child("student_index").child(studentClassObject.studentUID)
                                .setValue(studentClassObject.studentUID);
                        view.studentAddSuccess();
                    } else {
                        view.studentAddFail();
                    }
                }
            });
        }
    }

}
