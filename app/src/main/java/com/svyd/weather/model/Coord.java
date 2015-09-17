package com.svyd.weather.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Svyd on 17.09.2015.
 */
public class Coord {
    @SerializedName("lon")
    String longitude;

    @SerializedName("lat")
    String latitude;
}
