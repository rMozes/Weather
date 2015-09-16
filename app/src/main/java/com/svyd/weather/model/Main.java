package com.svyd.weather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Svyd on 15.09.2015.
 */
public class Main {
    @SerializedName("temp")
    @Expose
    String tempAvg;
    @SerializedName("temp_min")
    @Expose
    String tempMin;
    @SerializedName("temp_max")
    @Expose
    String tempMax;
    @SerializedName("humidity")
    @Expose
    String humidity;
    @SerializedName("pressure")
    @Expose
    String pressure;
}
