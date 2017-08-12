package com.venkibellu.myapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class aboutapp extends AppCompatActivity {
    TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutapp);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        t = (TextView) findViewById(R.id.t);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/LibreBaskerville-Bold.ttf");
        t.setTypeface(font);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.aboutuvce_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.developer_options) {
            Intent i = new Intent(this, developers.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

