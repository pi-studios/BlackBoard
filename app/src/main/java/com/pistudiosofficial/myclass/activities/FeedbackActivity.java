package com.pistudiosofficial.myclass.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.adapters.AdapterFeedbackUser;
import com.pistudiosofficial.myclass.model.FeedbackHODModel;
import com.pistudiosofficial.myclass.view.FeedbackView;

import java.util.ArrayList;
import java.util.HashMap;

public class FeedbackActivity extends AppCompatActivity implements FeedbackView {
    String classID;
    FeedbackHODModel feedbackHODModel;
    RecyclerView recyclerView;
    Button bt_submit;
    LinearLayoutManager llm;
    AdapterFeedbackUser adapter;
    HashMap<String,String> rating;
    ArrayList<String> question;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        setTitle("Feedback");
        classID = getIntent().getStringExtra("class_id");
        feedbackHODModel = new FeedbackHODModel(this);
        llm = new LinearLayoutManager(this);
        feedbackHODModel.performFeedbackLoad(classID);
        recyclerView = findViewById(R.id.recyclerView_feedback);
        bt_submit = findViewById(R.id.bt_feedback_submit_user);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rating.size() != question.size()){
                    Toast.makeText(FeedbackActivity.this,"Complete The Feedback Please",Toast.LENGTH_SHORT).show();
                }
                else{
                    feedbackHODModel.performFeedbackSubmitUser(rating,classID);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @SuppressLint("WrongConstant")
    @Override
    public void loadSuccess(ArrayList<String> question) {
        this.question = question;
        rating = new HashMap<>();
        adapter = new AdapterFeedbackUser(question,rating);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                llm.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void feedbackSubmitSuccess() {
        Toast.makeText(FeedbackActivity.this,"Done",Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void feedbackSubmitFailed() {
        Toast.makeText(this,"Already Submitted",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void feedbackDontExist(){
        Toast.makeText(this,"Feedback not found",Toast.LENGTH_SHORT).show();
        finish();
    }
}
