package com.pistudiosofficial.myclass.objects;

import android.graphics.Bitmap;
import java.util.ArrayList;

public class PostObject {
    private String creatorName, creationDate, body, postType, creatorUID,creatorProPickLink, postID;

    public PostObject() {
    }

    public String getCreatorUID() {
        return creatorUID;
    }

    public void setCreatorUID(String creatorUID) {
        this.creatorUID = creatorUID;
    }

    public String getCreatorProPickLink() {
        return creatorProPickLink;
    }

    public void setCreatorProPickLink(String creatorProPickLink) {
        this.creatorProPickLink = creatorProPickLink;
    }

    public PostObject(String creatorName, String creationDate, String body,
                      String postType, String creatorUID, String creatorProPickLink, String postID
                    ) {
        this.postID = postID;
        this.creatorName = creatorName;
        this.creationDate = creationDate;
        this.body = body;
        this.postType = postType;
        this.creatorProPickLink = creatorProPickLink;
        this.creatorUID = creatorUID;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
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
