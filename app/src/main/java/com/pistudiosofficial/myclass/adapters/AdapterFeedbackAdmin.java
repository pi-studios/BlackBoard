package com.pistudiosofficial.myclass.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.model.FeedbackHODModel;
import com.pistudiosofficial.myclass.objects.FeedbackMainAdminObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.pistudiosofficial.myclass.Common.CURRENT_USER;

public class AdapterFeedbackAdmin extends RecyclerView.Adapter<AdapterFeedbackAdmin.MyViewHolder> {

    ArrayList<FeedbackMainAdminObject> objects;
    Context context;

    public AdapterFeedbackAdmin(ArrayList<FeedbackMainAdminObject> objects,Context context) {
        this.objects = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.admin_feedback_row,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.className.setText(objects.get(i).className);
        myViewHolder.dateString.setText(objects.get(i).dateString);
        myViewHolder.senderName.setText(objects.get(i).senderName);
        myViewHolder.session.setText(objects.get(i).session);
        myViewHolder.feedbackDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FeedbackHODModel feedbackHODModel = new FeedbackHODModel();
                feedbackHODModel.performFeedbackDownload(CURRENT_USER.UID,objects.get(i).nodeKey,objects.get(i).className);
                Toast.makeText(context,"Exported",Toast.LENGTH_SHORT).show();
                myViewHolder.feedbackDownload.setEnabled(false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView senderName,dateString,session,className;
        ImageButton feedbackDownload;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            senderName = itemView.findViewById(R.id.bt_admin_feedback_senderName);
            dateString = itemView.findViewById(R.id.tv_admin_feedback_date);
            session = itemView.findViewById(R.id.tv_admin_feedback_session);
            className = itemView.findViewById(R.id.tv_admin_feedback_className);
            feedbackDownload = itemView.findViewById(R.id.bt_admin_feedback_download);
        }
    }

}