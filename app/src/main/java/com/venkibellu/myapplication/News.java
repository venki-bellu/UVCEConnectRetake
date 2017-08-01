package com.venkibellu.myapplication;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
    private ProgressDialog progress;
    private FloatingActionButton fab;
    public static AlertDialog.Builder builder;
    public static ValueEventListener myevent;



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
        Toast.makeText(getApplicationContext(),
                "Long press an image to download it",
                Toast.LENGTH_SHORT).show();
        ref = mFirebaseDatabase.getInstance().getReference().child("News");

        ref.addValueEventListener(myevent = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{

                    newsname.clear();
                    newsdetails.clear();
                    newsimage.clear();
                    newsorganization.clear();
                    newstime.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        newsname.add(snapshot.child("News_Name").getValue().toString());
                        newsdetails.add(snapshot.child("News_Details").getValue().toString());
                        newsimage.add(snapshot.child("News_Image").getValue().toString());
                        newsorganization.add(snapshot.child("News_Organization").getValue().toString());
                        newstime.add(snapshot.child("Timestamp").getValue().toString());
                    }
                    news_adapter.notifyDataSetChanged();
                    progress.dismiss();

                } catch (Exception e){ }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        progress = new ProgressDialog(News.this);
        progress.setMessage("Fetching Data.....");
        progress.setTitle("Please Wait");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.show();
        news_adapter = new News_Adapter(newsname, this, this, newsdetails, newsorganization, newsimage, newstime);
        final ListView listView = (ListView)findViewById(R.id.news_list);
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

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
