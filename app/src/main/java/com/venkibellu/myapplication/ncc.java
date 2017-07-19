package com.venkibellu.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ncc extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ncc);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
