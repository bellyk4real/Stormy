package com.kenneth.stormy;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;


public class MainActivity extends ActionBarActivity {

     private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String ApiKey = "451e6d0d0b44e059aa1ec0670eac2a86";
        double Longitude= 37.8267;
        double Latitude = -122.423;
        String forecastUrl = "https://api.forecast.io/forecast/"+ ApiKey +
                "/" + Longitude+ ","+ Latitude;

        OkHttpClient client = new OkHttpClient();
        // Build a request that the client will send to the server
        // create a client, request, call.
        Request request = new Request.Builder()
                .url(forecastUrl)
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            // check if request was successful
            if (response.isSuccessful()) {

                Log.v(TAG, response.body().string());
            }
        } catch (IOException e) {
            Log.e(TAG, "we caught exception", e);
        }
    }
}
