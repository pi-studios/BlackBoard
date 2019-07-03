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

import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.adapters.AdapterHelloRequest;
import com.pistudiosofficial.myclass.objects.HelloListObject;
import com.pistudiosofficial.myclass.presenter.HelloRequestPresenter;
import com.pistudiosofficial.myclass.view.HelloRequestView;

import java.util.ArrayList;

import static com.pistudiosofficial.myclass.Common.HELLO_REQUEST_USERS;

public class HelloRequestFragment extends Fragment implements HelloRequestView {

    ArrayList<HelloListObject> helloList;
    HelloRequestPresenter presenter;
    RecyclerView recyclerView;
    AdapterHelloRequest adapterHelloRequest;
    LinearLayoutManager llm;

    public HelloRequestFragment() {
    }

    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_hello_request,container,false);

        helloList = new ArrayList<>();
        for ( String key : HELLO_REQUEST_USERS.keySet() ) {
            helloList.add(HELLO_REQUEST_USERS.get(key));
        }
        for (int i = 0; i<helloList.size(); i++){
            if (helloList.get(i).hello == 0 || helloList.get(i).hello == 2){
                helloList.remove(i);
            }
        }
        llm = new LinearLayoutManager(getContext());
        presenter = new HelloRequestPresenter(this);
        adapterHelloRequest = new AdapterHelloRequest(helloList,getContext(),presenter);
        recyclerView = view.findViewById(R.id.recyclerView_helloRequestList);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapterHelloRequest);
        adapterHelloRequest.notifyDataSetChanged();
        return view;
    }

    @Override
    public void requestAccepted(String UID) {
        Toast.makeText(getContext(), "Accepted", Toast.LENGTH_SHORT).show();
        removeItem(UID);
    }

    @Override
    public void requestRejected(String UID) {
        Toast.makeText(getContext(), "Rejected", Toast.LENGTH_SHORT).show();
        removeItem(UID);
    }

    @Override
    public void requestActionFailed() {
        Toast.makeText(getContext(), "Action Failed", Toast.LENGTH_SHORT).show();
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
