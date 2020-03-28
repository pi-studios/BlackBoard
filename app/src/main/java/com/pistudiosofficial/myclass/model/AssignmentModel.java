package com.pistudiosofficial.myclass.model;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pistudiosofficial.myclass.view.AssignmentCreationView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.pistudiosofficial.myclass.Common.CURRENT_ADMIN_CLASS_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_CLASS_ID_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_INDEX;
import static com.pistudiosofficial.myclass.Common.LOG;
import static com.pistudiosofficial.myclass.Common.mREF_classList;

public class AssignmentModel {

    AssignmentCreationView assignmentCreationView;
    private String assignmentid;
    int counter;
    public AssignmentModel(AssignmentCreationView assignmentCreationView) {
        this.assignmentCreationView = assignmentCreationView;
    }

    public void performAssignmentCreation(String title, String description, String dueDate, ArrayList<Uri> uriArrayList){
        counter = uriArrayList.size();
        assignmentCreationFinal(title,description,dueDate);
        if (uriArrayList.size() == 0){
            assignmentCreationView.assignmentCreationSuccess();
        }
        else{
            for (Uri uri:uriArrayList){
                uploadAssignmentResource(uri);
            }
        }
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
        String currentTime = DateFormat.getDateTimeInstance().format(new Date());
        databaseReference.child(newID).child("assignment_id").setValue(newID);
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
    }

}
