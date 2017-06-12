package com.example.srkanna.location;

import android.os.Bundle;
import android.util.Log;

import android.support.v7.app.AppCompatActivity;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SettingsActivity","Entered Settings Activity");
        setContentView(R.layout.yelp_activity_settings);
    }
}


