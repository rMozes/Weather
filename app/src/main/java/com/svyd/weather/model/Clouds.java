package com.svyd.weather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Svyd on 15.09.2015.
 */
public class Clouds {

    @SerializedName("all")
    @Expose
    String clouds;
}
