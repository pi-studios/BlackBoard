package com.pistudiosofficial.myclass.adapters;

import android.graphics.Color;

import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pistudiosofficial.myclass.R;

import static com.pistudiosofficial.myclass.Common.ROLL_LIST;
import static com.pistudiosofficial.myclass.Common.TEMP01_LIST;

public class AdapterNewAttendenceList extends RecyclerView.Adapter<AdapterNewAttendenceList.MyViewHolder> {

    OnItemListener onItemListener;
    public AdapterNewAttendenceList(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.new_attendence_row,viewGroup,false);
        return new MyViewHolder(view,onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i){
        myViewHolder.textView.setText(ROLL_LIST.get(i));

        if(TEMP01_LIST.get(i).equals("PRESENT")){
            //Change the color to red and green as absent and present
            myViewHolder.tv_name.setText("PRESENT");
            myViewHolder.textView.getBackground().setColorFilter(Color.parseColor("#13800B"), PorterDuff.Mode.SRC_ATOP);
//            myViewHolder.textView.setBackgroundColor(Color.parseColor("#13800B"));
        }else {
            myViewHolder.tv_name.setText("ABSENT");
            myViewHolder.textView.getBackground().setColorFilter(Color.parseColor("#CE1212"), PorterDuff.Mode.SRC_ATOP);
        }
    }

    @Override
    public int getItemCount() {
        return ROLL_LIST.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView,tv_name;
        OnItemListener onItemListener;

        public MyViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_new_attendance);
            tv_name = itemView.findViewById(R.id.tv_new_attendance_name);
            this.onItemListener= onItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemListener{
        void onItemClick(int position);
    }

}
