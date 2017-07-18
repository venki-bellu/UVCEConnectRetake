package com.venkibellu.myapplication;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class News extends Activity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference ref;
    private DatabaseReference myref;
    private DatabaseReference removeref;
    private ArrayList<String> newsname = new ArrayList<String>();
    private ArrayList<String> newsdetails = new ArrayList<String>();
    private ArrayList<String> newsimage = new ArrayList<String>();
    private ArrayList<String> newsorganization = new ArrayList<String>();
    private ArrayList<String> newnewsname = new ArrayList<String>();
    private ArrayList<String> newnewsdetails = new ArrayList<String>();
    private ArrayList<String> newnewsimage = new ArrayList<String>();
    private ArrayList<String> newnewsorganization = new ArrayList<String>();
    private News_Adapter news_adapter;
    private ProgressDialog progress;
    private FloatingActionButton fab;
    private AdapterView.OnItemClickListener itemclick;
    AlertDialog.Builder builder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);


        ref = mFirebaseDatabase.getInstance().getReference().child("News");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{

                    newsname.clear();
                    newsdetails.clear();
                    newsimage.clear();
                    newsorganization.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        newsname.add(snapshot.child("News_Name").getValue().toString());
                        newsdetails.add(snapshot.child("News_Details").getValue().toString());
                        newsimage.add(snapshot.child("News_Image").getValue().toString());
                        newsorganization.add(snapshot.child("News_Organization").getValue().toString());
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
        progress.show();
        news_adapter = new News_Adapter(newsname, this, this, newsdetails, newsorganization, newsimage);
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
        myref = FirebaseDatabase.getInstance().getReference().child("Registered Users");
        Query query = myref.orderByChild("Google_ID").equalTo(Registered_User_Id.registered_user_id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(snapshot.child("Designation").getValue().toString().equals("NORMAL"))
                            fab.setVisibility(View.GONE);
                    }
                }catch(Exception e) {}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

}
