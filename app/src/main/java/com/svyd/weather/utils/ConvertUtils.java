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

    /**
     * getting the city name by coordinates
     *
     * @param _context for geocoder
     * @param _latLng coordinates of city to find
     * @return array of strings with the nme of the city and country
     * @throws IOException
     */

    public static String[] getCityName(Context _context, LatLng _latLng) throws IOException {
        Geocoder geocoder = new Geocoder(_context, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(_latLng.latitude, _latLng.longitude, 1);
        try {
            return new String[]{
                    addresses.get(0).getAddressLine(0),
            };
        } catch (IndexOutOfBoundsException e) {
            Toast.makeText(_context, "Error, probably bad input", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    /**
     *
     * finds the coordinates of the cityName
     *
     * @param _context for geocoder
     * @param _cityName to find the coordinates
     * @return LatLng of the param cityName
     */

    public static LatLng cityNameToLatLng(Context _context, String _cityName) {
        if (Geocoder.isPresent()) {
            try {
                String location = _cityName;
                Geocoder gc = new Geocoder(_context);
                List<Address> addresses = gc.getFromLocationName(location, 5); // get the found Address Objects

                List<LatLng> ll = new ArrayList<>(addresses.size()); // A list to save the coordinates if they are available
                for (Address a : addresses) {
                    if (a.hasLatitude() && a.hasLongitude()) {
                        ll.add(new LatLng(a.getLatitude(), a.getLongitude()));
                    }
                }
                return ll.get(0);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(_context, "Error, probably bad input", Toast.LENGTH_SHORT).show();
            }
        }
        return null;
    }
}
