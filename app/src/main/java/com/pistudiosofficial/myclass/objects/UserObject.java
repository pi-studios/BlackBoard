package com.pistudiosofficial.myclass.objects;

public class UserObject {

    public String Email;
    public String UID;
    public String Phone;
    public String Name;
    public String AdminLevel;
    public String Roll;
    public String Bio;
    public String profilePicLink;

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
