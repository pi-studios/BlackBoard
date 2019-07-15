package com.pistudiosofficial.myclass.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pistudiosofficial.myclass.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterFeedbackUser extends RecyclerView.Adapter<AdapterFeedbackUser.MyViewHolder> {

    ArrayList<String> question;
    HashMap<String,String> rating;
    public AdapterFeedbackUser(ArrayList<String> question,HashMap<String,String> rating) {
        this.question = question;
        this.rating = rating;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.feedback_row,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.textView.setText(question.get(i));
        myViewHolder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int j) {
                int id = radioGroup.getCheckedRadioButtonId();
                switch (id){
                    case R.id.rb_feedback_row_0:
                        rating.put(question.get(i),"0");
                        break;
                    case R.id.rb_feedback_row_n1:
                        rating.put(question.get(i),"-1");
                        break;
                    case R.id.rb_feedback_row_n2:
                        rating.put(question.get(i),"-2");
                        break;
                    case R.id.rb_feedback_row_p1:
                        rating.put(question.get(i),"1");
                        break;
                    case R.id.rb_feedback_row_p2:
                        rating.put(question.get(i),"2");
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return question.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        RadioGroup radioGroup;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            radioGroup = itemView.findViewById(R.id.rg_feedbackRow);
            textView = itemView.findViewById(R.id.tv_feedback_row);
        }
    }

}