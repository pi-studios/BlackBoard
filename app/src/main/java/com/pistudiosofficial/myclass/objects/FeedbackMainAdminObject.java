package com.pistudiosofficial.myclass.objects;

public class FeedbackMainAdminObject {

    public String senderName,dateString,session,className,nodeKey;

    public FeedbackMainAdminObject(String senderName, String dateString, String session, String className) {
        this.senderName = senderName;
        this.dateString = dateString;
        this.session = session;
        this.className = className;
    }

    public FeedbackMainAdminObject() {
    }
}
