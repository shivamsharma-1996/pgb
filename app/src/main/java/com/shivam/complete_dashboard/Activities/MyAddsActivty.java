package com.shivam.complete_dashboard.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.google.firebase.database.Query;
import com.shivam.complete_dashboard.Holders.AdViewHolder;
import com.shivam.complete_dashboard.Holders.myAdd_viewholder;
import com.shivam.complete_dashboard.Models.Advertise;
import com.shivam.complete_dashboard.R;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyAddsActivty extends AppCompatActivity {

    //toolbar
    private Toolbar mToolbar;

    //Firebase
    private DatabaseReference m_myAdDatabase;
    private FirebaseUser mCurrentUser;
    private String mCurrent_uid;


    //Recyclerview
    private RecyclerView myAdds_rec;
    private ProgressDialog mProgressDialog;


    private FirebaseRecyclerAdapter<Advertise, myAdd_viewholder> myadsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_adds_activty);

        //toolbar set
        mToolbar = (Toolbar) findViewById(R.id.myAddtoolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("My Adds");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mCurrentUser != null)
        {
            mCurrent_uid = mCurrentUser.getUid();
        }

        m_myAdDatabase = FirebaseDatabase.getInstance().getReference().child("My_Ads").child(mCurrent_uid);

        myAdds_rec = (RecyclerView) findViewById(R.id.rec_myAdds_list);
        myAdds_rec.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        myAdds_rec.setHasFixedSize(true);

        mProgressDialog = new ProgressDialog(MyAddsActivty.this);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        mProgressDialog.setTitle("Wait while Getting Adds");
        mProgressDialog.setCanceledOnTouchOutside(true);
        mProgressDialog.show();

        Log.i("onStart", "onStart");
        myadsAdapter = new FirebaseRecyclerAdapter<Advertise, myAdd_viewholder>(
                new FirebaseRecyclerOptions.Builder<Advertise>()
                        .setQuery(m_myAdDatabase, Advertise.class)
                        .build())
        {
            @Override
            public myAdd_viewholder onCreateViewHolder(ViewGroup parent, int viewType)
            {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_my_adds_layout, parent, false);

                return new myAdd_viewholder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull myAdd_viewholder myAdd_viewholder, int position, @NonNull final Advertise myAdd)
            {
                final String myAd_id = getRef(position).getKey();

                Log.i("myAds", myAdd.toString());

                myAdd_viewholder.setName(myAdd.getAccom_name());
                myAdd_viewholder.setStatus(myAdd.getVerification_status());
                myAdd_viewholder.setDate(myAdd.getTimestamp());
                /*myAdd_viewholder.adCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //                        Toast.makeText(MyAddsActivty.this, "hostel", Toast.LENGTH_LONG).show();
//                        Intent detailIntent  = new Intent(MyAddsActivty.this, DetailViewActivity2.class);
//                        startActivity(detailIntent);

                    }
                });*/
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
        myadsAdapter.startListening();
        myAdds_rec.setAdapter(myadsAdapter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        myadsAdapter.stopListening();

    }
}
