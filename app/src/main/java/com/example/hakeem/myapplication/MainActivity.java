package com.example.hakeem.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    IBMToneAPI ita = new IBMToneAPI();
    JSONObject analyzedJson = ita.analyze("Hello, World!");
    String jString = analyzedJson.toString();
}
