package com.pistudiosofficial.myclass.presenter;

import com.pistudiosofficial.myclass.model.CreatePollModel;
import com.pistudiosofficial.myclass.presenter.presenter_interfaces.CreatePollPresenterInterface;
import com.pistudiosofficial.myclass.view.CreatePollView;

import java.util.ArrayList;

public class CreatePollPresenter implements CreatePollPresenterInterface {
    CreatePollView view;
    CreatePollModel model;
    public CreatePollPresenter(CreatePollView view) {
        this.view = view;
        model = new CreatePollModel(this);
    }

    public void performCreatePoll(String title, ArrayList<String> optionList){
        model.performCreatePoll(title,optionList);
    }


    @Override
    public void createPollSuccess() {
        view.createPollSuccess();
    }

    @Override
    public void createPollFailed() {
        view.createPollFailed();
    }
}
