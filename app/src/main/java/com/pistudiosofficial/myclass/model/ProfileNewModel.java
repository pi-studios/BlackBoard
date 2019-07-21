package com.pistudiosofficial.myclass.model;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pistudiosofficial.myclass.Common;
import com.pistudiosofficial.myclass.objects.UserObject;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.ProfileNewPresenterInterface;

import static com.pistudiosofficial.myclass.Common.CURRENT_USER;
import static com.pistudiosofficial.myclass.Common.LOG;
import static com.pistudiosofficial.myclass.Common.mREF_users;


public class ProfileNewModel {
    ProfileNewPresenterInterface presenter;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    public ProfileNewModel(ProfileNewPresenterInterface presenter) {
        this.presenter = presenter;
    }

    public void performImageUpload(Uri imgURI, String uploadType, String ext){
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        if(uploadType.equals("profile_pic")){
            StorageReference ref_store = storageReference.child("profile_pic/"+ Common.CURRENT_USER.UID+"."+ext);
            UploadTask uploadTask = ref_store.putFile(imgURI);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    // Continue with the task to get the download URL
                    return ref_store.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        mREF_users.child(CURRENT_USER.UID).child("profilePicLink").setValue(downloadUri.toString());
                        presenter.profilePicUploadSuccess();
                    } else {
                        presenter.profilePicUploadFailed();
                    }
                }
            });
        }

    }

    public void performSendHello(String UID){
        mREF_users.child(CURRENT_USER.UID).child("hello").child(UID).setValue("0");
        mREF_users.child(UID).child("hello").child(CURRENT_USER.UID).setValue("1",
            new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if (databaseError == null){
                        presenter.helloSendSuccess();
                    }
                    else {
                        presenter.helloSendFailed();
                    }
                }
            });

    }

    public void performProfilePictureLoad(String UID){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    presenter.profileLoadSuccess(dataSnapshot.getValue(UserObject.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                presenter.profilePictureLoadFailed();
            }
        };
        mREF_users.child(UID).addListenerForSingleValueEvent(valueEventListener);
    }


    public void performHelloStatusCheck(String UID){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child(UID).exists()) {
                        //do ur stuff
                        if (dataSnapshot.child(UID).getValue().toString().equals("0")){
                            presenter.helloStatusCheckSuccess(0);
                        }
                        if (dataSnapshot.child(UID).getValue().toString().equals("1")){
                            presenter.helloStatusCheckSuccess(1);
                        }
                        if (dataSnapshot.child(UID).getValue().toString().equals("2")){
                            presenter.helloStatusCheckSuccess(2);
                        }
                    } else {
                        //do something if not exists
                        presenter.helloStatusCheckSuccess(3);
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mREF_users.child(CURRENT_USER.UID).child("hello").addValueEventListener(valueEventListener);
    }

}
