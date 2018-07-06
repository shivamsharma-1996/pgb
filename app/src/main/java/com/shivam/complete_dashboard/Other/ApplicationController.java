package com.shivam.complete_dashboard.Other;

import android.app.Application;
import android.content.Context;
//import android.support.multidex.MultiDex;
import android.text.TextUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by shivam sharma on 9/29/2017.
 */



//Ideally you should have one centralized place for your Queue, and the best place to initialize queue is in your Application class
public class ApplicationController extends Application
{
    public static final String TAG = "VolleyPatterns";
    private RequestQueue mRequestQueue;      //Global request queue for Volley that is used among all activities

    //A singleton instance of the application class for easy access in other places
    private static ApplicationController sInstance;


    /*@Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }*/

    @Override
    public void onCreate()
    {
        super.onCreate();
        sInstance = this;    // initialize the singleton

        //newer version of firebase, used in application class
        if(FirebaseApp.getApps(this).isEmpty())
        {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

    }



    /* *
      * @return ApplicationController singleton instance
     */
    public static synchronized ApplicationController getInstance()
    {
        return sInstance;
    }

    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    public RequestQueue getRequestQueue()
    {
         //The queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null)
        {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     *
     * @param req
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> req, String tag)
    {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        mRequestQueue.add(req);
    }

    /* *
      * Adds the specified request to the global queue using the Default TAG.
      *
      * @param req

      */
    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is not sent
        req.setTag(TAG);

        getRequestQueue().add(req);
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important
     * to specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
