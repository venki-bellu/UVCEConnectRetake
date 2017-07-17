package com.venkibellu.myapplication;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class QuestionPapers extends AppCompatActivity {
    private AutoCompleteTextView subjectSuggestionBox;
    private String[] subjectsList;
    private ArrayAdapter<String> suggestionsAdapter;
    private Spinner branchSpinner, yearSpinner;
    private String branch;
    private Integer year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_papers);

        subjectSuggestionBox = (AutoCompleteTextView) findViewById(R.id.suggestionTextView);

        branchSpinner = (Spinner) findViewById(R.id.branchSpinner);
        yearSpinner = (Spinner) findViewById(R.id.yearSpinner);

        branch = branchSpinner.getSelectedItem().toString();
        year = Integer.parseInt(yearSpinner.getSelectedItem().toString());

        branchSpinner.setOnItemSelectedListener(new branchSelectedListener());
        yearSpinner.setOnItemSelectedListener(new yearSelectedListener());

        subjectsList = getResources().getStringArray(R.array.FirstYearSubjects);
        suggestionsAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_dropdown_item_1line, subjectsList);

        System.out.println("String from resource: " + Arrays.toString(subjectsList));
        subjectSuggestionBox.setAdapter(suggestionsAdapter);
    }

    private class branchSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            branch = adapterView.getSelectedItem().toString();
            Toast.makeText(getApplicationContext(), branch, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            // required implementation.
        }
    }

    private class yearSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            year = Integer.parseInt(adapterView.getSelectedItem().toString());
            Toast.makeText(getApplicationContext(), year.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            // required implementation.
        }
    }

    public void downloadButtonClicked(View view) {
        Toast.makeText(getApplicationContext(), "No files yet!", Toast.LENGTH_SHORT).show();
    }
}
