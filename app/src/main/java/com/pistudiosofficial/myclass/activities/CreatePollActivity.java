package com.pistudiosofficial.myclass.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.presenter.CreatePollPresenter;
import com.pistudiosofficial.myclass.view.CreatePollView;

import java.util.ArrayList;

public class CreatePollActivity extends AppCompatActivity implements CreatePollView {

    Button bt_addPollOption,bt_pushPoll;
    EditText et_pollDescription, et_addPollOption;
    ListView listView;
    ArrayList<String> pollOption;
    ArrayAdapter adapter;
    CreatePollPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll);
        presenter = new CreatePollPresenter(this);
        bt_addPollOption = findViewById(R.id.bt_addPollOption);
        bt_pushPoll = findViewById(R.id.bt_pushPoll);
        et_addPollOption = findViewById(R.id.et_addPollOption);
        et_pollDescription = findViewById(R.id.et_poll_description);
        listView = findViewById(R.id.recyclerView_create_poll);
        pollOption = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pollOption);
        listView.setAdapter(adapter);
        bt_addPollOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String x = et_addPollOption.getText().toString();
                if(x!=null && !x.equals("")){
                    pollOption.add(x);
                    et_addPollOption.setText("");
                    adapter.notifyDataSetChanged();
                }
            }
        });
        bt_pushPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pollOption.size()>=2 && !et_pollDescription.getText().toString().equals("")){
                    presenter.performCreatePoll(et_pollDescription.getText().toString(),pollOption);
                }
                else{
                    Toast.makeText(CreatePollActivity.this,"Insufficient Option",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void createPollSuccess() {
        finish();
    }

    @Override
    public void createPollFailed() {
        Toast.makeText(this,"Poll Upload Failed",Toast.LENGTH_SHORT).show();
    }
}
