package com.example.srkanna.location;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {
    Button mButton;
    EditText mEdit;
    EditText mCountry;

    ImageButton london;
    ImageButton dubai;
    ImageButton paris;
    ImageButton singaore;
    ImageButton tokyo;
    ImageButton newyork;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (Button)findViewById(R.id.button_location);
        london = (ImageButton) findViewById(R.id.london);
        paris = (ImageButton) findViewById(R.id.paris);
        dubai = (ImageButton) findViewById(R.id.dubai);
        singaore = (ImageButton) findViewById(R.id.singapore);
        tokyo = (ImageButton) findViewById(R.id.tokyo);
        newyork = (ImageButton) findViewById(R.id.newyork);

        final String londonLoc= "london,england";
        final String parisLoc= "paris,france";
        final String dubaiLoc= "dubai,uae";
        final String singaporeLoc= "singapore,singapore";
        final String nyLoc= "newyork,usa";
        final String tokyoLoc= "tokyo,japan";

        london.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start NewActivity.class
                Intent i = new Intent(MainActivity.this,
                        ChooseActivity.class);
                i.putExtra("location",londonLoc);
                startActivity(i);
            }
        });
        singaore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start NewActivity.class
                Intent i = new Intent(MainActivity.this,
                        ChooseActivity.class);
                i.putExtra("location",singaporeLoc);
                startActivity(i);
            }
        });
        newyork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start NewActivity.class
                Intent i = new Intent(MainActivity.this,
                        ChooseActivity.class);
                i.putExtra("location",nyLoc);
                startActivity(i);
            }
        });
        dubai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start NewActivity.class
                Intent i = new Intent(MainActivity.this,
                        ChooseActivity.class);
                i.putExtra("location",dubaiLoc);
                startActivity(i);
            }
        });
        tokyo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start NewActivity.class
                Intent i = new Intent(MainActivity.this,
                        ChooseActivity.class);
                i.putExtra("location",tokyoLoc);
                startActivity(i);
            }
        });
        paris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start NewActivity.class
                Intent i = new Intent(MainActivity.this,
                        ChooseActivity.class);
                i.putExtra("location",parisLoc);
                startActivity(i);
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent i = new Intent(MainActivity.this,
                        ChooseActivity.class);
                mEdit = (EditText)findViewById(R.id.city);
                mCountry = (EditText)findViewById(R.id.country);

                String city = mEdit.getText().toString();
                city = city.replace("\\s+","");

                String country = mCountry.getText().toString();
                country = country.replace("\\s+","");

                String Location = city+","+country;

                //  String location = mEdit.getText().toString();
                i.putExtra("location",Location);
                 startActivity(i);
            }
        });

    }


}
