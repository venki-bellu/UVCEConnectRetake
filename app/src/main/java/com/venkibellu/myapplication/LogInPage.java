package com.venkibellu.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;


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
    private AVLoadingIndicatorView avi;
    private final String PREFERENECE = "UVCE-prefereceFile-AccountID";
    private SharedPreferences preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.background_video);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        avi.hide();


//        Button b = (Button) findViewById(R.id.bypass);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Registered_User_Id.admin = "ADMIN";
//                startActivity(homepageIntent);
//            }
//        });

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

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            avi.show();
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
        avi.show();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, REQ_CODE);
    }


    public void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            account = result.getSignInAccount();
            ref = FirebaseDatabase.getInstance().getReference().child("Registered Users");
            Registered_User_Id.registered_user_id = account.getId();
            preference = getSharedPreferences(PREFERENECE, MODE_PRIVATE);
            SharedPreferences.Editor editorid = preference.edit();
            editorid.putString("ID", account.getId());
            editorid.apply();
            Registered_User_Id.registered_user_email = account.getEmail();
            accountcheck = account.getId();

            final Query query = ref.orderByChild("Google_ID").equalTo(accountcheck);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    avi.hide();
                    Intent intent;
                    if (dataSnapshot.getValue() == null) {
                        intent = new Intent(LogInPage.this, RegisterPage.class);
                    } else {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Registered_User_Id.name = snapshot.child("Name").getValue().toString();
                            Registered_User_Id.admin = snapshot.child("Designation").getValue().toString();
                        }

                        intent = new Intent(LogInPage.this, NewHomePage.class);
                    }

                    query.removeEventListener(this);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            editor.putFloat(getString(R.string.LOGIN_TYPE), 2);
            editor.commit();

        } else {
            avi.hide();
            Toast.makeText(getApplicationContext(), "Google Sign in failed!", Toast.LENGTH_LONG).show();
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