package com.pistudiosofficial.myclass.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.adapters.AdapterHelloList;
import com.pistudiosofficial.myclass.objects.UserObject;
import com.pistudiosofficial.myclass.presenter.HelloListPresenter;
import com.pistudiosofficial.myclass.view.HelloListView;

import java.util.ArrayList;

public class HelloListActivity extends AppCompatActivity implements HelloListView {

    HelloListPresenter presenter;
    RecyclerView recyclerView;
    AdapterHelloList adapterHelloList;
    LinearLayoutManager llm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_list);
        setTitle("Hello List");

    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter = new HelloListPresenter(this);
        presenter.performHelloListDownload();
    }

    @SuppressLint("WrongConstant")
    @Override
    public void helloListLoadSuccess(ArrayList<UserObject> userObjects) {
        adapterHelloList = new AdapterHelloList(userObjects,this);
        llm = new LinearLayoutManager(this);
        presenter = new HelloListPresenter(this);
        recyclerView = findViewById(R.id.recyclerView_helloList);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapterHelloList);
        adapterHelloList.notifyDataSetChanged();
    }

    @Override
    public void helloListLoadFailed() {

    }
}
