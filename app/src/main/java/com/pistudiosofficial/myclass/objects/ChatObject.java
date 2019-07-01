package com.pistudiosofficial.myclass.objects;

public class ChatObject {

    public String message,senderUID,recieverUID,timestamp;

    public ChatObject() {
    }

    public ChatObject(String message, String senderUID, String recieverUID, String timestamp) {
        this.message = message;
        this.senderUID = senderUID;
        this.recieverUID = recieverUID;
        this.timestamp = timestamp;
    }
}
