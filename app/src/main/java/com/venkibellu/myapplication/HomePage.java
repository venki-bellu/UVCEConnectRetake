package com.venkibellu.myapplication;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class HomePage extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


    }

    public void onSyllabusImageViewClicked(View view) {
        Intent AcademicPageIntent = new Intent(this, AcademicActivity.class);
        startActivity(AcademicPageIntent);
    }


}
