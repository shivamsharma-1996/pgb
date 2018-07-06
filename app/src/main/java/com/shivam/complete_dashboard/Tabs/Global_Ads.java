package com.shivam.complete_dashboard.Tabs;

import com.shivam.complete_dashboard.Models.Advertise;
import com.shivam.complete_dashboard.Models.LatLng;
import com.shivam.complete_dashboard.Other.ApplicationController;

import java.util.List;

/**
 * Created by Shivam on 3/7/2018.
 */

public class Global_Ads extends ApplicationController
{
    List<Advertise>  hostel_ads, pg_ads, rent_ads;

    private LatLng global_latLng = new LatLng();

    public Global_Ads()
    {
    }

    public Global_Ads(List<Advertise> ads)
    {
        this.hostel_ads = ads;
    }

    public List<Advertise> getHostel_ads()
    {
        return hostel_ads;
    }

    public List<Advertise> getPg_ads()
    {
        return pg_ads;
    }

    public List<Advertise> getRent_ads()
    {
        return rent_ads;
    }

    public void setLatLng(double latitude, double longitude)
    {
        global_latLng.setLatitude(latitude);
        global_latLng.setLongitude(longitude);
    }

    public LatLng getGlobal_latLng() {
        return global_latLng;
    }

}
