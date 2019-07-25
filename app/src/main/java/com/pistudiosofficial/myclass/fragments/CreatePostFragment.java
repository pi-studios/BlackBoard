package com.pistudiosofficial.myclass.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.objects.PollOptionValueLikeObject;
import com.pistudiosofficial.myclass.objects.PostObject;
import com.pistudiosofficial.myclass.presenter.CheckAttendancePresenter;
import com.pistudiosofficial.myclass.view.CheckAttendanceFragView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import static com.pistudiosofficial.myclass.Common.CURRENT_USER;
import static com.pistudiosofficial.myclass.Common.LOG;

public  class CreatePostFragment extends Fragment implements CheckAttendanceFragView {

    private CheckAttendancePresenter presenter;
    private Uri imgURI01,imgURI02,imgURI03, fileURI;
    private Button uploadFile;
    private ImageButton pickImage,pickFile;
    private ImageView img1,img2,img3;
    private String fileUploadLink = "";
    int temp;
    private View view;
    private ProgressDialog progressDialogPosting;
    private static final int PICK_IMAGE_REQUEST01 = 1,PICK_IMAGE_REQUEST02 = 2, PICK_IMAGE_REQUEST03 = 3,
            PICK_FILE_REQUEST = 4;
//    public CreatePostFragment() {
//        // Required empty public constructor
//    }
//
//    public static CreatePostFragment newInstance(String param1, String param2) {
//        CreatePostFragment fragment = new CreatePostFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view= inflater.inflate(R.layout.fragment_create_post, container, false);
        Log.d("LOG","HereInFraag");
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Toolbar toolbar = view.findViewById(R.id.toolbar_admin);
        toolbar.setTitle("CreatePost");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });
        createPost();
        return view;
    }
    private void createPost(){
        temp = 0;
        Button postDone = view.findViewById(R.id.bt_create_post);
        uploadFile = view.findViewById(R.id.bt_create_post_upload_file);

        pickFile = view.findViewById(R.id.img_uploadFile);
        pickImage = view.findViewById(R.id.img_uploadImage);

        img1 = view.findViewById(R.id.img_create_post_01);
        img2 = view.findViewById(R.id.img_create_post_02);
        img3 = view.findViewById(R.id.img_create_post_03);
        EditText et_post_content = view.findViewById(R.id.et_create_post_body);
//        view.show();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a MMM d, ''yy");
        String simpleTime = simpleDateFormat.format(new Date());

        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgURI01 == null){
                    openFileChooser(PICK_IMAGE_REQUEST01);
                }else{
                    if (imgURI02 == null){
                        openFileChooser(PICK_IMAGE_REQUEST02); temp++;
                    }else {
                        if (imgURI03 == null){
                            openFileChooser(PICK_IMAGE_REQUEST03); temp++;
                        }else{
                            imgURI03 =null;
                            imgURI01 = null;
                            imgURI02 = null;
                            img1.setVisibility(View.INVISIBLE);
                            img2.setVisibility(View.INVISIBLE);
                            img3.setVisibility(View.INVISIBLE);
                            Toast.makeText(getContext(),"Max 3 Image",Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });
        pickFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser(PICK_FILE_REQUEST);
            }
        });
        postDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!et_post_content.getText().toString().equals("") && et_post_content.getText().toString() != null){
                    ArrayList<Uri> imgURILIST = new ArrayList<>();
                    ArrayList<String> extensionList = new ArrayList<>();
                    imgURILIST.clear();
                    extensionList.clear();
                    PostObject postObject;
                    if (imgURI01 != null){imgURILIST.add(imgURI01); extensionList.add(getExtension(imgURI01));}
                    if (imgURI02 != null){imgURILIST.add(imgURI02); extensionList.add(getExtension(imgURI02));}
                    if (imgURI03 != null){imgURILIST.add(imgURI03); extensionList.add(getExtension(imgURI03));}
                    if(fileUploadLink.equals("")){
                        postObject = new PostObject(
                                CURRENT_USER.Name,simpleTime,
                                et_post_content.getText().toString(),"simple_admin_post",
                                CURRENT_USER.UID,CURRENT_USER.profilePicLink,null);
                    }
                    else {
                        postObject = new PostObject(
                                CURRENT_USER.Name,simpleTime,
                                et_post_content.getText().toString()+". Link: "+fileUploadLink,"simple_admin_post",
                                "simple_class_post",CURRENT_USER.UID,CURRENT_USER.profilePicLink);
                    }
                    progressDialogPosting = ProgressDialog.show(getContext(), "",
                            "Posting. Please wait...", true);

                    if (imgURILIST.size() > 0) {
                        presenter.performPosting(postObject, imgURILIST, extensionList);
                    } else {
                        presenter.performPosting(postObject, null, null);
                    }

                }
                else{
                    Toast.makeText(getActivity(),"Cannot Have Empty Post !",Toast.LENGTH_SHORT).show();
//                    view.dismiss();
                }
            }
        });

    }
    private void openFileChooser(int imgRQST){
        Intent intent = new Intent();
        if (imgRQST == PICK_FILE_REQUEST){
            intent.setType("application/pdf");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,imgRQST);
        }
        else {
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,imgRQST);
        }

    }

    private String getExtension(Uri uri){
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    @Override
    public void success(ArrayList<Double> attendancePercentageList) {

    }

    @Override
    public void failed() {

    }

    @Override
    public void exportCsvSuccess() {

    }

    @Override
    public void exportCsvFailed() {

    }

    @Override
    public void notifySuccess() {

    }

    @Override
    public void notifyFailed() {

    }

    @Override
    public void postingSuccess() {

    }

    @Override
    public void postingFailed() {

    }

    @Override
    public void checkAttendanceReturn(boolean b) {

    }

    @Override
    public void fileUploadDone(String link) {

    }

    @Override
    public void loadPostSuccess(ArrayList<PostObject> postObjects, HashMap<String, PollOptionValueLikeObject> post_poll_option, ArrayList<String> post_like_list, HashMap<String, ArrayList<String>> post_url_list, ArrayList<String> comment_count, ArrayList<String> likedID, HashMap<String, String> postPollSelect) {

    }
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
