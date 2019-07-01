package com.pistudiosofficial.myclass.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.adapters.AdapterHelloRequest;
import com.pistudiosofficial.myclass.adapters.AdapterNotificationHistory;
import com.pistudiosofficial.myclass.objects.HelloListObject;
import com.pistudiosofficial.myclass.presenter.HelloRequestPresenter;
import com.pistudiosofficial.myclass.view.HelloRequestView;

import java.util.ArrayList;

import static com.pistudiosofficial.myclass.Common.HELLO_USERS;

public class HelloRequestActivity extends AppCompatActivity implements HelloRequestView {

    ArrayList<HelloListObject> helloList;
    HelloRequestPresenter presenter;
    RecyclerView recyclerView;
    AdapterHelloRequest adapterHelloRequest;
    LinearLayoutManager llm;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_request);

        helloList = new ArrayList<>();
        for ( String key : HELLO_USERS.keySet() ) {
            helloList.add(HELLO_USERS.get(key));
        }
        for (int i = 0; i<helloList.size(); i++){
            if (helloList.get(i).hello == 0 || helloList.get(i).hello == 2){
                helloList.remove(i);
            }
        }
        llm = new LinearLayoutManager(this);
        presenter = new HelloRequestPresenter(this);
        adapterHelloRequest = new AdapterHelloRequest(helloList,this,presenter);
        recyclerView = findViewById(R.id.recyclerView_helloRequestList);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapterHelloRequest);
        adapterHelloRequest.notifyDataSetChanged();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void requestAccepted(String UID) {
        Toast.makeText(this, "Accepted", Toast.LENGTH_SHORT).show();
        removeItem(UID);
    }

    @Override
    public void requestRejected(String UID) {
        Toast.makeText(this, "Rejected", Toast.LENGTH_SHORT).show();
        removeItem(UID);
    }

    @Override
    public void requestActionFailed() {
        Toast.makeText(this, "Action Failed", Toast.LENGTH_SHORT).show();
    }

    private void removeItem(String UID){
        for (int j = 0; j<helloList.size(); j++){
            if(helloList.get(j).userObject.UID.equals(UID)){
                helloList.remove(j);
            }
        }
        adapterHelloRequest.notifyDataSetChanged();
    }
}
