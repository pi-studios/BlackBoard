package com.pistudiosofficial.myclass.adapters;

import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.activities.ProfileNewActivity;
import com.pistudiosofficial.myclass.objects.UserObject;

import java.util.ArrayList;

import static com.pistudiosofficial.myclass.Common.SELECTED_USER_PROFILE;


public class AdapterConnectionList extends RecyclerView.Adapter<AdapterConnectionList.MyViewHolder> {

    ArrayList<UserObject> userObjectsList;
    Dialog dialog;

    public AdapterConnectionList(ArrayList<UserObject> userObjectArrayList, Dialog dialog) {
        this.userObjectsList = userObjectArrayList;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.connection_list_row,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.textView.setText(userObjectsList.get(i).Name);
        myViewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(view.getContext(), ProfileNewActivity.class);
                SELECTED_USER_PROFILE = userObjectsList.get(i);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userObjectsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_connection_row);
        }
    }

}
