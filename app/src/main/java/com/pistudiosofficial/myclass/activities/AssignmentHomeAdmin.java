package com.pistudiosofficial.myclass.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.objects.AssignmentObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.pistudiosofficial.myclass.Common.CURRENT_CLASS_ID_LIST;
import static com.pistudiosofficial.myclass.Common.CURRENT_INDEX;
import static com.pistudiosofficial.myclass.Common.mREF_classList;

public class AssignmentHomeAdmin extends AppCompatActivity {

    FloatingActionButton fab_new_assignmnents;
    RecyclerView assignmemt_RecyclerView;
    LinearLayoutManager layoutManager;
    RecyclerView.Adapter mAdapter;
    ArrayList<AssignmentObject> myDataset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myDataset=downloadData();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_home_admin);
        assignmemt_RecyclerView=findViewById(R.id.rv_assignmentHome_admin);
        fab_new_assignmnents = findViewById(R.id.fab_new_assignments_admin);
        fab_new_assignmnents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AssignmentCreationActivity.class));
            }
        });
        layoutManager = new LinearLayoutManager(this);
        assignmemt_RecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyAdapter(myDataset);
        assignmemt_RecyclerView.setAdapter(mAdapter);


    }
    public ArrayList<AssignmentObject> downloadData(){
        DatabaseReference databaseReference = mREF_classList.child(CURRENT_CLASS_ID_LIST.get(CURRENT_INDEX)).child("assignment_online");
        ArrayList<AssignmentObject> list= new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dpShot:dataSnapshot.getChildren()){
                    list.add(new AssignmentObject(dpShot.child("description").getValue().toString(),dpShot.child("due_date").getValue().toString(),null,dpShot.child("title").getValue().toString()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return list;
    }
    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private ArrayList<AssignmentObject> mDataSet;
        public static class MyViewHolder extends RecyclerView.ViewHolder {
            TextView assignmentDesc,dueDate;
            public MyViewHolder(View v) {
                super(v);
                this.assignmentDesc= v.findViewById(R.id.assignment_title_row);
                this.dueDate=v.findViewById(R.id.assignment_due_date);
            }
        }

        MyAdapter(ArrayList<AssignmentObject> myDataSet) {
            mDataSet = myDataSet;
        }

        @NotNull
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
            LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
            View v=layoutInflater.inflate(R.layout.assignment_row,parent,false);
            return new MyViewHolder(v);
        }
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.assignmentDesc.setText(mDataSet.get(position).getTitle());
            holder.dueDate.setText(mDataSet.get(position).getDueDate());
        }
        @Override
        public int getItemCount() {
            return mDataSet.size();
        }
    }
}
