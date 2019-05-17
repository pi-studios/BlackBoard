package com.pistudiosofficial.myclass;

public class UserObject {

    public String Email;
    public String UID;
    public String Phone;
    public String Name;
    public String AdminLevel;
    public String Roll;

    public UserObject(String email, String phone, String name, String adminLevel, String roll) {
        Email = email;
        Phone = phone;
        Name = name;
        AdminLevel = adminLevel;
        Roll = roll;
    }

    public UserObject() {
    }
}
