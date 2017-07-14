package com.venkibellu.myapplication;


import android.os.Bundle;
import android.app.Activity;
import android.widget.ListView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus__says);

        ref = mFirebaseDatabase.getInstance().getReference().child("Campus Says");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        campusname.add(snapshot.child("Campus_Name").getValue().toString());
                        campusdetails.add(snapshot.child("Campus_Details").getValue().toString());
                        campusimage.add(snapshot.child("Campus_Image").getValue().toString());
                        campusorganization.add(snapshot.child("Campus_Organization").getValue().toString());
                    }
                    campus_adapter.notifyDataSetChanged();

                } catch (Exception e){}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        campus_adapter = new News_Adapter(campusname, this, this, campusdetails, campusorganization, campusimage);
        ListView listView = (ListView)findViewById(R.id.campus_list);
        listView.setAdapter(campus_adapter);



    }

}
