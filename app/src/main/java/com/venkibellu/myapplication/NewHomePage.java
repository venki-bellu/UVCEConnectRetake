package com.venkibellu.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewHomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    Intent loginPageIntent;
    SharedPreferences logintype;
    GoogleSignInOptions gso;
    GoogleApiClient googleApiClient;
    private Animation animation;
    Context context;
    Intent sendIntent;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    private final String PREFERENECE = "UVCE-prefereceFile-Download";
    private SharedPreferences preference;
    String setting = "AskAgain";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_home_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.up_from_bottom);

        context = this;

        TextView news = (TextView) findViewById(R.id.newsfeed);
        news.startAnimation(animation);

        TextView campus = (TextView) findViewById(R.id.campus_says);
        campus.startAnimation(animation);

        TextView academics = (TextView) findViewById(R.id.syllabus);
        academics.startAnimation(animation);

        loginPageIntent = new Intent(this, LogInPage.class);
        logintype = getSharedPreferences(getString(R.string.PREF_FILE), MODE_PRIVATE);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "1917uvce@gmail.com", null));

        preference = getSharedPreferences(PREFERENECE, MODE_PRIVATE);

        if (!(preference.contains(setting) && preference.getBoolean(setting, false))) {
            showDisclaimer();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void onSyllabusImageViewClicked(View view) {
        Intent academicPageIntent = new Intent(this, Academic.class);
        startActivity(academicPageIntent);
    }

    public void onNewsImageViewClicked(View view) {
        Intent AcademicPageIntent = new Intent(this, News.class);
        startActivity(AcademicPageIntent);
    }

    public void onCampusImageViewClicked(View view) {
        Intent AcademicPageIntent = new Intent(this, Campus_Says.class);
        startActivity(AcademicPageIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        if(Registered_User_Id.admin.equals("ADMIN"))
        menuInflater.inflate(R.menu.admin_menu, menu);
        else
        menuInflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.signout) {
            Float type = logintype.getFloat(getString(R.string.LOGIN_TYPE), 0);

            if (type == 0) {
                Toast.makeText(getApplicationContext(), "Not Logged In", Toast.LENGTH_SHORT).show();
            } else if (type == 2) {
                Toast.makeText(getApplicationContext(), "Signed out successfully", Toast.LENGTH_LONG).show();
                Auth.GoogleSignInApi.signOut(googleApiClient);
                startActivity(loginPageIntent);
                finish();
            }

        } else if (item.getItemId() == R.id.contact_us) {
            context.startActivity(Intent.createChooser(sendIntent, null));
            return true;
        }
        else if(item.getItemId()== R.id.view_users)
        {
            Toast.makeText(getApplicationContext(),"Viewing users",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(NewHomePage.this,ViewUsers.class));
        }
        else if(item.getItemId()==R.id.change_to_alumni)
        {
            Toast.makeText(getApplicationContext(),"Promoted to Alumni",Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.About_us:
                Intent i1 = new Intent(this, about_us.class);
                startActivity(i1);
                break;

            case R.id.Associations:
                Intent i2 = new Intent(this, associations.class);
                startActivity(i2);
                break;

            case R.id.PO:
                Intent i3 = new Intent(this, po.class);
                startActivity(i3);
                break;

            case R.id.Clubs:
                Intent i5 = new Intent(this, activities.class);
                startActivity(i5);
                break;

            case R.id.Fests:
                Intent i6 = new Intent(this, fests.class);
                startActivity(i6);
                break;

            case R.id.Help:
                Intent i8 = new Intent(this, aboutapp.class);
                startActivity(i8);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void showDisclaimer() {
        AlertDialog.Builder disclaimerDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View view = inflater.inflate(R.layout.disclaimer_image, null);
        final CheckBox dontShowAgain = (CheckBox) view.findViewById(R.id.checkBoxid_image);

        disclaimerDialog.setView(view)
                .setTitle("Please Note")
                .setMessage("\nLong press an image in News feed or Campus Says to download it.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        SharedPreferences.Editor editor = preference.edit();
                        editor.putBoolean(setting, dontShowAgain.isChecked());
                        editor.apply();
                    }
                })
                .setCancelable(false)
                .show();
    }
}
