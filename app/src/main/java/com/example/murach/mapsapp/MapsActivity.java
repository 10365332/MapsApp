package com.example.murach.mapsapp;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.google.android.gms.gcm.Task;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.CameraUpdateFactory;

import java.util.Arrays;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnSeekBarChangeListener  {

    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
    private static final int WIDTH_MAX = 50;
    private static final int HUE_MAX = 360;
    private static final int ALPHA_MAX = 255;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private Polygon mMutablePolygon;

    private SeekBar mColorBar;
    private SeekBar mAlphaBar;
    private SeekBar mWidthBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mColorBar = (SeekBar) findViewById(R.id.hueSeekBar);
        mColorBar.setMax(HUE_MAX);
        mColorBar.setProgress(0);

        mAlphaBar = (SeekBar) findViewById(R.id.alphaSeekBar);
        mAlphaBar.setMax(ALPHA_MAX);
        mAlphaBar.setProgress(127);

        mWidthBar = (SeekBar) findViewById(R.id.widthSeekBar);
        mWidthBar.setMax(WIDTH_MAX);
        mWidthBar.setProgress(10);

        setUpMapIfNeeded();

        String forecastDaysNum = "3";
        String city = "London, UK";
        String lang = "en";

        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{city,lang, forecastDaysNum});
        //Ophalen van de Weatherdata
        //String data = ( (new WeatherHttpClient()).getForecastWeatherData(new String[]{city,lang, forecastDaysNum}));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        getWeatherData();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }
    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */

    private class JSONWeatherTask extends AsyncTask<String, Void, Object>{

        @Override
        protected Object doInBackground(String... params) {
            String data = ( (new WeatherHttpClient()).getForecastWeatherData(params[0], params[1], params[2]));
            return null;
        }
    }

    private void getWeatherData() {
        //Code to parse the weatherdata to an ideal format
        //Show the weatherdata on the map
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        // Create a rectangle with two rectangular holes.
        mMap.addPolygon(new PolygonOptions()
                .addAll(createRectangle(new LatLng(-20, 130), 5, 5))
                .addHole(createRectangle(new LatLng(-22, 128), 1, 1))
                .addHole(createRectangle(new LatLng(-18, 133), 0.5, 1.5))
                .fillColor(Color.GREEN)
                .strokeColor(Color.GRAY)
                .strokeWidth(3));

        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        // Create a rectangle centered at Sydney.
        PolygonOptions options = new PolygonOptions().addAll(createRectangle(SYDNEY, 5, 8));

        int fillColor = Color.HSVToColor(mAlphaBar.getProgress(), new float[] {mColorBar.getProgress(), 1, 1});
        mMutablePolygon = mMap.addPolygon(options
                .strokeWidth(mWidthBar.getProgress())
                .strokeColor(Color.BLACK)
                .fillColor(fillColor));

        mColorBar.setOnSeekBarChangeListener(this);
        mAlphaBar.setOnSeekBarChangeListener(this);
        mWidthBar.setOnSeekBarChangeListener(this);

        // Move the map so that it is centered on the mutable polygon.
        mMap.moveCamera(CameraUpdateFactory.newLatLng(SYDNEY));
    }

    private List<LatLng> createRectangle(LatLng center, double halfWidth, double halfHeight) {
        return Arrays.asList(new LatLng(center.latitude - halfHeight, center.longitude - halfWidth),
                new LatLng(center.latitude - halfHeight, center.longitude + halfWidth),
                new LatLng(center.latitude + halfHeight, center.longitude + halfWidth),
                new LatLng(center.latitude + halfHeight, center.longitude - halfWidth),
                new LatLng(center.latitude - halfHeight, center.longitude - halfWidth));
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // Don't do anything here.
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // Don't do anything here.
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mMutablePolygon == null) {
            return;
        }

        if (seekBar == mColorBar) {
            mMutablePolygon.setFillColor(Color.HSVToColor(
                    Color.alpha(mMutablePolygon.getFillColor()), new float[] {progress, 1, 1}));
        } else if (seekBar == mAlphaBar) {
            int prevColor = mMutablePolygon.getFillColor();
            mMutablePolygon.setFillColor(Color.argb(
                    progress, Color.red(prevColor), Color.green(prevColor),
                    Color.blue(prevColor)));
        } else if (seekBar == mWidthBar) {
            int fillColor = Color.HSVToColor(mAlphaBar.getProgress(), new float[] {mColorBar.getProgress(), 1, 1});
            PolygonOptions options = new PolygonOptions().addAll(createRectangle(SYDNEY, progress, 8));
            mMap.clear();
            mMutablePolygon = mMap.addPolygon(options
                    .strokeWidth(mWidthBar.getProgress())
                    .strokeColor(Color.BLACK)
                    .fillColor(fillColor));
        }
    }
}
