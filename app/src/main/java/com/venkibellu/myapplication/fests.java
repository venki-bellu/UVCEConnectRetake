package com.venkibellu.myapplication;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class fests extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fests);

        TextView t=(TextView)findViewById(R.id.t);
        Typeface mycustomfont=Typeface.createFromAsset(getAssets(),"fonts/LibreBaskerville-Bold.ttf");
        t.setTypeface(mycustomfont);


        TextView t2=(TextView)findViewById(R.id.t2);
        Typeface mycustomfont2=Typeface.createFromAsset(getAssets(),"fonts/LibreBaskerville-Bold.ttf");
        t2.setTypeface(mycustomfont2);

        TextView t3=(TextView)findViewById(R.id.t3);
        Typeface mycustomfont3=Typeface.createFromAsset(getAssets(),"fonts/LibreBaskerville-Bold.ttf");
        t3.setTypeface(mycustomfont3);



        TextView t4=(TextView)findViewById(R.id.t4);
        Typeface mycustomfont4=Typeface.createFromAsset(getAssets(),"fonts/LibreBaskerville-Bold.ttf");
        t4.setTypeface(mycustomfont4);


        TextView t5=(TextView)findViewById(R.id.t5);
        Typeface mycustomfont5=Typeface.createFromAsset(getAssets(),"fonts/LibreBaskerville-Bold.ttf");
        t5.setTypeface(mycustomfont5);



    TextView t6=(TextView)findViewById(R.id.t6);
    Typeface mycustomfont6=Typeface.createFromAsset(getAssets(),"fonts/LibreBaskerville-Bold.ttf");
        t6.setTypeface(mycustomfont6);



        TextView t7=(TextView)findViewById(R.id.t7);
        Typeface mycustomfont7=Typeface.createFromAsset(getAssets(),"fonts/LibreBaskerville-Bold.ttf");
        t7.setTypeface(mycustomfont7);


        TextView t8=(TextView)findViewById(R.id.t8);
        Typeface mycustomfont8=Typeface.createFromAsset(getAssets(),"fonts/LibreBaskerville-Bold.ttf");
        t8.setTypeface(mycustomfont8);



    }
}
