package com.pistudiosofficial.myclass.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.activities.ProfileNewActivity;
import com.pistudiosofficial.myclass.objects.CommentObject;

import java.util.ArrayList;

import static com.pistudiosofficial.myclass.Common.COMMENT_LOAD_POST_OBJECT;
import static com.pistudiosofficial.myclass.Common.SELECTED_PROFILE_UID;

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.MyViewHolder> {

    ArrayList<CommentObject> commentObjects;
    Context context;
    public AdapterComment( ArrayList<CommentObject> commentObjects,Context context) {
        this.commentObjects = commentObjects;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.comment_row,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tv_userName.setText(commentObjects.get(i).userName);
        myViewHolder.tv_time.setText(commentObjects.get(i).timestamp);
        myViewHolder.tv_body.setText(commentObjects.get(i).comment);
        myViewHolder.tv_userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileNewActivity.class);
                SELECTED_PROFILE_UID = commentObjects.get(i).userUID;
                ((Activity)context).startActivity(intent);
            }
        });
        if (commentObjects.get(i).profile_pic_link != null){
            Glide.with(context).load(commentObjects.get(i).profile_pic_link)
                    .into(myViewHolder.img_profile_pic);
        }
    }

    @Override
    public int getItemCount() { return commentObjects.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_userName,tv_time,tv_body;
        ImageView img_profile_pic;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_userName = itemView.findViewById(R.id.tv_comment_username);
            tv_body = itemView.findViewById(R.id.tv_comment_body);
            tv_time = itemView.findViewById(R.id.tv_comment_time);
            img_profile_pic = itemView.findViewById(R.id.img_comment_profile);
        }
    }

}
