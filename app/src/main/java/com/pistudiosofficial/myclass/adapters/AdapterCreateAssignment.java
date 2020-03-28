package com.pistudiosofficial.myclass.adapters;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pistudiosofficial.myclass.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class AdapterCreateAssignment extends RecyclerView.Adapter<AdapterCreateAssignment.MyViewHolder> {

    ArrayList<Uri> uriArrayList;
    Context context;
    public AdapterCreateAssignment(ArrayList<Uri> uris, Context context) {
        this.uriArrayList = uris;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.assignment_create_row,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        ContentResolver cR = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = mime.getExtensionFromMimeType(cR.getType(uriArrayList.get(i)));
        if (type.equals("pdf")){
            myViewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.pdf_icon));
        }else{
            Picasso.with(context).load(uriArrayList.get(i)).into(myViewHolder.imageView);
        }
        myViewHolder.textView.setText(fileName(uriArrayList.get(i)));
    }

    @Override
    public int getItemCount() {
        return uriArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_create_assignment_row);
            imageView = itemView.findViewById(R.id.iv_create_assignment_row);
        }
    }

    String fileName(Uri data){
        String fileName = "";
        if (data.getScheme().equals("file")) {
            fileName = data.getLastPathSegment();
        } else {
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(data, new String[]{
                        MediaStore.Images.ImageColumns.DISPLAY_NAME
                }, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME));
                }
            } finally {

                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return fileName;
    }

}
