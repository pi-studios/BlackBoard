package com.pistudiosofficial.myclass;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterPostLoad extends RecyclerView.Adapter<AdapterPostLoad.MyViewHolder> {

    ArrayList<PostObject> postObjectArrayList;

    public AdapterPostLoad(ArrayList<PostObject> postObjectArrayList) {
        this.postObjectArrayList = postObjectArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.post_row,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tv_title.setText(postObjectArrayList.get(i).getCreatorName());
        myViewHolder.tv_creatIon_time.setText(postObjectArrayList.get(i).getCreationDate());
        myViewHolder.tv_post_content.setText(postObjectArrayList.get(i).getBody());
        // Need To add postYpe/photos/comment/like/share
    }

    @Override
    public int getItemCount() {
        return postObjectArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_title,tv_creatIon_time,tv_post_content;
        Button bt_like, bt_comment, bt_share;
        ImageView img_post_icon;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title_post_row);
            tv_creatIon_time = itemView.findViewById(R.id.tv_creation_time_post_row);
            tv_post_content = itemView.findViewById(R.id.tv_post_content_post_row);
            bt_like = itemView.findViewById(R.id.bt_like_post_row);
            bt_comment = itemView.findViewById(R.id.bt_comment_post_row);
            bt_share = itemView.findViewById(R.id.bt_share_post_row);
        }
    }

}
