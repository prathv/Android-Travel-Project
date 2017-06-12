// Created by Alex D. Hoffer

package com.example.srkanna.location.utils;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import android.net.Uri;
import android.util.Log;

import com.example.srkanna.location.OpenWeatherActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

// Utility class to get the URL needed and parse JSON returned
public class OpenWeatherUtils
{
    // http://api.openweathermap.org/data/2.5/forecast?q={city name},{country code}&ce0378671da4c2e0f484c8280bd90bb2

    private final static String OPENWEATHER_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast";
    private final static String OPENWEATHER_QUERY_PARAM = "q";
    private final static String OPENWEATHER_APIID_PARAM = "appid";
    private final static String OPENWEATHER_APIID = "ce0378671da4c2e0f484c8280bd90bb2";

    public static String buildOpenWeatherURL()
    {
        return Uri.parse(OPENWEATHER_BASE_URL).buildUpon()
                .appendQueryParameter(OPENWEATHER_QUERY_PARAM, OpenWeatherActivity.Location)
                .appendQueryParameter(OPENWEATHER_APIID_PARAM, OPENWEATHER_APIID)
                .build()
                .toString();
    }

    public static String convertTemperature(String Kelvin)
    {
        double numKelvin = 0;
        numKelvin = Double.parseDouble(Kelvin);

        double numFahrenheit = 0;
        double numCelsius = 0;

        numCelsius = numKelvin - 273.0;

        numFahrenheit = (numCelsius * 9.0/5.0) + 32.0;

        String actualTemp = String.format("%.2f", numFahrenheit);
        actualTemp = actualTemp + "F";

        return actualTemp;
    }

    public static ArrayList<String> parseOpenWeatherResultsJSON(String fullWeatherJSON)
    {
        try
        {
            JSONObject fullJSONString = new JSONObject(fullWeatherJSON); // treat the string passed in like a JSON object
            JSONArray weatherObjects = fullJSONString.getJSONArray("list"); // use the member function to parse the different objects in the object

            ArrayList<String> forecasts = new ArrayList<String>();

            for (int i = 0; i < weatherObjects.length(); i++)
            {
                String forecast = new String();
                JSONObject weatherObject = weatherObjects.getJSONObject(i);

                // get the date and time
                String dateTime = weatherObject.getString("dt_txt");

                // get temperature
                String temperature = weatherObject.getJSONObject("main").getString("temp");
                String realTemp = convertTemperature(temperature);

                // get the weather sub array, get the thing at its first index, get the main string
                JSONArray weatherSubArray = weatherObject.getJSONArray("weather");
                JSONObject weatherSubObject = weatherSubArray.getJSONObject(0);
                String weather = weatherSubObject.getString("main");

                Log.d("OpenWeatherUtils", "weather: " + weather);

                forecast = dateTime + " - " + realTemp + " - " + weather;
                Log.d("OpenWeatherUtils", "Forecast: " + forecast);
                forecasts.add(forecast);
            }

            return forecasts;
        }

        catch (JSONException e)
        {
            return null;
        }
    }

}
