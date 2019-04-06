package com.pistudiosofficial.myclass;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Common {

    public static boolean LOGGED_IN = false;
    public static UserObject CURRENT_USER = null;
    public static FirebaseAuth mAUTH  = null;
    public static FirebaseUser FIREBASE_USER = null;
    public static FirebaseDatabase FIREBASE_DATABASE = null;
    public static DatabaseReference mREF_users = null;



}
