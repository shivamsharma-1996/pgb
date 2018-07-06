package com.shivam.complete_dashboard.Advertise;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shivam.complete_dashboard.Activities.MainActivity;
import com.shivam.complete_dashboard.Adapter.ammenity_adpater;
import com.shivam.complete_dashboard.Adapter.previewPagerAdapter;
import com.shivam.complete_dashboard.Models.Advertise;
import com.shivam.complete_dashboard.Models.Ammenity;
import com.shivam.complete_dashboard.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.zelory.compressor.Compressor;

public class PreviewActivity extends AppCompatActivity implements View.OnClickListener
{
    //Buttons
    private Button mBtn_edit, mBtn_upload;
    //Textviews
    private TextView mTv_Name, mTv_Address, mTv_owner, mTv_ownerPhone, mTv_Room, mTv_roomRent;
    private LinearLayout preview_ll_Room;


    //Data for Preview
    private String owner_name = "", accom_type = "", accom_name = "", accom_area = "", accom_address = "", accom_landmark = "",
                    accom_address_with_map = "", contact1 = "", contact2 = "" , gender = "", isRentsNegotiable = "", electricity_charge = "", water_charge = "";
    private Map<String , String > starting_rate_map;
    private Map<String, String> facilities_map = new HashMap<>();
    private com.shivam.complete_dashboard.Models.LatLng accom_latLng;
    private List<Room_last> RoomsList = new ArrayList();
    private HashMap<String, Bitmap> uploadImagesMap = new HashMap<>();
    private HashMap<String, Bitmap> img_firebase_storage_map = new HashMap<>();
    private Map<String, String> img_firebase_db_map = new HashMap<>();
    private Map<String, Room_last> RoomHashMap = new HashMap<>();
    private List<String> rules_list = new ArrayList<>();
    private String radio_RAWECF;
    private String deposit = "", advance = "";
    private  Time_Availability time_availability = new Time_Availability();

    private Map<String, String> electronics_facility_map = new HashMap<>();
    private Map<String, String> services_facility_map = new HashMap<>();
    private Map<String, String> others_facility_map = new HashMap<>();


    //Booking
    private String booking_fee = "", booking_start_from = "", booking_including_rent = "";


    private ViewPager vp;
    private previewPagerAdapter adapter;
    private Toolbar mToolbar;

    //horizontal recyclerview;
    RecyclerView ammenity_recView;
    List<Ammenity> ammenityList = new ArrayList<>();




    //Firebase
    private DatabaseReference mRootRef, mAdvetiseDatabase;
    private StorageReference mImageStorage;
    private Map<String, String > imgUrl_map = new HashMap<>();
    private FirebaseUser mCurrentUser;
    private String current_uid;
    private String Ad_id;

    //ProgressDialog
    private ProgressDialog mProgressDialog;
    private  int IMG_COUNT = 0;
    private int UPLOAD_COUNT = 0;
    private Bitmap compressedImageBitmap = null;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);      //full scree mode
        setContentView(R.layout.activity_preview);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Preview");


        Intent main_intent = getIntent();
        Bundle ad_extras = main_intent.getExtras();
        owner_name = ad_extras.getString("owner_name");
        accom_type = ad_extras.getString("accom_type");
        accom_name = ad_extras.getString("accom_name");
        accom_type = ad_extras.getString("accom_type");
        accom_area = ad_extras.getString("accom_area");
        accom_address = ad_extras.getString("accom_address");
        accom_landmark = ad_extras.getString("accom_landmark");
        accom_address_with_map = ad_extras.getString("accom_address_with_map");
        accom_latLng = (com.shivam.complete_dashboard.Models.LatLng) ad_extras.getSerializable("accom_latLng");
        contact1 = ad_extras.getString("contact1");
        contact2 = ad_extras.getString("contact2");
        gender = ad_extras.getString("gender");
        deposit =  ad_extras.getString("deposit");
        advance =  ad_extras.getString("advance");

        booking_fee = ad_extras.getString("booking_fee");
        booking_start_from = ad_extras.getString("booking_start_from");
        booking_including_rent = ad_extras.getString("booking_including_rent");

        electricity_charge = ad_extras.getString("electricity_charge");
        water_charge = ad_extras.getString("water_charge");

        Log.i("preview_accom_latLng", String.valueOf(accom_latLng));

        isRentsNegotiable = ad_extras.getString("isRentsNegotiable");
//        isFacilityAvailWithNoCharge = ad_extras.getString("isFacilityAvailWithNoCharges");
        radio_RAWECF = ad_extras.getString("radio_RAWECF");


        time_availability = new AdActivity().getTimeAvailability();
        RoomsList =  new AdActivity().getRoomTypesList();

        int DISTINGUISER = 0;
        for(Room_last Room : RoomsList)
        {
            Log.i("preview123Room", Room.toString());
            RoomHashMap.put(Room.getFull_type() + (++DISTINGUISER),Room);
//            switch (Room.getBed_type())
//            {
//                case "1-Bed":
//                    RoomHashMap.put(Room.getTag_of_room(),Room);
//                    break;
//
//                case "2-Bed":
//                    RoomHashMap.put(Room.getFull_type(),Room);
//                    break;
//
//                case "3-Bed":
//                    RoomHashMap.put(Room.getFull_type(),Room);
//                    break;
//
//                case "4-Bed":
//                    RoomHashMap.put(Room.getFull_type(),Room);
//                    break;
//            }
        }

        Log.i("getUploadImagesMap", String.valueOf(new AdActivity().getUploadImagesMap()));


        facilities_map = new AdActivity().getFacilitiesMap();
        uploadImagesMap = new AdActivity().getUploadImagesMap();
        Log.i("uploadImagesMap", String.valueOf(uploadImagesMap));

        rules_list = new AdActivity().getRules_list();
        starting_rate_map = new AdActivity().getStarting_rate_map();



        Log.i("preview", isRentsNegotiable + "\n"  + RoomHashMap + "\n" + facilities_map + "" + rules_list + "" + radio_RAWECF);

        initViews();

        mBtn_edit.setOnClickListener(this);
        mBtn_upload.setOnClickListener(this);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        mTv_Name.setText(accom_name);
        mTv_Address.setText(accom_address);
        mTv_owner.setText(owner_name);
        mTv_ownerPhone.setText(contact1);


        LayoutInflater inflater = getLayoutInflater();


        for(Room_last Room : RoomsList)
        {
            View room_view = inflater.inflate(R.layout.room_details_layout2,null);
            //Log.i("Room",Room.toString());
            mTv_Room = room_view.findViewById(R.id.preview_room_type);
            mTv_roomRent = room_view.findViewById(R.id.preview_room_rent);
            mTv_Room.setText(Room.getBed_type() + " Room");
            mTv_roomRent.setText( "₹" + Room.getRent() +"/" +  Room.getTime());
            preview_ll_Room.addView(room_view);
        }
    }




    private void initViews()
    {
        //Buttons
        mBtn_edit = (Button) findViewById(R.id.preview_btn_edit);
        mBtn_upload = (Button) findViewById(R.id.preview_btn_upload);

        //Textviews
        mTv_Name = (TextView) findViewById(R.id.preview_name);
        mTv_Address = (TextView) findViewById(R.id.preview_address);
        mTv_owner = (TextView) findViewById(R.id.preview_owner_name);
        mTv_ownerPhone = (TextView) findViewById(R.id.preview_owner_phone);
        preview_ll_Room = (LinearLayout) findViewById(R.id.preview_ll_rooms);

        //Image_slider
        vp = (ViewPager) findViewById(R.id.view_pager);
        adapter = new previewPagerAdapter(this, uploadImagesMap);
        vp.setAdapter(adapter);



        //Ammenities
        ammenity_recView = (RecyclerView) findViewById(R.id.ammenity_rec_list);

        //Firebase
        mImageStorage = FirebaseStorage.getInstance().getReference();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAdvetiseDatabase = FirebaseDatabase.getInstance().getReference().child("Unverified_Advertise");
        Ad_id = mAdvetiseDatabase.child(accom_type).push().getKey();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mCurrentUser != null)
        {
            current_uid = mCurrentUser.getUid();
        }
        Log.i("uid", mCurrentUser.getUid() + " ");

        //Objects
        ammenityList.add(new Ammenity(R.mipmap.ic_launcher, "Geaser"));
        ammenityList.add(new Ammenity(R.mipmap.ic_launcher, "Geaser"));
        ammenityList.add(new Ammenity(R.mipmap.ic_launcher, "Geaser"));
        ammenityList.add(new Ammenity(R.mipmap.ic_launcher, "Geaser"));
        ammenityList.add(new Ammenity(R.mipmap.ic_launcher, "Geaser"));
        ammenityList.add(new Ammenity(R.mipmap.ic_launcher, "Geaser"));
        ammenityList.add(new Ammenity(R.mipmap.ic_launcher, "Geaser"));
        ammenityList.add(new Ammenity(R.mipmap.ic_launcher, "Geaser"));
        ammenityList.add(new Ammenity(R.mipmap.ic_launcher, "Geaser"));
        ammenityList.add(new Ammenity(R.mipmap.ic_launcher, "Geaser"));
        ammenityList.add(new Ammenity(R.mipmap.ic_launcher, "Geaser"));
        ammenityList.add(new Ammenity(R.mipmap.ic_launcher, "Geaser"));
        ammenityList.add(new Ammenity(R.mipmap.ic_launcher, "Geaser"));
        ammenityList.add(new Ammenity(R.mipmap.ic_launcher, "Geaser"));
        ammenityList.add(new Ammenity(R.mipmap.ic_launcher, "Geaser"));

        //create ammenity horizontal_view
        buildAmmenityView();
    }


    private void buildAmmenityView()
    {
        ammenity_recView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ammenity_recView.setHasFixedSize(true);
        ammenity_recView.setAdapter(new ammenity_adpater(ammenityList));
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.preview_btn_edit:
                finish();
                startActivity(new Intent(this, AdActivity.class));
                break;

            case R.id.preview_btn_upload:
                showUploadImgProgress();
                upload();
                break;
        }

    }




    private void showUploadImgProgress()
    {
        mProgressDialog = new ProgressDialog((this));
        mProgressDialog.setTitle("Uploading Image...");
        mProgressDialog.setMessage("Please wait while we upload");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }
    private void upload()
    {
        //storing ad to database without images
        store_Ad_To_Database();

        Log.i("keySet", String.valueOf(uploadImagesMap.keySet()));
        for (String key: uploadImagesMap.keySet())
        {
            IMG_COUNT++;
            Log.i("checkc_1", IMG_COUNT + " " + uploadImagesMap.size());
            Log.d("key" , key);
            Log.d("key" , String.valueOf(uploadImagesMap.get(key)));
            try
            {
                compressedImageBitmap = new Compressor(this)
                        .setMaxWidth(200)
                        .setMaxHeight(200)
                        .setQuality(40)
                        .compressToBitmap(convertBitmapToFile(uploadImagesMap.get(key), uploadImagesMap.get(key).getClass().getName()));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            img_firebase_storage_map.put("Room" + IMG_COUNT,compressedImageBitmap);
        }
        //NOW, Uploading images to Firebase storages & store urls to database
        uploadImages(img_firebase_storage_map);

    }

    public void uploadImages(final HashMap<String, Bitmap> img_firebase_storage_map)
    {
        //these 3 line of code copied from firebase docs and used to upload an bitmap img to firebase storage
        Bitmap compressedImageBitmap_to_upload = null;

        for(final String key : img_firebase_storage_map.keySet())
        {
            UPLOAD_COUNT++;

            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            compressedImageBitmap_to_upload = img_firebase_storage_map.get(key);
            compressedImageBitmap_to_upload.compress(Bitmap.CompressFormat.PNG, 60, baos);
            final byte[] thumb_byte = baos.toByteArray();

            // Log.i("pathString", String.valueOf(mImageStorage.child("advertise/room.png")));
            final StorageReference thumb_filepath = mImageStorage.child("advertise").child(current_uid).child(Ad_id).child( key  +  ".png");

            UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task)
                {
                    //after successfully store  image into firebase storage ,then storing the imageurl to  current user database so,downloading it first.

                    if (thumb_task.isSuccessful())
                    {
                        //if hashmap is used instead of Map object then current user storage only contains image & thumb_img then we get null pointer error so created a Map object

                        //store_Ad_To_Database();

                        @SuppressWarnings("VisibleForTests")
                        String thumb_downloadUrl = thumb_task.getResult().getDownloadUrl().toString();
                        imgUrl_map.put(key, thumb_downloadUrl);

                        if(UPLOAD_COUNT == img_firebase_storage_map.size())
                        {
                            storeImageUrls(imgUrl_map);
                        }

                    }
                    else
                    {
                        Toast.makeText(PreviewActivity.this,"error in uploading thumbnail", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        //Stoing imgUrls to database

    }

    private void storeImageUrls(Map<String, String> imgUrl_map)
    {
        Log.i("imgUrl_map", String.valueOf(imgUrl_map));
        //        Map<String, Object> thumb_updates = new HashMap<>();
//
//        thumb_updates.put("Unverified_Advertise/" + accom_type + "/" + Ad_id + "/thumbs", String.valueOf(imgUrl_map));
//
//        mRootRef.updateChildren(thumb_updates).addOnCompleteListener(new OnCompleteListener<Void>()
//        {
//            @Override
//            public void onComplete(@NonNull Task<Void> task)
//            {
//                if(task.isSuccessful())
//                {
//                    Log.i("img upload status", "succesful");
//                }
//                else
//                {
//                }
//            }
//        });

        mAdvetiseDatabase.child(accom_type).child(Ad_id).child("thumbs").setValue(imgUrl_map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    Log.i("img upload status", "succesful");

                    Log.i("imgURls are stored", "storeImageUrls");

                    mProgressDialog.dismiss();

                    AlertDialog.Builder builder = new AlertDialog.Builder(PreviewActivity.this);
                    builder.setTitle("Thank you " + owner_name)
                            .setMessage("Our Represetive will contact1 you within 2-3 hours to verify your details!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i)
                                {
                                    //finish();
                                    startActivity(new Intent(PreviewActivity.this, MainActivity.class));
                                }
                            });

                    AlertDialog successDialog = builder.create();
                    successDialog.show();
//                    if(UPLOAD_COUNT == uploadImagesMap.size())
//                    {
//
//                    }
                }
                else
                {
                    Log.i("img upload status", "unsuccesful");
                }
            }
        });
    }
    private File convertBitmapToFile(Bitmap bitmap, String name)
    {
        File filesDir = getApplicationContext().getFilesDir();
        File imageFile = new File(filesDir, name + ".png");

        OutputStream os;
        try
        {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        }
        catch (Exception e)
        {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return imageFile;
    }


    private void store_Ad_To_Database()
    {
        Advertise upload_ad = new Advertise("pending", owner_name, accom_type, accom_name, accom_area, accom_address, accom_landmark, accom_address_with_map, accom_latLng, contact1,contact2, gender, deposit, advance, starting_rate_map, booking_fee ,booking_start_from, booking_including_rent, electricity_charge, water_charge, time_availability, RoomHashMap, img_firebase_db_map, ServerValue.TIMESTAMP, isRentsNegotiable, radio_RAWECF, facilities_map, rules_list);

        //adding 2 queries in a single hashmap , so that we can minimized a lot of code & can improve our code
        Map ad_requestMap = new HashMap();


       /* Map user_ads_map = new HashMap();
        user_ads_map.put(accom_type, Ad_id);*/

        ad_requestMap.put("/Unverified_Advertise/" + accom_type + "/" + Ad_id, upload_ad);           //unverified advertisement
        ad_requestMap.put("/My_Ads/" + current_uid + "/"  + Ad_id , upload_ad);
        ad_requestMap.put("All_Ads/" + Ad_id, upload_ad);
        mRootRef.updateChildren(ad_requestMap, new DatabaseReference.CompletionListener()
        {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference)
            {
                if(databaseError != null)
                {
                    Toast.makeText(PreviewActivity.this, "There was some error in uploading your property",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(PreviewActivity.this, "",Toast.LENGTH_LONG).show();
                }
            }
        });
    }



    @Override
    protected void onStop()
    {
        super.onStop();
        preview_ll_Room.removeAllViews();
    }
}