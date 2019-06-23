package com.pistudiosofficial.myclass;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pistudiosofficial.myclass.model.PostInteractionModel;

import java.util.ArrayList;

public class AdapterPostLoad extends RecyclerView.Adapter<AdapterPostLoad.MyViewHolder> {

    ArrayList<PostObject> postObjectArrayList;
    PostInteractionModel model;
    Context context;
    public AdapterPostLoad(ArrayList<PostObject> postObjectArrayList,Context context) {
        this.postObjectArrayList = postObjectArrayList;
        this.context = context;
        model = new PostInteractionModel();
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
        myViewHolder.bt_like.setText("Like:"+Common.POST_LIKE_LIST.get(i));
        myViewHolder.bt_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.likeClicked(Common.POST_OBJECT_ID_LIST.get(i),Common.CURRENT_CLASS_ID_LIST.get(Common.CURRENT_INDEX));
            }
        });
        if (postObjectArrayList.get(i).getPostType().equals("admin_poll")){
            PollOptionValueLikeObject obj = Common.POST_POLL_OPTIONS.get(Common.POST_OBJECT_ID_LIST.get(i));
            for (int j=0;j<obj.optionList.size();j++){
                    Button tv = new Button(context);
                    tv.setId(j);
                    if(Common.CURRENT_USER.AdminLevel.equals("admin")){
                        tv.setText(obj.optionList.get(j)+" : "+obj.votesCountList.get(j));
                    }else {
                        tv.setText(obj.optionList.get(j));
                    }
                    tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    tv.setTextSize(16);
                    tv.setPadding(5, 3, 0, 3);
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String option = tv.getText().toString();
                            if(Common.CURRENT_USER.AdminLevel.equals("user")){
                                model.pollClicked(option,Common.CURRENT_CLASS_ID_LIST.get(Common.CURRENT_INDEX),
                                        Common.POST_OBJECT_ID_LIST.get(i));
                            }
                        }
                    });
                    myViewHolder.listView.addView(tv);
                }
        }
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
        LinearLayout listView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title_post_row);
            tv_creatIon_time = itemView.findViewById(R.id.tv_creation_time_post_row);
            tv_post_content = itemView.findViewById(R.id.tv_post_content_post_row);
            bt_like = itemView.findViewById(R.id.bt_like_post_row);
            bt_comment = itemView.findViewById(R.id.bt_comment_post_row);
            bt_share = itemView.findViewById(R.id.bt_share_post_row);
            listView = itemView.findViewById(R.id.linearLayout_post_row);
        }
    }

}
