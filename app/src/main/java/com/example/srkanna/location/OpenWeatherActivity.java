// Alex D. Hoffer: Get and parse JSON results corresponding to the weather of the location
// indicated by the user.

package com.example.srkanna.location;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.srkanna.location.utils.OpenWeatherNetworkUtils;
import com.example.srkanna.location.utils.OpenWeatherUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Alex D. Hoffer
 */

public class OpenWeatherActivity extends AppCompatActivity implements ForecastAdapter.OnForecastClickListener
{

    public static String Location;

    private RecyclerView mForecastListRV; // present the forecasts we get from openweather
    private ForecastAdapter mForecastAdapter;
    private ProgressBar mLoadingIndicatorPB; // for our OnPreExecute, a loading bar
    private TextView mLoadingErrorMessageTV; // if there is an error in accessing the URL, present it here

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openweather_main);

        mLoadingIndicatorPB = (ProgressBar)findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessageTV = (TextView)findViewById(R.id.tv_loading_error_message);

        mForecastListRV = (RecyclerView)findViewById(R.id.rv_forecast_list);

        mForecastListRV.setLayoutManager(new LinearLayoutManager(this));
        mForecastListRV.setHasFixedSize(true);

        mForecastAdapter = new ForecastAdapter(this);
        mForecastListRV.setAdapter(mForecastAdapter);

        Bundle b = getIntent().getExtras();

        Location = b.getString("Location");

        Log.d("OpenWeatherMap","Bundle Location is "+ Location);

        // Alex: make it so the title isn't crunched together
        String[] parts = Location.split(",");
        String titleString1 = parts[0];
        String titleString2 = parts[1];

        setTitle("Weather Forecast for " + titleString1 + ", " + titleString2);

        lookUpWeather(); // load data asynchronously
    }

    // When a forecast item is clicked, load a new activity using Intents.
    @Override
    public void onForecastClick(String forecast)
    {
        //Toast.makeText(this, forecast, Toast.LENGTH_SHORT).show();

        // Create a new intent, pass it the main activity as its previous activity and the new activity class
        //Intent detailedForecastActivityIntent = new Intent(this, DetailedForecastActivity.class);

        // putExtra method shoots the forecast data over to the new activity
       // detailedForecastActivityIntent.putExtra("Detailed_Forecast_Data", forecast);
       // startActivity(detailedForecastActivityIntent);

    }

    // Called from main activity, this initiates a background load of weather data.
    // Step 1: Build a URL that corresponds to Corvallis, OR.
    // Step 2: Call the background task method
    private void lookUpWeather()
    {
        String OpenWeatherURL = OpenWeatherUtils.buildOpenWeatherURL();
        Log.d("MainActivity", "URL passed: " + OpenWeatherURL);
        new OpenWeatherTask().execute(OpenWeatherURL);
    }

    // subclass of AsyncTask that:
    // 1) Displays progress bar
    // 2) Gets forecast data as JSON string
    // 3) Cleans up errors, removes progress bar after execution
    public class OpenWeatherTask extends AsyncTask<String, Void, String>
    {
        // Display progress bar.
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute(); // call the parent class's pre execute function
            mLoadingIndicatorPB.setVisibility(View.VISIBLE); // set the bar to be visisble
        }

        // Get forecast data as JSON string asynchronously
        @Override
        protected String doInBackground(String... params)
        {
            String OpenWeatherURL = params[0]; // passed in, built URL
            String searchResults = null; // JSON string

            try
            {
                searchResults = OpenWeatherNetworkUtils.doHTTPGet(OpenWeatherURL); // use the network utility class to perform a GET request
            }

            catch (IOException e)
            {
                e.printStackTrace(); // Invalid url given
            }

            return searchResults;
        }

        // If a valid JSON string is received, parse it and add the resulting Strings to our array.
        @Override
        protected void onPostExecute(String s)
        {
            Log.d("Onpostexecute", "String: " + s);
            mLoadingIndicatorPB.setVisibility(View.INVISIBLE); // make the progress bar go away

            if (s != null) // no error
            {
                mLoadingErrorMessageTV.setVisibility(View.INVISIBLE); // No error so make error message invisible
                mForecastListRV.setVisibility(View.VISIBLE); // We've successfully retrieved data so present it
                ArrayList<String> forecasts = OpenWeatherUtils.parseOpenWeatherResultsJSON(s);

                mForecastAdapter.updateForecastData(forecasts); // Parse the string results using the utility class and put them in a list for presentation
            }

            else
            {
                mForecastListRV.setVisibility(View.INVISIBLE);
                mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
            }
        }
    }
}
