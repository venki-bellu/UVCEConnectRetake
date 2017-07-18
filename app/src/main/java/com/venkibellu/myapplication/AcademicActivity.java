package com.venkibellu.myapplication;

import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic);

        branchSpinner = (Spinner) findViewById(R.id.branchSpinner);
        semesterSpinner = (Spinner) findViewById(R.id.semesterSpinner);

        syllabusRadioButton = (RadioButton) findViewById(R.id.syllabusRadioButton);
        noteTextView = (TextView) findViewById(R.id.noteTextView);
    }

    public void downloadButtonClicked(View view) {
        noteTextView.setText("");
        String branch = getBranch();
        Integer semester = getSemester();

        /* invoke the class URLGetter, context needs to be passed
            to access the strings in resource file.
         */
        URLGetter urlGetter = new URLGetter(getApplicationContext());

        // get the downloadURL on the basis of branch and semester selected.
        if (syllabusRadioButton.isChecked()) {
            int year = (int) Math.ceil(semester / 2.0);
            noteTextView.setText(Html.fromHtml(getString(R.string.Note)));

            downloadURL = urlGetter.getSyllabusURL(branch, year);
            fileName = branch.replaceAll("\\s+", "-") + "-Syllabus.pdf";

        } else {
            downloadURL = urlGetter.getQuestionPaperURL(branch, semester);
            fileName = branch.replaceAll("\\s+", "-");
        }

        // if syllabus not available return.
        if (downloadURL.isEmpty()) {
            Toast.makeText(getApplicationContext(), "No resources found.\n" +
                                                    "Will be added soon!",
                                                        Toast.LENGTH_SHORT).show();

            return ;
        }



        // required for android 6.0 and above.
        checkStoragePermission();
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
                            "Permission denied to read your External storage",
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
            request.setDescription("Syllabus");
            request.setTitle(fileName);
            request.allowScanningByMediaScanner();
            request.setDestinationInExternalPublicDir("/UVCE-Connect", fileName);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            DownloadManager manager = (DownloadManager)
                    getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
            return null;
        }
    }
}
