package com.venkibellu.myapplication;

import android.app.Activity;

import android.content.Context;

import android.net.Uri;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;




public class News_Adapter extends BaseAdapter implements ListAdapter {
    private  static ArrayList<String> listname = new ArrayList<String>();
    private Activity activity;
    private static ArrayList<String> newsimage = new ArrayList<String>();
    private static ArrayList<String> newsextraimage = new ArrayList<String>();
    private static ArrayList<String> listdetails = new ArrayList<>();
    private static ArrayList<String> timestamplist = new ArrayList<>();
    private Context context;



    public News_Adapter(ArrayList<String> listname, Context context, Activity activity, ArrayList<String> listdetails, ArrayList<String> newsimage, ArrayList<String> newsextraimage, ArrayList<String> timestamplist) {
        this.listname = listname;
        this.context = context;
        this.activity = activity;
        this.listdetails = listdetails;
        this.newsimage = newsimage;
        this.newsextraimage = newsextraimage;
        this.timestamplist = timestamplist;
    }

    @Override
    public int getCount() {
        return listname.size();
    }

    @Override
    public Object getItem(int pos) {
        return listname.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.news_list, null);
            holder = new ViewHolder();
            holder.nameTextView = (TextView)view.findViewById(R.id.news_name);
            holder.timeTextView = (TextView)view.findViewById(R.id.timestamp);
            holder.detailsTextView = (TextView)view.findViewById(R.id.news_details);
            holder.extraImageView = (ImageView)view.findViewById(R.id.news_extraimage);
            holder.organImageView = (ImageView)view.findViewById(R.id.news_image);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }


        //Handle TextView and display string from your list

        holder.nameTextView.setText(listname.get(position));

        holder.detailsTextView.setText(listdetails.get(position));

        holder.timeTextView.setText(timestamplist.get(position));
        holder.extraImageView.setImageBitmap(null);
        holder.organImageView.setImageBitmap(null);




        if(!newsimage.get(position).equals("")) {

            StorageReference organref = FirebaseStorage.getInstance().getReference().child(newsimage.get(position));
            organref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {


                    Glide.with(context).load(uri).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.organImageView);
                }
            });
        } else
            holder.organImageView.setImageBitmap(null);


        if(!newsextraimage.get(position).equals("")) {


            StorageReference mountainsRef = FirebaseStorage.getInstance().getReference().child(newsextraimage.get(position));
            mountainsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {


                    Glide.with(context).load(uri).override(parent.getWidth(),parent.getHeight()).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(holder.extraImageView);
                }
            });


        } else
            holder.extraImageView.setImageBitmap(null);

        return view;
    }
    static class ViewHolder {
        private TextView nameTextView;
        private TextView timeTextView;
        private TextView detailsTextView;
        private ImageView extraImageView;
        private ImageView organImageView;
    }
}
