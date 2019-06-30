package com.pistudiosofficial.myclass.objects;

public class NotificationStoreObj{
    public String Title;
    public String Body;
    public String dateCreated;

    public NotificationStoreObj(String title, String body,String dateCreated) {
        Title = title;
        Body = body;
        this.dateCreated = dateCreated;
    }

    public NotificationStoreObj() {
    }
}