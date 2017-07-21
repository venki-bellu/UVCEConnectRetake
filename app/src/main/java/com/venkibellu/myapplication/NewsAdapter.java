package com.venkibellu.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;


public class NewsAdapter extends ArrayAdapter<News> {
    private int lastPosition;
    private List<News> newsList;
    private Context context;
    private View view;

    public NewsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<News> objects) {
        super(context, resource, objects);
        this.context = context;
        this.newsList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        view = convertView;
        final ViewHolder viewHolder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.news_list, null);

            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


        News currentNews = newsList.get(position);
        setNewsData(currentNews, viewHolder);

        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        view.startAnimation(animation);
        lastPosition = position;
        return view;
    }

    private void setNewsData(final News currentNews, final ViewHolder viewHolder) {
        viewHolder.setNameTextView(currentNews.getName());
        viewHolder.setTimeTextView(currentNews.getTime());
        viewHolder.setDetailsTextView(currentNews.getDetails());
        viewHolder.setOrganImageView(null);
        viewHolder.setExtraImageView(null);

        if (!currentNews.getImage().isEmpty()) {
            StorageReference organref = FirebaseStorage.getInstance().getReference().child(currentNews.getImage());
            organref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context).load(uri).override(view.getWidth(), view.getHeight()).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.extraImageView);
                }
            });
        }

        if (!currentNews.getOrganisation().isEmpty()) {
            StorageReference mountainsRef = FirebaseStorage.getInstance().getReference().child(currentNews.getOrganisation());
            mountainsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context).load(uri).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.organImageView);
                }
            });
        }
    }

    private class ViewHolder {
        private TextView nameTextView, timeTextView, detailsTextView;
        private ImageView extraImageView, organImageView;

        ViewHolder(View view) {
            nameTextView = (TextView) view.findViewById(R.id.news_name);
            timeTextView = (TextView) view.findViewById(R.id.timestamp);
            detailsTextView = (TextView) view.findViewById(R.id.news_details);
            extraImageView = (ImageView) view.findViewById(R.id.news_extraimage);
            organImageView = (ImageView) view.findViewById(R.id.news_image);
        }

        void setNameTextView(String text) {
            this.nameTextView.setText(text);
        }

        void setTimeTextView(String text) {
            this.timeTextView.setText(text);
        }

        void setDetailsTextView(String text) {
            this.detailsTextView.setText(text);
        }

        void setExtraImageView(Bitmap bitmap) {
            this.extraImageView.setImageBitmap(bitmap);
        }

        void setOrganImageView(Bitmap bitmap) {
            this.organImageView.setImageBitmap(bitmap);
        }
    }
}