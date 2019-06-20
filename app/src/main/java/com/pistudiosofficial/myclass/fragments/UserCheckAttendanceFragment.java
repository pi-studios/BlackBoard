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

import com.pistudiosofficial.myclass.AdapterPostLoad;
import com.pistudiosofficial.myclass.R;

import static com.pistudiosofficial.myclass.Common.POST_OBJECT_LIST;

public class UserCheckAttendanceFragment extends Fragment {

    public UserCheckAttendanceFragment() {
    }

    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_check_attendence, container, false);
        RecyclerView recyclerViewPost = v.findViewById(R.id.recyclerView_user_check_attendance);
        AdapterPostLoad adapterPostLoad = new AdapterPostLoad(POST_OBJECT_LIST);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewPost.getContext(),
                llm.getOrientation());
        recyclerViewPost.addItemDecoration(dividerItemDecoration);
        recyclerViewPost.setLayoutManager(llm);
        llm.scrollToPosition(POST_OBJECT_LIST.size()-1);
        recyclerViewPost.setAdapter(adapterPostLoad);
        return v;
    }
}
