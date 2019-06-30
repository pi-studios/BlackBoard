package com.pistudiosofficial.myclass.model;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pistudiosofficial.myclass.Common;
import com.pistudiosofficial.myclass.objects.PostObject;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.CheckAttendancePresenterInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.pistudiosofficial.myclass.Common.ATTD_PERCENTAGE_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_CLASS_ID_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_INDEX;
import static com.pistudiosofficial.myclass.Common.ROLL_LIST;
import static com.pistudiosofficial.myclass.Common.TEMP01_LIST;
import static com.pistudiosofficial.myclass.Common.mREF_classList;

public class CheckAttendanceModel {
    CheckAttendancePresenterInterface presenter;
    ArrayList<Double> checkAttendanceList;
    ValueEventListener valueEventListener;
    String retrivedDate;
    ArrayList<Uri> imgURI; ArrayList<String> extensionList; String key;
    ArrayList<String> storageURL;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    public CheckAttendanceModel(CheckAttendancePresenterInterface presenter) {
        this.presenter = presenter;
        storageURL = new ArrayList<>();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    public void performCheckAttendanceDownload(){
        int startRoll = Integer.parseInt(Common.CURRENT_ADMIN_CLASS_LIST.get(Common.CURRENT_INDEX).startRoll);
        int endRoll = Integer.parseInt(Common.CURRENT_ADMIN_CLASS_LIST.get(Common.CURRENT_INDEX).endRoll);
        for (int i = startRoll; i<=endRoll; i++){
            ROLL_LIST.add(Integer.toString(i));
            TEMP01_LIST.add("ABSENT");
        }
        checkAttendanceList = new ArrayList<>();
        valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(int i = 0; i<dataSnapshot.getChildrenCount(); i++){
                            checkAttendanceList.add(dataSnapshot.child(ROLL_LIST.get(i))
                                    .getValue(Double.class));
                        }
                        mREF_classList.child(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX)).child("attendance_percentage")
                                .removeEventListener(valueEventListener);
                        for (int i =0; i<checkAttendanceList.size(); i++){
                            ATTD_PERCENTAGE_LIST.add(checkAttendanceList.get(i).toString());
                        }
                        presenter.adminCheckAttendanceDataDownloadSuccess(checkAttendanceList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        presenter.adminCheckAttendanceDataDownloadFailed();
                    }
        };
        mREF_classList.child(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX)).child("attendance_percentage")
                .addValueEventListener(valueEventListener);
    }

    public void performPostFileUpload(Uri fileURI, String extension){
        StorageReference fileREF = storageReference
                .child("post_file/"+ CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX)
                        +"/"+System.currentTimeMillis()+"."+extension);
        UploadTask uploadTask = fileREF.putFile(fileURI);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return fileREF.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    presenter.fileUploadLink(downloadUri.toString());
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    public void performPosting(PostObject postObject, ArrayList<Uri> imgURI, ArrayList<String> extensionList){
        String key = mREF_classList.child(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX))
                .child("post").push().getKey();
        mREF_classList.child(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX))
                .child("post").child(key).setValue(postObject, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError == null){
                    if(imgURI != null && extensionList != null) {
                       uploadInit(imgURI,extensionList,key);
                    }
                    else{
                        presenter.postingSuccess();
                    }
                }
                else {
                    presenter.postingFailed();
                }
            }
        });

    }

    public void checkMultipleAttendance(){
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String todayString = formatter.format(todayDate);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                retrivedDate = dataSnapshot.getValue(String.class);
                if(retrivedDate != null) {
                    if (retrivedDate.equals(todayString)) {
                        presenter.checkMultipleAttendanceReturn(false);
                    }
                    else {
                        presenter.checkMultipleAttendanceReturn(true);
                    }
                }
                else {
                    presenter.checkMultipleAttendanceReturn(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mREF_classList.child(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX))
                .child("last_attendance_date").addListenerForSingleValueEvent(valueEventListener);
    }

    private void uploadInit(ArrayList<Uri> imgURI, ArrayList<String> extensionList, String key){
        this.imgURI = imgURI; this.extensionList = extensionList; this.key = key;
        StorageReference img01REF = storageReference
                .child("post/"+CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX)
                        +"/"+key+"/"+System.currentTimeMillis()+"."+extensionList.get(0));
        UploadTask uploadTask = img01REF.putFile(imgURI.get(0));
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return img01REF.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    storageURL.add(downloadUri.toString());
                    if (imgURI.size()>1){
                        upload02();
                    }
                    else {
                        uploadMetaData();
                    }
                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }
    private void upload02(){
        StorageReference img02REF = storageReference
                .child("post/"+CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX)
                        +"/"+key+"/"+System.currentTimeMillis()+"."+extensionList.get(1));
        UploadTask uploadTask = img02REF.putFile(imgURI.get(1));
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return img02REF.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    storageURL.add(downloadUri.toString());
                    if (imgURI.size()==3){
                        upload03();
                    }
                    else {
                        uploadMetaData();
                    }
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }
    private void upload03(){
        StorageReference img03REF = storageReference
                .child("post/"+CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX)
                        +"/"+key+"/"+System.currentTimeMillis()+"."+extensionList.get(2));
        UploadTask uploadTask = img03REF.putFile(imgURI.get(2));
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return img03REF.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    storageURL.add(downloadUri.toString());
                    uploadMetaData();

                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    private void uploadMetaData(){
        for (int i = 0; i<storageURL.size(); i++){
            mREF_classList.child(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX))
                    .child("post").child(key).child("meta_data").child(Integer.toString(i))
                    .setValue(storageURL.get(i));
        }
        presenter.postingSuccess();
    }

 }

