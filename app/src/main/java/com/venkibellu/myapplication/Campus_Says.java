package com.venkibellu.myapplication;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Campus_Says extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference ref;
    private ArrayList<String> campusname = new ArrayList<String>();
    private ArrayList<String> campusdetails = new ArrayList<String>();
    private ArrayList<String> campusimage = new ArrayList<String>();
    private ArrayList<String> campusorganization = new ArrayList<String>();
    private ArrayList<String> campustime = new ArrayList<String>();
    private News_Adapter campus_adapter;
    private FloatingActionButton fab;
    private ProgressDialog progress;
    private DatabaseReference myref;
    public static AlertDialog.Builder builderc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus__says);

        builderc = new AlertDialog.Builder(this);

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

        Toast.makeText(getApplicationContext(),
                "Long press an image to download it",
                Toast.LENGTH_SHORT).show();
        Registered_User_Id.fromactivity = "Campus Says";

            ref = mFirebaseDatabase.getInstance().getReference().child("Campus Says");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    campusname.clear();
                    campusdetails.clear();
                    campusimage.clear();
                    campusorganization.clear();
                    campustime.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        campusname.add(snapshot.child("Campus_Name").getValue().toString());
                        campusdetails.add(snapshot.child("Campus_Details").getValue().toString());
                        campusimage.add(snapshot.child("Campus_Image").getValue().toString());
                        campusorganization.add(snapshot.child("Campus_Organization").getValue().toString());
                        campustime.add(snapshot.child("Timestamp").getValue().toString());
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
        progress.setCancelable(false);
        progress.show();
        campus_adapter = new News_Adapter(campusname, this, this, campusdetails, campusorganization, campusimage, campustime);
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
