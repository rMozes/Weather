package com.svyd.weather.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.svyd.weather.API.WeatherApi;
import com.svyd.weather.API.WeatherSearcherAPI;
import com.svyd.weather.R;
import com.svyd.weather.model.WeatherModel;
import com.svyd.weather.utils.ConvertUtils;
import com.svyd.weather.utils.ModelUtils;

import java.io.IOException;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    private final String SAVED_WEATHER_MODEL = "saved_model"; //key to get model from savedInstanceState
    private final String API = "http://api.openweathermap.org";

    private Toolbar mToolbar;
    private SearchView mSearchView;
    private GridView mGridView;
    private GoogleMap mMap;
    private WeatherModel mWeatherModel;
    private boolean mByCoords;
    private WeatherSearcherAPI mWeatherSearcherAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initStuff();

        if (savedInstanceState != null) {
            mWeatherModel = (WeatherModel) savedInstanceState.getSerializable(SAVED_WEATHER_MODEL);
            if (mWeatherModel != null) {
                refreshData(mWeatherModel);
            } else {
                fillEmptyData();
            }
        } else {
            fillEmptyData();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mWeatherModel != null) {
            outState.putSerializable(SAVED_WEATHER_MODEL, mWeatherModel);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_refresh:
                if (mByCoords) {
                    item.setTitle(getResources().getString(R.string.action_by_name));
                    mSearchView.setQueryHint(getResources().getString(R.string.toolbar_hint_coords));
                    mSearchView.setQuery("", false);
                    mSearchView.clearFocus();
                    mByCoords = false;
                } else {
                    item.setTitle(getResources().getString(R.string.action_by_coordinates));
                    mSearchView.setQueryHint(getResources().getString(R.string.toolbar_hint_name));
                    mSearchView.setQuery("", false);
                    mSearchView.clearFocus();
                    mByCoords = true;
                }
                break;
            case R.id.action_search:
                search();
                break;
        }
        return true;
    }

    /**
     * performs the searching of the city depending on mByCoords boolean.
     * if true - it searches by latitude and longitude
     * if false - searches by city name
     */

    private final void search() {
        if (!mByCoords) {
            getWeatherModel(ConvertUtils.getLatLngFromString(mSearchView.getQuery().toString()));
        } else {
            getWeatherModel(mSearchView.getQuery().toString());
        }
    }

    /**
     * initialising the views setting up the map etc
     */

    private final void initStuff() {
        mWeatherSearcherAPI = new WeatherSearcherAPI();
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mSearchView = (SearchView) mToolbar.findViewById(R.id.search_view);
        mGridView = (GridView) findViewById(R.id.gridView);
        if (mByCoords) {
            mSearchView.setQueryHint(getResources().getString(R.string.toolbar_hint_name));
        } else {
            mSearchView.setQueryHint(getResources().getString(R.string.toolbar_hint_coords));
        }

        setSupportActionBar(mToolbar);
        setUpMapIfNeeded();
    }

    /**
     * fills the gridView with names of the cells
     */

    private final void fillEmptyData() {
        mGridView.setAdapter(new ArrayAdapter<>(this,
                R.layout.list_item,
                ModelUtils.getEmptyDataList()));
    }

    /**
     * casts weatherModel to arrayList and fills up the listView
     * @param _weatherModel as an input data
     */

    private final void dataToListView(final WeatherModel _weatherModel) {

        ArrayList<String> list = ModelUtils.modelToList(_weatherModel);
        if (list != null) {
            try {
                mGridView.setAdapter(new ArrayAdapter<>(this,
                        R.layout.list_item,
                        list));
            } catch (NullPointerException e) {
                e.printStackTrace();
                Toast.makeText(getApplication(), "Error, probably bad input", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * setting up the map
     */

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setScrollGesturesEnabled(false);
        }
    }

    /**
     * refreshes the data in gridView with new from
     * @param _weatherModel (new data)
     */

    private final void refreshData(WeatherModel _weatherModel) {
        try {
            if (_weatherModel != null) {
                dataToListView(mWeatherModel);
                LatLng coordinates = new LatLng(_weatherModel.getLat(), _weatherModel.getLon());
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(coordinates).title(mWeatherModel.getCity() + ", " + mWeatherModel.getCountry())).showInfoWindow();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(coordinates));
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Toast.makeText(getApplication(), "Error, probably bad input", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(getApplication(), "Error, probably bad input", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * gets the weatherModel using retrofit framework
     * @param _request to send on api server
     */

    private final void getWeatherModel(final String _request) {
        try {

            mWeatherSearcherAPI.getWeatherApi().getFeed(_request, new Callback<WeatherModel>() {
                @Override
                public void success(WeatherModel weatherModel, Response response) {
                    mWeatherModel = weatherModel;
                    refreshData(weatherModel);
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getApplication(), "Error, probably bad input", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(getApplication(), "Error, probably bad input", Toast.LENGTH_SHORT).show();
        }
    }
    private final void getWeatherModel(final LatLng _latLng) {

        try {
            mWeatherSearcherAPI.getWeatherApi().getFeed(_latLng.latitude, _latLng.longitude, new Callback<WeatherModel>() {
                @Override
                public void success(WeatherModel weatherModel, Response response) {
                    mWeatherModel = weatherModel;
                    refreshData(weatherModel);
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getApplication(), "Error, probably bad input", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(getApplication(), "Error, probably bad input", Toast.LENGTH_SHORT).show();
        }
    }
}
