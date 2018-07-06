package com.shivam.complete_dashboard.Models;

import java.io.Serializable;

/**
 * Created by shivam on 13/1/18.
 */

public class Roomtype implements Serializable
{
    String type;
    String count;
    String rent;
    String months;
    String imageUrl;

    public Roomtype()
    {
        //Empty Constructor is required
    }
    public Roomtype(String type, String count, String rent, String months, String imageUrl)
    {
        this.type =type;
        this.count = count;
        this.rent = rent;
        this.months = months;
        this.imageUrl = imageUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getRent()
    {
        return rent;
    }

    public void setRent(String rent)
    {
        this.rent = rent;
    }

    public String getMonths()
    {
        return months;
    }

    public void setMonths(String months)
    {
        this.months = months;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
