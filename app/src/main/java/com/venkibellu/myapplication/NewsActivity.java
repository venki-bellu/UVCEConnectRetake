package com.venkibellu.myapplication;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends Activity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference ref;
    private DatabaseReference myref;
    private List<News> newsList = new ArrayList<>();
    private ArrayAdapter<News> newsAdapter;
    private ProgressDialog progress;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        syncData();
        showProgress();
        populateNewsFeed();
        handleFloatingActionButton();
    }
    private void syncData() {
        ref = mFirebaseDatabase.getInstance().getReference().child("NewsActivity");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{

                    newsList.clear();

                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String name = snapshot.child("News_Name").getValue().toString();
                        String details = snapshot.child("News_Details").getValue().toString();
                        String image = snapshot.child("News_Image").getValue().toString();
                        String organisation = snapshot.child("News_Organization").getValue().toString();
                        String time = snapshot.child("Timestamp").getValue().toString();

                        newsList.add(new News(name, time, details, image, organisation));
                        System.out.println(name + '\n' + details + '\n' + image + '\n' + organisation + '\n' + time);
                    }

                    newsAdapter.notifyDataSetChanged();
                    progress.dismiss();

                } catch (Exception e){
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void populateNewsFeed() {
        newsAdapter = new NewsAdapter(NewsActivity.this, R.layout.news_list, newsList);
        ListView newsFeed = (ListView) findViewById(R.id.news_list);
        newsFeed.setAdapter(newsAdapter);
    }

    private void showProgress() {
        progress = new ProgressDialog(NewsActivity.this);
        progress.setMessage("Fetching Data.....");
        progress.setTitle("Please Wait");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    private void handleFloatingActionButton() {
        fab = (FloatingActionButton) findViewById(R.id.add_news);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsActivity.this, News_Adding.class);
                startActivity(intent);
                finish();
            }
        });

        fab.setVisibility(View.GONE);
        myref = FirebaseDatabase.getInstance().getReference().child("Registered Users");
        Query query = myref.orderByChild("Google_ID").equalTo(Registered_User_Id.registered_user_id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(snapshot.child("Designation").getValue().toString().equals("ADMIN"))
                            fab.setVisibility(View.VISIBLE);
                    }
                }catch(Exception e) {}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
