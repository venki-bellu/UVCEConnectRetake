package com.venkibellu.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class activities extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ieee_logo:
                startActivity(new Intent(activities.this, ieee.class));
                break;

            case R.id.chethana_logo:
                startActivity(new Intent(activities.this, chethana.class));
                break;

            case R.id.thathva_logo:
                startActivity(new Intent(activities.this, thatva.class));
                break;

            case R.id.g2c2_logo:
                startActivity(new Intent(activities.this, g2c2.class));
                break;

            case R.id.vinimaya_logo:
                startActivity(new Intent(activities.this, vinimaya.class));
                break;

            case R.id.sae_logo:
                startActivity(new Intent(activities.this, sae.class));
                break;

            case R.id.gb_ram_logo:
                startActivity(new Intent(activities.this, gb_ram.class));
                break;

            case R.id.chakravyuha_logo:
                startActivity(new Intent(activities.this, chakravyuha.class));
                break;

            case R.id.ncc_logo:
                startActivity(new Intent(activities.this, ncc.class));
                break;

            case R.id.sports_logo:
                startActivity(new Intent(activities.this, sports.class));
                break;
        }
    }
}
