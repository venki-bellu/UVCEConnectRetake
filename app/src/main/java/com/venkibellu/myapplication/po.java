package com.venkibellu.myapplication;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;

public class po extends AppCompatActivity {
   TextView t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_po);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        t=(TextView)findViewById(R.id.t);
        Typeface mycustomfont=Typeface.createFromAsset(getAssets(),"fonts/LibreBaskerville-Bold.ttf");
        t.setTypeface(mycustomfont);
    }
}
