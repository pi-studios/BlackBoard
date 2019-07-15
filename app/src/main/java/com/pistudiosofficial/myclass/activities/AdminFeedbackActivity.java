package com.pistudiosofficial.myclass.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.adapters.AdapterFeedbackAdmin;
import com.pistudiosofficial.myclass.model.FeedbackHODModel;
import com.pistudiosofficial.myclass.objects.FeedbackMainAdminObject;
import com.pistudiosofficial.myclass.view.AdminFeedbackView;

import java.util.ArrayList;

import static com.pistudiosofficial.myclass.Common.CURRENT_USER;
import static com.pistudiosofficial.myclass.Common.LOG;


public class AdminFeedbackActivity extends AppCompatActivity implements AdminFeedbackView {
    FeedbackHODModel feedbackHODModel;
    RecyclerView recyclerView;
    LinearLayoutManager llm;
    AdapterFeedbackAdmin adapterFeedbackAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_admin);
        setTitle("Feedback");
    }

    @Override
    protected void onStart() {
        super.onStart();
        feedbackHODModel = new FeedbackHODModel(this);
        feedbackHODModel.performFeedbackLoadAdmin(CURRENT_USER.UID);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void feedbackListLoad(ArrayList<FeedbackMainAdminObject> objects) {
        recyclerView = findViewById(R.id.recyclerView_admin_feedback);
        llm = new LinearLayoutManager(this);
        adapterFeedbackAdmin = new AdapterFeedbackAdmin(objects,this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                llm.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapterFeedbackAdmin);
    }
}
