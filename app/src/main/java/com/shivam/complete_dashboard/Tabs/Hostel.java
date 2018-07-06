package com.shivam.complete_dashboard.Tabs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shivam.complete_dashboard.Activities.DetailViewActivity2;
import com.shivam.complete_dashboard.Holders.AdViewHolder;
import com.shivam.complete_dashboard.Models.Advertise;
import com.shivam.complete_dashboard.R;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Hostel extends Fragment
{
    //Views
    private View mHostelView, mAdFooterView, mAdHeaderView, mAdCard_view;
    private RecyclerView mADList;
    private ProgressDialog mProgressDialog;


    //Firebase
    private DatabaseReference mAdDatabase;
    //private DatabaseReference mThumbDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrent_user;


    private FirebaseRecyclerAdapter<Advertise, AdViewHolder> firebaseRecyclerAdapter;

    private String googlemap_url;

    private double sourceLatitude, sourceLongitude;
    public Hostel()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        Log.i("splash: container:", container.toString());
        SharedPreferences prfs = getContext().getSharedPreferences("Device_latlng", Context.MODE_PRIVATE);
        sourceLatitude = Double.parseDouble(prfs.getString("device_latitude", ""));
        sourceLongitude = Double.parseDouble(prfs.getString("device_longitude", ""));
        mProgressDialog = new ProgressDialog(getContext());

        mHostelView = inflater.inflate(R.layout.tab_hostel, container, false);
        mAdCard_view = inflater.inflate(R.layout.single_ad_layout, container, false);
        mAdHeaderView = inflater.inflate(R.layout.adcard_header, container, false);
        mAdFooterView = inflater.inflate(R.layout.adcard_footer, container, false);

        init();


        //annomnymous login code
         //        mAuth = FirebaseAuth.getInstance();
//        mAuth.signInAnonymously()
//                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful())
//                        {
//                            Log.i("login success", "signInAnonymously:success");
//                        }
//                        else
//                        {
//                            Log.e("login failure", "signInAnonymously:failure", task.getException());
//                            Toast.makeText(getContext(), "signInAnonymously:failure", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });

       // mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();

        mAdDatabase =  FirebaseDatabase.getInstance().getReference().child("Unverified_Advertise");
        //mThumbDatabase = FirebaseDatabase.getInstance().getReference().child("Advertise").child(mCurrent_user.getUid());
        return mHostelView;
    }

    private void init()
    {
        //recycelerview of feeds
        mADList =  mHostelView.findViewById(R.id.ad_list);
        mADList.setHasFixedSize(true);
        mADList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

    }


    @Override
    public void onStart()
    {
        super.onStart();

        mProgressDialog.setTitle("Fetching Data");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        //        mAdDatabase.child("Hostel").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot)
//            {
//                Advertise ad = dataSnapshot.getValue(Advertise.class);
//                Log.i("main_splash", ad.toString());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError)
//            {
//
//            }
//        });



//        FirebaseRecyclerOptions<Advertise> options = new FirebaseRecyclerOptions.Builder<Advertise>()
//                .setQuery(mAdDatabase.child("Hostel"), Advertise.class)
//                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Advertise, AdViewHolder>(
                new FirebaseRecyclerOptions.Builder<Advertise>()
                        .setQuery(mAdDatabase.child("Hostel"), Advertise.class)
                        .build())
        {
            @Override
            public AdViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
            {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                Log.i("hellohaptik", "hellow");
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_ad_layout, parent, false);

                return new AdViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull final AdViewHolder adViewHolder, int position, @NonNull final Advertise Ad)
            {
                final String hostel_id = getRef(position).getKey();
                googlemap_url = "http://maps.google.com/maps?saddr=" + sourceLatitude + "," + sourceLongitude + "&daddr=" + Ad.getAccom_latLng().getLatitude() + "," + Ad.getAccom_latLng().getLongitude() + "&travelmode=driving";

                Log.i("Ad", Ad.toString());

                //adViewHolder.setViewPager(Ad.getThumbs());
                adViewHolder.setName(Ad.getAccom_name());
                adViewHolder.setAddress(Ad.getAccom_address());
                adViewHolder.setDistance(sourceLatitude, sourceLongitude, Ad.getAccom_latLng().getLatitude(), Ad.getAccom_latLng().getLongitude());
                adViewHolder.setImage(Ad.getFirstThumbUrl());

                Object timestamp  =  Ad.getTimestamp();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                sdf.format(new Date((Long) timestamp));

                Log.i("date", String.valueOf(sdf.format(new Date((Long) timestamp))));


                adViewHolder.img_location.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        // Toast.makeText(getActivity(), "img_location" +new Global_Ads().getGlobal_latLng(), Toast.LENGTH_LONG).show();
//                        Intent mapIntent, chooser;
//                        mapIntent = new Intent(Intent.ACTION_VIEW);
//                        mapIntent.setData(Uri.parse("geo:19.076, 72.8777"));
//                        chooser = Intent.createChooser(mapIntent, "Launch Maps");
//                        startActivity(chooser);
                        Log.i("hihihi", sourceLatitude + sourceLongitude + "");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(googlemap_url));
                        startActivity(mapIntent);
                    }
                });

                adViewHolder.adCardView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {

                        Toast.makeText(getActivity(), "hostel", Toast.LENGTH_LONG).show();
                        Intent detailIntent  = new Intent(getContext(), DetailViewActivity2.class);
                       /* detailIntent.putExtra("acom_id", hostel_id);
                        detailIntent.putExtra("type", "Hostel");*/
                        detailIntent.putExtra("Ad",  Ad);
                        detailIntent.putExtra("Ad_rooms", (Serializable) Ad.getRoom_types());
                        detailIntent.putExtra("Ad_thumbs", (Serializable) Ad.getThumbs());
                        detailIntent.putExtra("Ad_facilities", (Serializable) Ad.getFacilities_map());
                        detailIntent.putExtra("googlemap_url", googlemap_url);
                        detailIntent.putExtra("Ad_distance", (Serializable) adViewHolder.getDistanceInMeters());
                        Log.i("detailIntent", detailIntent.toString() + " dsvv");
                        startActivity(detailIntent);
                    }
                });
            }

            @Override
            public void onDataChanged()
            {
                super.onDataChanged();
                if (mProgressDialog != null && mProgressDialog.isShowing())
                {
                    mProgressDialog.dismiss();
                }
            }

            @Override
            public void onError(DatabaseError e)
            {
                Log.i("onError", String.valueOf(e));
            }
        };
        firebaseRecyclerAdapter.startListening();
        mADList.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();

    }
}




