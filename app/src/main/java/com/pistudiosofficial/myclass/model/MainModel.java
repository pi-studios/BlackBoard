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
import com.pistudiosofficial.myclass.AdminClassObject;
import com.pistudiosofficial.myclass.ClassObject;
import com.pistudiosofficial.myclass.Common;
import com.pistudiosofficial.myclass.NotificationStoreObj;
import com.pistudiosofficial.myclass.StudentClassObject;
import com.pistudiosofficial.myclass.UserObject;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.MainPresenterInterface;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;
import static com.pistudiosofficial.myclass.Common.CURRENT_ADMIN_CLASS_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_CLASS_ID_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_USER;
import static com.pistudiosofficial.myclass.Common.CURRENT_USER_CLASS_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_USER_CLASS_LIST_ID;
import static com.pistudiosofficial.myclass.Common.FIREBASE_USER;
import static com.pistudiosofficial.myclass.Common.LOG;
import static com.pistudiosofficial.myclass.Common.mREF_admin_classList;
import static com.pistudiosofficial.myclass.Common.mREF_classList;
import static com.pistudiosofficial.myclass.Common.mREF_oldRecords;
import static com.pistudiosofficial.myclass.Common.mREF_student_classList;
import static com.pistudiosofficial.myclass.Common.mREF_users;

public class MainModel {

    MainPresenterInterface presenter;
    ArrayList<ClassObject> classObjectArrayList;
    ArrayList<String> adminList;
    ArrayList<UserObject> userObjectsList;
    ClassObject classObject;
    String temp;String collabUID = "";
    int roll,flag =0;
    ArrayList<String> adminclasslistTemp;
    DatabaseReference fromPath;
    DatabaseReference toPath;
    AdminClassObject adminClassObject;
    ValueEventListener tempListener, tempListener2,tempListener3;
    ValueEventListener tempListener4;
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
        classObjectArrayList = new ArrayList<>();
        classObjectArrayList.clear();
       // presenter.adminClassListDownloadSuccess(classObjectArrayList);
        tempListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (int i=0; i<CURRENT_CLASS_ID_LIST.size();i++){
                    classObject = dataSnapshot.child(CURRENT_CLASS_ID_LIST.get(i)).getValue(ClassObject.class);
                    classObjectArrayList.add(classObject);
                }
                presenter.adminClassListDownloadSuccess(classObjectArrayList);
                mREF_classList.removeEventListener(tempListener2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        tempListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()){
                    adminClassObject = s.getValue(AdminClassObject.class);
                    if(adminClassObject.adminUID.equals(CURRENT_USER.UID)){
                        CURRENT_CLASS_ID_LIST.add(adminClassObject.classKey);
                    }
                }
                mREF_classList.addValueEventListener(tempListener2);
                mREF_admin_classList.removeEventListener(tempListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


        mREF_admin_classList.addValueEventListener(tempListener);
    }

    public void performAdminAddClass(final ClassObject classObject){
        CURRENT_CLASS_ID_LIST.clear();
        CURRENT_ADMIN_CLASS_LIST.clear();
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
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                    String simpleTime = simpleDateFormat.format(new Date());
                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                    mREF_classList.child(newID).child("notification").push()
                            .setValue(new NotificationStoreObj(classObject.className,
                                    "Class Created: \n"+currentDateTimeString,simpleTime));
                    AdminClassObject obj = new AdminClassObject(CURRENT_USER.UID, newID);
                    mREF_admin_classList.push().setValue(obj);
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
            fromPath = mREF_classList.child(CURRENT_CLASS_ID_LIST.get(index));
            toPath = mREF_oldRecords;
            deleteIndex(CURRENT_CLASS_ID_LIST.get(index));
            moveRecord(fromPath,toPath);
        }
        if(CURRENT_USER.AdminLevel.equals("user")){
            DatabaseReference fromPath = mREF_student_classList.child(CURRENT_USER_CLASS_LIST_ID.get(index));
            fromPath.removeValue();
        }
        else{

        }
    }

    private void moveRecord(final DatabaseReference fromPath, final DatabaseReference toPath) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.push().setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            fromPath.removeValue();
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
    }

    public void performUserClassListDownload(){
        ChildEventListener childEventListener;
        studentClassObjectArrayList = new ArrayList<>();
        userAttendancePercentList = new ArrayList<>();
        classObjectArrayList = new ArrayList<>();
        roll = Integer.parseInt(CURRENT_USER.Roll)%1000;
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                studentClassObject = dataSnapshot.getValue(StudentClassObject.class);
                if (studentClassObject.studentUID.equals(CURRENT_USER.UID)){
                    studentClassObjectArrayList.add(studentClassObject);
                    CURRENT_USER_CLASS_LIST_ID.add(dataSnapshot.getKey());
                    CURRENT_CLASS_ID_LIST.add(studentClassObject.classKey);
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
        };
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

                        mREF_student_classList.removeEventListener(childEventListener);
                        presenter.userClassListDownloadSuccess(classObjectArrayList, userAttendancePercentList);
                    }
                    if (studentClassObjectArrayList == null || studentClassObjectArrayList.size()<1){
                        presenter.downloadDataFailed();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    presenter.downloadDataFailed();
                }
            };
        mREF_student_classList.addChildEventListener(childEventListener);
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

    public void deleteIndex(final String x){
        adminclasslistTemp= new ArrayList<>();
        mREF_admin_classList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                adminClassObject = dataSnapshot.getValue(AdminClassObject.class);
                if(adminClassObject.classKey.equals(x)){
                    adminclasslistTemp.add(dataSnapshot.getKey());
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
        mREF_admin_classList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(int i=0; i<adminclasslistTemp.size();i++){
                    mREF_admin_classList.child(adminclasslistTemp.get(i)).removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addCollab(int index, final String email){
        final String classID = CURRENT_CLASS_ID_LIST.get(index);


        mREF_users.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserObject userObject = dataSnapshot.getValue(UserObject.class);
                if(userObject.Email.equals(email)){
                    collabUID = userObject.UID;
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
        mREF_users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!collabUID.equals("")){
                    AdminClassObject obj = new AdminClassObject(collabUID, classID);
                    searchAndAddCollabFinal(obj);
                    return;
                }
                else {
                    presenter.transferCollabActionFailed();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void transferClass(final int index, final String email){
            mREF_admin_classList.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    AdminClassObject adminClassObject = dataSnapshot.getValue(AdminClassObject.class);
                    if((adminClassObject.adminUID.equals(CURRENT_USER.UID))&&
                            (adminClassObject.classKey.equals(CURRENT_CLASS_ID_LIST.get(index)))){
                        mREF_admin_classList.child(dataSnapshot.getKey()).removeValue();

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

            tempListener3 = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mREF_admin_classList.removeEventListener(tempListener3);
                    addCollab(index,email);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    presenter.transferCollabActionFailed();
                }
            };
        mREF_admin_classList.addValueEventListener(tempListener3);
    }

    public void searchAndAddCollabFinal(final AdminClassObject obj){
        flag = 0;

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AdminClassObject objTemp;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    objTemp = snapshot.getValue(AdminClassObject.class);
                    if(objTemp.adminUID.equals(obj.adminUID)&&objTemp.classKey.equals(obj.classKey)){
                        flag = 1;
                    }
                }
                if(flag == 0){
                    mREF_admin_classList.push().setValue(obj);
                }
                return;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mREF_admin_classList.addValueEventListener(valueEventListener);
    }

    public void performConnectionDownload(){
        adminList = new ArrayList<>();
        userObjectsList = new ArrayList<>();
        ValueEventListener valueEventListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mREF_admin_classList.removeEventListener(valueEventListener);
                for(int count = 0; count<adminList.size();count++) {
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        UserObject obj = s.getValue(UserObject.class);
                        if(adminList.get(count).equals(obj.UID)){
                            flag = 0;
                            for (int i = 0; i<userObjectsList.size();i++){
                                if(userObjectsList.get(0) != null){
                                   if(userObjectsList.get(i).UID.equals(obj.UID)){
                                       flag = 1;
                                   }
                                }
                            }
                            if (flag == 0){
                                userObjectsList.add(obj);
                            }
                        }
                    }
                }

                presenter.connectionListDownloadSuccess(userObjectsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                presenter.connectionListDownloadFailed();
            }
        };

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(int count = 0; count<CURRENT_CLASS_ID_LIST.size();count++) {
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        AdminClassObject obj = s.getValue(AdminClassObject.class);
                        if(CURRENT_CLASS_ID_LIST.get(count).equals(obj.classKey)){
                            adminList.add(obj.adminUID);
                        }
                    }
                }
                mREF_users.addValueEventListener(valueEventListener1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mREF_admin_classList.addValueEventListener(valueEventListener);

    }


}
