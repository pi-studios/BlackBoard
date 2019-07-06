package com.pistudiosofficial.myclass.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;

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
import com.pistudiosofficial.myclass.objects.ResourceBucketObject;
import com.pistudiosofficial.myclass.view.ResourceBucketView;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ResourceBucketModel {

    private ResourceBucketView view;
    private Context context;
    private boolean flag1,flag2,flag3,flag4;
    public ResourceBucketModel(ResourceBucketView view,Context context) {
        this.view  = view;
        this.context = context;
        flag1 = false;flag2 = false;flag3 = false;flag4 = false;
    }

    public void performResourceUpload(DatabaseReference databaseReference, Uri img1, Uri img2, Uri img3,
                                      Uri pdf, String classID){
        String currentTime = DateFormat.getDateTimeInstance().format(new Date());

        if (img1 != null){
            ResourceBucketObject object01 = new ResourceBucketObject();
            object01.file_name = getFileDetailFromUri(context,img1);
            object01.date_created = currentTime;
            object01.file_type = "photo";
            uploadURI(img1,classID,object01,databaseReference,1);
        }
        if (img1 == null){flag1 =true;}
        if (img2 != null){
            ResourceBucketObject object02 = new ResourceBucketObject();
            object02.file_name = getFileDetailFromUri(context,img1);
            object02.date_created = currentTime;
            object02.file_type = "photo";
            uploadURI(img2,classID,object02,databaseReference,2);
        }
        if (img2 == null){flag2 = true;}
        if (img3 != null){
            ResourceBucketObject object03 = new ResourceBucketObject();
            object03.file_name = getFileDetailFromUri(context,img1);
            object03.date_created = currentTime;
            object03.file_type = "photo";
            uploadURI(img3,classID,object03,databaseReference,3);
        }
        if (img3 == null){flag3 = true;}
        if (pdf != null){
            ResourceBucketObject object04 = new ResourceBucketObject();
            object04.file_name = getFileDetailFromUri(context,pdf);
            object04.date_created = currentTime;
            object04.file_type = "pdf";
            uploadURI(pdf,classID,object04,databaseReference,4);
        }
        if (pdf == null){flag4 = true;}
    }

    private String getFileDetailFromUri(final Context context, final Uri uri) {
        String fileDetail = "";
        if (uri != null) {
            // File Scheme.
            if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
                File file = new File(uri.getPath());
                fileDetail = file.getName();
            }
            // Content Scheme.
            else if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
                Cursor returnCursor =
                        context.getContentResolver().query(uri, null, null, null, null);
                if (returnCursor != null && returnCursor.moveToFirst()) {
                    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                    fileDetail = returnCursor.getString(nameIndex);
                    returnCursor.close();
                }
            }
        }
        return fileDetail;
    }

    private void uploadURI(Uri uri,String classID,ResourceBucketObject bucketObject,DatabaseReference reference,int count){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference imgREF = storageReference
                .child("resource_bucket/"+classID
                        +"/"+"/"+System.currentTimeMillis());
        UploadTask uploadTask = imgREF.putFile(uri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return imgREF.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    bucketObject.file_link = downloadUri.toString();
                    String newID = reference.push().getKey();
                    bucketObject.file_UID = newID;
                    reference.child(newID).setValue(bucketObject);
                    if (count == 1){flag1 = true;}
                    if (count == 2){flag2 = true;}
                    if (count == 3){flag3 = true;}
                    if (count == 4){flag4 = true;}
                    if (flag1 && flag2 && flag3 && flag4){
                        view.uploadSuccess();
                    }
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    public void performDataLoad(DatabaseReference reference, ArrayList<ResourceBucketObject> bucketObjects){
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s: dataSnapshot.getChildren()){
                    bucketObjects.add(s.getValue(ResourceBucketObject.class));
                }
                view.loadSuccess(bucketObjects);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
