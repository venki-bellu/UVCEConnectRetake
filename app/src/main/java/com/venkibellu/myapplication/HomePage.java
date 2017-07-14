package com.venkibellu.myapplication;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;



public class HomePage extends AppCompatActivity {

    Intent loginPageIntent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        loginPageIntent=new Intent(this,LogInPage.class);

    }

    public void onSyllabusImageViewClicked(View view) {
        Intent AcademicPageIntent = new Intent(this, AcademicActivity.class);
        startActivity(AcademicPageIntent);
    }
    public void onNewsImageViewClicked(View view) {
        Intent AcademicPageIntent = new Intent(this, News.class);
        startActivity(AcademicPageIntent);
    }
    public void onCampusImageViewClicked(View view) {
        Intent AcademicPageIntent = new Intent(this, Campus_Says.class);
        startActivity(AcademicPageIntent);
    }

    //Start of methods related to action bar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.signout)
        {
            startActivity(loginPageIntent);
            finish();
            return true;
        }
        return false;
    }
}
