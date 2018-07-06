package com.shivam.complete_dashboard.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shivam.complete_dashboard.R;

public class DetailViewActivity extends AppCompatActivity {

    private TextView mDetail_ownerName, mDetail_acomName, mDetail_acomType, mDetail_ownerNo,
             mDetail_mapAddress, mDetail_gender, mDetail_roomType, mDetail_roomImages,
             mDetail_facilities,mDetail_rules;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabseAcom;
    String mAcom_id;
    String mAcom_type;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        initViews();

        mAcom_id = getIntent().getExtras().getString("acom_id");
        mAcom_type = getIntent().getExtras().getString("type");

        Log.i("mAcom_id", mAcom_id);
        Log.i("mAcom_type", mAcom_type );

        mAuth = FirebaseAuth.getInstance();
        mDatabseAcom = FirebaseDatabase.getInstance().getReference().child("Advertise");
        mDatabseAcom.keepSynced(true);
    }

    private void initViews()
    {
        mDetail_ownerName = (TextView) findViewById(R.id.detail_ownerName);
        mDetail_acomName = (TextView) findViewById(R.id.detail_acomName);
        mDetail_acomType = (TextView) findViewById(R.id.detail_acomType);
        mDetail_ownerNo = (TextView) findViewById(R.id.detail_ownerNo);
        mDetail_mapAddress = (TextView) findViewById(R.id.detail_mapAddress);
        mDetail_gender = (TextView) findViewById(R.id.detail_gender);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        mDatabseAcom.child(mAcom_type).child(mAcom_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mDetail_ownerName.setText(dataSnapshot.child("owner_name").getValue().toString());
                mDetail_acomName.setText(dataSnapshot.child("Name").getValue().toString());
                mDetail_acomType.setText(dataSnapshot.child("accomType").getValue().toString());
                mDetail_ownerNo.setText(dataSnapshot.child("contact").getValue().toString());
                mDetail_mapAddress.setText(dataSnapshot.child("owner_name").getValue().toString());
                mDetail_gender.setText(dataSnapshot.child("gender").getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }
}
