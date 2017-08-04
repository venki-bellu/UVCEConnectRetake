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
    Button vinimayaButton;
    Button saeButton;
    Button gbRamButton;
    Button chakravyuhaButton;
    Button nccButton;
    Button sportsButton;



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

        vinimayaButton=(Button)findViewById(R.id.vinimaya_logo);
        vinimayaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activities.this,vinimaya.class);
                startActivity(intent);
            }
        });

        saeButton=(Button)findViewById(R.id.sae_logo);
        saeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activities.this,sae.class);
                startActivity(intent);
            }
        });

        gbRamButton=(Button)findViewById(R.id.gb_ram_logo);
        gbRamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activities.this,gb_ram.class);
                startActivity(intent);
            }
        });

        chakravyuhaButton=(Button)findViewById(R.id.chakravyuha_logo);
        chakravyuhaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activities.this,chakravyuha.class);
                startActivity(intent);
            }
        });

        nccButton=(Button)findViewById(R.id.ncc_logo);
        nccButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activities.this,ncc.class));
            }
        });

        sportsButton=(Button)findViewById(R.id.sports_logo);
        sportsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activities.this,sports.class));
            }
        });
    }





}
