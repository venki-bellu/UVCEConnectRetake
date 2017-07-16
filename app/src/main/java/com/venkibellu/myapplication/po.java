package com.venkibellu.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;

public class po extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_po);

        final TextView myClickableUrl = (TextView) findViewById(R.id.textView8);
        myClickableUrl.setText("http://campusuvce.in/index.php");
        Linkify.addLinks(myClickableUrl, Linkify.WEB_URLS);
    }
}
