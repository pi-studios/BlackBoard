package com.pistudiosofficial.myclass.view;

import com.pistudiosofficial.myclass.objects.CommentObject;

import java.util.ArrayList;

public interface CommentView {

    void commmentLoaded(ArrayList<CommentObject> commentObjects);
    void commentNotify();
}
