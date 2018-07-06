package com.shivam.complete_dashboard.Tabs;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class PG extends Fragment
{
    //Views
    private View mPGView, mAdFooterView, mAdHeaderView;
    private RecyclerView mADList;
    private ProgressDialog mProgressDialog;


    //Firebase
    private DatabaseReference mAdDatabase;
    //private DatabaseReference mThumbDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrent_user;

    public PG()
    {
        // Required empty public constructor
    }


    private Global_Ads global_ads  = new Global_Ads();
    private FirebaseRecyclerAdapter<Advertise, AdViewHolder> firebaseRecyclerAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        mProgressDialog = new ProgressDialog(getContext());

        mPGView = inflater.inflate(R.layout.tab_hostel, container, false);
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
        return mPGView;
    }

    private void init()
    {
        //recycelerview of feeds
        mADList =  mPGView.findViewById(R.id.ad_list);
        mADList.setHasFixedSize(true);
        mADList.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    @Override
    public void onStart()
    {
        super.onStart();

        mProgressDialog.setTitle("Fetching Data");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

       firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Advertise, AdViewHolder>(
                new FirebaseRecyclerOptions.Builder<Advertise>()
                        .setQuery(mAdDatabase.child("PG"), Advertise.class)
                        .build())
        {
            @Override
            public AdViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
            {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_ad_layout, parent, false);

                return new AdViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull AdViewHolder adViewHolder, int position, @NonNull final Advertise Ad)
            {
                final String hostel_id = getRef(position).getKey();
                Log.i("Ad", Ad.toString());

                adViewHolder.setName(Ad.getAccom_name());
                adViewHolder.setAddress(Ad.getAccom_address());


                Object timestamp  =  Ad.getTimestamp();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                sdf.format(new Date((Long) timestamp));
                Log.i("date", String.valueOf(sdf.format(new Date((Long) timestamp))));

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
            public void onError(DatabaseError e) {
                // Called when there is an error getting data. You may want to update
                // your UI to display an error message to the user.
                // ...
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
    public void onStop()
    {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }
}
