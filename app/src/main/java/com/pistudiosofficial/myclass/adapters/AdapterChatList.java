package com.pistudiosofficial.myclass.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.pistudiosofficial.myclass.activities.ChatActivity;
import com.pistudiosofficial.myclass.activities.ProfileNewActivity;
import com.pistudiosofficial.myclass.objects.ChatListObject;
import com.pistudiosofficial.myclass.objects.UserObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pistudiosofficial.myclass.Common.SELECTED_CHAT_UID;
import static com.pistudiosofficial.myclass.Common.SELECTED_PROFILE_UID;

public class AdapterChatList extends RecyclerView.Adapter<AdapterChatList.MyViewHolder> {

    HashMap<String, ChatListObject> hashMap;
    ArrayList<UserObject> userObjects;
    ArrayList<String> chatindex, chatcounts;
    Context context;

    public AdapterChatList(HashMap<String, ChatListObject> hashMap,ArrayList<UserObject> userObjects,
            ArrayList<String> chatindex, ArrayList<String> chatcounts,
             Context context) {
        this.hashMap = hashMap;
        this.userObjects = userObjects;
        this.context = context;
        this.chatcounts = chatcounts;
        this.chatindex = chatindex;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.chat_list_row, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tv_username.setText(userObjects.get(i).Name);
        if (userObjects.get(i).profilePicLink != null) {
            Glide.with(context).load(userObjects.get(i).profilePicLink).into(myViewHolder.circleImageView);
        }
        if (hashMap.get(chatindex.get(i)) != null) {
            myViewHolder.tv_lastText.setText(Objects.requireNonNull(hashMap.get(chatindex.get(i)).lastChat));

            myViewHolder.tv_count.setText(Objects.requireNonNull(hashMap.get(chatindex.get(i))).chatCount);
        }
        myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                context.startActivity(intent);
                SELECTED_CHAT_UID = userObjects.get(i).UID;
                ((Activity) context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userObjects.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_username, tv_lastText,tv_count;
        CircleImageView circleImageView;
        LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_username = itemView.findViewById(R.id.tv_username_chatlist);
            circleImageView = itemView.findViewById(R.id.img_chatlist_profile_photo);
            linearLayout = itemView.findViewById(R.id.linearLayout_chatlist);
            tv_lastText = itemView.findViewById(R.id.tv_lastText_chatlist);
            tv_count = itemView.findViewById(R.id.tv_chatlist_count);
        }
    }
}