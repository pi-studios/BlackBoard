package com.pistudiosofficial.myclass.view;

import com.pistudiosofficial.myclass.objects.PollOptionValueLikeObject;
import com.pistudiosofficial.myclass.objects.PostObject;

import java.util.ArrayList;
import java.util.HashMap;

public interface HomeView {
    void loadFeedSuccess(ArrayList<PostObject> postObjects ,
                         HashMap<String, PollOptionValueLikeObject> post_poll_option ,
                         ArrayList<String> post_like_list ,
                         HashMap<String, ArrayList<String>> post_url_list, ArrayList<String> comment_count,
                         HashMap<String,String> postClassID);
}
