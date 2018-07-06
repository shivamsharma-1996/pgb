package com.shivam.complete_dashboard.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shivam.complete_dashboard.Tabs.Hostel;
import com.shivam.complete_dashboard.Tabs.PG;
import com.shivam.complete_dashboard.Tabs.Rent;


/**
 * Created by shivam sharma on 8/9/2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter
{
    private Context context;

    public ViewPagerAdapter(FragmentManager fm, Context context)
    {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0:
                Hostel hostel = new Hostel();
                return hostel;
            case 1:
                PG PG = new PG();
                return PG;
            case 2:
                Rent rent = new Rent();
                return rent;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
            case 0:
                return "HOSTEL";
            case 1:
                return "PG";
            case 2:
                return "Rent";
            default:
                return null;

        }
    }
}

