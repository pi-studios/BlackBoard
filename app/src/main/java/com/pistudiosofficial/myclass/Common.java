package com.pistudiosofficial.myclass;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pistudiosofficial.myclass.adapters.AdapterClassList;
import com.pistudiosofficial.myclass.objects.ChatListMasterObject;
import com.pistudiosofficial.myclass.objects.ClassObject;
import com.pistudiosofficial.myclass.objects.HelloListObject;
import com.pistudiosofficial.myclass.objects.MasterPostObject;
import com.pistudiosofficial.myclass.objects.PollOptionValueLikeObject;
import com.pistudiosofficial.myclass.objects.PostObject;
import com.pistudiosofficial.myclass.objects.StudentClassObject;
import com.pistudiosofficial.myclass.objects.UserObject;

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
    public static DatabaseReference mREF_chat = null;

    public static ArrayList<ClassObject> CURRENT_ADMIN_CLASS_LIST = null;
    public static ArrayList<String> CURRENT_CLASS_ID_LIST = null;
    public static ArrayList<String> ATTD_PERCENTAGE_LIST = null;
    public static ArrayList<String> TEMP01_LIST = null;
    public static ArrayList<String> ROLL_LIST = null;
    public static ArrayList<Integer> TOTAL_PRESENT_DAYS=null;
    public static ArrayList<StudentClassObject> CURRENT_USER_CLASS_LIST = null;
    public static HashMap<String,String> CURRENT_ADMIN_FEEDBACK_STATUS = null;
    public static ArrayList<String> CURRENT_USER_CLASS_LIST_ID = null;
    public static HashMap<String, HelloListObject> HELLO_REQUEST_USERS = null;
    public static AdapterClassList ADAPTER_CLASS_LIST = null;
    public static long TOTAL_CLASSES=0;
    public static ArrayList<Double> SHOW_ATTENDANCE_PERCENTAGE=null;

    //Comment
    public static DatabaseReference mREF_COMMENT_LOAD = null;
    public static PostObject COMMENT_LOAD_POST_OBJECT = null;
    public static ArrayList<Integer> CHECK_NEW_COMMENT_POST ;
    public static HashMap<String,Boolean> CHECK_NEW_COMMENT ;
    //ResourceBucketActivity Common
    public static DatabaseReference mREF_RESOURCE_BUCKET = null;

    //Profile Related Common
    public static String SELECTED_PROFILE_UID = "";

    //Chat Related Common
    public static String SELECTED_CHAT_UID = "";
    public static HashMap<String,ChatListMasterObject> CHAT_LIST_HASH_MAP = null;

    public static SharedPreferences SHARED_PREFERENCES;

    public static int CURRENT_INDEX = 0;

    public static void LOG(){
        Log.i("TAG","ACCEPTED");
    }

}
