package com.example.srkanna.location.utils;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hessro on 4/25/17.
 */

public class Connection {

    private static final OkHttpClient mHTTPClient = new OkHttpClient();

    public static String doHTTPGet(String url) throws IOException {
        Response response =null;
        try {
        Request request = new Request.Builder()
                .url(url)
                .build();
            Log.d("request is",request.toString());
         response = mHTTPClient.newCall(request).execute();

           return   response.body().string();
        }
        catch (Exception e) {
            Log.d("Failed in connn","I am found");
            e.printStackTrace();

        }
       /* finally {

            response.close();
        }*/
        return null;
    }
}
