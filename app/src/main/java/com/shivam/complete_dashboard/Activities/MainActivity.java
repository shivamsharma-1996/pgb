package com.shivam.complete_dashboard.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.BuildConfig;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
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
import com.shivam.complete_dashboard.Adapter.ViewPagerAdapter;
import com.shivam.complete_dashboard.Advertise.AdActivity;
import com.shivam.complete_dashboard.Database.AuthId;
import com.shivam.complete_dashboard.Models.LatLng;
import com.shivam.complete_dashboard.Other.SessionManager;
import com.shivam.complete_dashboard.R;
import com.shivam.complete_dashboard.Start.LoginActivity;
import com.shivam.complete_dashboard.Start.User;
import com.shivam.complete_dashboard.Tabs.Global_Ads;
import com.shivam.complete_dashboard.Tabs.Hostel;

import java.util.Locale;


public class MainActivity extends AppCompatActivity{

    //navigationView & DrawerLayout
    private DrawerLayout mDrawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mToggle;

    private ViewPager mViewPager;
    private ViewPagerAdapter mSectionsViewPagerAdapter;
    private TabLayout mTabLayout;

    private Toolbar mToolbar;


    //Firebase Database
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private String currentUID;
    private AuthId userIdentity = new AuthId();


    //Session Manager Class
    private SessionManager session;


    private TextView tv_profile_name, tv_profile_email;
    private String profile_name, profile_email;




    Global_Ads global_ads = new Global_Ads();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        SharedPreferences.Editor editor = getSharedPreferences("preview_state", MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();


        //<-----------------------"To overlay navigationDrawer"-------------------------------------------->
        // we remove default toolbar , and adding new toolbar dynamically.
        mToolbar = (Toolbar) findViewById(R.id.toolbar_top);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("PGBEE");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Firebase
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        if(mCurrentUser == null)
        {
            Intent login_intent = new Intent(MainActivity.this, LoginActivity.class);
            login_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(login_intent);
            finish();
        }
        else
        {
            //ise sharedpreference se karna hai
            currentUID = mCurrentUser.getUid();
            mDatabaseRef.child("Users").child("Seller").child(currentUID).addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    User loggedin_User = dataSnapshot.getValue(User.class);
                    profile_name = loggedin_User.getName();
                    profile_email = loggedin_User.getEmail();
                    Log.i("loggedin_User", String.valueOf(profile_name + " "  + profile_email ));

                    tv_profile_name.setText(profile_name);
                    tv_profile_email.setText(profile_email);
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {

                }
            });
        }

        //Nav_Drawer
        mDrawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawer.closeDrawer(Gravity.START);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        mToggle = new ActionBarDrawerToggle(this, mDrawer, R.string.open, R.string.close);

        //Nav_Header
        //View nav_header = navigationView.inflateHeaderView(R.layout.navigation_header)
        View nav_header = navigationView.getHeaderView(0);
        tv_profile_name = nav_header.findViewById(R.id.tv_profile_name);
        tv_profile_email = nav_header.findViewById(R.id.tv_profile_email);

//        Log.i("main_", String.valueOf(tv_profile_name));
//        Log.i("main_", String.valueOf(tv_profile_email));


        mViewPager = (ViewPager) findViewById(R.id.tabPager);
        mSectionsViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager() , this);
        mViewPager.setAdapter(mSectionsViewPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        mDrawer.addDrawerListener(mToggle);   //Setting the mToggle to drawer
        mToggle.syncState();                //without this statement togglebutton is not appears.




        // Session class instance
        session = new SessionManager(getApplicationContext());
       // Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();
        /**
             ***************Call this function whenever you want to check user login
             ***************this will redirect user to LoginActivity,if he is not logged in
         **/
       // session.checkLogin();



       // retrieveIdentity();



        //initializing navigation menu

        setUpNavigationView();

    }


    private void retrieveIdentity()
    {
        //        if(currentUID!= null)
//        {
//            mDatabaseRef.child("OIDs").child(currentUID).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot)
//                {
//                    userIdentity = dataSnapshot.getValue(AuthId.class);
//                    Log.i("dataSnapshot",userIdentity.getIdentity());
//
//                    if(userIdentity.getIdentity()!=null)
//                    {
//                        Log.i("userIdentty.getIdentity", userIdentity.getIdentity());
//                    }
//                    if(mCurrentUser!=null)
//                    {
//                        Log.i("mCurrentUser",mCurrentUser.toString());
//                    }
//
//                    Log.i("value:", String.valueOf((mCurrentUser!=null && userIdentity.getIdentity().equals("Seller"))));
//                    updateUI();
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    Log.i("OIDs","Can't retrieve identity");
//
//                }
//            });
//
//        }
    }
    private void updateUI()
    {
       //        Toast.makeText(this,"updateUI called",Toast.LENGTH_LONG).show();
//
//        //dynamically change drawer menu after seller login
//        if(mCurrentUser!=null && userIdentity.getIdentity().equals("Seller")) /*&& session.getUserDetails().get(SessionManager.KEY_IDENTITY).equals("Seller")*/
//        {
//            //Toast.makeText(this,"updateUI1",Toast.LENGTH_LONG).show();
//            navigationView.getMenu().findItem(R.id.nav_addProperty).setVisible(true);
//            navigationView.getMenu().findItem(R.id.nav_bookmarks).setVisible(false);
//            navigationView.getMenu().findItem(R.id.nav_signUp).setVisible(false);
//            navigationView.getMenu().findItem(R.id.nav_signIn).setVisible(false);
//            navigationView.getMenu().findItem(R.id.nav_upgrade_seeker).setVisible(false);
//            navigationView.getMenu().findItem(R.id.nav_ads).setVisible(true);
//            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
//        }
//        else if(mCurrentUser!=null && userIdentity.getIdentity().equals("Seeker")) /*&& session.getUserDetails().get(SessionManager.KEY_IDENTITY).equals("Seeker")*/
//        {
//            // Toast.makeText(this,"updateUI2",Toast.LENGTH_LONG).show();
//
//            navigationView.getMenu().findItem(R.id.nav_addProperty).setVisible(false);
//            navigationView.getMenu().findItem(R.id.nav_bookmarks).setVisible(true);
//            navigationView.getMenu().findItem(R.id.nav_upgrade_seeker).setVisible(true);
//            navigationView.getMenu().findItem(R.id.nav_signUp).setVisible(false);
//            navigationView.getMenu().findItem(R.id.nav_signIn).setVisible(false);
//            navigationView.getMenu().findItem(R.id.nav_ads).setVisible(false);
//            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
//        }
    }



    //If onOptionsItemSelected() is not overridded, mToggle button dont open the drawer
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (mToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return  super.onOptionsItemSelected(item);
    }


    private void setUpNavigationView()
    {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            // This method will trigger onItem Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId())
                {
                    case R.id.nav_addProperty:
                      //  Toast.makeText(Device_location.this, "addProperty is clicked", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this, AdActivity.class);
                        startActivity(intent);
                       // Toast.makeText(Device_location.this, "addProperty after", Toast.LENGTH_LONG).show();
                        mDrawer.closeDrawers();
                        break;
                        /*    AlertDialog.Builder builder = new AlertDialog.Builder(Device_location.this);
                        builder.setMessage("You have to create account as SELLER to add property")
                                .setCancelable(false)
                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.setCanceledOnTouchOutside(false);
                        alert.show();*/




                    case R.id.nav_bookmarks:
                       // Toast.makeText(Device_location.this, "bookmarks is clicked", Toast.LENGTH_LONG).show();
                        //mDrawer.closeDrawers();
                        startActivity(new Intent(MainActivity.this, BookmarkActivity.class));
                        break;

                    case R.id.nav_ads:
                      //  Toast.makeText(Device_location.this, "My Adds is clicked", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MainActivity.this, MyAddsActivty.class));
                        mDrawer.closeDrawers();
                        break;

                        //                    case R.id.nav_signUp:
//                      //  Toast.makeText(Device_location.this, "signUp is clicked", Toast.LENGTH_LONG).show();
//                        //mDrawer.closeDrawers();
//                        startActivity(new Intent(Device_location.this, RegisterActivity.class));
//                        break;
//
//                    case R.id.nav_signIn:
//                        Toast.makeText(Device_location.this, "signIn is clicked", Toast.LENGTH_LONG).show();
//                       // mDrawer.closeDrawers();
//                        startActivity(new Intent(Device_location.this, LoginActivity.class));
//                        break;

                    case R.id.nav_logout:
                       // Toast.makeText(Device_location.this, "logout is clicked", Toast.LENGTH_LONG).show();
                        //mDrawer.closeDrawers();

                        // Clear the session data
                        // This will clear all session data and
                        // redirect user to LoginActivity
                        session.logoutUser();
                        FirebaseAuth.getInstance().signOut();
                       // Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

                        finish();
                        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                        break;

                        default:
                }

                //Checking if the item is in checked state or not, if not make it in checked state
               /* if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);*/

                //loadHomeFragment();

                return true;
            }
        });


        //        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close) {
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
//                super.onDrawerClosed(drawerView);
//            }
//
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
//                super.onDrawerOpened(drawerView);
//            }
//        };
 }




}
