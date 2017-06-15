package com.example.srkanna.location;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;



public class ChooseActivity extends AppCompatActivity {

    private Button mYelpButton;
    private Button mOpenWeatherButton;
    private Button mDirections;
    String getLocation = new String();
    Bundle b;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_activity);

        //TextView tv = (TextView)findViewById(R.id.TV_choose_activity);
        Intent i = getIntent();
        b = getIntent().getExtras();

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

        mDirections = (Button) findViewById(R.id.directions);

        mDirections.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class

//                Bundle b = getIntent().getExtras();
//                String locaa=(String) b.get("location");
                Intent i = new Intent(ChooseActivity.this, PolygonActivityForAsyncLoader.class);
                //  String location = mEdit.getText().toString();
                i.putExtra("Location", getLocation);
                startActivity(i);
            }
        });
    }

}
