package com.venkibellu.myapplication;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class chakravyuha extends AppCompatActivity {
TextView t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chakravyuha);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        t=(TextView)findViewById(R.id.t);
        Typeface mycustomfont=Typeface.createFromAsset(getAssets(),"fonts/LibreBaskerville-Bold.ttf");
        t.setTypeface(mycustomfont);
    }
}
