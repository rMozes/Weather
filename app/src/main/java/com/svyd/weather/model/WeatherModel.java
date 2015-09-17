package com.svyd.weather.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Svyd on 15.09.2015.
 */
public class WeatherModel implements Serializable {

    private final double KELVIN = - 273.15;

    @SerializedName("main")
    @Expose
    private Main main;

    @SerializedName("wind")
    @Expose
    private Wind wind;

    @SerializedName("clouds")
    @Expose
    private Clouds clouds;

    @SerializedName("visibility")
    @Expose
    private String visibility;

    @SerializedName("sys")
    @Expose
    private Sun sun;

    @SerializedName("weather")
    @Expose
    private List<WeatherDescription> desc;

    @SerializedName("coord")
    @Expose
    private Coord coordinates;

    @SerializedName("name")
    private String city;

    public double getTempAvg() {

        return Double.parseDouble(main.tempAvg) + KELVIN;
    }

    public double getTempMin() {
        return Double.parseDouble(main.tempMin) + KELVIN;
    }

    public double getTempMax() {
        return Double.parseDouble(main.tempMax) + KELVIN;
    }

    public String getHumidity() {
        return main.humidity;
    }

    public String getPressure() {
        return main.pressure;
    }

    public String getWindSpeed() {
        return wind.speed;
    }

    public String getWindDegree() {
        return wind.degree;
    }

    public String getClouds() {
        return clouds.clouds;
    }

    public String getVisibility() {
        return visibility;
    }

    public String getSunrise(){
        return sun.sunrise;
    }

    public String getSunset() {
        return sun.sunset;
    }

    public String getShortDesc() {
        return desc.get(0).main;
    }

    public String getDesc() {
        return desc.get(0).desc;
    }

    public String getCountry() {
        return sun.country;
    }

    public String getCity() {
        return city;
    }

    public Double getLat() {return Double.parseDouble(coordinates.latitude);}

    public Double getLon() {return Double.parseDouble(coordinates.longitude);}

}
