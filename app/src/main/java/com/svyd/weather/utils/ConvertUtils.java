package com.svyd.weather.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Helper class used for converting different stuff
 */
public class ConvertUtils {

    /**
     *
     * @param _timeString unix time
     * @return time in format HH:mm
     */

    public static String unixToHhMm(String _timeString) {

        long unixSeconds = Long.parseLong(_timeString);
        Date date = new Date(unixSeconds*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); // the format of your date
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    /**
     * converts string with coordinates to LatLng object
     * @param _string is converted to LatLng
     * @return
     */

    public static LatLng getLatLngFromString(String _string) {
        String[] numbers = _string.split("\\s+");
        try {
            return new LatLng(Double.parseDouble(numbers[0]),Double.parseDouble(numbers[1]) );
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
