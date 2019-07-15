package com.pistudiosofficial.myclass.model;

import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.opencsv.CSVWriter;
import com.pistudiosofficial.myclass.objects.FeedbackExportObject;
import com.pistudiosofficial.myclass.objects.FeedbackMainAdminObject;
import com.pistudiosofficial.myclass.objects.PostObject;
import com.pistudiosofficial.myclass.view.AdminFeedbackView;
import com.pistudiosofficial.myclass.view.FeedbackView;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.pistudiosofficial.myclass.Common.CURRENT_USER;
import static com.pistudiosofficial.myclass.Common.LOG;
import static com.pistudiosofficial.myclass.Common.mREF_classList;
import static com.pistudiosofficial.myclass.Common.mREF_users;

public class FeedbackHODModel {
    ArrayList<String> feedback_Question;
    FeedbackView feedbackView;
    AdminFeedbackView adminFeedbackView;

    List<String []> data;


    public FeedbackHODModel(AdminFeedbackView adminFeedbackView) {
        this.adminFeedbackView = adminFeedbackView;
    }

    boolean flag = true;
    public FeedbackHODModel() {
    }

    public FeedbackHODModel(FeedbackView feedbackView) {
        this.feedbackView = feedbackView;
    }

    public void performInitiateFeedback(
            String to_email, String className, String session, String from_name,String classID){
            mREF_users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot s : dataSnapshot.getChildren()){
                        if (s.child("Email").getValue().toString().equals(to_email)){
                            temp01(to_email,s.getKey(),className,session,from_name,classID);
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }

    private void temp01(String to_email,String UID,String className, String session, String from_name,String classID){
        feedback_Question = new ArrayList<>();

        mREF_classList.child(classID).child("feedback").child("reciever_mail").setValue(to_email);
        mREF_classList.child(classID).child("feedback").child("reciever_uid").setValue(UID);
        mREF_classList.child(classID).child("feedback").child("class_name").setValue(className);
        mREF_classList.child(classID).child("feedback").child("session").setValue(session);
        mREF_classList.child(classID).child("feedback").child("sender_name").setValue(from_name);
        String time = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
        mREF_classList.child(classID).child("feedback").child("date").setValue(time);
        UPLOAD_FEEDBACK_QUESTION(mREF_classList.child(classID).child("feedback").child("question"));
        PUSH_FEEDBACK_POST(classID);
    }
    private void UPLOAD_FEEDBACK_QUESTION(DatabaseReference mRef){
        feedback_Question.add("Course: Satisfaction-");
        feedback_Question.add("Course: Content-");
        feedback_Question.add("Course: Design and Structure-");
        feedback_Question.add("Instructor: Clarity of Explanation-");
        feedback_Question.add("Instructor: Interactive with Students-");
        feedback_Question.add("Instructor: Approachable-");
        feedback_Question.add("Instructor: Relate subject with practical example-");
        feedback_Question.add("Instructor: Able to handle class-");
        feedback_Question.add("Class: Tutorials were useful-");
        feedback_Question.add("Class: Discussion were useful-");
        feedback_Question.add("Class: Materials were useful-");
        for (String s: feedback_Question){
            mRef.child(s).setValue("0");
        }
    }

    private void PUSH_FEEDBACK_POST(String classID){
        PostObject postObject = new PostObject();
        postObject.setPostType("admin_feedback");
        postObject.setCreatorUID(CURRENT_USER.UID);
        postObject.setCreatorProPickLink(CURRENT_USER.profilePicLink);
        String time = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
        postObject.setCreationDate(time);
        postObject.setCreatorName(CURRENT_USER.Name);
        postObject.setBody("Respond to the Feedback ->HERE");

        String key = mREF_classList.child(classID)
                .child("post").push().getKey();
        postObject.setPostID(key);
        mREF_classList.child(classID)
                .child("post").child(key).setValue(postObject);
    }

    public void performFeedbackSubmitAdmin(String classID){
        mREF_classList.child(classID).child("feedback").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String to_UID = dataSnapshot.child("reciever_uid").getValue().toString();
                DatabaseReference fPath = mREF_classList.child(classID).child("feedback");
                DatabaseReference tPath = mREF_users.child(to_UID).child("feedback");
                moveRecord(fPath,tPath);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void moveRecord(final DatabaseReference fromPath, final DatabaseReference toPath) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String key = toPath.push().getKey();
                toPath.child(key).setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            toPath.child(key).child("node_key").setValue(key);
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

    public void performFeedbackLoad(String classID){
        ArrayList<String> question = new ArrayList<>();
        mREF_classList.child(classID).child("feedback")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot s: dataSnapshot.child("question").getChildren()){
                                question.add(s.getKey());
                            }
                            feedbackView.loadSuccess(question);
                        }
                        else{
                            feedbackView.feedbackDontExist();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void performFeedbackSubmitUser(HashMap<String,String> hashMap,String classID){
        mREF_classList.child(classID).child("feedback").child("entry")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0){
                            for (DataSnapshot s :dataSnapshot.getChildren()){
                                if (s.getKey().equals(CURRENT_USER.UID)){
                                    flag = false;
                                }
                            }
                            if (flag){
                                uploadFeedbackUser(hashMap,classID);
                            }
                            else{
                                feedbackView.feedbackSubmitFailed();
                            }
                        }
                        else{
                            uploadFeedbackUser(hashMap,classID);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    private void uploadFeedbackUser(HashMap<String,String> hashMap,String classID){
        for (String key : hashMap.keySet()) {
            if (hashMap.get(key).equals("0")) {
                mREF_classList.child(classID).child("feedback").child("question").child(key).child("ok")
                        .child(CURRENT_USER.UID).setValue("true");
            }
            if (hashMap.get(key).equals("1")) {
                mREF_classList.child(classID).child("feedback").child("question").child(key).child("good")
                        .child(CURRENT_USER.UID).setValue("true");
            }
            if (hashMap.get(key).equals("2")) {
                mREF_classList.child(classID).child("feedback").child("question").child(key).child("vgood")
                        .child(CURRENT_USER.UID).setValue("true");
            }
            if (hashMap.get(key).equals("-1")) {
                mREF_classList.child(classID).child("feedback").child("question").child(key).child("bad")
                        .child(CURRENT_USER.UID).setValue("true");
            }
            if (hashMap.get(key).equals("-2")) {
                mREF_classList.child(classID).child("feedback").child("question").child(key).child("vbad")
                        .child(CURRENT_USER.UID).setValue("true");
            }
        }
        mREF_classList.child(classID).child("feedback").child("entry").child(CURRENT_USER.UID).setValue("true");
        feedbackView.feedbackSubmitSuccess();
    }

    public void performFeedbackLoadAdmin(String uid){
        ArrayList<FeedbackMainAdminObject> objects = new ArrayList<>();
        mREF_users.child(uid).child("feedback").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        FeedbackMainAdminObject obj = new FeedbackMainAdminObject();
                        obj.className = s.child("class_name").getValue().toString();
                        obj.dateString = s.child("date").getValue().toString();
                        obj.senderName = s.child("sender_name").getValue().toString();
                        obj.session = s.child("session").getValue().toString();
                        obj.nodeKey = s.child("node_key").getValue().toString();
                        objects.add(obj);
                    }
                    adminFeedbackView.feedbackListLoad(objects);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void performFeedbackDownload(String uid,String nodeKey,String csvName){
        HashMap<String, FeedbackExportObject> hashMap = new HashMap<>();
        mREF_users.child(uid).child("feedback").child(nodeKey).child("question")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot s:dataSnapshot.getChildren()){
                            FeedbackExportObject obj = new FeedbackExportObject();
                            obj.ok = Long.toString(s.child("ok").getChildrenCount());
                            obj.good = Long.toString(s.child("good").getChildrenCount());
                            obj.vgood = Long.toString(s.child("vgood").getChildrenCount());
                            obj.bad = Long.toString(s.child("bad").getChildrenCount());
                            obj.vbad = Long.toString(s.child("vbad").getChildrenCount());
                            hashMap.put(s.getKey(),obj);
                        }
                        exportFeedbackFinal(hashMap,csvName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void exportFeedbackFinal(HashMap<String,FeedbackExportObject> hashMap,String csvName){
        data = new ArrayList<>();
        String [] column = {"Question","Very Bad","Bad","Neutral","Good","Very Good"};
        data.add(column);
        ArrayList<String> values;
        for (String key : hashMap.keySet()){
            values = new ArrayList<>();
            values.add(key);
            values.add(hashMap.get(key).vbad);
            values.add(hashMap.get(key).bad);
            values.add(hashMap.get(key).ok);
            values.add(hashMap.get(key).good);
            values.add(hashMap.get(key).vgood);
            data.add(values.toArray(new String[0]));
        }
        String csv = (Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+csvName+".csv"); // Here csv file name is MyCsvFile.csv

        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new FileWriter(csv));

            writer.writeAll(data);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
