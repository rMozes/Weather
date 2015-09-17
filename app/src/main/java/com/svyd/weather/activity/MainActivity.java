package com.svyd.weather.activity;

import android.app.LoaderManager;
import android.content.Loader;
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
import com.svyd.weather.R;
import com.svyd.weather.global.Constants;
import com.svyd.weather.model.WeatherModel;
import com.svyd.weather.utils.ConvertUtils;
import com.svyd.weather.utils.ModelUtils;
import com.svyd.weather.utils.WeatherLoader;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<WeatherModel> {

    private Toolbar mToolbar;
    private SearchView mSearchView;
    private GridView mGridView;
    private GoogleMap mMap;
    private WeatherModel mWeatherModel;
    private boolean mByCoords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initStuff();
        initLoader();

        if (savedInstanceState != null ) {
            mWeatherModel = (WeatherModel) savedInstanceState.getSerializable(Constants.SAVED_WEATHER_MODEL);
            if (mWeatherModel != null) {
                refreshData(mWeatherModel);
            } else {
                mByCoords = true;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mWeatherModel != null) {
            outState.putSerializable(Constants.SAVED_WEATHER_MODEL, mWeatherModel);
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
                if (!mByCoords) {
                    item.setTitle(getResources().getString(R.string.action_by_name));
                    mSearchView.setQueryHint(getResources().getString(R.string.toolbar_hint_coords));
                    mSearchView.setQuery("", false);
                    mSearchView.clearFocus();
                    mByCoords = true;
                } else {
                    item.setTitle(getResources().getString(R.string.action_by_coordinates));
                    mSearchView.setQueryHint(getResources().getString(R.string.toolbar_hint_name));
                    mSearchView.setQuery("", false);
                    mSearchView.clearFocus();
                    mByCoords = false;
                }
                break;
            case R.id.action_search:
                search();
                break;
        }
        return true;
    }

    private final void initLoader() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.LOADER_ARGS, Constants.UZHGOROD);
        getLoaderManager().initLoader(Constants.LOADER_NAME_ID,
                bundle , this).forceLoad();
    }

    /**
     * performs the searching of the city depending on mByCoords boolean.
     * if true - it searches by latitude and longitude
     * if false - searches by city name
     */

    private final void search() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.LOADER_ARGS, mSearchView.getQuery().toString());
        if (!mByCoords) {
            getLoaderManager().restartLoader(Constants.LOADER_NAME_ID, bundle, this).forceLoad();
        } else {
            getLoaderManager().restartLoader(Constants.LOADER_COORDS_ID, bundle, this).forceLoad();
        }
    }

    /**
     * initialising the views setting up the map etc
     */

    private final void initStuff() {
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
     * casts weatherModel to arrayList and fills up the listView
     *
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
     *
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

    @Override
    public Loader<WeatherModel> onCreateLoader(int id, Bundle args) {
        if (id == Constants.LOADER_NAME_ID) {
            return new WeatherLoader(getApplicationContext(), args.getString(Constants.LOADER_ARGS));
        } else {
            return new WeatherLoader(getApplicationContext(),
                    ConvertUtils.getLatLngFromString(args.getString(Constants.LOADER_ARGS)));
        }

    }

    @Override
    public void onLoadFinished(Loader<WeatherModel> loader, WeatherModel data) {
        mWeatherModel = data;
        refreshData(mWeatherModel);
    }

    @Override
    public void onLoaderReset(Loader<WeatherModel> loader) {

    }
}
