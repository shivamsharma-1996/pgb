package com.shivam.complete_dashboard.Other;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.shivam.complete_dashboard.Start.LoginActivity;

import java.util.HashMap;

/**
 * Created by shivam sharma on 12/5/2017.
 */

public class SessionManager
{
    SharedPreferences pref;               //Shared Preferences
    SharedPreferences.Editor editor;      //Editor for Shared preferences
    Context context;                     //Context
    int PRIVATE_MODE = 0;                 //hared pref mode

    private static final String PREF_NAME = "PgbeePref";               //Sharedpref file name

    private static final String IS_LOGIN = "IsLoggedIn";     //All Shared Preferences Keys
    public static final String KEY_NAME = "name";            //User name
    public static final String KEY_EMAIL = "email";          //Email
    public static final String KEY_IDENTITY = "identity";    //Identity
    public static final String KEY_PASSWORD = "identity";    //Identity

    public SessionManager(Context context)
    {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //Create login sesion
    public void createLoginSession(String email, String identity, String password)
    {
        editor.putBoolean(IS_LOGIN,true);
        //editor.putString(KEY_NAME,name);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_IDENTITY,identity);
        editor.putString(KEY_PASSWORD,password);
        editor.commit();
    }

    //Get stored session data(from signed-in user)
    public HashMap<String, String> getUserDetails()
    {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_IDENTITY, pref.getString(KEY_IDENTITY, null));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        return user;
    }



    //It'll check user's login_status. If false, it'll redirect user to login page Else won't do anything.
    public void checkLogin()
    {
        //Checking login status
        if(!isLoggedIn())
        {
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }



    //Clear session details
    public void logoutUser()
    {
        //Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Staring Login Activity
        context.startActivity(i);


    }




    // Get Login State
    public boolean isLoggedIn()
    {
        return pref.getBoolean(IS_LOGIN, false);
    }
}