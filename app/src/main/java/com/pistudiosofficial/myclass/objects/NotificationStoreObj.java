package com.pistudiosofficial.myclass.objects;

public class NotificationStoreObj{
    public String Title;
    public String Body;
    public String dateCreated;
    public String classID;

    public NotificationStoreObj(String title, String body,String dateCreated, String classID) {
        Title = title;
        Body = body;
        this.dateCreated = dateCreated;
        this.classID = classID;
    }

    public NotificationStoreObj() {
    }
}