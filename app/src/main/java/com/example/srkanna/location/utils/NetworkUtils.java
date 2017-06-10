package com.example.srkanna.location.utils;

import android.content.ContentValues;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by hessro on 4/25/17.
 */

public class NetworkUtils {

    public static final MediaType xwww
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    private static final OkHttpClient mHTTPClient = new OkHttpClient();

    private static final String Authorization = "Authorization";

    public static String post(String url) throws IOException {

        String client_id = "CsBoHv9RJx1PR_dcRBc3-Q";
        String client_secret = "uHm8HuK65BM9ShuNjv7Y6iPm0Seo8Xs55MRlyOb7QoYViSKXXsu2X5zGWSkrOEx0";
        String grant_type = "client_credentials";


        //String auth_resc = "?client_id="+client_id+"&=="+client_secret+"&grant_type=client_credentials";
        RequestBody body = RequestBody.create(xwww, "");
        Log.d("Network.Utils","url is"+ url);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = mHTTPClient.newCall(request).execute();
        return response.body().string();
    }
    public static String doHTTPGet(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = mHTTPClient.newCall(request).execute();

        try {
            return response.body().string();
        } finally {
            response.close();
        }
    }
    public static String doHTTPGetYelp(String url,String access) throws IOException {

        Log.d("Network utils","Access is "+access);
        Request request = new Request.Builder()
                .url(url)
                .addHeader(Authorization,"Bearer "+access)
                .build();
        Response response = mHTTPClient.newCall(request).execute();

        try {
            return response.body().string();
        } finally {
            response.close();
        }
    }
}
