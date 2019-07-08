package com.pistudiosofficial.myclass.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.adapters.AdapterChatList;
import com.pistudiosofficial.myclass.adapters.AdapterComment;
import com.pistudiosofficial.myclass.model.CommentModel;
import com.pistudiosofficial.myclass.objects.CommentObject;
import com.pistudiosofficial.myclass.view.CommentView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.pistudiosofficial.myclass.Common.COMMENT_LOAD_POST_OBJECT;
import static com.pistudiosofficial.myclass.Common.CURRENT_USER;
import static com.pistudiosofficial.myclass.Common.mREF_COMMENT_LOAD;

public class CommentActivity extends AppCompatActivity implements CommentView {

    RecyclerView recyclerView;
    ArrayList<CommentObject> commentObjects;
    CommentModel model;
    AdapterComment adapterComment;
    LinearLayoutManager llm;
    EditText et_comment;
    ImageView bt_sendComment, profile_pic;
    TextView tv_title, tv_body, tv_time;
    Button bt_like;
    String postid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        postid = getIntent().getStringExtra("post_id");
        setTitle("Comment");
        et_comment = findViewById(R.id.et_commetInput_comment);
        bt_sendComment = findViewById(R.id.bt_commentSend_comment);
        bt_sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!et_comment.getText().toString().isEmpty() || !et_comment.getText().toString().equals("")){
                    CommentObject commentObject = new CommentObject(
                            CURRENT_USER.Name,CURRENT_USER.UID,new SimpleDateFormat("h:mm a MMM d, ''yy").format(new Date()),
                            CURRENT_USER.profilePicLink, et_comment.getText().toString()
                    );
                    et_comment.setText("");
                    model.performCommentPost(commentObject,mREF_COMMENT_LOAD,postid);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        model.removeListener();
        model = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        model = new CommentModel(this);
        commentObjects = new ArrayList<>();
        model.performCommentLoad(mREF_COMMENT_LOAD,COMMENT_LOAD_POST_OBJECT,commentObjects);
        model.performRemoveReadComment(COMMENT_LOAD_POST_OBJECT.getPostID());
    }

    @SuppressLint("WrongConstant")
    @Override
    public void commmentLoaded(ArrayList<CommentObject> commentObjects) {
        adapterComment = new AdapterComment(commentObjects,this);
        recyclerView = findViewById(R.id.recyclerView_commentList_comment);
        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapterComment);
        tv_title = findViewById(R.id.tv_title_post_row);
        tv_body = findViewById(R.id.tv_post_content_post_row);
        tv_time = findViewById(R.id.tv_creation_time_post_row);
        profile_pic = findViewById(R.id.img_post_icon_post_row);
        bt_like = findViewById(R.id.bt_like_post_row);

        tv_time.setText(COMMENT_LOAD_POST_OBJECT.getCreationDate());
        tv_body.setText(COMMENT_LOAD_POST_OBJECT.getBody());
        tv_title.setText(COMMENT_LOAD_POST_OBJECT.getCreatorName());
        if (COMMENT_LOAD_POST_OBJECT.getCreatorProPickLink() != null){
            Glide.with(this).load(COMMENT_LOAD_POST_OBJECT.getCreatorProPickLink()).into(profile_pic);
        }
    }

    @Override
    public void commentNotify() {
        adapterComment.notifyDataSetChanged();
    }
}
