package com.pistudiosofficial.myclass.model;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pistudiosofficial.myclass.Common;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.ProfileNewPresenterInterface;



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

        StorageReference ref_store = storageReference.child("profile_pic/"+ Common.CURRENT_USER.UID+"."+ext);
        ref_store.putFile(imgURI)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                presenter.profilePicUploadSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                presenter.profilePicUploadFailed();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });

    }

    public void performSendHello(String UID){
        // Need TO Work
    }

}
