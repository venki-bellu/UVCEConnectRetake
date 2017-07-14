package com.venkibellu.myapplication;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;


public class HomePage extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    Intent loginPageIntent;
    SharedPreferences logintype;
    GoogleSignInOptions gso;
    GoogleApiClient googleApiClient;




    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        loginPageIntent=new Intent(this,LogInPage.class);
        logintype=getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

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

    //Coding related to sign Out. Added by Venkatesh Belavadi
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.signout)
        {
            Float type=logintype.getFloat(getString(R.string.LOGIN_TYPE),0);
            if(type==0)
            {
                Toast.makeText(getApplicationContext(),"Not Logged In",Toast.LENGTH_SHORT).show();
            }
            else if(type==1)
            {
                Toast.makeText(getApplicationContext(),"Facebook Log Out Success",Toast.LENGTH_SHORT).show();
                LoginManager.getInstance().logOut();
                startActivity(loginPageIntent);
                finish();
            }
            else if(type==2)
            {
                Toast.makeText(getApplicationContext(),"Google Sign Out Success",Toast.LENGTH_LONG).show();
                Auth.GoogleSignInApi.signOut(googleApiClient);
                startActivity(loginPageIntent);
                finish();
            }
            return true;
        }
        return false;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
