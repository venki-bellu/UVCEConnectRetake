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
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;


public class News_Adapter extends BaseAdapter implements ListAdapter {
    private  static ArrayList<String> listname = new ArrayList<String>();
    private Activity activity;
    private static ArrayList<String> newsimage = new ArrayList<String>();
    private static ArrayList<String> newsextraimage = new ArrayList<String>();
    private static ArrayList<String> listdetails = new ArrayList<>();
    private Context context;



    public News_Adapter(ArrayList<String> listname, Context context, Activity activity, ArrayList<String> listdetails, ArrayList<String> newsimage, ArrayList<String> newsextraimage) {
        this.listname = listname;
        this.context = context;
        this.activity = activity;
        this.listdetails = listdetails;
        this.newsimage = newsimage;
        this.newsextraimage = newsextraimage;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.news_list, null);
        }


        //Handle TextView and display string from your list
        TextView name = (TextView)view.findViewById(R.id.news_name);
        name.setText(listname.get(position));
        TextView details = (TextView)view.findViewById(R.id.news_details);
        details.setText(listdetails.get(position));


        final ImageView news_extraimage = (ImageView)view.findViewById(R.id.news_extraimage);
        ImageView news_image = (ImageView)view.findViewById(R.id.news_image);


        if(!newsextraimage.get(position).equals("")) {

            StorageReference mountainsRef = FirebaseStorage.getInstance().getReference().child(newsextraimage.get(position));
            mountainsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(context).load(uri).into(news_extraimage);
                }
            });


        } else
            news_extraimage.setImageBitmap(null);

        return view;
    }
}