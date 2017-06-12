package com.example.srkanna.location;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;



public class ChooseActivity extends AppCompatActivity {

    private Button mYelpButton;
    private Button mOpenWeatherButton;
    String getLocation = new String();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_activity);

        //TextView tv = (TextView)findViewById(R.id.TV_choose_activity);
        Intent i = getIntent();
        Bundle b = getIntent().getExtras();

        if (b != null) {
            getLocation = (String) b.get("location");
            //  tv.setText(getLocation);
        }
        Log.d("ChooseActivity", "ChooseActivity Location received is" + getLocation);

        mYelpButton = (Button) findViewById(R.id.button4);

        mYelpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent i = new Intent(ChooseActivity.this, YelpActivity.class);
                //  String location = mEdit.getText().toString();
                i.putExtra("Location", getLocation);
                startActivity(i);
            }
        });

        // Alex H.: Hook it up so clicking the "Weather" button launches a new activity
        // with weather information corresponding to what the user put
        mOpenWeatherButton = (Button) findViewById(R.id.button);

        mOpenWeatherButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg) {
                Intent weatherIntent = new Intent(ChooseActivity.this, OpenWeatherActivity.class);
                weatherIntent.putExtra("Location", getLocation); // pass it the city, state info
                startActivity(weatherIntent); // launch the activity
            }
        });
    }

}
