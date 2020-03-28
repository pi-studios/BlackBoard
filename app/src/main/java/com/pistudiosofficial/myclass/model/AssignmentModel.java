package com.pistudiosofficial.myclass.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

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
import com.pistudiosofficial.myclass.objects.AssignmentObject;
import com.pistudiosofficial.myclass.objects.AssignmentSubmissionObject;
import com.pistudiosofficial.myclass.objects.PostObject;
import com.pistudiosofficial.myclass.view.AssignmentCreationView;
import com.pistudiosofficial.myclass.view.AssignmentHomeView;
import com.pistudiosofficial.myclass.view.AssignmentViewerInfoFragView;
import com.pistudiosofficial.myclass.view.AssignmentViewerSubmissionFragView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.pistudiosofficial.myclass.Common.CURRENT_CLASS_ID_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_INDEX;
import static com.pistudiosofficial.myclass.Common.CURRENT_USER;
import static com.pistudiosofficial.myclass.Common.mREF_classList;

public class AssignmentModel {

    private AssignmentCreationView assignmentCreationView;
    private AssignmentHomeView assignmentHomeView;
    private AssignmentViewerInfoFragView assignmentViewerInfoFragView;
    private AssignmentViewerSubmissionFragView assignmentViewerSubmissionFragView;
    private Context context;
    private String assignmentid;
    private int counter;
    private ArrayList<Uri> uriArrayList;
    public AssignmentModel(AssignmentCreationView assignmentCreationView,Context context) {
        this.assignmentCreationView = assignmentCreationView;
        this.context = context;
    }

    public AssignmentModel(AssignmentHomeView assignmentHomeView) {
        this.assignmentHomeView = assignmentHomeView;
    }

    public AssignmentModel(AssignmentViewerInfoFragView assignmentViewerInfoFragView) {
        this.assignmentViewerInfoFragView = assignmentViewerInfoFragView;
    }

    public AssignmentModel(AssignmentViewerSubmissionFragView assignmentViewerSubmissionFragView) {
        this.assignmentViewerSubmissionFragView = assignmentViewerSubmissionFragView;
    }

    //Assignment Creation
    public void performAssignmentCreation(String title, String description, String dueDate, ArrayList<Uri> uriArrayList, String edit_key){
        counter = uriArrayList.size();
        assignmentCreationFinal(title,description,dueDate,edit_key);
        this.uriArrayList = uriArrayList;
    }
    private void assignmentCreationFinal(String title, String description, String dueDate,String edit_key){
        DatabaseReference databaseReference = mREF_classList.child(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX)).child("assignment_online");
        String newID;
        if (edit_key != null){
            newID=edit_key;
        }else {
            newID = databaseReference.push().getKey();
        }
        assignmentid = newID;
        PostObject postObject = new PostObject();
        String currentTime = DateFormat.getDateTimeInstance().format(new Date());
        databaseReference.child(newID).child("assignment_id").setValue(newID);
        postObject.setCreationDate(currentTime);
        postObject.setBody(title);
        postObject.setCreatorName(CURRENT_USER.Name);
        postObject.setCreatorProPickLink(CURRENT_USER.profilePicLink);
        postObject.setCreatorUID(CURRENT_USER.UID);
        postObject.setPostType("simple_admin_post");
        databaseReference.child(newID).child("title").setValue(title);
        databaseReference.child(newID).child("timestamp").setValue(currentTime);
        if (!description.equals("") && !description.isEmpty()){
            databaseReference.child(newID).child("description").setValue(description);
        }else{
            databaseReference.child(newID).child("description").setValue("No Info");
        }
        if (!dueDate.equals("") && !dueDate.isEmpty()){
            databaseReference.child(newID).child("due_date").setValue(dueDate);
        }else{
            databaseReference.child(newID).child("due_date").setValue("No Due Date");
        }
        String key = mREF_classList.child(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX))
                .child("post").push().getKey();
        postObject.setPostID(key);
        ;
        mREF_classList.child(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX))
                .child("post").child(key).setValue(postObject, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError == null){
                    postReadIndex();
                    pushNotification(postObject.getBody());
                    if (uriArrayList.size() == 0){
                        assignmentCreationView.assignmentCreationSuccess();
                    }
                    else{
                        for (Uri uri:uriArrayList){
                            uploadAssignmentResource(uri);
                        }
                    }
                }

            }
        });

    }
    private void uploadAssignmentResource(Uri uri){
        StorageReference imgREF = FirebaseStorage.getInstance().getReference()
                .child("assignments/"+CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX)
                        +"/"+"/"+System.currentTimeMillis());
        UploadTask uploadTask = imgREF.putFile(uri);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return imgREF.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    DatabaseReference databaseReference = mREF_classList.child(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX)).child("assignment_online");
                    databaseReference.child(assignmentid).child("meta_data").child(counter+"").child("link").setValue(downloadUri.toString());
                    ContentResolver cR = context.getContentResolver();
                    MimeTypeMap mime = MimeTypeMap.getSingleton();
                    String type = mime.getExtensionFromMimeType(cR.getType(uri));
                    if (type.equals("pdf")){type = "pdf";}
                    else{type = "jpg";}
                    databaseReference.child(assignmentid).child("meta_data").child(counter+"").child("type").setValue(type);
                    databaseReference.child(assignmentid).child("meta_data").child(counter+"").child("name").setValue(fileName(uri));
                    counter--;
                    if (counter == 0){assignmentCreationView.assignmentCreationSuccess();}
                } else {
                    // Handle failures
                    // ...
                    assignmentCreationView.assignmentCreationFailed();
                }
            }
        });
    }
    private void postReadIndex(){
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
    private void pushNotification(String body){

        String currentTime = DateFormat.getDateTimeInstance().format(new Date());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a MMM d, ''yy");
        String simpleTime = simpleDateFormat.format(new Date());
        PushNotificationSenderModel notifModel = new PushNotificationSenderModel(body,
                body, Common.CURRENT_ADMIN_CLASS_LIST.get(Common.CURRENT_INDEX).className,
                currentTime,Common.CURRENT_CLASS_ID_LIST.get(Common.CURRENT_INDEX),simpleTime);
        notifModel.performBroadcast();
    }
    String fileName(Uri data){
        String fileName = "";
        if (data.getScheme().equals("file")) {
            fileName = data.getLastPathSegment();
        } else {
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(data, new String[]{
                        MediaStore.Images.ImageColumns.DISPLAY_NAME
                }, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME));
                }
            } finally {

                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return fileName;
    }


    //Downloading assignment list.
    public void downloadAssignmentList(String classid){
        DatabaseReference databaseReference = mREF_classList.child(classid).child("assignment_online");
        ArrayList<AssignmentObject> list= new ArrayList<>();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dpShot:dataSnapshot.getChildren()){
                    AssignmentObject object = new AssignmentObject(dpShot.child("description").getValue().toString(),
                            dpShot.child("due_date").getValue().toString(),dpShot.child("timestamp").getValue().toString(),
                            dpShot.child("title").getValue().toString());
                    object.setAssignmentid(dpShot.child("assignment_id").getValue(String.class));
                    list.add(object);
                }
                assignmentHomeView.assignmentDownloadSuccess(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //For downloading single assignment for assignmentviewer
    public void downloadAssignmentForViewer(String key){
        mREF_classList.child(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX)).child("assignment_online")
                .child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AssignmentObject assignmentObject = new AssignmentObject(
                        dataSnapshot.child("description").getValue(String.class),
                        dataSnapshot.child("due_date").getValue(String.class),
                        dataSnapshot.child("timestamp").getValue(String.class),
                        dataSnapshot.child("title").getValue(String.class)
                    );
                assignmentObject.setAssignmentid(dataSnapshot.child("assignment_id").getValue(String.class));
                HashMap<String,String> metaData = new HashMap<>();
                ArrayList<String> name_meta_data = new ArrayList<>();
                if (dataSnapshot.child("meta_data").getValue() != null){
                    for (DataSnapshot snapshot:dataSnapshot.child("meta_data").getChildren()){
                        metaData.put(snapshot.child("link").getValue(String.class),snapshot.child("type").getValue(String.class));
                        name_meta_data.add(snapshot.child("name").getValue(String.class));
                    }
                }
                assignmentViewerInfoFragView.downloadSuccess(assignmentObject,metaData,name_meta_data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                assignmentViewerInfoFragView.downloadFailed();
            }
        });
    }

    //For downloading Grid for submission in assignment submission
    public void downloadAssignmentSubmissionStudent(String assignmentid,String classId){
        ArrayList<AssignmentSubmissionObject> assignmentSubmissionObjects = new ArrayList<>();
        mREF_classList.child(classId).child("assignment_online").child(assignmentid).child("submission")
                .child(CURRENT_USER.Roll).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    if (snapshot.getValue() != null && !snapshot.getKey().equals("name")) {
                        assignmentSubmissionObjects.add(snapshot.getValue(AssignmentSubmissionObject.class));
                    }
                }
                assignmentViewerSubmissionFragView.downloadSuccessStudent(assignmentSubmissionObjects);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                assignmentViewerSubmissionFragView.downloadFailed();
            }
        });
    }

    //For downloading student list who have submitted assignment
    public void downloadAssignmentSubmissionStudentList(String assignmentid,String classId){
        ArrayList<String> roll_list = new ArrayList<>();
        ArrayList<String> name_list = new ArrayList<>();
        mREF_classList.child(classId).child("assignment_online").child(assignmentid).child("submission")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            roll_list.add(snapshot.getKey());
                            name_list.add(snapshot.child("name").getValue(String.class));
                        }
                        assignmentViewerSubmissionFragView.downloadSuccessSubmissionStudentList(roll_list,name_list);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        assignmentViewerSubmissionFragView.downloadFailed();
                    }
                });
    }

    //For Faculty to check individual submission
    public void downloadAssignmentSubmissionCheckFaculty(String assignmentid,String classId,String roll){
        ArrayList<AssignmentSubmissionObject> assignmentSubmissionObjects = new ArrayList<>();
        mREF_classList.child(classId).child("assignment_online").child(assignmentid).child("submission")
                .child(roll).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    if (snapshot.getValue() != null && !snapshot.getKey().equals("name")) {
                        assignmentSubmissionObjects.add(snapshot.getValue(AssignmentSubmissionObject.class));
                    }
                }
                assignmentViewerSubmissionFragView.downloadSuccessStudent(assignmentSubmissionObjects);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                assignmentViewerSubmissionFragView.downloadFailed();
            }
        });
    }

}
