package com.example.android.githubsearchwithlifecycle.utils;

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
}
