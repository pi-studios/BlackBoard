package com.pistudiosofficial.myclass.objects;

import android.graphics.Bitmap;
import java.util.ArrayList;

public class PostObject {
    private String creatorName, creationDate, body, postType;
    private ArrayList<Comments> comments;

    public PostObject() {
    }

    public PostObject(String creatorName, String creationDate, String body,
                      ArrayList<Comments> comments, String postType
                    ) {
        this.creatorName = creatorName;
        this.creationDate = creationDate;
        this.body = body;
        this.comments = comments;
        this.postType = postType;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ArrayList<Comments> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comments> comments) {
        this.comments = comments;
    }

    private class Comments{
        private String commentBody, commenterName;

        public Comments(String commentBody, String commenterName) {
            this.commentBody = commentBody;
            this.commenterName = commenterName;
        }

        public Comments() {
        }

        public String getCommentBody() {
            return commentBody;
        }

        public void setCommentBody(String commentBody) {
            this.commentBody = commentBody;
        }

        public String getCommenterName() {
            return commenterName;
        }

        public void setCommenterName(String commenterName) {
            this.commenterName = commenterName;
        }
    }
}
