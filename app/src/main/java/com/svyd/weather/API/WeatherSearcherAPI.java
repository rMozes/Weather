package com.svyd.weather.API;

import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.svyd.weather.model.WeatherModel;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Svyd on 17.09.2015.
 */
public class WeatherSearcherAPI {

    private final String API = "http://api.openweathermap.org";
    private RestAdapter mRestAdapter;
    private WeatherApi mWeatherApi;

    public WeatherSearcherAPI() {
        mWeatherApi = null;
        mRestAdapter = null;
    }

    public WeatherApi getWeatherApi() {
        if (mWeatherApi == null) {
            mRestAdapter = new RestAdapter.Builder()
                    .setEndpoint(API).build();
            mWeatherApi = mRestAdapter.create(WeatherApi.class);
        }
        return mWeatherApi;
    }
}
