package com.example.srkanna.location;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;;
import android.widget.TextView;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {
    Button mButton;
    EditText mEdit;
    EditText mCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (Button)findViewById(R.id.button_location);

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
