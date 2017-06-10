package com.example.srkanna.location;

import android.os.Bundle;
import android.util.Log;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SettingsActivity","Entered Settings Activity");
        setContentView(R.layout.activity_settings);
    }
}


