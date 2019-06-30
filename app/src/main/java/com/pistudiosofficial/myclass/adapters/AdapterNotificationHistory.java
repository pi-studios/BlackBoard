package com.pistudiosofficial.myclass.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pistudiosofficial.myclass.R;

import java.util.ArrayList;


public class AdapterNotificationHistory extends RecyclerView.Adapter<AdapterNotificationHistory.MyViewHolder> {

    ArrayList<String> title,body;

    public AdapterNotificationHistory(ArrayList<String> title, ArrayList<String> body) {
        this.title = title;
        this.body = body;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.notification_history_row,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.textViewTitle.setText(title.get(i));
        myViewHolder.textViewBody.setText(body.get(i));
    }

    @Override
    public int getItemCount() {
        return title.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle,textViewBody;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tv_notification_title_row);
            textViewBody = itemView.findViewById(R.id.tv_notification_body_row);
        }
    }

}
