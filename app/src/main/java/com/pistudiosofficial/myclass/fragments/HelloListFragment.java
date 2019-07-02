package com.pistudiosofficial.myclass.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.adapters.AdapterHelloList;
import com.pistudiosofficial.myclass.objects.UserObject;
import com.pistudiosofficial.myclass.presenter.HelloListPresenter;
import com.pistudiosofficial.myclass.view.HelloListView;

import java.util.ArrayList;

public class HelloListFragment extends Fragment implements HelloListView {

    HelloListPresenter presenter;
    RecyclerView recyclerView;
    AdapterHelloList adapterHelloList;
    LinearLayoutManager llm;
    View view;
    public HelloListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_hello_list,container,false);
        presenter = new HelloListPresenter(this);
        presenter.performHelloListDownload();


        return view;
    }

    @SuppressLint("WrongConstant")
    @Override
    public void helloListLoadSuccess(ArrayList<UserObject> userObjects) {
        adapterHelloList = new AdapterHelloList(userObjects,getContext());
        llm = new LinearLayoutManager(getContext());
        presenter = new HelloListPresenter(this);
        recyclerView = view.findViewById(R.id.recyclerView_helloList);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapterHelloList);
        adapterHelloList.notifyDataSetChanged();
    }

    @Override
    public void helloListLoadFailed() {

    }
}
