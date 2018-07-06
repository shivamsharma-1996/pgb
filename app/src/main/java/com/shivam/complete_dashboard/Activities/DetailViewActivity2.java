package com.shivam.complete_dashboard.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shivam.complete_dashboard.Adapter.detailviewPagerAdapter;
import com.shivam.complete_dashboard.Advertise.Detail_Facility_Adapter;
import com.shivam.complete_dashboard.Advertise.Detail_Rules_adapter;
import com.shivam.complete_dashboard.Advertise.Room_final;
import com.shivam.complete_dashboard.Advertise.Room_last;
import com.shivam.complete_dashboard.Models.Advertise;
import com.shivam.complete_dashboard.Models.Roomtype;
import com.shivam.complete_dashboard.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetailViewActivity2 extends AppCompatActivity implements View.OnClickListener{

    //Textviews
    private TextView mTv_Name, mTv_Address, mTv_owner, mTv_ownerPhone, mTv_roomType, mTv_roomRent, mTv_room_facilties, mTv_no_of_rooms, mTv_installment1, mTv_installment2, mDate;
    private LinearLayout preview_ll_roomtype;

    //Imageview
    ImageView mapImg;


    //Data for Preview
    private String owner_name = "", accom_type = "", accom_name = "", accom_area = "", accom_address = "",accom_address_with_map = "", contact = "", gender = "";
    private ArrayList<Roomtype> roomTypesList = new ArrayList();
    private HashMap<String, Bitmap> uploadImagesMap = new HashMap<>();
    private HashMap<String, Bitmap> img_firebase_storage_map = new HashMap<>();
    private Map<String, String> img_firebase_db_map = new HashMap<>();


    private ViewPager vp;
    private detailviewPagerAdapter adapter;
    private Toolbar mToolbar;






    //Firebase
    private DatabaseReference mAdvetiseDatabase;
    private StorageReference mImageStorage;
    private FirebaseUser mCurrentUser;
    private String current_uid;


    //ProgressDialog
    private ProgressDialog mProgressDialog;
    private static int IMG_COUNT = 0;
    private static int ROOM_COUNT = 0;
    private Bitmap compressedImageBitmap = null;


    //facilities
    private RecyclerView rec_detail_facilitiesList1, rec_detail_facilitiesList2;
    private Detail_Facility_Adapter detail_Facility_Adapter;
    private Map<String, String> facility_including_in_rent = new HashMap<>(), facility_extra_charges = new HashMap<>();

    //Rules_list
    private RecyclerView rec_detail_rulesList;
    private Detail_Rules_adapter detail_rules_adapter;
    private List<String> rules_list = new ArrayList<>();
    private static int rule_counter = 0;

    Advertise detailAd;
    private double adcard_distance;
    private String googlemap_url ="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);      //full scree mode
        setContentView(R.layout.activity_detail_view2);

        Intent intent = getIntent();
        detailAd = (Advertise) intent.getSerializableExtra("Ad");

        Log.i("detailAd", String.valueOf(detailAd));

        detailAd.setRoom_types((Map<String, Room_last>) intent.getSerializableExtra("Ad_rooms"));
        detailAd.setThumbs((Map<String, String>) intent.getSerializableExtra("Ad_thumbs"));

        adcard_distance = intent.getDoubleExtra("Ad_distance", 123);
        googlemap_url = intent.getStringExtra("googlemap_url");
        Log.i("googlemap_url", googlemap_url);


        for(String rule : detailAd.getRules_list())
        {
            rule =   (++rule_counter) + rule.replaceAll("[1-9]","") ;
            //Log.i("rule", rule);
            rules_list.add(rule);
        }

        Log.i("detailAd", String.valueOf(detailAd));

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        actionBar.setTitle("");
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));

        initViews();

        mTv_ownerPhone.setOnClickListener(this);
        mapImg.setOnClickListener(this);

        Log.i("detailAd", detailAd.toString());


        //Back button listner
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(DetailViewActivity2.this, "click", Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }




    @SuppressLint("ResourceType")
    @Override
    protected void onStart()
    {
        super.onStart();


        if(!(detailAd.getAccom_name()).toLowerCase().contains("boys"))
        {
            mTv_Name.setText(detailAd.getAccom_name() + "(Boys)");
        }
        mTv_Address.setText(detailAd.getAccom_address());
        mTv_owner.setText(detailAd.getOwner_name());
        mTv_ownerPhone.setText(detailAd.getContact1());



        for(String key : detailAd.getRoom_types().keySet())
        {
            Room_last room_last = detailAd.getRoom_types().get(key);
            View room_view = getLayoutInflater().inflate(R.layout.room_details_layout2,null);
            mTv_roomType = room_view.findViewById(R.id.preview_room_type);
            mTv_roomRent = room_view.findViewById(R.id.preview_room_rent);

            mTv_room_facilties = room_view.findViewById(R.id.tv_room_facilities);
            mTv_no_of_rooms = room_view.findViewById(R.id.tv_no_of_rooms);
            mTv_installment1 = room_view.findViewById(R.id.preview_installment1);
            mTv_installment2 = room_view.findViewById(R.id.preview_installment2);
            mDate = room_view.findViewById(R.id.preview_date);

            mTv_roomType.setText("  " + room_last.getBed_type());
            mTv_roomRent.setText( "₹" + room_last.getRent() +"/" +  room_last.getTime());
            mTv_room_facilties.setText(room_last.getAc_type() + " + " + room_last.getLet_bath_type());
            mTv_no_of_rooms.setText("No. of rooms : " + room_last.getNo_of_rooms());

            Map<String, String> installment_map = room_last.getInstallments_map();
            if(installment_map.size()==2)
            {
                mTv_installment1.setText("₹" + installment_map.get("Installment-1") );
                mTv_installment2.setText("₹" + installment_map.get("Installment-2") );
            }
            else
            {
                Toast.makeText(this, installment_map.size(), Toast.LENGTH_LONG).show();
            }

            preview_ll_roomtype.addView(room_view);
        }


        if(facility_including_in_rent.size() == 0)
        {
            (findViewById(R.id.tv_including_rent_faciltiy)).setVisibility(View.GONE);
            rec_detail_facilitiesList1.setVisibility(View.GONE);
        }
        else if(facility_extra_charges.size() == 0)
        {
            (findViewById(R.id.tv_extra_charge_faciltiy)).setVisibility(View.GONE);
            rec_detail_facilitiesList2.setVisibility(View.GONE);
        }
    }


    private void initViews()
    {
        //Textviews
        mTv_Name = (TextView) findViewById(R.id.preview_name);
        mTv_Address = (TextView) findViewById(R.id.preview_address);
        mTv_owner = (TextView) findViewById(R.id.preview_owner_name);
        mTv_ownerPhone = (TextView) findViewById(R.id.preview_owner_phone);
        preview_ll_roomtype = (LinearLayout) findViewById(R.id.preview_ll_rooms);


        //Imageview
        mapImg = (ImageView) findViewById(R.id.preview_map);

        //Image_slider
        vp = (ViewPager) findViewById(R.id.view_pager);
        adapter = new detailviewPagerAdapter(this, detailAd.getThumbs(), "DetailViewActivity2");
        vp.setAdapter(adapter);

        Log.i("detailAd.getThumbs()", String.valueOf(detailAd.getThumbs()));



        //Firebase
        mImageStorage = FirebaseStorage.getInstance().getReference();
        mAdvetiseDatabase = FirebaseDatabase.getInstance().getReference().child("Advertise");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        //current_uid = mCurrentUser.getUid();



        for(String key : (detailAd.getFacilities_map()).keySet())
        {
            switch ((detailAd.getFacilities_map()).get(key))
            {
                case "including in rent":
                    facility_including_in_rent.put(key, "Including In Rent" );
                    break;
                default:
                    facility_extra_charges.put(key, "₹" + (detailAd.getFacilities_map()).get(key) );
                    break;
            }
        }




        //Facilities
        rec_detail_facilitiesList1 = (RecyclerView) findViewById(R.id.rec_detail_facilitiesList1);
        detail_Facility_Adapter = new Detail_Facility_Adapter(this, facility_including_in_rent);
        rec_detail_facilitiesList1.setHasFixedSize(true);
        rec_detail_facilitiesList1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rec_detail_facilitiesList1.setAdapter(detail_Facility_Adapter);

        rec_detail_facilitiesList2 = (RecyclerView) findViewById(R.id.rec_detail_facilitiesList2);
        detail_Facility_Adapter = new Detail_Facility_Adapter(this, facility_extra_charges);
        rec_detail_facilitiesList2.setHasFixedSize(true);
        rec_detail_facilitiesList2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rec_detail_facilitiesList2.setAdapter(detail_Facility_Adapter);


        //Rules_list
        rec_detail_rulesList = (RecyclerView) findViewById(R.id.rec_detail_rulesList);
        detail_rules_adapter = new Detail_Rules_adapter(this ,rules_list);



        rec_detail_rulesList.setHasFixedSize(true);
        rec_detail_rulesList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rec_detail_rulesList.setAdapter( detail_rules_adapter);
    }


    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch (id)
        {
            case R.id.preview_owner_phone:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mTv_ownerPhone.getText()));
                startActivity(intent);
                break;

            case R.id.preview_map:
                //                Intent mapIntent, chooser;
//                mapIntent = new Intent(Intent.ACTION_VIEW);
//                mapIntent.setData(Uri.parse("geo:19.076, 72.8777"));
//                chooser = Intent.createChooser(mapIntent, "Launch Maps");
//                startActivity(chooser);
//
//                com.shivam.complete_dashboard.Models.LatLng latLng = detailAd.getAccom_latLng();
//                String url = "https://www.google.com/maps/dir/?api=1&destination=" + latLng.getLatitude() + "," + latLng.getLongitude() + "&travelmode=driving";
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(mapIntent);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(googlemap_url));
                startActivity(mapIntent);

             //Toast.makeText(DetailViewActivity2.this,"Under Developement!", Toast.LENGTH_LONG).show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        //getMenuInflater().inflate(R.menu.detailview_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.nav_bookmark)
        {
           // item.setIcon(R.drawable.bookmark_yes);
            Toast.makeText(this,"Under Developement!",Toast.LENGTH_LONG).show();
        }
        else if (id == R.id.nav_share)
        {
            //            Intent sharingIntent = new Intent("android.intent.action.SEND");
//            sharingIntent.setFull_type("text/plain");
//            String shareBody = "https://play.google.com/store/apps/details?id=" + getPackageName();
//            sharingIntent.putExtra("android.intent.extra.SUBJECT", "Walldroid Wallpapers v1.2.3");
//            sharingIntent.putExtra("android.intent.extra.TEXT", shareBody);
//            startActivity(Intent.createChooser(sharingIntent, "Share via"));
//        }
            Toast.makeText(this,"Under Developement!",Toast.LENGTH_LONG).show();
        }
        return true;
    }


    @Override
    protected void onStop()
    {
        super.onStop();
        preview_ll_roomtype.removeAllViews();
        adapter = null;
    }
}


