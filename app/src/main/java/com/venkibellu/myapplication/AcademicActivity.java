package com.venkibellu.myapplication;

import android.app.DownloadManager;
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
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

public class AcademicActivity extends AppCompatActivity {
    private Spinner branchSpinner, yearSpinner;
    private String fileName, downloadURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic);

        branchSpinner = (Spinner) findViewById(R.id.branchSpinner);
        yearSpinner = (Spinner) findViewById(R.id.yearSpinner);
    }

    public void downloadButtonClicked(View view) {
        String branch = getBranch();
        Integer year = getYear();

        /* invoke the class URLGetter, context needs to be passed
            to access the strings in resource file.
         */
        URLGetter urlGetter = new URLGetter(getApplicationContext());

        // get the downloadURL on the basis of branch and year selected.
        downloadURL = urlGetter.getSyllabusURL(branch, year);

        // if syllabus not available return.
        if (downloadURL.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Sorry! No resources found.",
                                                        Toast.LENGTH_SHORT).show();

            return ;
        }

        // stores the name of the file which will appear on downloading.
        fileName = branch.replaceAll("\\s+", "-") + "-Syllabus.pdf";


        // required for android 6.0 and above.
        checkStoragePermission();
    }

    private String getBranch() {
        return branchSpinner.getSelectedItem().toString();
    }

    private Integer getYear() {
        return Integer.parseInt(yearSpinner.getSelectedItem().toString());
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
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url[0]));
            request.setDescription("Syllabus");
            request.setTitle(fileName);
            request.allowScanningByMediaScanner();
            request.setDestinationInExternalFilesDir(getApplicationContext(),
                                             Environment.DIRECTORY_DOWNLOADS, fileName);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            DownloadManager manager = (DownloadManager)
                    getApplicationContext().getSystemService(DOWNLOAD_SERVICE);
            manager.enqueue(request);
            return null;
        }
    }
}
