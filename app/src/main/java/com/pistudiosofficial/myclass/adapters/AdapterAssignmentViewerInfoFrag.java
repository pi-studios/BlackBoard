package com.pistudiosofficial.myclass.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pistudiosofficial.myclass.PhotoFullPopupWindow;
import com.pistudiosofficial.myclass.R;
import com.pistudiosofficial.myclass.activities.AssignmentViewerActivity;
import com.pistudiosofficial.myclass.objects.AssignmentObject;
import com.pistudiosofficial.myclass.objects.AssignmentSubmissionObject;
import com.pistudiosofficial.myclass.objects.ResourceBucketObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static com.pistudiosofficial.myclass.Common.CURRENT_USER;

public class AdapterAssignmentViewerInfoFrag extends BaseAdapter {
    private LayoutInflater layoutinflater;
    private HashMap<String, String> meta_data;
    private ArrayList<String> links;
    private ArrayList<String> name_meta_data;
    private Context context;
    private ArrayList<AssignmentSubmissionObject> assignmentSubmissionObjects;

    public AdapterAssignmentViewerInfoFrag(HashMap<String, String> meta_data, ArrayList<String> name_date, Context context) {
        this.meta_data = meta_data;
        this.context = context;
        this.name_meta_data = name_date;
        layoutinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        links = new ArrayList<>();
        for (String s: meta_data.keySet()){links.add(s);}
    }

    public AdapterAssignmentViewerInfoFrag(ArrayList<AssignmentSubmissionObject> assignmentSubmissionObjects,Context context) {
        this.context = context;
        this.assignmentSubmissionObjects = assignmentSubmissionObjects;
        layoutinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        if (assignmentSubmissionObjects == null) {
            return meta_data.size();
        }
        else{
            return assignmentSubmissionObjects.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder listViewHolder;
        if (convertView == null) {
            listViewHolder = new ViewHolder();
            convertView = layoutinflater.inflate(R.layout.resource_bucket_grid_row, parent, false);
            listViewHolder.textInListView = convertView.findViewById(R.id.tv_grid_view_bucket);
            listViewHolder.imageInListView = convertView.findViewById(R.id.img_grid_view_bucket);
            convertView.setTag(listViewHolder);
        } else {
            listViewHolder = (ViewHolder) convertView.getTag();
        }
        if (assignmentSubmissionObjects == null) {
            listViewHolder.textInListView.setText(name_meta_data.get(position));
            if (meta_data.get(links.get(position)).equals("jpg")) {
                Glide.with(context).load(links.get(position)).into(listViewHolder.imageInListView);
            }
            if (meta_data.get(links.get(position)).equals("pdf")) {
                listViewHolder.imageInListView.setImageResource(R.drawable.pdf_icon);
            }
        }
        else{
            listViewHolder.textInListView.setText(assignmentSubmissionObjects.get(position).timestamp);
            if (assignmentSubmissionObjects.get(position).type.equals("jpg")) {
                Glide.with(context).load(assignmentSubmissionObjects.get(position).link).into(listViewHolder.imageInListView);
            }
            if (assignmentSubmissionObjects.get(position).type.equals("pdf")) {
                listViewHolder.imageInListView.setImageResource(R.drawable.pdf_icon);
            }
        }
        listViewHolder.imageInListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (assignmentSubmissionObjects == null) {
                    if (meta_data.get(links.get(position)).equals("jpg")) {
                        String url = links.get(position);
                        new PhotoFullPopupWindow(context, R.layout.popup_photo_full, v, url, null);
                    }
                    else{
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                        browserIntent.setDataAndType(Uri.parse(links.get(position)),"application/pdf");

                        Intent chooser = Intent.createChooser(browserIntent, "Pdf Viewer");
                        chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // optional

                        context.startActivity(chooser);
                    }
                }
                else{
                    if (assignmentSubmissionObjects.get(position).type.equals("jpg")) {
                        String url = assignmentSubmissionObjects.get(position).link;
                        new PhotoFullPopupWindow(context, R.layout.popup_photo_full, v, url, null);
                    }
                    else{
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                        browserIntent.setDataAndType(Uri.parse(assignmentSubmissionObjects.get(position).link),"application/pdf");

                        Intent chooser = Intent.createChooser(browserIntent, "Pdf Viewer");
                        chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // optional

                        context.startActivity(chooser);
                    }
                }
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView textInListView;
        ImageView imageInListView;
    }
}
