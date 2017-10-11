
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<quakes>> {


    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final String SAMPLE_JSON_RESPONSE = "https://earthquake.usgs.gov/fdsnws/event/1/query";
   //         "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    private quakeAdapter madapter;
    private TextView mEmptylistView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);


        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        Log.v("xxxx", "say hi");
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();


        if (isConnected) {
            ListView earthquakeListView = (ListView) findViewById(R.id.list);
            Log.v("xxxx", "isconeected");
            madapter = new quakeAdapter(this, new ArrayList<quakes>());
            earthquakeListView.setAdapter(madapter);
            LoaderManager loaderManager = getLoaderManager();
            Log.v(LOG_TAG, "loadmanager");
            loaderManager.initLoader(1, null, this);

            mEmptylistView = (TextView) findViewById(R.id.empty_text);
            earthquakeListView.setEmptyView(mEmptylistView);

            earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    quakes web = madapter.getItem(i);
                    Uri earthquakeUri = Uri.parse(web.getUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                    startActivity(intent);

                }
            });


        } else

        {
            View loadingProgress = (View) findViewById(R.id.loading_spinner);
            Log.v("XXXXX","not connected");
            loadingProgress.setVisibility(View.GONE);
            mEmptylistView.setText("No internet connection");
        }


    }

    @Override
    public Loader<List<quakes>> onCreateLoader(int i, Bundle bundle) {
        Log.v(LOG_TAG, "onCreateLoader");
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );
        Uri baseUri = Uri.parse(SAMPLE_JSON_RESPONSE);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new EarthquakeLoader(this, uriBuilder.toString());

    }

    @Override
    public void onLoadFinished(Loader<List<quakes>> loader, List<quakes> quakes) {

        View loadingProgress = (View) findViewById(R.id.loading_spinner);
        loadingProgress.setVisibility(View.GONE);
        mEmptylistView.setText(R.string.empty_view);


        if (quakes != null || !quakes.isEmpty()) {

            Log.v(LOG_TAG, "onLoadFinished");
            madapter.addAll(quakes);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<quakes>> loader) {
        Log.v(LOG_TAG, "onLoadReset");
        madapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}