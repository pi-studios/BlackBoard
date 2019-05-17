package com.pistudiosofficial.myclass.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.presenter.ConnectionListPresenter;
import com.pistudiosofficial.myclass.view.ConnectionListActivityView;

public class ConnectionListActivity extends AppCompatActivity implements ConnectionListActivityView {

    ConnectionListPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_list);


    }

    @Override
    public void downloadFailed() {

    }

    @Override
    public void loadData() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter = new ConnectionListPresenter(this);
    }
}
