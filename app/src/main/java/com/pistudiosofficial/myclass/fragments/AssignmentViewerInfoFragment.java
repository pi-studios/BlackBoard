package com.pistudiosofficial.myclass.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.activities.AssignmentCreationActivity;
import com.pistudiosofficial.myclass.adapters.AdapterAssignmentViewerInfoFrag;
import com.pistudiosofficial.myclass.adapters.AdapterGridResourceBucket;
import com.pistudiosofficial.myclass.model.AssignmentModel;
import com.pistudiosofficial.myclass.objects.AssignmentObject;
import com.pistudiosofficial.myclass.objects.AssignmentSubmissionObject;
import com.pistudiosofficial.myclass.presenter.NotificationHistoryPresenter;
import com.pistudiosofficial.myclass.view.AssignmentViewerInfoFragView;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.pistudiosofficial.myclass.Common.ASSIGNMENT_OBJECT_TEMP;
import static com.pistudiosofficial.myclass.Common.CURRENT_CLASS_ID_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_INDEX;
import static com.pistudiosofficial.myclass.Common.CURRENT_USER;
import static com.pistudiosofficial.myclass.Common.LOG;
import static com.pistudiosofficial.myclass.Common.SELECTED_ASSIGNMENT_ID;
import static com.pistudiosofficial.myclass.Common.mREF_classList;

public class AssignmentViewerInfoFragment extends Fragment implements AssignmentViewerInfoFragView {

    public AssignmentViewerInfoFragment() {
    }

    private GridView gridView;
    private TextView tv_title,tv_creation_date,tv_due_date,tv_description;
    private Button bt_edit_submit;
    private AssignmentModel assignmentModel;
    private AdapterAssignmentViewerInfoFrag adapterAssignmentViewerInfoFrag;
    private static final int PICK_FILE_REQUEST = 1;
    ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assignmentviewer_info,container,false);
        gridView = view.findViewById(R.id.gridViewAssignmentViewerInfo);
        tv_title = view.findViewById(R.id.tv_title_asssignmentViewer);
        tv_creation_date = view.findViewById(R.id.tv_creationDate_assignmentviewer);
        tv_due_date = view.findViewById(R.id.tv_due_date_assignmentviewer);
        tv_description = view.findViewById(R.id.tv_description_assignmentviewer);
        bt_edit_submit = view.findViewById(R.id.bt_assignmentviewer);
        if (CURRENT_USER.AdminLevel.equals("admin")){bt_edit_submit.setText("Edit");}
        else{bt_edit_submit.setText("Submit");}
        bt_edit_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CURRENT_USER.AdminLevel.equals("admin")) {
                    Intent intent = new Intent(getContext(), AssignmentCreationActivity.class);
                    intent.putExtra("status", "edit");
                    startActivity(intent);
                }
                else{
                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.show();
                    openFileChooser(PICK_FILE_REQUEST);
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        assignmentModel = new AssignmentModel(this);
        assignmentModel.downloadAssignmentForViewer(SELECTED_ASSIGNMENT_ID);
    }

    @Override
    public void downloadSuccess(AssignmentObject assignmentObject, HashMap<String,String> meta_data,ArrayList<String> name_meta_data) {
        if (assignmentObject != null){
            ASSIGNMENT_OBJECT_TEMP = assignmentObject;
            tv_title.setText(assignmentObject.getTitle());
            tv_description.setText(assignmentObject.getDescription());
            tv_due_date.setText(assignmentObject.getDueDate());
            tv_creation_date.setText(assignmentObject.getTimePosted());
        }
        if (meta_data.size()>0){
            adapterAssignmentViewerInfoFrag = new AdapterAssignmentViewerInfoFrag(meta_data,name_meta_data,getContext());
            gridView.setAdapter(adapterAssignmentViewerInfoFrag);
        }
    }

    @Override
    public void downloadFailed() {
        Toast.makeText(getContext(),"Try Again",Toast.LENGTH_SHORT).show();
    }

    private void openFileChooser(int imgRQST){
        Intent intent = getFileChooserIntent();
        startActivityForResult(intent,imgRQST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == -1 && data != null && data.getData() != null){
            new AlertDialog.Builder(getContext())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Submit File")
                    .setMessage("File once submitted cannot be undone")
                    .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String newID = mREF_classList.child(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX))
                                    .child("assignment_online").child(SELECTED_ASSIGNMENT_ID).child("submission")
                                    .child(CURRENT_USER.Roll).push().getKey();
                            mREF_classList.child(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX))
                                    .child("assignment_online").child(SELECTED_ASSIGNMENT_ID).child("submission")
                                    .child(CURRENT_USER.Roll).child("name").setValue(CURRENT_USER.Name);
                            uploadSubmission(mREF_classList.child(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX))
                                    .child("assignment_online").child(SELECTED_ASSIGNMENT_ID).child("submission")
                                    .child(CURRENT_USER.Roll).child(newID),data.getData());
                        }
                    }).setNegativeButton("Cancel",null).show();
        }

    }

    private Intent getFileChooserIntent() {
        String[] mimeTypes = {"image/*", "application/pdf"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";

            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }

            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }

        return intent;
    }

    private void uploadSubmission(DatabaseReference databaseReference, Uri uri){
        StorageReference imgREF = FirebaseStorage.getInstance().getReference()
                .child("assignments/"+CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX)+"/submission"
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
                    ContentResolver cR = getContext().getContentResolver();
                    MimeTypeMap mime = MimeTypeMap.getSingleton();
                    String type = mime.getExtensionFromMimeType(cR.getType(uri));
                    if (type.equals("pdf")){type = "pdf";}
                    else{type = "jpg";}
                    String currentTime = DateFormat.getDateTimeInstance().format(new Date());
                    AssignmentSubmissionObject assignmentSubmissionObject = new AssignmentSubmissionObject();
                    assignmentSubmissionObject.link = downloadUri.toString();
                    assignmentSubmissionObject.name = CURRENT_USER.Name;
                    assignmentSubmissionObject.student_uid = CURRENT_USER.UID;
                    assignmentSubmissionObject.type = type;
                    assignmentSubmissionObject.timestamp = currentTime;
                    databaseReference.setValue(assignmentSubmissionObject);
                    progressDialog.dismiss();
                    Toast.makeText(getContext(),"Submitted",Toast.LENGTH_SHORT).show();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

}
