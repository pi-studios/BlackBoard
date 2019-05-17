package com.pistudiosofficial.myclass;

public class ConnectionObject {
    String master_admin;
    String admin;
    String user;

    public ConnectionObject(String master_admin, String admin, String user) {
        this.master_admin = master_admin;
        this.admin = admin;
        this.user = user;
    }

    public ConnectionObject() {
    }
}
