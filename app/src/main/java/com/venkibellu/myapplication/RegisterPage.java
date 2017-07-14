package com.venkibellu.myapplication;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterPage extends AppCompatActivity {

    Spinner spinner;
    TextView textView;
    EditText registerNumber;
    EditText yearOfJoining;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        spinner=(Spinner)findViewById(R.id.registerpage_branch);
        textView=(TextView)findViewById(R.id.registerpage_title_rollno);
        registerNumber=(EditText)findViewById(R.id.registerpage_registerno);
        yearOfJoining=(EditText)findViewById(R.id.registerpage_yearofjoining);
        radioGroup=(RadioGroup)findViewById(R.id.registerpage_radiogroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId)
                {
                    case R.id.registerpage_status_student:
                        textView.setVisibility(View.VISIBLE);
                        registerNumber.setVisibility(View.VISIBLE);
                        break;
                    case R.id.registerpage_status_Alumni:
                        textView.setVisibility(View.INVISIBLE);
                        registerNumber.setVisibility(View.INVISIBLE);
                        break;

                }
            }
        });
    }

    public void registerUser(View view)
    {
        Toast.makeText(getApplicationContext(),"Registration Success",Toast.LENGTH_SHORT).show();
    }
}
