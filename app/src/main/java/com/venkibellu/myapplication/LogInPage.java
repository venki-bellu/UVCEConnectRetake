package com.venkibellu.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LogInPage extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private FirebaseAnalytics mFirebaseAnalytics;
    Intent homepageIntent;

    SignInButton signInButton;
    GoogleApiClient googleApiClient;
    static final int REQ_CODE = 9001;
    GoogleSignInOptions gso;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Intent intent;
    private DatabaseReference ref;
    private GoogleSignInAccount account;
    public static String accountcheck;
    private LinearLayout progress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);


        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.background_video);



        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        progress = (LinearLayout) findViewById(R.id.progressLayout);

        homepageIntent = new Intent(this, NewHomePage.class);

        sharedPreferences = LogInPage.this.getSharedPreferences(getString(R.string.PREF_FILE), MODE_PRIVATE);
        editor = sharedPreferences.edit();
        intent = getIntent();


        //Start of google sign in codes
        signInButton = (SignInButton) findViewById(R.id.google_sign_in_button);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton.setOnClickListener(this);


        //This Might lead to fake sign ins. Just check this. Added by Jerry
        //Ignore above comment. This is working fine. (Bellu)
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            progress.setVisibility(View.VISIBLE);
            GoogleSignInResult result = opr.get();

            handleSignInResult(result);

        }
    }

    //These methods belong to google sign in (Start)
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.google_sign_in_button:
                signIn();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signIn() {
        progress.setVisibility(View.VISIBLE);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, REQ_CODE);
    }


    public void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            account = result.getSignInAccount();
            ref = FirebaseDatabase.getInstance().getReference().child("Registered Users");
            Registered_User_Id.registered_user_id = account.getId();
            Registered_User_Id.registered_user_email = account.getEmail();
            accountcheck = account.getId();


            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        int counter = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.child("Google_ID").getValue().toString().equals(accountcheck)) {
                                progress.setVisibility(View.GONE);
                                Registered_User_Id.name = snapshot.child("Name").getValue().toString();
                                Registered_User_Id.admin = snapshot.child("Designation").getValue().toString();
                                counter++;
                                startActivity(homepageIntent);
                                finish();
                            }
                        }
                        if (counter == 0) {
                            progress.setVisibility(View.GONE);
                            Intent intent = new Intent(LogInPage.this, RegisterPage.class);
                            startActivity(intent);
                            finish();
                        }

                    } catch (Exception e) {
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            editor.putFloat(getString(R.string.LOGIN_TYPE), 2);
            editor.commit();

        } else {
            progress.setVisibility(View.GONE);
            System.out.println("Sign-in Failed: " + result.getStatus());
            Toast.makeText(getApplicationContext(), "Google Sign In failure", Toast.LENGTH_LONG).show();
        }
    }
    //Stop of google sign in methods

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }



}




