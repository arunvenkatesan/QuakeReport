package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    public static List<quakes> fetchEarthquakeData(String requestUrl) {

        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            Log.v(LOG_TAG,"inqueriesUtils");
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        List<quakes> earthquakes = extractFeatureFromJson(jsonResponse);

        return earthquakes;

    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {

            url = new URL(stringUrl);
            Log.v("XXXX","url" +url);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }


    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            Log.v(LOG_TAG,"in makerequest");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            Log.v("XXXX","urlconnectioncode" +urlConnection.getResponseCode());
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }



        private static List<quakes> extractFeatureFromJson (String earthquakeJSON){

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // If the JSON string is empty or null, then return early.
            if (TextUtils.isEmpty(earthquakeJSON)) {
                return null;
            }

            List<quakes> earthquakes = new ArrayList<>();

            try {
                JSONObject root = new JSONObject(earthquakeJSON);
                Log.v("xxxxx", "root");

                JSONArray query = root.getJSONArray("features");
                Log.v("xxxx", "query:" + query);
                for (int i = 0; i < query.length(); i++) {
                    Log.v("xxxx", "HI in loop");
                    JSONObject firstqry = query.getJSONObject(i);
                    JSONObject prop = firstqry.getJSONObject("properties");
                    double mag = prop.getDouble("mag");
                    String location = prop.getString("place");
                    long time = prop.getLong("time");
                    String url = prop.getString("url");
                    earthquakes.add(new quakes(mag, location, time, url));


                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
            }
            return earthquakes;
        }
    }


