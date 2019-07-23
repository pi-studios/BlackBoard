package com.pistudiosofficial.myclass.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pistudiosofficial.myclass.adapters.AdapterNotificationHistory;
import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.presenter.NotificationHistoryPresenter;
import com.pistudiosofficial.myclass.view.NotificationHistoryView;

import java.util.ArrayList;
import java.util.Collections;

public class NotifTabFragment extends Fragment implements NotificationHistoryView {
    RecyclerView recyclerView;
    AdapterNotificationHistory adapter;
    LinearLayoutManager llm;
    NotificationHistoryPresenter presenter;
    public NotifTabFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification,container,false);
        presenter = new NotificationHistoryPresenter(this);
        presenter.performNotificationHistoryDownload();
        recyclerView = view.findViewById(R.id.recycler_notification_history);
        llm = new LinearLayoutManager(getContext());

        return view;
    }

    @SuppressLint("WrongConstant")
    @Override
    public void loadRecyclerSuccess(ArrayList<String> title, ArrayList<String> body) {

        Collections.reverse(title);Collections.reverse(body);
        adapter = new AdapterNotificationHistory(title,body);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        //llm.scrollToPosition(title.size()-1);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void loadRecyclerFailed() {
        //Toast.makeText(getContext(),"History Load Failed",Toast.LENGTH_SHORT).show();
    }
}
