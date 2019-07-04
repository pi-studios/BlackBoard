package com.pistudiosofficial.myclass.objects;

public class CommentObject {
    public String userName,userUID,timestamp,profile_pic_link, comment;

    public CommentObject(String userName, String userUID, String timestamp, String profile_pic_link, String comment) {
        this.userName = userName;
        this.userUID = userUID;
        this.timestamp = timestamp;
        this.profile_pic_link = profile_pic_link;
        this.comment = comment;
    }

    public CommentObject() {
    }
}
