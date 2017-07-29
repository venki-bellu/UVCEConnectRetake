package com.venkibellu.myapplication;

import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class g2c2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g2c2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView t=(TextView)findViewById(R.id.t);
        Typeface mycustomfont=Typeface.createFromAsset(getAssets(),"fonts/LibreBaskerville-Bold.ttf");
        t.setTypeface(mycustomfont);

    }
}
