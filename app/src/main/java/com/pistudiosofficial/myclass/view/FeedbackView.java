package com.pistudiosofficial.myclass.view;

import java.util.ArrayList;

public interface FeedbackView {

    void loadSuccess(ArrayList<String> question);

    void feedbackSubmitSuccess();
    void feedbackSubmitFailed();

    void feedbackDontExist();

}
