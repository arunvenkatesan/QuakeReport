package com.example.android.quakereport;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class quakeAdapter extends ArrayAdapter<quakes> {



    public quakeAdapter(Activity context, ArrayList<quakes> androidquake) {
        super(context, 0, androidquake);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.activity_main, parent, false);
        }
        quakes earth = getItem(position);


        DecimalFormat formatter = new DecimalFormat("0.0");
        String magnitude  = formatter.format(earth.getMagnitude());
        TextView name1 = (TextView) listItemView.findViewById(R.id.magnitude);
        name1.setText(magnitude);

        GradientDrawable magnitudeCircle = (GradientDrawable) name1.getBackground();
        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(earth.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        String string = earth.getLocation();
        if(string.contains("of")) {
            String[] parts = string.split("of");
            String part1 = parts[0];
            String part2 = parts[1];
            TextView name2a = (TextView) listItemView.findViewById(R.id.location_offset);
            name2a.setText(part1 + "of");

            TextView name2b = (TextView) listItemView.findViewById(R.id.primary_location);
            name2b.setText(part2);
        }
        else
        {
            TextView name2a = (TextView) listItemView.findViewById(R.id.location_offset);
            name2a.setText("Near the");

            TextView name2b = (TextView) listItemView.findViewById(R.id.primary_location);
            name2b.setText(earth.getLocation());
        }

        Date dateObject = new Date(earth.getDate());
        TextView name3 = (TextView) listItemView.findViewById(R.id.date);
        String formattedDate = formatDate(dateObject);
        name3.setText(formattedDate);

        TextView name4 = (TextView) listItemView.findViewById(R.id.time);
        String formattedtime = formatTime(dateObject);
        name4.setText(formattedtime);

        return listItemView;
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }

}
