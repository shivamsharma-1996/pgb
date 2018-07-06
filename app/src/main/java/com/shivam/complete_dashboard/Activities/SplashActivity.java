package com.shivam.complete_dashboard.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shivam.complete_dashboard.Holders.AdViewHolder;
import com.shivam.complete_dashboard.Models.Advertise;
import com.shivam.complete_dashboard.R;
import com.shivam.complete_dashboard.Start.LoginActivity;

import java.io.Serializable;

public class SplashActivity extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 5000;

    //Firebase
    private DatabaseReference mAdDatabase;
    private FirebaseUser mCurrentUser;

    private ProgressDialog mProgressDialog;

    //Device's Location
    protected Location mLastLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    //Firebase RecyclerAdapter can not be implemented on splash Activity to load data ( becoz data is to shown on recyclerview on mainactivity)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);



        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();


    }


    @Override
    protected void onStart()
    {
        super.onStart();

        if(!checkPermissions())
        {
            requestPermissions();
        }
        else
        {
            getLastLocation();
        }
    }

    private boolean checkPermissions(){
        int permissionState = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return  permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);

        if(shouldProvideRationale)
        {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            //startLocationPermissionRequest();

            showSnackBar(R.string.textwarn,  android.R.string.ok,
                    new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view) {

                            //Request permission
                            startLocationPermissionRequest();
                        }
                    });
        }
        else
        {
            Log.i(TAG, "Reuesting permission");
            startLocationPermissionRequest();
        }
    }

    private void showSnackBar(final int mainTextStringId, final  int actionStringId, View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.i(TAG, "onRequestPermissionsResult");

        if(requestCode == REQUEST_PERMISSIONS_REQUEST_CODE)
        {
            if(grantResults.length <= 0)
            {
                //If user interaction was interupted, the permission request is cancelled & you
                // recieve empty arrays.
                Log.i(TAG, "User interaction was cancelled");
            }
            else if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                //Permission granted
                getLastLocation();
            }
            else
            {
                //Permission denied.
                //So, Making run time permission for fine_location

                showSnackBar(R.string.textwarn,  R.string.settings,
                        new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                //Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        getPackageName(), null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    private void getLastLocation()
    {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if(task.isSuccessful() && task.getResult() != null)
                        {
                            mLastLocation = task.getResult();

                           /*tv_mLatitude.setText(String.format(Locale.ENGLISH, "%s: %f",
                                    mLatitudeLabel,
                                    mLastLocation.getLatitude();

                                    tv_mLongitude.setText(String.format(Locale.ENGLISH, "%s: %f",
                                    mLongitudeLabel,
                                    mLastLocation.getLongitude()));*/


                            SharedPreferences preferences = getSharedPreferences("Device_latlng", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("device_latitude", String.valueOf(mLastLocation.getLatitude()));
                            editor.putString("device_longitude", String.valueOf(mLastLocation.getLongitude()));
                            editor.apply();
                            Log.i("Device_latlng", String.valueOf(mLastLocation.getLatitude() +mLastLocation.getLongitude()));

                            if(mCurrentUser == null)
                            {
                                Intent login_intent = new Intent(SplashActivity.this, LoginActivity.class);
                                startActivity(login_intent);
                                finish();
                            }
                            else
                            {
                                new Handler().postDelayed(new Runnable()
                                {

                                    @Override
                                    public void run()
                                    {
                                        Intent home_intent = new Intent(SplashActivity.this, MainActivity.class);
                                        startActivity(home_intent);

                                        finish();
                                    }
                                }, SPLASH_TIME_OUT);

                                Intent home_intent = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(home_intent);
                                finish();
                            }
                        }
                        else
                        {
                            Log.i(TAG, " getLastLocation:exception", task.getException());
                            Toast.makeText(SplashActivity.this, task.getException() + "", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

//    *
//     * Async Task to make http call
//
//    private class PrefetchData extends AsyncTask<Void, Void, Void>
//    {
//        @Override
//        protected void onPreExecute()
//        {
//            super.onPreExecute();
//            // before making http calls
//            Log.i("splash","onPreExecute");
//        }
//
//        @Override
//        protected Void doInBackground(Void... arg0)
//        {
//            Log.i("splash","doInBackground");
//
////            mProgressDialog.setTitle("Fetching Data");
////            mProgressDialog.setCanceledOnTouchOutside(false);
////            mProgressDialog.show();
//
//
//            Log.i("splash", "test2");
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result)
//        {
//            super.onPostExecute(result);
//            Log.i("splash","onPostExecute");
//
//            // After completing http call
//            // will close this activity and lauch main activity
//            Intent i = new Intent(SplashActivity.this, Device_location.class);
//            i.putExtra("now_playing", now_playing);
//            i.putExtra("earned", earned);
//           // startActivity(i);
//
//            // close this activity
//          //  finish();
//        }
}






