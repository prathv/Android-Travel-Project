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
    String getLocation = new String();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_activity);

        //TextView tv = (TextView)findViewById(R.id.TV_choose_activity);
        Intent i = getIntent();
        Bundle b = getIntent().getExtras();

        if (b != null ) {
             getLocation = (String) b.get("location");
          //  tv.setText(getLocation);
        }
        Log.d("ChooseActivity","ChooseActivity Location received is"+getLocation);


        mYelpButton = (Button)findViewById(R.id.button4);

        mYelpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent i = new Intent(ChooseActivity.this, YelpActivity.class);
                //  String location = mEdit.getText().toString();
                i.putExtra("Location",getLocation);
                startActivity(i);
            }
        });
    }
}
