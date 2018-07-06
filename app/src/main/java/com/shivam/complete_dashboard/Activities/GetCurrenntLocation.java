package com.shivam.complete_dashboard.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.shivam.complete_dashboard.Advertise.AdActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by shivam sharma on 2/8/2018.
 */

public class GetCurrenntLocation
{
    private static final int REQUEST_LOCATION = 1;

    LocationManager locationManager;
    double lattitude,longitude;
    Context ctx;

    Geocoder geocoder;
    List<Address> addresses;

    public GetCurrenntLocation(Context ctx)
    {
        this.ctx = ctx;
        ActivityCompat.requestPermissions((Activity) ctx, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
    }


    public void check()
    {
        locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) ctx, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = latti;
                longitude = longi;

                Toast.makeText(ctx,"Your current location is"+ "\n" + "Lattitude = " + lattitude
                        + "\n" + "Longitude = " + longitude, Toast.LENGTH_LONG).show();

            } else  if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = latti;
                longitude = longi;

                Toast.makeText(ctx,"Your current location is"+ "\n" + "Lattitude = " + lattitude
                        + "\n" + "Longitude = " + longitude, Toast.LENGTH_LONG).show();


            } else  if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = latti;
                longitude = longi;

                Toast.makeText(ctx,"Your current location is"+ "\n" + "Lattitude = " + lattitude
                        + "\n" + "Longitude = " + longitude, Toast.LENGTH_LONG).show();

            }else{

                Toast.makeText(ctx,"Unble to Trace your location",Toast.LENGTH_SHORT).show();

            }


            //converting latlngs to complete address
            geocoder = new Geocoder(ctx, Locale.getDefault());
            try
            {
                addresses = geocoder.getFromLocation(lattitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(final DialogInterface dialog, final int id)
                    {
                        ctx.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    public double getLattitude()
    {
        return lattitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public String getAddresses()
    {
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();
        return address;
    }
}


