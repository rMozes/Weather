package com.svyd.weather.utils;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;

import com.google.android.gms.maps.model.LatLng;
import com.svyd.weather.API.WeatherApi;
import com.svyd.weather.model.WeatherModel;

import retrofit.RestAdapter;

/**
 * Created by Svyd on 17.09.2015.
 */
public class WeatherLoader extends AsyncTaskLoader<WeatherModel> {


    private final String API = "http://api.openweathermap.org";

    private LatLng mCoords;
    private String mCity;
    private RestAdapter mAdapter;
    private WeatherApi mApi;

    private WeatherModel mModel;

    public WeatherLoader(Context context, LatLng _latLng) {
        super(context);

        mCoords = _latLng;

        initApi();
    }

    public WeatherLoader(Context context, final String _request) {
        super(context);

        mCity = _request;

        initApi();
    }

    private void initApi() {
        mAdapter = new RestAdapter.Builder()
                .setEndpoint(API).build();
        mApi = mAdapter.create(WeatherApi.class);
    }

    @Override
    public WeatherModel loadInBackground() {
        if (mCoords != null) {
            mModel = mApi.getFeed(mCoords.latitude, mCoords.longitude);
        } else {
            if (mCity != null) {
                mModel = mApi.getFeed(mCity);
            }
        }
        return mModel;
    }
}
