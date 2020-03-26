package com.pistudiosofficial.myclass.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.adapters.AdapterChatList;
import com.pistudiosofficial.myclass.objects.ChatListMasterObject;
import com.pistudiosofficial.myclass.view.ChatListView;

import java.util.HashMap;

import static com.pistudiosofficial.myclass.Common.CHAT_LIST_HASH_MAP;

public class
ChatListDialogFragment extends DialogFragment implements ChatListView {

    RecyclerView recyclerView;
    LinearLayoutManager llm;
    HashMap<String, ChatListMasterObject> chatHashMap;
    AdapterChatList adapterChatList;
    TextView noFriendsList;

    public ChatListDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ChatListDialogFragment newInstance(String title) {
        ChatListDialogFragment frag = new ChatListDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.chatHashMap = CHAT_LIST_HASH_MAP;
        View view = inflater.inflate(R.layout.activity_chat_list, container);
        adapterChatList = new AdapterChatList(chatHashMap,getContext());
        noFriendsList=view.findViewById(R.id.no_friends_list);
        if(chatHashMap.size()==0) {
            noFriendsList.setVisibility(View.VISIBLE);
            noFriendsList.setText("No Friends!\nCreate new friends first");
            noFriendsList.setGravity(Gravity.CENTER_HORIZONTAL);
        }
        else {
            recyclerView = view.findViewById(R.id.recyclerView_chatList);
            llm = new LinearLayoutManager(getContext());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(llm);
            recyclerView.setAdapter(adapterChatList);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Fetch arguments from bundle and set title
        getDialog().setTitle("Chat List");

    }
    @Override
    public void ChatLoaded(ChatListMasterObject chatListMasterObject) {
        adapterChatList.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, (int) (metrics.heightPixels * 0.30));// here i have fragment height 30% of window's height you can set it as per your requirement
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        getDialog().getWindow().getAttributes().windowAnimations = R.style.Animation_WindowSlideUpDown;

    }
}