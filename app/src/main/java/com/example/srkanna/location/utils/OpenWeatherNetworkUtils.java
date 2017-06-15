package com.example.srkanna.location.utils;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Courtesy of my teacher, Rob Hess.
 */

public class OpenWeatherNetworkUtils {

    private static final OkHttpClient mHTTPClient = new OkHttpClient();

    public static String doHTTPGet(String url) throws IOException {
        Request request;
        Response response = null;
        try {
             request = new Request.Builder()
                    .url(url)
                    .build();
             response = mHTTPClient.newCall(request).execute();
        }
        catch(Exception e){
            e.printStackTrace();
            Log.d("Openweather","Error on get directions");
        }

        try {
            return response.body().string();
        } finally {
            response.close();
        }
    }
}