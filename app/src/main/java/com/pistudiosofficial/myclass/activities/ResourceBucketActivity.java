package com.pistudiosofficial.myclass.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.adapters.AdapterGridResourceBucket;
import com.pistudiosofficial.myclass.model.ResourceBucketModel;
import com.pistudiosofficial.myclass.objects.ResourceBucketObject;
import com.pistudiosofficial.myclass.view.ResourceBucketView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.pistudiosofficial.myclass.Common.CURRENT_CLASS_ID_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_INDEX;
import static com.pistudiosofficial.myclass.Common.CURRENT_USER;
import static com.pistudiosofficial.myclass.Common.mREF_RESOURCE_BUCKET;

public class ResourceBucketActivity extends AppCompatActivity implements ResourceBucketView {
    Dialog uploadDialog;
    FloatingActionButton fab_add_res;
    ResourceBucketModel model;
    ImageView img_01,img_02,img_03;
    ImageButton bt_selectPhoto, bt_select_pdf;
    Button bt_pdf_selected,bt_upload_resource;
    ProgressDialog progressDialogUpload;
    final int PICK_IMG_01 = 1;
    final int PICK_IMG_02 = 2;
    final int PICK_IMG_03 = 3;
    final int PICK_FILE = 4;
    Uri imgURI01,imgURI02,imgURI03, fileURI;
    AdapterGridResourceBucket adapter;
    GridView gridView;
    ArrayList<ResourceBucketObject> bucketObjects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource__bucket);
        setTitle("Resource");
        final ActionBar abar = getSupportActionBar();
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        View viewActionBar = getLayoutInflater().inflate(R.layout.actionbar_titletext_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Resource Bucket");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setHomeButtonEnabled(true);
        fab_add_res = findViewById(R.id.fab_add_res_bucket);
        if (CURRENT_USER.AdminLevel.equals("user")){
            fab_add_res.setVisibility(View.GONE);
        }
        fab_add_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addResource();
            }
        });

    }

    private void addResource(){
        uploadDialog = new Dialog(this);
        uploadDialog.setContentView(R.layout.upload_res_dialog);
        uploadDialog.show();
        img_01 = uploadDialog.findViewById(R.id.img_create_res_01);
        img_02 = uploadDialog.findViewById(R.id.img_create_res_02);
        img_03 = uploadDialog.findViewById(R.id.img_create_res_03);
        bt_selectPhoto = uploadDialog.findViewById(R.id.img_uploadImage_bucket);
        bt_select_pdf = uploadDialog.findViewById(R.id.img_uploadFile_bucket);
        bt_pdf_selected = uploadDialog.findViewById(R.id.bt_pdf_selected_bucket);
        bt_upload_resource = uploadDialog.findViewById(R.id.bt_upload_res_bucket);
        bt_upload_resource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (img_01 != null || fileURI != null){
                    model.performResourceUpload(mREF_RESOURCE_BUCKET,
                            imgURI01,imgURI02,imgURI03,fileURI,CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX)
                    );
                    progressDialogUpload = ProgressDialog.show(ResourceBucketActivity.this, "",
                            "Uploading. Please wait...", true);
                }else {
                    Toast.makeText(getApplicationContext(),"Choose File To upload !",Toast.LENGTH_SHORT).show();
                }
            }
        });
        bt_selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgURI01 == null){
                    openFileChooser(PICK_IMG_01);
                }else{
                    if (imgURI02 == null){
                        openFileChooser(PICK_IMG_02);
                    }else {
                        if (imgURI03 == null){
                            openFileChooser(PICK_IMG_03);
                        }else{
                            imgURI03 =null;
                            imgURI01 = null;
                            imgURI02 = null;
                            img_01.setVisibility(View.INVISIBLE);
                            img_02.setVisibility(View.INVISIBLE);
                            img_03.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(),"Max 3 Image",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        bt_select_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser(PICK_FILE);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        model = new ResourceBucketModel(this,this);
        bucketObjects = new ArrayList<>();
        model.performDataLoad(mREF_RESOURCE_BUCKET,bucketObjects);
    }

    private void openFileChooser(int requestCode){
        Intent intent = new Intent();
        if (requestCode == PICK_FILE){
            intent.setType("application/pdf");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,requestCode);
        }
        else {
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,requestCode);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMG_01 && resultCode == -1 && data != null && data.getData() != null){
            imgURI01 = data.getData();
            img_01.setImageURI(imgURI01);
            img_01.setVisibility(View.VISIBLE);
            Picasso.with(this).load(imgURI01).into(img_01);
        }
        if (requestCode == PICK_IMG_02 && resultCode == -1 && data != null && data.getData() != null){
            imgURI02 = data.getData();
            img_02.setImageURI(imgURI02);
            img_02.setVisibility(View.VISIBLE);
            Picasso.with(this).load(imgURI02).into(img_02);
        }
        if (requestCode == PICK_IMG_03 && resultCode == -1 && data != null && data.getData() != null){
            imgURI03 = data.getData();
            img_03.setImageURI(imgURI03);
            img_03.setVisibility(View.VISIBLE);
            Picasso.with(this).load(imgURI03).into(img_03);
        }
        if (requestCode == PICK_FILE && resultCode == -1 && data != null && data.getData() != null){
            fileURI = data.getData();
            bt_pdf_selected.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void uploadSuccess() {
        uploadDialog.dismiss();
        progressDialogUpload.dismiss();
        Toast.makeText(this,"Success- Reload the window",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadSuccess(ArrayList<ResourceBucketObject> bucketObjects) {
        gridView = findViewById(R.id.gridViewBucket);
        adapter = new AdapterGridResourceBucket(this,bucketObjects);
        gridView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // close this activity as oppose to navigating up
        return false;
    }

}
