package com.svyd.weather.utils;

import com.svyd.weather.global.Constants;
import com.svyd.weather.model.WeatherModel;

import java.util.ArrayList;

/**
 * Helper class that performs some methods to work with weatherModel
 */
public class ModelUtils {

    /**
     * converts @param weatherModel to
     * @return arrayist of weather details
     */

    public static ArrayList<String> modelToList(final WeatherModel _weatherModel) {

        if (_weatherModel != null) {
            try {
                ArrayList<String> list = new ArrayList<>();

                list.add("Avg: " + String.format("%.2f", _weatherModel.getTempAvg()) + Constants.DEGREE);
                list.add("Max: " + String.format("%.2f", _weatherModel.getTempMax()) + Constants.DEGREE);
                list.add("Min: " + String.format("%.2f", _weatherModel.getTempMin()) + Constants.DEGREE);
                list.add(_weatherModel.getShortDesc() + " (" + _weatherModel.getDesc() + ")");
                list.add("Clouds: " + _weatherModel.getClouds() + "%");
                list.add("Humidity: " + _weatherModel.getHumidity() + "%");
                list.add("Sunrise: " + ConvertUtils.unixToHhMm(_weatherModel.getSunrise()));
                list.add("Sunset: " + ConvertUtils.unixToHhMm(_weatherModel.getSunset()));
                list.add("Pressure: " + _weatherModel.getPressure() + " hpa");
                if (_weatherModel.getVisibility() != null) {
                    list.add("Visibility: " + _weatherModel.getVisibility() + " m");
                } else {
                    list.add("Visibility: unknown");
                }
                list.add("Wind speed: " + _weatherModel.getWindSpeed() + " m/s");
                if (_weatherModel.getWindDegree() != null) {
                    list.add("Wind degree: " + _weatherModel.getWindDegree() + Constants.DEGREE);
                } else {
                    list.add("Wind degree: unknown");
                }
                return list;
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
