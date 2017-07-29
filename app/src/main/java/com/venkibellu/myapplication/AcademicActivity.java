package com.venkibellu.myapplication;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class AcademicActivity extends AppCompatActivity {
    private Spinner branchSpinner, semesterSpinner;
    private String fileName, downloadURL;
    private RadioButton syllabusRadioButton;
    private TextView noteTextView;
    Intent sendIntent;
    Context context = this;

    private final String PREFERENECE = "UVCE-prefereceFile";
    private SharedPreferences preference;
    String setting = "AskAgain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic);

        branchSpinner = (Spinner) findViewById(R.id.branchSpinner);
        semesterSpinner = (Spinner) findViewById(R.id.semesterSpinner);

        syllabusRadioButton = (RadioButton) findViewById(R.id.syllabusRadioButton);
        noteTextView = (TextView) findViewById(R.id.noteTextView);

        sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "1917uvce@gmail.com", null));

        preference = getSharedPreferences(PREFERENECE, MODE_PRIVATE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.academic_page_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.academicPage_contribute:
                context.startActivity(Intent.createChooser(sendIntent, null));
                return true;

            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return false;
    }

    public void downloadButtonClicked(View view) {
        noteTextView.setText("");
        String branch = getBranch();
        Integer year = getSemester();

        /* invoke the class URLGetter, context needs to be passed
            to access the strings in resource file.
         */
        URLGetter urlGetter = new URLGetter(getApplicationContext());

        // get the downloadURL on the basis of branch and semester selected.
        if (syllabusRadioButton.isChecked()) {
            noteTextView.setText(Html.fromHtml(getString(R.string.Note)));

            downloadURL = urlGetter.getSyllabusURL(branch, year);
            fileName = branch.replaceAll("\\s+", "-") + '-' + String.valueOf(year) + "-Syllabus.pdf";

        } else {
            downloadURL = urlGetter.getQuestionPaperURL(branch, year);
            fileName = branch.replaceAll("\\s+", "-");
        }

        // if syllabus not available return.
        if (downloadURL.isEmpty()) {
            Toast.makeText(getApplicationContext(), "No resources found.\n" +
                                                    "Will be added soon! \n" +
                                                    "Please contribute if available.",
                                                        Toast.LENGTH_LONG).show();
            return ;
        }

        if (preference.contains(setting) && preference.getBoolean(setting, false)) {
            checkStoragePermission();
        } else {
            showDisclaimer();
        }
    }

    private void showDisclaimer() {
        AlertDialog.Builder disclaimerDialog = new AlertDialog.Builder(AcademicActivity.this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View view = inflater.inflate(R.layout.disclaimer, null);
        final CheckBox dontShowAgain = (CheckBox) view.findViewById(R.id.checkBoxid);

        disclaimerDialog.setView(view)
                        .setTitle("Disclaimer")
                        .setMessage(R.string.disclaimer)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                SharedPreferences.Editor editor = preference.edit();
                                editor.putBoolean(setting, dontShowAgain.isChecked());
                                editor.apply();

                                checkStoragePermission();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setCancelable(false)
                        .show();
    }

    private String getBranch() {
        return branchSpinner.getSelectedItem().toString();
    }

    private Integer getSemester() {
        return Integer.parseInt(semesterSpinner.getSelectedItem().toString());
    }

    protected void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) { // if android version >= 6.0
            if (ContextCompat.checkSelfPermission(
                    getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // if permission was not granted initially, ask the user again.
                ActivityCompat.requestPermissions(AcademicActivity.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            } else {

                // if android >= 6.0 and permission already granted, continue to download.
                startDownload();
            }
        } else {

            // if android < 6.0 continue to download, no need to ask permission again.
            startDownload();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // if permission allowed by the user, continue to download.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startDownload();
                } else {

                    // permission not granted, download can't take place.
                    Toast.makeText(getApplicationContext(),
                            "Permission denied to read External storage",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // execute the AsyncTask to download Files.
    private void startDownload() {
        new downloadSyllabus().execute(downloadURL);
    }


    // AsyncTask which will take care of the download using, download manager.
    private class downloadSyllabus extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(String... url) {
            File directory = new File(Environment.getExternalStorageDirectory() + "/UVCE-Connect");

            if (!directory.exists()) {
                directory.mkdirs();
            }

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url[0]));
            request.setDescription("Syllabus")
                    .setTitle(fileName)
                    .setNotificationVisibility(DownloadManager.Request
                                                              .VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir("/UVCE-Connect", fileName)
                    .allowScanningByMediaScanner();

            DownloadManager manager = (DownloadManager)
                                getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
            return null;
        }
    }
}
