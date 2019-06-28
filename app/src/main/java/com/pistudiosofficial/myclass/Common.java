package com.pistudiosofficial.myclass;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class Common {

    public static UserObject CURRENT_USER = null;
    public static UserObject SELECTED_USER_PROFILE = null;

    public static FirebaseAuth mAUTH  = null;
    public static FirebaseUser FIREBASE_USER = null;
    public static FirebaseDatabase FIREBASE_DATABASE = null;

    public static DatabaseReference mREF_users = null;
    public static DatabaseReference mREF_connections = null;
    public static DatabaseReference mREF_classList = null;
    public static DatabaseReference mREF_oldRecords = null;
    public static DatabaseReference mREF_student_classList = null;
    public static DatabaseReference mREF_admin_classList = null;

    public static ArrayList<ClassObject> CURRENT_ADMIN_CLASS_LIST = null;
    public static ArrayList<String> CURRENT_CLASS_ID_LIST = null;
    public static ArrayList<String> ATTD_PERCENTAGE_LIST = null;
    public static ArrayList<String> TEMP01_LIST = null;
    public static ArrayList<String> ROLL_LIST = null;
    public static ArrayList<StudentClassObject> CURRENT_USER_CLASS_LIST = null;
    public static ArrayList<String> CURRENT_USER_CLASS_LIST_ID = null;

    //Post Related Common
    public static ArrayList<PostObject> POST_OBJECT_LIST = null;
    public static ArrayList<String> POST_OBJECT_ID_LIST = null;
    public static HashMap<String,PollOptionValueLikeObject> POST_POLL_OPTIONS = null;
    public static ArrayList<String> POST_LIKE_LIST = null;
    public static HashMap<String, ArrayList<String>> POST_URL_LIST = null;

    public static SharedPreferences SHARED_PREFERENCES;

    public static int CURRENT_INDEX = 0;

    public static void LOG(){
        Log.i("TAG","ACCEPTED");
    }

}
