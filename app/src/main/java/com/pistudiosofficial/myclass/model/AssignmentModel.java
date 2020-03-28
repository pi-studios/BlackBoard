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
import com.pistudiosofficial.myclass.objects.AssignmentObject;
import com.pistudiosofficial.myclass.objects.PostObject;
import com.pistudiosofficial.myclass.view.AssignmentCreationView;
import com.pistudiosofficial.myclass.view.AssignmentHomeView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.pistudiosofficial.myclass.Common.CURRENT_CLASS_ID_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_INDEX;
import static com.pistudiosofficial.myclass.Common.CURRENT_USER;
import static com.pistudiosofficial.myclass.Common.mREF_classList;

public class AssignmentModel {

    AssignmentCreationView assignmentCreationView;
    AssignmentHomeView assignmentHomeView;
    private String assignmentid;
    int counter;
    ArrayList<Uri> uriArrayList;
    public AssignmentModel(AssignmentCreationView assignmentCreationView) {
        this.assignmentCreationView = assignmentCreationView;
    }

    public AssignmentModel(AssignmentHomeView assignmentHomeView) {
        this.assignmentHomeView = assignmentHomeView;
    }

    public void performAssignmentCreation(String title, String description, String dueDate, ArrayList<Uri> uriArrayList){
        counter = uriArrayList.size();
        assignmentCreationFinal(title,description,dueDate);
        this.uriArrayList = uriArrayList;
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
                    databaseReference.child(assignmentid).child("meta_data").child(counter+"").setValue(downloadUri.toString());
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
    private void assignmentCreationFinal(String title, String description, String dueDate){
        DatabaseReference databaseReference = mREF_classList.child(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX)).child("assignment_online");
        String newID = databaseReference.push().getKey();
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
                    list.add(object);
                }
                assignmentHomeView.assignmentDownloadSuccess(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
