package com.venkibellu.myapplication;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ListView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Campus_Says extends Activity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference ref;
    private ArrayList<String> campusname = new ArrayList<String>();
    private ArrayList<String> campusdetails = new ArrayList<String>();
    private ArrayList<String> campusimage = new ArrayList<String>();
    private ArrayList<String> campusorganization = new ArrayList<String>();
    private News_Adapter campus_adapter;
    private FloatingActionButton fab;
    private ProgressDialog progress;
    private DatabaseReference myref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus__says);

        ref = mFirebaseDatabase.getInstance().getReference().child("Campus Says");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    campusname.clear();
                    campusdetails.clear();
                    campusimage.clear();
                    campusorganization.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        campusname.add(snapshot.child("Campus_Name").getValue().toString());
                        campusdetails.add(snapshot.child("Campus_Details").getValue().toString());
                        campusimage.add(snapshot.child("Campus_Image").getValue().toString());
                        campusorganization.add(snapshot.child("Campus_Organization").getValue().toString());
                    }
                    campus_adapter.notifyDataSetChanged();
                    progress.dismiss();

                } catch (Exception e){}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        progress = new ProgressDialog(Campus_Says.this);
        progress.setMessage("Fetching Data.....");
        progress.setTitle("Please Wait");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        campus_adapter = new News_Adapter(campusname, this, this, campusdetails, campusorganization, campusimage);
        ListView listView = (ListView)findViewById(R.id.campus_list);
        listView.setAdapter(campus_adapter);

        fab = (FloatingActionButton) findViewById(R.id.add_campus);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Campus_Says.this, Campus_Adding.class);
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
