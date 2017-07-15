package com.venkibellu.myapplication;


import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ListView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class News extends Activity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference ref;
    private ArrayList<String> newsname = new ArrayList<String>();
    private ArrayList<String> newsdetails = new ArrayList<String>();
    private ArrayList<String> newsimage = new ArrayList<String>();
    private ArrayList<String> newsorganization = new ArrayList<String>();
    private News_Adapter news_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        ref = mFirebaseDatabase.getInstance().getReference().child("News");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        newsname.add(snapshot.child("News_Name").getValue().toString());
                        newsdetails.add(snapshot.child("News_Details").getValue().toString());
                        newsimage.add(snapshot.child("News_Image").getValue().toString());
                        newsorganization.add(snapshot.child("News_Organization").getValue().toString());
                    }
                    news_adapter.notifyDataSetChanged();

                } catch (Exception e){}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        news_adapter = new News_Adapter(newsname, this, this, newsdetails, newsorganization, newsimage);
        ListView listView = (ListView)findViewById(R.id.news_list);
        listView.setAdapter(news_adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_news);
        fab.setVisibility(View.GONE);

    }

}
