package com.venkibellu.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.firebase.analytics.FirebaseAnalytics;


public class LogInPage extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {

    private FirebaseAnalytics mFirebaseAnalytics;
    Intent homepageIntent;
    LoginButton loginButton;
    CallbackManager callbackManager;
    SignInButton signInButton;
    GoogleApiClient googleApiClient;
    static final int REQ_CODE=9001;
    GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        homepageIntent = new Intent(this, HomePage.class);

        //Start of facebook Log In Codes
        loginButton = (LoginButton) findViewById(R.id.fblogin_button);
        callbackManager = CallbackManager.Factory.create();



        if (isLoggedIn()) {

            startActivity(homepageIntent);
            finish();
        }


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("FACEBOOK", "SignInSuccess");

                startActivity(homepageIntent);
                finish();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Facebook LogIn Cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        //End of facebook Sign In Codes
        //
        //
        //
        //
        //Start of google sign in codes

        signInButton=(SignInButton)findViewById(R.id.google_sign_in_button);
        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton.setOnClickListener(this);

        //This Might lead to fake sign ins. Just check this. Added by Jerry
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone())
        {

            GoogleSignInResult result = opr.get();

            handleSignInResult(result);

        }
    }


    // if already logged in accessToken will not be null
    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
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
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, REQ_CODE);
    }



    public void handleSignInResult(GoogleSignInResult result)
    {
        if(result.isSuccess())
        {
            startActivity(homepageIntent);
            finish();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Google Sign In failure",Toast.LENGTH_LONG).show();
        }
    }
    //Stop of google sign in methods

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }


    public void bypass(View v) {
        startActivity(homepageIntent);
        finish();
    }
}




