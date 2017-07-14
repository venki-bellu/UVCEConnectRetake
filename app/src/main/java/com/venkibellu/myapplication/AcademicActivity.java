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
        URLGetter urlGetter = new URLGetter(getApplicationContext());

        Toast.makeText(getApplicationContext(),
                urlGetter.getSyllabusURL(branch, year),
                Toast.LENGTH_SHORT).show();

        System.out.println(urlGetter.getSyllabusURL(branch, year));
    }

    private String getBranch() {
        return branchSpinner.getSelectedItem().toString();
    }

    private Integer getYear() {
        return Integer.parseInt(yearSpinner.getSelectedItem().toString());
    }
}
