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
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.adapters.AdapterCourseItem;
import com.pistudiosofficial.myclass.objects.ClassObject;
import com.pistudiosofficial.myclass.objects.StudentClassObject;
import com.pistudiosofficial.myclass.objects.UserObject;
import com.pistudiosofficial.myclass.presenter.ProfileNewPresenter;
import com.pistudiosofficial.myclass.view.ProfileNewView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pistudiosofficial.myclass.Common.ATTD_PERCENTAGE_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_ADMIN_CLASS_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_USER;
import static com.pistudiosofficial.myclass.Common.CURRENT_USER_CLASS_LIST_ID;
import static com.pistudiosofficial.myclass.Common.FIREBASE_DATABASE;
import static com.pistudiosofficial.myclass.Common.SELECTED_CHAT_UID;
import static com.pistudiosofficial.myclass.Common.SELECTED_PROFILE_UID;
import static com.pistudiosofficial.myclass.Common.mAUTH;
import static com.pistudiosofficial.myclass.Common.mREF_student_classList;

public class ProfileNewActivity extends AppCompatActivity implements ProfileNewView {

    CircleImageView img_profile;
    int PICK_PROFILE_IMG_REQUEST = 101;
    Uri uriProfilePic;
    Button bt_hello, bt_chat,bt_helloList;
    ImageButton bt_back,bt_edit,bt_settings,confirmProfileEdit,cancelProfileEdit;
    TextView userProfileName,userEmail;
    EditText userBio,userDept,userSem,userBatch,userSgpi,userCgpi;
    ProfileNewPresenter presenter;
    ProgressDialog progressDialogProfilePic;
    public PieChart pieChart;
    public PieDataSet dataSet;
    UserObject userDetail;
    ArrayList<ClassObject> currClasses;
    public ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();
    Menu menu;
    Toolbar toolbar;
    private ArrayList<String> mCourseNames=new ArrayList<>();
    private ArrayList<String> mInstructorNames=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        findViewById();
        setProfileData();
        //Todo:
        //Bug:::: I want to show the pie chart for only student
        // These two lines show the same thing for admin : a blank pie chart which should not be showing in case of admin because I set visibility =gone in xml

//        if(CURRENT_USER.AdminLevel!="admin")
//        {
//            setPieChart();
//        }

//        setPieChart();


        courseAndInstructorNames();


        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

/*        img_profile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(SELECTED_PROFILE_UID.equals(CURRENT_USER.UID)){
                    openFileChooser(PICK_PROFILE_IMG_REQUEST);
                }
            }
        });*/


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
        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt_back.setVisibility(View.INVISIBLE);
                bt_settings.setVisibility(View.INVISIBLE);
                bt_hello.setVisibility(View.GONE);
                bt_helloList.setVisibility(View.GONE);
                bt_edit.setVisibility(View.GONE);
                confirmProfileEdit.setVisibility(View.VISIBLE);
                cancelProfileEdit.setVisibility(View.VISIBLE);
                pieChart.setVisibility(View.GONE);
                userBio.setEnabled(true);
                userDept.setEnabled(true);
                userSem.setEnabled(true);
                userBatch.setEnabled(true);
                userSgpi.setEnabled(true);
                userCgpi.setEnabled(true);

            }
        });
        confirmProfileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userBio.setText(userBio.getText());
                userBio.setEnabled(false);
                userDept.setEnabled(false);
                userSem.setEnabled(false);
                userBatch.setEnabled(false);
                userSgpi.setEnabled(false);
                userCgpi.setEnabled(false);
                confirmProfileEdit.setVisibility(View.GONE);
                cancelProfileEdit.setVisibility(View.GONE);
                bt_back.setVisibility(View.VISIBLE);
                bt_settings.setVisibility(View.VISIBLE);
                bt_helloList.setVisibility(View.VISIBLE);
                bt_edit.setVisibility(View.VISIBLE);
//                pieChart.setVisibility(View.VISIBLE);
            }
        });
        cancelProfileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void findViewById(){
        pieChart=findViewById(R.id.attendnace_pie_chart);
        img_profile = findViewById(R.id.img_profile_pic_1);
        bt_back=findViewById(R.id.backButton);
        bt_chat = findViewById(R.id.bt_profile_msg);
        bt_hello = findViewById(R.id.bt_profile_hello);
        bt_helloList=findViewById(R.id.bt_helloList_profile);
        bt_edit=findViewById(R.id.editbutton);
        bt_settings=findViewById(R.id.setting_button);
        confirmProfileEdit=findViewById(R.id.confirm_edit_profile);
        cancelProfileEdit=findViewById(R.id.cancelEditProfile);
        userProfileName = findViewById(R.id.tv_name_profile);
        userEmail=findViewById(R.id.user_email);
        userBio=findViewById(R.id.user_bio);
        userDept=findViewById(R.id.depart_ment);
        userSem=findViewById(R.id.sem_ester);
        userBatch=findViewById(R.id.bat_ch);
        userSgpi=findViewById(R.id.sg_pi);
        userCgpi=findViewById(R.id.cg_pi);

        currClasses=CURRENT_ADMIN_CLASS_LIST;
        computeTotalAttendace();
    }

    private void computeTotalAttendace() {
        float totalAttendancePer=0,sum;
        for (int i=0;i<ATTD_PERCENTAGE_LIST.size();i++) {
            sum=Float.parseFloat(ATTD_PERCENTAGE_LIST.get(i));
            totalAttendancePer+=sum;
        }
        totalAttendancePer=totalAttendancePer/ATTD_PERCENTAGE_LIST.size();
        yValues.add(new PieEntry(totalAttendancePer,"Present",0));
        yValues.add(new PieEntry(100-totalAttendancePer,"Absent",1));

    }

    private void setProfileData() {

        FirebaseUser user =mAUTH.getCurrentUser();
        userDetail=new UserObject(user.getEmail(),user.getPhoneNumber(),user.getDisplayName(),null,null);
        userEmail.setText(userDetail.Email);
        userProfileName.setText(userDetail.Name);
    }

    public void setPieChart() {
        pieChart.setVisibility(View.VISIBLE);
        pieChart.setUsePercentValues(true);
        pieChart.setTransparentCircleRadius(20f);
        dataSet = new PieDataSet(yValues, "");
        PieData data = new PieData( dataSet);
        data.setValueFormatter(new PercentFormatter());
        Description description= new Description();
        description.setText("");
        pieChart.setDescription(description);
        pieChart.setData(data);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(20f);
        pieChart.setHoleRadius(20f);
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.DKGRAY);
        pieChart.animateXY(1400, 1400);
    }

//    public void updatePieChart(ArrayList<PieEntry> Values){
//        yValues.add(Values.get(0));
//        yValues.add(Values.get(1));
//        pieChart.notifyDataSetChanged();
//        pieChart.invalidate();
//    }

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
        setProfileData();
        userProfileName.setText(userObject.Name);
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

        for(int i=0;i<currClasses.size();i++){
            mCourseNames.add(currClasses.get(i).className);
            mInstructorNames.add(currClasses.get(i).facultyName);
        }
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
