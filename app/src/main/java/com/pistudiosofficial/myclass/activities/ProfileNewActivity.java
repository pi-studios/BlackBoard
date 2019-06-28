package com.pistudiosofficial.myclass.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.presenter.ProfileNewPresenter;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.ProfileNewPresenterInterface;
import com.pistudiosofficial.myclass.view.ProfileNewView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileNewActivity extends AppCompatActivity implements ProfileNewView {

    CircleImageView img_profile;
    int PICK_PROFILE_IMG_REQUEST = 101;
    Uri uriProfilePic;

    ProfileNewPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);

        img_profile = findViewById(R.id.img_profile_pic);

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser(PICK_PROFILE_IMG_REQUEST);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter = new ProfileNewPresenter(this);
    }

    private void openFileChooser(int imgRQST){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,imgRQST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PROFILE_IMG_REQUEST && resultCode == -1 && data != null && data.getData() != null){
            uriProfilePic = data.getData();
            Picasso.with(this).load(uriProfilePic).into(img_profile);
            presenter.performImageUpload(uriProfilePic,"profile_pic",getExtension(uriProfilePic));
        }
    }

    private String getExtension(Uri uri){
        ContentResolver cr = this.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    @Override
    public void profilePicUploadSuccess() {
        Toast.makeText(this,"Profile Photo Changed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void profilePicUploadFailed() {
        Toast.makeText(this,"Failed", Toast.LENGTH_SHORT).show();
    }


}
