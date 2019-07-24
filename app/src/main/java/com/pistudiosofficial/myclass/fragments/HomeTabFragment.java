package com.pistudiosofficial.myclass.fragments;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import androidx.appcompat.widget.AppCompatImageButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.activities.ProfileNewActivity;
import com.pistudiosofficial.myclass.adapters.AdapterHomeFeed;
import com.pistudiosofficial.myclass.model.LiveMainModel;
import com.pistudiosofficial.myclass.objects.PollOptionValueLikeObject;
import com.pistudiosofficial.myclass.objects.PostObject;
import com.pistudiosofficial.myclass.objects.UserObject;
import com.pistudiosofficial.myclass.view.HomeView;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pistudiosofficial.myclass.Common.CURRENT_CLASS_ID_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_USER;
import static com.pistudiosofficial.myclass.Common.LOG;
import static com.pistudiosofficial.myclass.Common.SELECTED_PROFILE_UID;
import static com.pistudiosofficial.myclass.Common.mREF_users;

public class HomeTabFragment extends Fragment implements HomeView {

    AppCompatImageButton bt_search;
    EditText et_search;
    RecyclerView recyclerViewSearch;
    DatabaseReference mUserRef;
    AdapterHomeFeed adapterHomeFeed;
    public HomeTabFragment() {
    }

    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home,container,false);
        bt_search = v.findViewById(R.id.bt_search_main);
        et_search = v.findViewById(R.id.et_search_main);
        recyclerViewSearch = v.findViewById(R.id.recycler_view_main_search);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewSearch.getContext(),
                llm.getOrientation());
        recyclerViewSearch.addItemDecoration(dividerItemDecoration);
        recyclerViewSearch.setLayoutManager(llm);
        loadFeed();
        mUserRef = FirebaseDatabase.getInstance().getReference("users");
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!et_search.getText().toString().isEmpty() && !et_search.getText().toString().equals("")){
                    recyclerViewSearch.setVisibility(View.VISIBLE);
                    firebaseUserSearch(et_search.getText().toString().toLowerCase());
                }
            }
        });
        bt_search.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_search.setText("");
                recyclerViewSearch.setAdapter(adapterHomeFeed);
                et_search.clearFocus();
                return true;
            }
        });
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
        et_search.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == keyEvent.ACTION_DOWN
                        || keyEvent.getAction() == keyEvent.KEYCODE_ENTER) {
                    recyclerViewSearch.setVisibility(View.VISIBLE);
                    firebaseUserSearch(et_search.getText().toString().toLowerCase());
                    et_search.clearFocus();
                    return true;

                }
                return false;
            }
        });
        return v;
    }

    private void firebaseUserSearch(String searchText) {
        Query firebaseSearchQuery = mREF_users.orderByChild("lower_name").startAt(searchText).endAt(searchText+"\uf8ff");
        FirebaseRecyclerOptions<UserObject> options =
                new FirebaseRecyclerOptions.Builder<UserObject>()
                        .setQuery(firebaseSearchQuery, UserObject.class)
                        .build();
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<UserObject, UserViewHolder>(options) {
            @Override
            public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.search_main_row, parent, false);

                return new UserViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(UserViewHolder holder, int position, UserObject model) {

                if (model.Name != null) {
                    holder.tv_name.setText(model.Name);
                    holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SELECTED_PROFILE_UID = model.UID;
                            Intent intent = new Intent(getContext(), ProfileNewActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    });
                }
                if (model.Bio != null) {
                    holder.tv_bio.setText(model.Bio);
                }
                if (model.profilePicLink != null) {
                    Glide.with(getContext()).load(model.profilePicLink).into(holder.circleImageView);
                }
                if (model.UID.equals(CURRENT_USER.UID)){
                    holder.linearLayout.setVisibility(View.GONE);
                }
            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerViewSearch.setAdapter(adapter);

    }
    public class UserViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name, tv_bio;
        CircleImageView circleImageView;
        LinearLayout linearLayout;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_username_search);
            tv_bio = itemView.findViewById(R.id.tv_bio_search);
            circleImageView = itemView.findViewById(R.id.profile_image_search);
            linearLayout = itemView.findViewById(R.id.linearLayout_search_main);
        }
    }

    private void loadFeed(){
        LiveMainModel liveMainModel = new LiveMainModel(this);
        liveMainModel.performfeedload(CURRENT_CLASS_ID_LIST);
    }

    @Override
    public void loadFeedSuccess(ArrayList<PostObject> postObjects, HashMap<String,
            PollOptionValueLikeObject> post_poll_option,
                                ArrayList<String> post_like_list,
                                HashMap<String, ArrayList<String>> post_url_list,
                                ArrayList<String> comment_count,
                                HashMap<String, String> postClassID,ArrayList<String> likedPostID,
                                HashMap<String,String> pollSelectPostID) {
                adapterHomeFeed = new AdapterHomeFeed(postObjects,post_poll_option,
                post_like_list,post_url_list,comment_count,getContext(),postClassID,likedPostID,pollSelectPostID);
        recyclerViewSearch.setAdapter(adapterHomeFeed);
    }
}
