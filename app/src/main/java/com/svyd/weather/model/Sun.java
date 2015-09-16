package com.svyd.weather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Svyd on 15.09.2015.
 */
public class Sun {

    @SerializedName("sunrise")
    @Expose
    String sunrise;

    @SerializedName("sunset")
    @Expose
    String sunset;

    @SerializedName("country")
    String country;
}
