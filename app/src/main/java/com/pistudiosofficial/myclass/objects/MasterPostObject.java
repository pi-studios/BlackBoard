package com.pistudiosofficial.myclass.objects;

import java.util.ArrayList;
import java.util.HashMap;

public class MasterPostObject {
    public ArrayList<PostObject> POST_OBJECT_LIST ;
    public HashMap<String, PollOptionValueLikeObject> POST_POLL_OPTIONS ;
    public ArrayList<String> POST_LIKE_LIST ;
    public HashMap<String, ArrayList<String>> POST_URL_LIST ;

    public MasterPostObject(ArrayList<PostObject> POST_OBJECT_LIST, HashMap<String,
                            PollOptionValueLikeObject> POST_POLL_OPTIONS,
                            ArrayList<String> POST_LIKE_LIST,
                            HashMap<String, ArrayList<String>> POST_URL_LIST) {
        this.POST_OBJECT_LIST = POST_OBJECT_LIST;
        this.POST_POLL_OPTIONS = POST_POLL_OPTIONS;
        this.POST_LIKE_LIST = POST_LIKE_LIST;
        this.POST_URL_LIST = POST_URL_LIST;
    }

    public MasterPostObject() {
    }
}
