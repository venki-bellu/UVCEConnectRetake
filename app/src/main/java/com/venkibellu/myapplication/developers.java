package com.venkibellu.myapplication;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class developers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developers);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textView2=(TextView)findViewById(R.id.textView2);
        Typeface mycustomfont1=Typeface.createFromAsset(getAssets(),"fonts/LibreBaskerville-Bold.ttf");
        textView2.setTypeface(mycustomfont1);


        TextView textView3=(TextView)findViewById(R.id.textView3);
        Typeface mycustomfont2=Typeface.createFromAsset(getAssets(),"fonts/LibreBaskerville-Bold.ttf");
        textView3.setTypeface(mycustomfont2);

        TextView textView4=(TextView)findViewById(R.id.textView4);
        Typeface mycustomfont3=Typeface.createFromAsset(getAssets(),"fonts/LibreBaskerville-Bold.ttf");
        textView4.setTypeface(mycustomfont3);

        TextView textView5=(TextView)findViewById(R.id.textView5);
        Typeface mycustomfont4=Typeface.createFromAsset(getAssets(),"fonts/LibreBaskerville-Bold.ttf");
        textView5.setTypeface(mycustomfont4);

        TextView textView7=(TextView)findViewById(R.id.textView7);
        Typeface mycustomfont5=Typeface.createFromAsset(getAssets(),"fonts/LibreBaskerville-Bold.ttf");
        textView7.setTypeface(mycustomfont5);

        TextView textView8=(TextView)findViewById(R.id.textView8);
        Typeface mycustomfont6=Typeface.createFromAsset(getAssets(),"fonts/LibreBaskerville-Bold.ttf");
        textView8.setTypeface(mycustomfont6);

        TextView textView9=(TextView)findViewById(R.id.textView9);
        Typeface mycustomfont7=Typeface.createFromAsset(getAssets(),"fonts/LibreBaskerville-Bold.ttf");
        textView9.setTypeface(mycustomfont7);

        TextView textView10=(TextView)findViewById(R.id.textView10);
        Typeface mycustomfont8=Typeface.createFromAsset(getAssets(),"fonts/LibreBaskerville-Bold.ttf");
        textView10.setTypeface(mycustomfont8);

        TextView textView11=(TextView)findViewById(R.id.textView11);
        Typeface mycustomfont9=Typeface.createFromAsset(getAssets(),"fonts/LibreBaskerville-Bold.ttf");
        textView11.setTypeface(mycustomfont9);


    }
}
