package com.venkibellu.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ViewUsers extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    ListView listView;
    ArrayList<String> registerUserNames=new ArrayList<String>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        listView=(ListView)findViewById(R.id.view_users_list_view);
        firebaseDatabase=FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference().child("Registered Users");

        adapter=new ArrayAdapter<String>(this,R.layout.list_users_row,R.id.users,registerUserNames);
        listView.setAdapter(adapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                registerUserNames.clear();
                try{
                    for(DataSnapshot snapshot: dataSnapshot.getChildren())
                        registerUserNames.add(snapshot.child("Name").getValue().toString());
                }catch (Exception e){}
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
