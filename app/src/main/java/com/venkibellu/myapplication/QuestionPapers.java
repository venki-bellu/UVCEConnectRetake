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
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class QuestionPapers extends Fragment implements View.OnClickListener {
    private Button button;
    private Spinner branchSpinner, semesterSpinner;
    private String fileName, downloadURL;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_paper, container, false);

        button = (Button) view.findViewById(R.id.download_button);
        button.setOnClickListener(this);
        branchSpinner = (Spinner) view.findViewById(R.id.branchSpinner);
        semesterSpinner = (Spinner) view.findViewById(R.id.semesterSpinner);

        return view;
    }

    @Override
    public void onClick(View v) {
        downloadButtonClicked();
    }

    public void downloadButtonClicked() {
        String branch = getBranch();
        Integer semester = getSemester();

        if (semester > 8 && !branch.equals(getContext().getString(R.string.arch))) {
            Toast.makeText(getContext(), "Invalid Selection", Toast.LENGTH_SHORT).show();
            return ;
        }

        /*
            invoke the class URLGetter, context needs to be passed
            to access the strings in resource file.
         */
        URLGetter urlGetter = new URLGetter(getContext());

        // get the downloadURL on the basis of branch and semester selected.
        downloadURL = urlGetter.getQuestionPaperURL(branch, semester);
        fileName = branch.replaceAll("\\s+", "-") + '-' + String.valueOf(semester) + ".pdf";


        // if syllabus not available return.
        if (downloadURL.isEmpty()) {
            Toast.makeText(getActivity(), "No resources found.\n" +
                                          "Will be added soon! \n" +
                                          "Please contribute if available.",
                    Toast.LENGTH_LONG).show();
            return ;
        }

        checkStoragePermission();
    }

    private String getBranch() {
        return branchSpinner.getSelectedItem().toString();
    }

    private Integer getSemester() {
        return Integer.parseInt(semesterSpinner.getSelectedItem().toString());
    }

    protected void checkStoragePermission() {
        // if android version >= 6.0
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(
                    getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // if permission was not granted initially, ask the user again.
                ActivityCompat.requestPermissions(this.getActivity(),
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
                    Toast.makeText(getContext(),
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
            Toast.makeText(getContext(), "Downloading...", Toast.LENGTH_SHORT).show();
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
                    getContext().getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
            return null;
        }
    }
}
