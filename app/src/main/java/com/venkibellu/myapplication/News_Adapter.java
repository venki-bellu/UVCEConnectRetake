package com.venkibellu.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

import static com.venkibellu.myapplication.Campus_Says.builderc;
import static com.venkibellu.myapplication.News.builder;


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
    private FirebaseDatabase fbdb;
    private DatabaseReference refnum;
    Query query;
    ValueEventListener cquerynewevent;
    ValueEventListener querynewevent;
    ValueEventListener equerynewevent;
    ValueEventListener cequerynewevent;
    private int keyval;
    ValueEventListener queryevent;
    ValueEventListener cqueryevent;
    ValueEventListener equeryevent;
    ValueEventListener cequeryevent;
    private StorageReference remove;
    private Animation animation;
    private View view;


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
        view = convertView;
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
            holder.extraImageView.setVisibility(View.VISIBLE);
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
            holder.extraImageView.setVisibility(View.GONE);
        }

        Button delete = (Button)view.findViewById(R.id.news_delete);
        final TextView time = (TextView)view.findViewById(R.id.timestamp);
        final Button edit = (Button)view.findViewById(R.id.news_edit);
        final Button submit = (Button)view.findViewById(R.id.news_edit_submit);
        final EditText editText = (EditText)view.findViewById(R.id.news_details_edit);

        if(!Registered_User_Id.admin.equals("ADMIN"))
        {
            edit.setVisibility(View.GONE);
            submit.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Registered_User_Id.fromactivity.equals("News")) {

                builder.setCancelable(true);
                builder.setTitle("Alert");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setCancelable(true);
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.setMessage("Are you sure you want to delete this post?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                            refnum = fbdb.getInstance().getReference().child("News");
                            query = refnum.orderByKey().limitToFirst(1);
                            query.addValueEventListener(queryevent = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    try {
                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                            keyval = Integer.parseInt(child.getKey());
                                            keyval = keyval + position;
                                        }
                                    }catch (Exception e) {}

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });



                            refnum.addListenerForSingleValueEvent(querynewevent = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    try {

                                        if(!dataSnapshot.child(String.valueOf(keyval)).child("News_Image").getValue().toString().equals("")) {
                                            remove = FirebaseStorage.getInstance().getReference().child(dataSnapshot.child(String.valueOf(keyval)).child("News_Image").getValue().toString());
                                            remove.delete();
                                        }

                                            for (int i = keyval; i > keyval-position; i--) {
                                                refnum.child(String.valueOf(i)).child("Added_By").setValue(dataSnapshot.child(String.valueOf(i - 1)).child("Added_By").getValue());
                                                refnum.child(String.valueOf(i)).child("News_Details").setValue(dataSnapshot.child(String.valueOf(i - 1)).child("News_Details").getValue());
                                                refnum.child(String.valueOf(i)).child("News_Image").setValue(dataSnapshot.child(String.valueOf(i - 1)).child("News_Image").getValue());
                                                refnum.child(String.valueOf(i)).child("News_Name").setValue(dataSnapshot.child(String.valueOf(i - 1)).child("News_Name").getValue());
                                                refnum.child(String.valueOf(i)).child("News_Organization").setValue(dataSnapshot.child(String.valueOf(i - 1)).child("News_Organization").getValue());
                                                refnum.child(String.valueOf(i)).child("Timestamp").setValue(dataSnapshot.child(String.valueOf(i - 1)).child("Timestamp").getValue());

                                            }


                            refnum.child(String.valueOf(keyval-position)).removeValue();
                                        refnum.removeEventListener(querynewevent);


                                    } catch (Exception e) {
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                    }
                });

                    builder.show();

                        }
                        if(Registered_User_Id.fromactivity.equals("Campus Says"))
                        {
                            builderc.setCancelable(true);
                            builderc.setTitle("Alert");
                            builderc.setIcon(android.R.drawable.ic_dialog_alert);
                            builderc.setCancelable(true);
                            builderc.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            builderc.setMessage("Are you sure you want to delete this post?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                            refnum = fbdb.getInstance().getReference().child("Campus Says");
                            query = refnum.orderByKey().limitToFirst(1);
                            query.addValueEventListener(cqueryevent = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    try {
                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                            keyval = Integer.parseInt(child.getKey());
                                            keyval = keyval + position;
                                        }
                                    }catch (Exception e) {}

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });



                            refnum.addListenerForSingleValueEvent(cquerynewevent = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    try {

                                        if(!dataSnapshot.child(String.valueOf(keyval)).child("Campus_Image").getValue().toString().equals("")) {
                                            remove = FirebaseStorage.getInstance().getReference().child(dataSnapshot.child(String.valueOf(keyval)).child("Campus_Image").getValue().toString());
                                            remove.delete();
                                        }

                                        for (int i = keyval; i > keyval-position; i--) {
                                            refnum.child(String.valueOf(i)).child("Added_By").setValue(dataSnapshot.child(String.valueOf(i - 1)).child("Added_By").getValue());
                                            refnum.child(String.valueOf(i)).child("Campus_Details").setValue(dataSnapshot.child(String.valueOf(i - 1)).child("Campus_Details").getValue());
                                            refnum.child(String.valueOf(i)).child("Campus_Image").setValue(dataSnapshot.child(String.valueOf(i - 1)).child("Campus_Image").getValue());
                                            refnum.child(String.valueOf(i)).child("Campus_Name").setValue(dataSnapshot.child(String.valueOf(i - 1)).child("Campus_Name").getValue());
                                            refnum.child(String.valueOf(i)).child("Campus_Organization").setValue(dataSnapshot.child(String.valueOf(i - 1)).child("Campus_Organization").getValue());
                                            refnum.child(String.valueOf(i)).child("Timestamp").setValue(dataSnapshot.child(String.valueOf(i - 1)).child("Timestamp").getValue());

                                        }


                                        refnum.child(String.valueOf(keyval-position)).removeValue();
                                        refnum.removeEventListener(cquerynewevent);


                                    } catch (Exception e) {
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                                }
                            });

                            builderc.show();


                        }



            }
        });
        editText.setText(holder.detailsTextView.getText());

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.clearAnimation();

                if(Registered_User_Id.fromactivity.equals("News")) {


                    News.ref.removeEventListener(News.myevent);


                    editText.setVisibility(View.VISIBLE);

                    holder.detailsTextView.setVisibility(View.GONE);
                    submit.setVisibility(View.VISIBLE);
                    edit.setVisibility(View.GONE);
                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            refnum = fbdb.getInstance().getReference().child("News");
                            query = refnum.orderByKey().limitToFirst(1);
                            query.addValueEventListener(equeryevent = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    try{
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        keyval = Integer.parseInt(child.getKey());
                                        keyval = keyval + position;
                                    }
                                    }catch (Exception e) {}

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            refnum.addListenerForSingleValueEvent(equerynewevent = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {


                                    refnum.child(String.valueOf(keyval)).child("News_Details").setValue(editText.getText().toString());
                                    if(!time.getText().toString().endsWith("(Edited)"))
                                        refnum.child(String.valueOf(keyval)).child("Timestamp").setValue(time.getText().toString() + " (Edited)");
                                    submit.setVisibility(View.GONE);
                                    edit.setVisibility(View.VISIBLE);
                                    editText.setVisibility(View.INVISIBLE);
                                    holder.detailsTextView.setVisibility(View.VISIBLE);
                                    holder.detailsTextView.setText(editText.getText().toString());
                                    News.ref.addValueEventListener(News.myevent);
                                    refnum.removeEventListener(equerynewevent);



                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    });

                }
                if(Registered_User_Id.fromactivity.equals("Campus Says"))
                {
                    Campus_Says.ref.removeEventListener(Campus_Says.myevent);
                    editText.setVisibility(View.VISIBLE);
                    holder.detailsTextView.setVisibility(View.GONE);
                    submit.setVisibility(View.VISIBLE);
                    edit.setVisibility(View.GONE);
                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            refnum = fbdb.getInstance().getReference().child("Campus Says");
                            query = refnum.orderByKey().limitToFirst(1);
                            query.addValueEventListener(cequeryevent = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    try{
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        keyval = Integer.parseInt(child.getKey());
                                        keyval = keyval + position;
                                    }
                                    }catch (Exception e) {}

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            refnum.addListenerForSingleValueEvent(cequerynewevent = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    try{

                                        refnum.child(String.valueOf(keyval)).child("Campus_Details").setValue(editText.getText().toString());
                                        if(!time.getText().toString().endsWith("(Edited)"))
                                        refnum.child(String.valueOf(keyval)).child("Timestamp").setValue(time.getText().toString() + " (Edited)");
                                    submit.setVisibility(View.GONE);
                                    edit.setVisibility(View.VISIBLE);
                                    editText.setVisibility(View.INVISIBLE);
                                        holder.detailsTextView.setVisibility(View.VISIBLE);
                                        holder.detailsTextView.setText(editText.getText().toString());
                                        Campus_Says.ref.addValueEventListener(Campus_Says.myevent);
                                    refnum.removeEventListener(cequerynewevent);
                                    }catch (Exception e) {}


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    });

                }





            }
        });

        animation = AnimationUtils.loadAnimation(context,
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
