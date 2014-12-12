package com.example.murach.mapsapp;

import com.example.murach.mapsapp.model.Location;
import com.example.murach.mapsapp.model.Weather;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 * Created by 10365332 on 12/12/2014.
 */
public class WeatherJsonParser {
    public Weather getWeather(String data) throws JSONException {
        Weather weather = new Weather();
        Location location = new Location();

        JSONObject jWeatherObj = new JSONObject(data);

        JSONObject coordObj = getObject("coord", jWeatherObj);
        location.setLongitude(getFloat("lat", coordObj));
        location.setLatitude(getFloat("lon", coordObj));

        JSONObject sysObj = getObject("sys",jWeatherObj);
        location.setCity(getString("name",jWeatherObj));
        location.setCountry(getString("country",sysObj));
        location.setSunrise(getInt("sunrise",sysObj));
        location.setSunset(getInt("sunset",sysObj));

        weather.location = location;

        return weather;
    }

    private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }
    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float  getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static int  getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }
}
