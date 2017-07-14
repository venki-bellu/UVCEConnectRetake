package com.venkibellu.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import com.firebase.ui.storage.images.FirebaseImageLoader;


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
        View finalView = view;

        final ImageView news_extraimage = (ImageView)view.findViewById(R.id.news_extraimage);
        ImageView news_image = (ImageView)view.findViewById(R.id.news_image);

        if(!newsextraimage.get(position).equals("")) {


            StorageReference mountainsRef = FirebaseStorage.getInstance().getReference().child(newsextraimage.get(position));
            Glide.with(context)

                    .load(newsextraimage.get(position))
                    .into(news_extraimage);

        }

        return view;
    }
}