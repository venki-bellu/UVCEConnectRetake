package com.venkibellu.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class activities extends AppCompatActivity {

    Button ieeeButton;
    Button chethanaButton;
    Button thathvaButton;
    Button g2c2Button;



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

        thathvaButton=(Button)findViewById(R.id.thathva_logo);
        thathvaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activities.this,thatva.class);
                startActivity(intent);
            }
        });

        g2c2Button=(Button)findViewById(R.id.g2c2_logo);
        g2c2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activities.this,g2c2.class);
                startActivity(intent);
            }
        });
    }





}
