package com.kenneth.stormy;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.jar.JarException;


public class MainActivity extends ActionBarActivity {

     private CurrentWeather mCurrentweather;

     private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String ApiKey = "451e6d0d0b44e059aa1ec0670eac2a86";
        double Longitude = 37.8267;
        double Latitude = -122.423;
        String forecastUrl = "https://api.forecast.io/forecast/" + ApiKey +
                "/" + Longitude + "," + Latitude;


        if (isNetworkAvailable()) {

        OkHttpClient client = new OkHttpClient();
        // Build a request that the client will send to the server
        // Steps------create a client, request, call.
        Request request = new Request.Builder()
                .url(forecastUrl)
                .build();
        Call call = client.newCall(request);
        //enqueue executes the call in a worker thread
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    // check if request was successful
                    if (response.isSuccessful()) {
                      String jsonData = response.body().string() ;
                      mCurrentweather = getCurrentDetails(jsonData);

                    } else {
                        alertUserAboutError();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "we caught exception ", e);
                }
                catch (JSONException e) {
                    Log.e(TAG, "we caught exception ", e);
                }
            }

        });

        } else {
            Toast.makeText(this, getString(R.string.network_unavailable_message),
                    Toast.LENGTH_LONG).show();
        }
               Log.d(TAG, "main UI thread is running");
    }
  // THROWS JSONEXCEPTION TO WHOEVER IS CALLING getCurrentDetails;
    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timeZone =  forecast.getString("timezone");
        JSONObject currently  = forecast.getJSONObject("currently");

        CurrentWeather currentWeather = new CurrentWeather();

        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setTemperature(currently.getDouble("temperature"));
        currentWeather.setTimeZone(timeZone);

        return  currentWeather;
    }

    private boolean isNetworkAvailable() {

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        Boolean isAvalaible = false;
        // checking if there is a network and if it is available
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvalaible = true;
        }
          return isAvalaible;
    }

    private void alertUserAboutError() {
         AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

}
