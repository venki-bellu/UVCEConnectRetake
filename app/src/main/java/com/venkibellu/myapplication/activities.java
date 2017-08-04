package com.venkibellu.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class activities extends AppCompatActivity {

    Button ieeeButton;
    Button chethanaButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);

        ieeeButton=(Button)findViewById(R.id.ieee_logo);
        ieeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activities.this,ieee.class);
                startActivity(intent);
            }
        });

        chethanaButton=(Button)findViewById(R.id.chethana_logo);
        chethanaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activities.this,chethana.class);
                startActivity(intent);
            }
        });
    }



}
