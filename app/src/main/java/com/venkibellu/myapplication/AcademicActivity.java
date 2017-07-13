package com.venkibellu.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

public class AcademicActivity extends AppCompatActivity {

    private Spinner branchSpinner;
    private Spinner yearSpinner;

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

        Toast.makeText(getApplicationContext(),
                "Branch: " + branch + "\nYear: " + year,
                Toast.LENGTH_SHORT).show();
    }

    private String getBranch() {
        return branchSpinner.getSelectedItem().toString();
    }

    private Integer getYear() {
        return Integer.parseInt(yearSpinner.getSelectedItem().toString());
    }
}
