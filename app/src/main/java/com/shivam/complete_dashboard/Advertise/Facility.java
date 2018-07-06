package com.shivam.complete_dashboard.Advertise;

/**
 * Created by shivam sharma on 2/19/2018.
 */

public class Facility
{
    String title;
    String Charge;

    public Facility()
    {
    }

    public Facility(String title, String charge)
    {
        this.title = title;
        Charge = charge;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getCharge() {
        return Charge;
    }

    public void setCharge(String charge) {
        Charge = charge;
    }
}
