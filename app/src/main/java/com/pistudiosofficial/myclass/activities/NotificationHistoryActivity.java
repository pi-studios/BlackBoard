package com.pistudiosofficial.myclass.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.pistudiosofficial.myclass.AdapterNotificationHistory;
import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.presenter.NotificationHistoryPresenter;
import com.pistudiosofficial.myclass.view.NotificationHistoryView;

import java.util.ArrayList;

public class NotificationHistoryActivity extends AppCompatActivity implements NotificationHistoryView {

    RecyclerView recyclerView;
    AdapterNotificationHistory adapter;
    LinearLayoutManager llm;
    NotificationHistoryPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_history);
        recyclerView = findViewById(R.id.recycler_notification_history);
        llm = new LinearLayoutManager(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter = new NotificationHistoryPresenter(this);
        presenter.performNotificationHistoryDownload();
    }

    @SuppressLint("WrongConstant")
    @Override
    public void loadRecyclerSuccess(ArrayList<String> title, ArrayList<String> body) {


        adapter = new AdapterNotificationHistory(title,body);
        Log.i("TAG",title.size()+" "+body.size());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        llm.scrollToPosition(title.size()-1);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void loadRecyclerFailed() {
        Toast.makeText(NotificationHistoryActivity.this,"History Load Failed",Toast.LENGTH_SHORT).show();
    }
}
