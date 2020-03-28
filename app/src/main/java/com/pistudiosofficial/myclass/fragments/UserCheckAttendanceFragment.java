package com.pistudiosofficial.myclass.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.pistudiosofficial.myclass.activities.AssignmentHome;
import com.pistudiosofficial.myclass.activities.ResourceBucketActivity;
import com.pistudiosofficial.myclass.adapters.AdapterPostLoad;
import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.objects.PollOptionValueLikeObject;
import com.pistudiosofficial.myclass.objects.PostObject;
import com.pistudiosofficial.myclass.presenter.CheckAttendancePresenter;
import com.pistudiosofficial.myclass.view.CheckAttendanceFragView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static com.pistudiosofficial.myclass.Common.CURRENT_CLASS_ID_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_INDEX;
import static com.pistudiosofficial.myclass.Common.mREF_RESOURCE_BUCKET;
import static com.pistudiosofficial.myclass.Common.mREF_classList;


public class UserCheckAttendanceFragment extends Fragment implements CheckAttendanceFragView {
    RecyclerView recyclerViewPost;
    CheckAttendancePresenter presenter;
    FloatingActionButton fab_resourceBucket,fab_student_Assignment;
    TextView toolBarText;
    public UserCheckAttendanceFragment() {
    }

    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_check_attendence, container, false);
        // Add Toolbar with back button
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Toolbar toolbar = v.findViewById(R.id.toolbar_user);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        toolBarText=v.findViewById(R.id.toolbar_text_user);
        toolBarText.setText(getArguments().getString("ClassName"));
        recyclerViewPost = v.findViewById(R.id.recyclerView_user_check_attendance);
        presenter = new CheckAttendancePresenter(this);
        presenter.performLoadPost(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX));
        fab_resourceBucket = v.findViewById(R.id.fab_res_bucket_user);
        fab_student_Assignment=v.findViewById(R.id.fab_student_assignment);
        fab_resourceBucket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ResourceBucketActivity.class);
                mREF_RESOURCE_BUCKET = mREF_classList.child(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX))
                        .child("resource_bucket");
                startActivity(intent);
            }
        });
        fab_student_Assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getContext(), AssignmentHome.class);
                startActivity(intent);
            }
        });
        presenter.performDeleteRead(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX));
        return v;
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

    @SuppressLint("WrongConstant")
    @Override
    public void loadPostSuccess(ArrayList<PostObject> postObjects, HashMap<String,
            PollOptionValueLikeObject> post_poll_option, ArrayList<String> post_like_list,
                                HashMap<String, ArrayList<String>> post_url_list,
                                ArrayList<String> comment_count, ArrayList<String> likedPostID,
                                HashMap<String,String> postPollSelect) {
        Collections.reverse(postObjects);
        Collections.reverse(post_like_list);
        Collections.reverse(comment_count);
        Collections.reverse(likedPostID);
        ArrayList<PostObject> postlist = postObjects;
        AdapterPostLoad adapterPostLoad =
                new AdapterPostLoad(postlist,
                        post_poll_option,
                        post_like_list,
                        post_url_list,comment_count,
                        getContext(),likedPostID,postPollSelect);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewPost.getContext(),
//                llm.getOrientation());
//        recyclerViewPost.addItemDecoration(dividerItemDecoration);
        recyclerViewPost.setLayoutManager(llm);
        recyclerViewPost.setAdapter(adapterPostLoad);
    }
}
