package com.pistudiosofficial.myclass.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.adapters.AdapterCourseItem;
import com.pistudiosofficial.myclass.objects.UserObject;
import com.pistudiosofficial.myclass.presenter.ProfileNewPresenter;
import com.pistudiosofficial.myclass.view.ProfileNewView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pistudiosofficial.myclass.Common.CURRENT_USER;
import static com.pistudiosofficial.myclass.Common.SELECTED_CHAT_UID;
import static com.pistudiosofficial.myclass.Common.SELECTED_PROFILE_UID;

public class ProfileNewActivity extends AppCompatActivity implements ProfileNewView {

    CircleImageView img_profile;
    int PICK_PROFILE_IMG_REQUEST = 101;
    Uri uriProfilePic;
    Button bt_hello, bt_chat;
    ImageButton bt_back,bt_edit,bt_settings;
    TextView tv_profile_name;
    ProfileNewPresenter presenter;
    ProgressDialog progressDialogProfilePic;
    Menu menu;
    private ArrayList<String> mCourseNames=new ArrayList<>();
    private ArrayList<String> mInstructorNames=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        bt_back=findViewById(R.id.backButton);
        bt_edit=findViewById(R.id.editbutton);
        bt_settings=findViewById(R.id.setting_button);
        courseAndInstructorNames();
        img_profile = findViewById(R.id.img_profile_pic);
//        bt_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SELECTED_PROFILE_UID.equals(CURRENT_USER.UID)){
                    openFileChooser(PICK_PROFILE_IMG_REQUEST);
                }
            }
        });

        bt_chat = findViewById(R.id.bt_profile_msg);
        bt_hello = findViewById(R.id.bt_profile_hello);
        if (SELECTED_PROFILE_UID.equals(CURRENT_USER.UID)){
            bt_hello.setVisibility(View.GONE);
            bt_chat.setVisibility(View.GONE);
        }
        bt_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                SELECTED_CHAT_UID = SELECTED_PROFILE_UID;
                startActivity(intent);
            }
        });
        tv_profile_name = findViewById(R.id.tv_name_profile);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter = new ProfileNewPresenter(this);
        initializeProfile();

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
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Profile Picture")
                    .setMessage("Do you want to change your profile Picture?\nNote: Profile Pictire are always Public.")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            presenter.performImageUpload(uriProfilePic,
                                    "profile_picture",
                                    getExtension(uriProfilePic));
                            progressDialogProfilePic = ProgressDialog.show(ProfileNewActivity.this, "",
                                    "Uploading. Please wait...", true);
                        }
                    }).setNegativeButton("No",null).show();
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
        Picasso.with(this).load(uriProfilePic).into(img_profile);
        Toast.makeText(this,"Profile Photo Changed",Toast.LENGTH_SHORT).show();
        progressDialogProfilePic.dismiss();
    }

    @Override
    public void profilePicUploadFailed() {
        Toast.makeText(this,"Failed", Toast.LENGTH_SHORT).show();
        progressDialogProfilePic.dismiss();
    }

    @Override
    public void helloSendSuccess() {

    }

    @Override
    public void helloSendFailed() {
        Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void profileLoadSuccess(UserObject userObject) {
        if(userObject.profilePicLink !=null && !userObject.profilePicLink.equals("")){
            Glide.with(this).load(userObject.profilePicLink).into(img_profile);
        }
        tv_profile_name.setText(userObject.Name);
    }

    @Override
    public void profilePictureLoadFailed() {
        Toast.makeText(this,"Profile Picture Not Loaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void helloStatusCheckSuccess(int hello) {
        if(hello == 0){
            // request sent
            bt_chat.setVisibility(View.GONE);
            bt_hello.setEnabled(true);
            bt_hello.setText("Cancel");
            bt_hello.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.performHelloRequestRespond(SELECTED_PROFILE_UID,false);
                }
            });
        }
        if (hello == 1){
            //request Recieved
            bt_chat.setVisibility(View.GONE);
            bt_hello.setEnabled(true);
            bt_hello.setText("Accept");
            bt_hello.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.performHelloRequestRespond(SELECTED_PROFILE_UID,true);
                }
            });
        }
        if (hello == 2){
            // Already Friend
            bt_chat.setVisibility(View.VISIBLE);
            bt_hello.setEnabled(false);
            bt_hello.setText("Unfriend");
            bt_hello.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.performHelloRequestRespond(SELECTED_PROFILE_UID,false);
                }
            });
        }
        if (hello == 3){
            // no request sent or received and no friend
            bt_chat.setVisibility(View.GONE);
            bt_hello.setEnabled(true);
            bt_hello.setText("Hello!");
            bt_hello.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.performSendHello(SELECTED_PROFILE_UID);
                }
            });
        }

    }
    public void courseAndInstructorNames()
    {
        mCourseNames.add("Graphics");
        mInstructorNames.add("Badal Soni");

        mCourseNames.add("Maths");
        mInstructorNames.add("K.N.Das");

        mCourseNames.add("FLAT");
        mInstructorNames.add("Shyampada Mukherjee");

        mCourseNames.add("Signals And System");
        mInstructorNames.add("Anish Kumar");

        initRecyclerViewForCourse();
    }
    private void initRecyclerViewForCourse(){
//

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView=findViewById(R.id.course_recyclerview);
        recyclerView.setLayoutManager(linearLayoutManager);
        AdapterCourseItem adapterCourseItem= new AdapterCourseItem(mCourseNames,mInstructorNames,this);
        recyclerView.setAdapter(adapterCourseItem);
    }

    private void initializeProfile(){
        presenter.performProfilePictureLoad(SELECTED_PROFILE_UID);
        if(!CURRENT_USER.UID.equals(SELECTED_PROFILE_UID)){
            presenter.performHelloStatusCheck(SELECTED_PROFILE_UID);
        }
    }

}
