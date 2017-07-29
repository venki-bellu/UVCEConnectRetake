package com.venkibellu.myapplication;

import android.app.Activity;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;


import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;

import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

import static java.security.AccessController.getContext;


public class News_Adapter extends BaseAdapter implements ListAdapter {
    private  static ArrayList<String> listname = new ArrayList<String>();
    private Activity activity;
    private static ArrayList<String> newsimage = new ArrayList<String>();
    private static ArrayList<String> newsextraimage = new ArrayList<String>();
    private static ArrayList<String> listdetails = new ArrayList<>();
    private static ArrayList<String> timestamplist = new ArrayList<>();
    private Context context;
    private int lastPosition = -1;
    private String fileName;


    public News_Adapter(ArrayList<String> listname,
                        Context context,
                        Activity activity,
                        ArrayList<String> listdetails,
                        ArrayList<String> newsimage,
                        ArrayList<String> newsextraimage,
                        ArrayList<String> timestamplist) {

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
            StorageReference organref = FirebaseStorage.getInstance()
                                                       .getReference()
                                                       .child(newsimage.get(position));

            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(organref)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.organImageView);

        } else {
            holder.organImageView.setImageBitmap(null);
        }

        if(!newsextraimage.get(position).equals("")) {
            final StorageReference mountainsRef = FirebaseStorage.getInstance()
                                                           .getReference()
                                                           .child(newsextraimage.get(position));
            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(mountainsRef)
                    .override(parent.getWidth(), parent.getHeight())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .into(holder.extraImageView);
            holder.extraImageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(true);
                    builder.setTitle("Download Image");
                    builder.setMessage("Do you want to download this image?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mountainsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    try {
                                        File directory = new File(Environment.getExternalStorageDirectory() + "/UVCE-Connect");

                                        if (!directory.exists()) {
                                            directory.mkdirs();
                                        }


                                        DownloadManager.Request request = new DownloadManager.Request(uri);
                                        fileName = newsextraimage.get(position);
                                        request.setDescription("Image")
                                                .setTitle(fileName)
                                                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                                .setDestinationInExternalPublicDir("/UVCE-Connect/Images", fileName + ".jpg")
                                                .allowScanningByMediaScanner();

                                        DownloadManager manager = (DownloadManager)
                                                context.getSystemService(Context.DOWNLOAD_SERVICE);
                                        manager.enqueue(request);
                                        Toast.makeText(context, "Downloading.....", Toast.LENGTH_SHORT).show();

                                    }catch(Exception e){
                                        Toast.makeText(context, "Permission not granted to read External storage. Please grant permission and try again.", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();


                    return true;

                }
            });

        } else {
            holder.extraImageView.setImageBitmap(null);
        }

        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);

        view.startAnimation(animation);
        lastPosition = position;

        return view;
    }


    static class ViewHolder {
        private TextView nameTextView, timeTextView, detailsTextView;
        private ImageView extraImageView, organImageView;
    }



}
