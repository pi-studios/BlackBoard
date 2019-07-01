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

import com.bumptech.glide.Glide;
import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.activities.ProfileNewActivity;
import com.pistudiosofficial.myclass.objects.HelloListObject;
import com.pistudiosofficial.myclass.objects.UserObject;
import com.pistudiosofficial.myclass.presenter.HelloRequestPresenter;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pistudiosofficial.myclass.Common.SELECTED_PROFILE_UID;

public class AdapterHelloList extends RecyclerView.Adapter<AdapterHelloList.MyViewHolder> {

    ArrayList<UserObject> helloList;
    Context context;
    public AdapterHelloList(ArrayList<UserObject> helloList, Context context){
        this.helloList = helloList;
        this.context = context;
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
        myViewHolder.tv_username.setText(helloList.get(i).Name);
        if (helloList.get(i).Bio != null){
            myViewHolder.tv_bio.setText(helloList.get(i).Bio);
        }
        if(helloList.get(i).profilePicLink != null){
            Glide.with(context).load(helloList.get(i).profilePicLink).into(myViewHolder.circleImageView);
        }
        myViewHolder.bt_hello_accept.setVisibility(View.GONE);
        myViewHolder.bt_hello_reject.setVisibility(View.GONE);
        myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileNewActivity.class);
                context.startActivity(intent);
                SELECTED_PROFILE_UID = helloList.get(i).UID;
                ((Activity)context).finish();
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
        LinearLayout linearLayout;
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