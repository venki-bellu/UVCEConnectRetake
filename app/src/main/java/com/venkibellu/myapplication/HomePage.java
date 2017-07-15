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
import com.google.firebase.auth.FirebaseAuth;


public class HomePage extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    Intent SigninPageIntent;
    SharedPreferences logintype;
    GoogleSignInOptions gso;
    GoogleApiClient googleApiClient;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener authStateListener;




    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        SigninPageIntent=new Intent(this,SignInPage.class);
        logintype=getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth=FirebaseAuth.getInstance();

        authStateListener= new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null)
                {
                    Toast.makeText(getApplicationContext(),"Sign Out Success", Toast.LENGTH_SHORT).show();
                    startActivity(SigninPageIntent);
                    finish();
                }
            }
        };

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
            mAuth.signOut();
            return true;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
