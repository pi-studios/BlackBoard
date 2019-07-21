package com.pistudiosofficial.myclass.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.objects.ChatObject;
import com.pistudiosofficial.myclass.objects.UserObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.pistudiosofficial.myclass.Common.CURRENT_USER;

public class AdapterChat extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<ChatObject> chatObjects;
    final int RMSG = 0, SMSG = 1;
    UserObject oppositeUser;
    public AdapterChat(ArrayList<ChatObject> chatObjects, UserObject userObject) {
        this.chatObjects = chatObjects;
        this.oppositeUser = userObject;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final RecyclerView.ViewHolder holder;
        View view;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (i) {
            case RMSG:
                view = inflater.inflate(R.layout.item_message_received, viewGroup, false);
                holder = new RecieveViewHolder(view);
                break;
            case SMSG:
                view = inflater.inflate(R.layout.item_message_sent, viewGroup, false);
                holder = new SentViewHolder(view);
                break;
            default:
                view = inflater.inflate(R.layout.item_message_sent, viewGroup, false);
                holder = new SentViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Date date = null;
        String newstr = "";
        try {
            date = new SimpleDateFormat("h:mm a MMM d, ''yy").parse(chatObjects.get(i).timestamp);
            newstr = new SimpleDateFormat("dd/MM, H:mm").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (viewHolder instanceof SentViewHolder) {
            ((SentViewHolder) viewHolder).sText.setText(chatObjects.get(i).message);
            ((SentViewHolder) viewHolder).sTime.setText(newstr);
        } else {
            ((RecieveViewHolder) viewHolder).rText.setText(chatObjects.get(i).message);
            ((RecieveViewHolder) viewHolder).rTime.setText(newstr);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (chatObjects.get(position).senderUID.equals(CURRENT_USER.UID)) {
            return SMSG;
        } else {
            return RMSG;
        }
    }

    @Override
    public int getItemCount() {
        return chatObjects.size();
    }

    public class SentViewHolder extends RecyclerView.ViewHolder {

        TextView sText, sTime;

        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            sText = itemView.findViewById(R.id.tv_body_sent);
            sTime = itemView.findViewById(R.id.tv_time_sent);
        }
    }

    public class RecieveViewHolder extends RecyclerView.ViewHolder {

        TextView rText, rTime;

        public RecieveViewHolder(@NonNull View itemView) {
            super(itemView);
            rText = itemView.findViewById(R.id.tv_body_recieved);
            rTime = itemView.findViewById(R.id.tv_time_recieved);
        }
    }
}