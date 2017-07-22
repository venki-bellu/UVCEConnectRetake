package com.venkibellu.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class clubs extends AppCompatActivity {

    Intent intent1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clubs);
        seOnClickListener();
    }


    public void seOnClickListener()
    {
        Button b2= (Button)findViewById(R.id.button2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent1 = new Intent("com.venkibellu.myapplication.g2c2");
                startActivity(intent1);    }
        });

                Button b3= (Button)findViewById(R.id.button3);
                b3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent2 = new Intent("com.venkibellu.myapplication.vinimaya");
                        startActivity(intent2);    }
                });
                        Button b4= (Button)findViewById(R.id.button4);
                        b4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent3 = new Intent("com.venkibellu.myapplication.sae");
                                startActivity(intent3);    }
                        });
                                Button b5= (Button)findViewById(R.id.button5);
                                b5.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent4 = new Intent("com.venkibellu.myapplication.chethana");
                                        startActivity(intent4);    }
                                });
                                        Button b6= (Button)findViewById(R.id.button6);
                                        b6.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent5 = new Intent("com.venkibellu.myapplication.thatva");
                                                startActivity(intent5);    }
                                        });
                                                Button b8= (Button)findViewById(R.id.button8);
                                                b8.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent6 = new Intent("com.venkibellu.myapplication.chakravyuha");
                                                        startActivity(intent6);
            }
        });Button b9= (Button)findViewById(R.id.button9);
        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent6 = new Intent("com.venkibellu.myapplication.ieee");
                startActivity(intent6);
            }
        });

                Button b10= (Button)findViewById(R.id.button10);
                b10.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               Intent intent6 = new Intent("com.venkibellu.myapplication.ncc");
                                               startActivity(intent6);
                                           }
                                       });
                        Button b11= (Button)findViewById(R.id.button11);
                        b11.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       Intent intent6 = new Intent("com.venkibellu.myapplication.sports");
                                                       startActivity(intent6);
                                                   }
                                               });

                                Button b12= (Button)findViewById(R.id.button12);
                                b12.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent6 = new Intent("com.venkibellu.myapplication.gb_ram");
                                        startActivity(intent6);
                                    }
                                });
    }


}

