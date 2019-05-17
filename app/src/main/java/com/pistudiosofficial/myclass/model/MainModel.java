package com.pistudiosofficial.myclass.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pistudiosofficial.myclass.ClassObject;
import com.pistudiosofficial.myclass.Common;
import com.pistudiosofficial.myclass.StudentClassObject;
import com.pistudiosofficial.myclass.UserObject;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.MainPresenterInterface;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static com.pistudiosofficial.myclass.Common.CURRENT_CLASS_ID_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_USER;
import static com.pistudiosofficial.myclass.Common.CURRENT_USER_CLASS_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_USER_CLASS_LIST_ID;
import static com.pistudiosofficial.myclass.Common.FIREBASE_USER;
import static com.pistudiosofficial.myclass.Common.LOG;
import static com.pistudiosofficial.myclass.Common.mREF_classList;
import static com.pistudiosofficial.myclass.Common.mREF_oldRecords;
import static com.pistudiosofficial.myclass.Common.mREF_student_classList;
import static com.pistudiosofficial.myclass.Common.mREF_users;

public class MainModel {

    MainPresenterInterface presenter;
    ArrayList<ClassObject> classObjectArrayList;
    ClassObject classObject;
    String temp;
    int roll;
    ValueEventListener valueEventListener;
    ArrayList<StudentClassObject> studentClassObjectArrayList;
    StudentClassObject studentClassObject;
    ArrayList<String> userAttendancePercentList;// Attendance to show top user side

    public MainModel(MainPresenterInterface presenter) {
        this.presenter = presenter;
    }

    public void performDataDownload(){

        //CURRENT USER
        mREF_users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Common.CURRENT_USER = dataSnapshot.child(FIREBASE_USER.getUid()).getValue(UserObject.class);
                if(CURRENT_USER != null){
                    presenter.downloadDataSuccess();
                }
                else { presenter.downloadDataFailed(); }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                    presenter.downloadDataFailed();
            }
        });


    }

    public void performAdminClassListDownload(){
        //Faculty Class List
        classObjectArrayList = new ArrayList<>();
        mREF_classList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                ClassObject classObject = dataSnapshot.getValue(ClassObject.class);
                if (classObject.facultyUID.equals(CURRENT_USER.UID)){
                    Common.CURRENT_CLASS_ID_LIST.add(dataSnapshot.getKey());
                    classObjectArrayList.add(dataSnapshot.getValue(ClassObject.class));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                presenter.downloadDataFailed();
            }
        });
        mREF_classList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                presenter.adminClassListDownloadSuccess(classObjectArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void performAdminAddClass(final ClassObject classObject){
        final String newID = mREF_classList.push().getKey();
        mREF_classList.child(newID).setValue(classObject, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError == null){
                    // now upload Fresh Blank Percentage List
                    double x=0;
                    for (int i = Integer.parseInt(classObject.startRoll); i<= Integer.parseInt(classObject.endRoll); i++){
                        mREF_classList.child(newID).child("attendance_percentage")
                                .child(Integer.toString(i)).setValue(x);
                    }
                    presenter.addAdminClassSuccess();
                }
                else{
                    presenter.addAdminClassFailed();
                }
            }
        });
    }

    public void performEndSession(int index){
        if(CURRENT_USER.AdminLevel.equals("admin")) {
            DatabaseReference fromPath;
            final DatabaseReference toPath;
            fromPath = mREF_classList.child(CURRENT_CLASS_ID_LIST.get(index));
            toPath = mREF_oldRecords;
            moveRecord(fromPath, toPath);
        }
        if(CURRENT_USER.AdminLevel.equals("user")){
            DatabaseReference fromPath = mREF_student_classList.child(CURRENT_USER_CLASS_LIST_ID.get(index));
            fromPath.removeValue();
        }
        else{

        }
    }

    private void moveRecord(DatabaseReference fromPath, final DatabaseReference toPath) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.push().setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            Log.d(TAG, "Success!");
                        } else {
                            Log.d(TAG, "Copy failed!");
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        fromPath.addListenerForSingleValueEvent(valueEventListener);
        fromPath.removeValue();
    }

    public void performUserClassListDownload(){
        studentClassObjectArrayList = new ArrayList<>();
        userAttendancePercentList = new ArrayList<>();
        classObjectArrayList = new ArrayList<>();
        roll = Integer.parseInt(CURRENT_USER.Roll)%1000;
        mREF_student_classList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                studentClassObject = dataSnapshot.getValue(StudentClassObject.class);
                if (studentClassObject.studentUID.equals(CURRENT_USER.UID)){
                    studentClassObjectArrayList.add(studentClassObject);
                    CURRENT_USER_CLASS_LIST_ID.add(dataSnapshot.getKey());
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(int i = 0; i<studentClassObjectArrayList.size(); i++){
                        temp = dataSnapshot.child(studentClassObjectArrayList.get(i).classKey)
                                .child("attendance_percentage")
                                .child(Integer.toString(roll))
                                .getValue(Double.class).toString();
                        classObjectArrayList.add(dataSnapshot.child(studentClassObjectArrayList.get(i).classKey)
                                .getValue(ClassObject.class));

                        userAttendancePercentList.add(temp);
                        mREF_classList.removeEventListener(valueEventListener);
                        presenter.userClassListDownloadSuccess(classObjectArrayList, userAttendancePercentList);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
        mREF_classList.addValueEventListener(valueEventListener);
    }

    public void performUserAddClass(final StudentClassObject studentClassObject){

        mREF_classList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                classObject = dataSnapshot.getValue(ClassObject.class);
                if(classObject.facultyEmail.equals(studentClassObject.facultyEmail) &&
                        classObject.joinCode.equals(studentClassObject.joinCode)){
                    studentClassObject.classKey = dataSnapshot.getKey();
                    mREF_student_classList.push().setValue(studentClassObject, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if(databaseError == null){presenter.addUserClassSuccess();}
                            else {presenter.addUserClassFailed();}
                        }
                    });
                }
                else {
                    presenter.addUserClassFailed();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
