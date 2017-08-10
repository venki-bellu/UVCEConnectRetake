package com.venkibellu.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.Collections;

public class ViewUsers extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    ListView listView;
    ArrayList<String> registerUserNames = new ArrayList<String>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    LinearLayout progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        listView = (ListView) findViewById(R.id.view_users_list_view);
        progress = (LinearLayout) findViewById(R.id.progressLayout);

        progress.setVisibility(View.VISIBLE);

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("Registered Users");

        adapter = new ArrayAdapter<String>(this, R.layout.list_users_row, R.id.users, registerUserNames);
        listView.setAdapter(adapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                registerUserNames.clear();
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        registerUserNames.add(snapshot.child("Name").getValue().toString());
                    }
                } catch (Exception e) {
                }

                formatNames();

                progress.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void formatNames() {
        for (int i = 0; i < registerUserNames.size(); ++i) {
            String name = registerUserNames.get(i), capitalizedName = "";
            capitalizedName = WordUtils.capitalizeFully(name);

            registerUserNames.set(i, capitalizedName);
        }

        Collections.sort(registerUserNames);
    }
}
