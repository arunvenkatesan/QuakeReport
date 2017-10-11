package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;


import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<quakes>> {

    private String mUrl;
    public static final String LOG_TAG= EarthquakeLoader.class.getName();

    public EarthquakeLoader(Context context,String url){

        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.v(LOG_TAG, "onSTartLoading");
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<quakes> loadInBackground() {
        if(mUrl == null){
            return null;
        }

        Log.v(LOG_TAG,"loadinbckgnd");
        List<quakes> earthquake = QueryUtils.fetchEarthquakeData(mUrl);
        return earthquake;

    }
}
