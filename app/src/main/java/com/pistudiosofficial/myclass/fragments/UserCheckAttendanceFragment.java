package com.pistudiosofficial.myclass.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


public class UserCheckAttendanceFragment extends Fragment implements CheckAttendanceFragView {
    RecyclerView recyclerViewPost;
    CheckAttendancePresenter presenter;
    public UserCheckAttendanceFragment() {
    }

    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_check_attendence, container, false);
        recyclerViewPost = v.findViewById(R.id.recyclerView_user_check_attendance);
        presenter = new CheckAttendancePresenter(this);
        presenter.performLoadPost(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX));
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
                                HashMap<String, ArrayList<String>> post_url_list, ArrayList<String> comment_count) {
        Collections.reverse(postObjects);
        Collections.reverse(post_like_list);
        Collections.reverse(comment_count);
        ArrayList<PostObject> postlist = postObjects;
        AdapterPostLoad adapterPostLoad =
                new AdapterPostLoad(postlist,
                        post_poll_option,
                        post_like_list,
                        post_url_list,comment_count,
                        getContext());
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewPost.getContext(),
                llm.getOrientation());
        recyclerViewPost.addItemDecoration(dividerItemDecoration);
        recyclerViewPost.setLayoutManager(llm);
        recyclerViewPost.setAdapter(adapterPostLoad);
    }
}
