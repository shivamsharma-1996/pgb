package com.shivam.complete_dashboard.Other;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by shivam sharma on 8/18/2017.
 */

public class ConnectionDetector {

    private Context _context;
    private Boolean b;

    public ConnectionDetector(Context context) {
        this._context = context;
    }

    public boolean isConnectingToInternet() {

        ConnectivityManager connectivityManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        b = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        if (b)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}