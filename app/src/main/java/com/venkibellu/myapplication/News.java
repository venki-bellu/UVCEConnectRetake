package com.venkibellu.myapplication;


import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

public class News extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference ref;
    private DatabaseReference myref;
    private ArrayList<String> newsname = new ArrayList<String>();
    private ArrayList<String> newstime = new ArrayList<String>();
    private ArrayList<String> newsdetails = new ArrayList<String>();
    private ArrayList<String> newsimage = new ArrayList<String>();
    private ArrayList<String> newsorganization = new ArrayList<String>();
    private News_Adapter news_adapter;
    private FloatingActionButton fab;
    public static AlertDialog.Builder builder;
    public static ValueEventListener myevent;
    //    private LinearLayout progress;
    private AVLoadingIndicatorView avi;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        builder = new AlertDialog.Builder(this);
        if (Build.VERSION.SDK_INT >= 23) { // if android version >= 6.0
            if (ContextCompat.checkSelfPermission(
                    getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // if permission was not granted initially, ask the user again.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }

        Registered_User_Id.fromactivity = "News";
        ref = mFirebaseDatabase.getInstance().getReference().child("News");

        ref.addValueEventListener(myevent = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {

                    newsname.clear();
                    newsdetails.clear();
                    newsimage.clear();
                    newsorganization.clear();
                    newstime.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        newsname.add(snapshot.child("News_Name").getValue().toString());
                        newsdetails.add(snapshot.child("News_Details").getValue().toString());
                        newsimage.add(snapshot.child("News_Image").getValue().toString());
                        newsorganization.add(snapshot.child("News_Organization").getValue().toString());
                        newstime.add(snapshot.child("Timestamp").getValue().toString());
                    }
                    news_adapter.notifyDataSetChanged();
//                    progress.setVisibility(View.GONE);
                    avi.hide();

                } catch (Exception e) {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        avi.show();
//        progress = (LinearLayout) findViewById(R.id.progressLayout);
//        progress.setVisibility(View.VISIBLE);

        news_adapter = new News_Adapter(newsname, this, this, newsdetails, newsorganization, newsimage, newstime);
        listView = (ListView) findViewById(R.id.news_list);
        listView.setAdapter(news_adapter);

        fab = (FloatingActionButton) findViewById(R.id.add_news);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(News.this, News_Adding.class);
                startActivity(intent);
                finish();
            }
        });

        fab.setVisibility(View.GONE);
        if (Registered_User_Id.admin.equals("ADMIN")) {
            fab.setVisibility(View.VISIBLE);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int currentVisibleItemCount;
            private int currentScrollState;
            private int currentFirstVisibleItem;
            private int totalItem;


            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.currentScrollState = scrollState;

                if (totalItem - currentFirstVisibleItem == currentVisibleItemCount
                        && this.currentScrollState == SCROLL_STATE_IDLE) {
                    fab.hide();
                } else if (Registered_User_Id.admin.equals("ADMIN")) {
                    fab.show();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
                this.totalItem = totalItemCount;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("id", Registered_User_Id.registered_user_id);
        outState.putString("email", Registered_User_Id.registered_user_email);
        outState.putString("name", Registered_User_Id.name);
        outState.putString("admin", Registered_User_Id.admin);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Registered_User_Id.registered_user_id = savedInstanceState.getString("id");
        Registered_User_Id.registered_user_email = savedInstanceState.getString("email");
        Registered_User_Id.name = savedInstanceState.getString("name");
        Registered_User_Id.admin = savedInstanceState.getString("admin");
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getApplicationContext(),
                            "Permission granted to read External storage",
                            Toast.LENGTH_SHORT).show();

                } else {

                    // permission not granted, download can't take place.
                    Toast.makeText(getApplicationContext(),
                            "Permission denied to read External storage",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
