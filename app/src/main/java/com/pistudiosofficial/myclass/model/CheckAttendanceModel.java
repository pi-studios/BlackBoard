package com.pistudiosofficial.myclass.model;

import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Log;

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
import com.pistudiosofficial.myclass.objects.PollOptionValueLikeObject;
import com.pistudiosofficial.myclass.objects.PostObject;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.CheckAttendancePresenterInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static com.pistudiosofficial.myclass.Common.ATTD_PERCENTAGE_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_CLASS_ID_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_INDEX;

import static com.pistudiosofficial.myclass.Common.CURRENT_USER;
import static com.pistudiosofficial.myclass.Common.LOG;
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
        postObject.setPostID(key);
        mREF_classList.child(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX))
                .child("post").child(key).setValue(postObject, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError == null){
                    postReadIndex(key);
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

    public void deleteClassRead(String classID){
        if (CURRENT_USER.AdminLevel.equals("admin")){
            mREF_classList.child(classID).child("admin_index").child(CURRENT_USER.UID).setValue("true");
        }
        if (CURRENT_USER.AdminLevel.equals("user")){
            mREF_classList.child(classID).child("student_index").child(CURRENT_USER.UID).child("new_post").setValue("false");
        }
    }

    private void postReadIndex(String key){
        ArrayList<String> studentUID = new ArrayList<>();
        mREF_classList.child(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX)).child("student_index")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot s :dataSnapshot.getChildren()){
                            studentUID.add(s.getKey());
                        }
                        for (String id :studentUID){
                            mREF_classList.child(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX))
                                .child("student_index").child(id).child("new_post").setValue(true);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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


    ArrayList<PostObject> postObjects ;
    HashMap<String, PollOptionValueLikeObject> post_poll_option ;
    ArrayList<String> post_like_list ;
    HashMap<String, ArrayList<String>> post_url_list ;
    ArrayList<String> post_comment_count;
    ArrayList<String> likedPostID;
    HashMap<String,String> pollSelectPostID;
    public void performLoadPost(String classID){
        postObjects = new ArrayList<>();
        pollSelectPostID = new HashMap<>();
        likedPostID = new ArrayList<>();
        post_like_list = new ArrayList<>();
        post_poll_option = new HashMap<>();
        post_url_list = new HashMap<>();
        post_comment_count = new ArrayList<>();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()>0) {
                    for (DataSnapshot s : dataSnapshot.getChildren()){
                        postObjects.add(s.getValue(PostObject.class));

                    }
                    likeLoad(postObjects,classID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mREF_classList.child(classID)
                .child("post").addListenerForSingleValueEvent(valueEventListener);
    }

    private void likeLoad(ArrayList<PostObject> postObject,String classID){
        ValueEventListener valueEventListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (PostObject p : postObject){
                    if (dataSnapshot.child(p.getPostID())
                            .child("like").child("like_count").getValue() != null){
                        post_like_list.add(dataSnapshot.child(p.getPostID())
                                .child("like").child("like_count").getValue().toString());
                    }
                    if (dataSnapshot.child(p.getPostID())
                            .child("like").child("like_count").getValue() == null){
                        post_like_list.add("0");
                    }
                    if (dataSnapshot.child(p.getPostID())
                            .child("comment").child("comment_count").getValue() != null){
                        post_comment_count.add(dataSnapshot.child(p.getPostID())
                                .child("comment").child("comment_count").getValue().toString());
                    }
                    if (dataSnapshot.child(p.getPostID())
                            .child("comment").child("comment_count").getValue()==null){
                        post_comment_count.add("0");
                    }
                    if (dataSnapshot.child(p.getPostID()).child("like").child(CURRENT_USER.UID).getValue() != null){
                        likedPostID.add(p.getPostID());
                    }
                    if (dataSnapshot.child(p.getPostID()).child("like").child(CURRENT_USER.UID).getValue() == null){
                        likedPostID.add("null");
                    }
                }
                metaDataLoad(postObject,classID);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mREF_classList.child(classID).child("post").addListenerForSingleValueEvent(valueEventListener1);
    }

    private void metaDataLoad(ArrayList<PostObject> postObject,String classID){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (PostObject p :postObject){
                    if (!p.getPostType().equals("admin_poll")){
                        if (dataSnapshot.child(p.getPostID())
                                .child("meta_data").getChildrenCount() != 0){
                            ArrayList<String> url = new ArrayList<>();
                            for (DataSnapshot s : dataSnapshot.child(p.getPostID())
                                    .child("meta_data").getChildren()){
                                url.add(s.getValue().toString());
                            }
                            post_url_list.put(p.getPostID(),url);

                        }
                    }else {
                        PollOptionValueLikeObject postmetaOBJ = new PollOptionValueLikeObject();
                        for (DataSnapshot s:dataSnapshot.child(p.getPostID()).child("options").getChildren()){
                            if(!s.getKey().equals("poll_clicked_user")) {
                                postmetaOBJ.optionList.add(s.getKey());
                                postmetaOBJ.votesCountList.add(s.getValue().toString());
                            }
                        }
                        if (dataSnapshot.child(p.getPostID()).child("options").child("poll_clicked_user").
                        child(CURRENT_USER.UID).getValue() != null){
                            pollSelectPostID.put(p.getPostID(),dataSnapshot.child(p.getPostID())
                                    .child("options").child("poll_clicked_user").
                                    child(CURRENT_USER.UID).getValue().toString());
                        }
                        if (dataSnapshot.child(p.getPostID()).child("options").child("poll_clicked_user").
                                child(CURRENT_USER.UID).getValue() == null){
                            pollSelectPostID.put(p.getPostID(),"null");
                        }
                        post_poll_option.put(p.getPostID(),postmetaOBJ);
                    }
                }
                presenter.loadPostSuccess(postObjects,post_poll_option,post_like_list,
                        post_url_list,post_comment_count,likedPostID,pollSelectPostID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mREF_classList.child(classID).child("post").addListenerForSingleValueEvent(valueEventListener);
    }
 }

