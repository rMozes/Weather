package com.svyd.weather.API;

import com.svyd.weather.model.WeatherModel;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Svyd on 15.09.2015.
 */
public interface WeatherApi {
    @GET("/data/2.5/weather")
    WeatherModel getFeed(@Query("q") String city);

    @GET("/data/2.5/weather")
    WeatherModel getFeed(@Query("lat") double lat, @Query("lon") double lon);

}
