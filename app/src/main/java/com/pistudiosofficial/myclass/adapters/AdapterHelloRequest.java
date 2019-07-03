package com.pistudiosofficial.myclass.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.bumptech.glide.Glide;
import com.pistudiosofficial.myclass.Common;
import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.activities.ProfileNewActivity;
import com.pistudiosofficial.myclass.objects.HelloListObject;
import com.pistudiosofficial.myclass.presenter.HelloRequestPresenter;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class AdapterHelloRequest extends RecyclerView.Adapter<AdapterHelloRequest.MyViewHolder> {

    ArrayList<HelloListObject> helloList;
    Context context;
    HelloRequestPresenter presenter;
    public AdapterHelloRequest(ArrayList<HelloListObject> helloList, Context context, HelloRequestPresenter presenter){
        this.helloList = helloList;
        this.context = context;
        this.presenter = presenter;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.hello_request_row, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tv_username.setText(helloList.get(i).userObject.Name);
        if (helloList.get(i).userObject.Bio != null){
            myViewHolder.tv_bio.setText(helloList.get(i).userObject.Bio);
        }
        if(helloList.get(i).userObject.profilePicLink != null){
            Glide.with(context).load(helloList.get(i).userObject.profilePicLink).into(myViewHolder.circleImageView);
        }
        myViewHolder.bt_hello_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.performHelloRequestRespond(helloList.get(i).userObject.UID, true);
            }
        });
        myViewHolder.bt_hello_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.performHelloRequestRespond(helloList.get(i).userObject.UID,false);
            }
        });
        myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileNewActivity.class);
                ((Activity)context).startActivity(intent);
                Common.SELECTED_PROFILE_UID = helloList.get(i).userObject.UID;
            }
        });

    }

    @Override
    public int getItemCount() {
        return helloList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_username,tv_bio;
        Button bt_hello_accept, bt_hello_reject;
        CircleImageView circleImageView;
        ConstraintLayout linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_username = itemView.findViewById(R.id.tv_username_hello);
            tv_bio = itemView.findViewById(R.id.tv_bio_hello);
            circleImageView = itemView.findViewById(R.id.img_hello_profile_photo);
            bt_hello_accept = itemView.findViewById(R.id.bt_hello_accept);
            bt_hello_reject = itemView.findViewById(R.id.bt_hello_reject);
            linearLayout = itemView.findViewById(R.id.linearLayout_search_main);
        }
    }


}
