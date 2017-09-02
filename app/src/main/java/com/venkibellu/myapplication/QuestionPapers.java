package com.venkibellu.myapplication;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class QuestionPapers extends Fragment implements View.OnClickListener {
    private Spinner branchSpinner, semesterSpinner;
    private String fileName;
    private String branch, semester;
    private final String QP = "Question_Papers";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_paper, container, false);

        Button button = (Button) view.findViewById(R.id.download_button);
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
        String b = getBranch();
        Integer s = getSemester();

        if (s > 8 && !b.equals(getContext().getString(R.string.arch))) {
            Toast.makeText(getContext(), "Invalid Selection", Toast.LENGTH_SHORT).show();
            return;
        }

        setBranchSemester(b, s);

        fileName = branch.replaceAll("\\s+", "-") + '-' + String.valueOf(semester) + ".pdf";

        checkStoragePermission();
    }

    private String getBranch() {
        return branchSpinner.getSelectedItem().toString();
    }

    private Integer getSemester() {
        return Integer.parseInt(semesterSpinner.getSelectedItem().toString());
    }

    private void setBranchSemester(String b, Integer s) {
        Map<String, String> branchMapper = new HashMap<>();
        Map<Integer, String> semesterMapper = new HashMap<>();

        final String arch = getContext().getString(R.string.arch),
                cse = getContext().getString(R.string.cse),
                ise = getContext().getString(R.string.ise),
                ce = getContext().getString(R.string.ce),
                me = getContext().getString(R.string.me),
                eee = getContext().getString(R.string.eee),
                ece = getContext().getString(R.string.ece);

        branchMapper.put(arch, "ARCH");
        branchMapper.put(cse, "CSE");
        branchMapper.put(ise, "ISE");
        branchMapper.put(eee, "EEE");
        branchMapper.put(ece, "ECE");
        branchMapper.put(ce, "CE");
        branchMapper.put(me, "ME");

        semesterMapper.put(1, "Sem_1");
        semesterMapper.put(2, "Sem_2");
        semesterMapper.put(3, "Sem_3");
        semesterMapper.put(4, "Sem_4");
        semesterMapper.put(5, "Sem_5");
        semesterMapper.put(6, "Sem_6");
        semesterMapper.put(7, "Sem_7");
        semesterMapper.put(8, "Sem_8");
        semesterMapper.put(9, "Sem_9");
        semesterMapper.put(10, "Sem_10");

        branch = branchMapper.get(b);
        semester = semesterMapper.get(s);
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
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(QP);

        ref.addValueEventListener(new ValueEventListener() {
            String url, id, downloadURL = "https://docs.google.com/uc?id=[FILE_ID]&export=download";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (semester.equals("Sem_1") && !branch.equals("ARCH")) {
                    url = dataSnapshot.child("Sem_1").getValue().toString();
                } else if(semester.equals("Sem_2") && !branch.equals("ARCH")) {
                    url = dataSnapshot.child("Sem_2").getValue().toString();
                } else {
                    url = dataSnapshot.child(branch).child(semester).getValue().toString();
                }

                if (url.equals("-1")) {
                    Toast.makeText(getContext(), "Resource not found\n" +
                                    "Will be added soon!\n" +
                                    "Please contribute if available.",
                            Toast.LENGTH_LONG).show();

                    return;
                }

                id = getID(url);
                downloadURL = downloadURL.replace("[FILE_ID]", id);
                Download md = new Download(getContext(), fileName, downloadURL);
                md.start();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // returns the ID from the google drive URL.
    private String getID(String url) {
        String ID = "";

        for (int i = url.length() - 1; i >= 0; --i) {
            if (url.charAt(i) != '=') {
                ID = url.charAt(i) + ID;
            } else {
                break;
            }
        }

        return ID;
    }
}
