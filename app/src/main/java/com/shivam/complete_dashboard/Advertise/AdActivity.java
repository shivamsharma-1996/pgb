package com.shivam.complete_dashboard.Advertise;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.shivam.complete_dashboard.Adapter.HorizontalAdapter;
import com.shivam.complete_dashboard.Models.LatLng;
import com.shivam.complete_dashboard.Models.Rec_Item;
import com.shivam.complete_dashboard.Other.ConnectionDetector;
import com.shivam.complete_dashboard.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.zelory.compressor.Compressor;


/**
 * Created by shivam on 12/1/18.
 */

public class AdActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //Device's Location
    protected Location mLastLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final String TAG = AdActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private LatLng mFusedLocationClient_latLng;


    //facility dialog
    AlertDialog extra_charge_dialog;
    AlertDialog.Builder builder;
    View extra_charge_view;
    Button bt_submit, bt_exit;
    TextView tv_no_charges, bt_as_per_meter_charges;
    EditText et_extra_charge;


    //toolbar
    private Toolbar mToolbar;

    //edittext
    private EditText et_ownerName, et_accomName, et_contact1, et_contact2, et_properAddress, et_landmark;

    //Textview
    private TextView tv_address, tv_availability_start, tv_availability_end, tv_electricity_charges, tv_water_charges;
    private static Time_Availability time_availability = new Time_Availability();

    //Spinner
    private Spinner areaSpinner, acommTypeSpinner;
    private ArrayAdapter<String> areaAdapter, acommTypeAdapter;

    //preview button
    private Button previewBtn;


    private String accom_area = "", owner_name = "", accom_type = "", accom_name = "", contact1 = "", contact2 = "", accom_address = "",
            accom_landmark = "", accom_address_with_map = "", gender = "", electricity_charge = "", water_charge = "", booking_fee = "", booking_start_from = "", booking_including_rent = "";
    private int starting_rate = 100000000;
    private String time_of_starting_rent;
    private static Map<String, String> starting_rate_map = new HashMap<>();

    private com.shivam.complete_dashboard.Models.LatLng accom_latLng = new LatLng();

    //Image Uploads block
    private RecyclerView horizontal_recycler_view;
    private HorizontalAdapter horizontalAdapter;
    private List<Rec_Item> rec_list = new ArrayList<>();
    private final static int GALLERY_REQUEST = 1;
    public Bitmap compressedImageBitmap = null;
    public ImageView rec_imgview, rec_imgDeleteBtn;
    private TextView rec_txtview;
    private String rec_title;
    private static int IMG_UPLOAD_COUNT = 0;
    private static HashMap<String, Bitmap> uploadImagesMap = new HashMap<>();

    //BOOKING SYSTEM
    private EditText et_booking_fee;
    private TextView tv_booking_startFrom;
    private RadioGroup rg_booking;
    private RadioButton rb_booking_selected;

    //Roomtypes block
    private static List<Room_last> roomTypesList = new ArrayList<>();
    private RadioGroup rg_isRentsNegotiable;
    private String isRentsNegotiable = "";
    private RadioButton radio_RAWECF_yes, radio_RAWECF_no;
    private String radio_RAWECF = "";


    //Advance & Deposit Block
    private EditText et_deposit, et_advance;
    private String deposit = "", advance = "";

    //Facilities block
    private RecyclerView electric_recyclerview, service_recyclerview, others_recycelrview;
    private facility_adapter facility_adapter;
    private List<String> electric_facility_list = new ArrayList<>(), service_facility_list = new ArrayList<>(), other_facility_list = new ArrayList<>();
    private static LinearLayout summery_facilities_ll;
    private View summery_facilities_view;
    private static Map<String, String> facilities_map = new HashMap<>();


    //Rules block
    private RecyclerView rules_recyclerview;
    private Rules_adapter rules_adapter;
    private static List<String> rules_list = new ArrayList<>();

    //public getters of AdActivity
    public LinearLayout getSummery_facilities_ll() {
        return summery_facilities_ll;
    }

    public static List<Room_last> getRoomTypesList() {
        return roomTypesList;
    }

    public static Time_Availability getTimeAvailability() {
        return time_availability;
    }

    public static HashMap<String, Bitmap> getUploadImagesMap() {
        return uploadImagesMap;
    }

    public static Map<String, String> getFacilitiesMap() {
        return facilities_map;
    }

    public static List<String> getRules_list() {
        return rules_list;
    }


    public static Map<String, String> getStarting_rate_map() {
        return starting_rate_map;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);

        //toolbar
        mToolbar = (Toolbar) findViewById(R.id.ad_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add Your Property");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        getLocationPermission();

        SharedPreferences restorePrefs = getSharedPreferences("preview_state", MODE_PRIVATE);
        if (restorePrefs != null) {
            String accom_area = restorePrefs.getString("accom_area", "");
            String owner_name = restorePrefs.getString("owner_name", "");
            String accom_type = restorePrefs.getString("accom_type", "");
            String accom_name = restorePrefs.getString("accom_name", "");
            String contact = restorePrefs.getString("contact", "");
            String accom_address = restorePrefs.getString("accom_address", "");
            String accom_address_with_map = restorePrefs.getString("accom_address_with_map", "");
            String gender = restorePrefs.getString("gender", "");

            if (accom_type.equals("Hostel")) {
                areaSpinner.setSelection(0);
            } else if (accom_type.equals("PG")) {
                areaSpinner.setSelection(1);
            }
            if (gender.equals("Boy")) {
                ((RadioButton) findViewById(R.id.boy)).setChecked(true);
            } else if (gender.equals("Girl")) {
                ((RadioButton) findViewById(R.id.girl)).setChecked(true);
            }
            et_ownerName.setText(owner_name);
            et_accomName.setText(accom_name);
            et_properAddress.setText(accom_address_with_map);
            tv_address.setText(accom_address);
            et_contact1.setText(contact);
        }


        //setting adapters
        areaSpinner.setAdapter(areaAdapter);
        acommTypeSpinner.setAdapter(acommTypeAdapter);


        //setOnItemSelectedListener
        areaSpinner.setOnItemSelectedListener(this);
        acommTypeSpinner.setOnItemSelectedListener(this);

    }

    private void getLocationPermission() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(AdActivity.this);
        if (!checkPermissions()) {
            requestPermissions();
            Log.i("checkPermissions()", "1");
        } else {
            Log.i("checkPermissions()", "0");

            getLastLocation();
        }
    }


    private void initViews() {
        //edittext
        et_ownerName = (EditText) findViewById(R.id.et_ownerName);
        et_accomName = (EditText) findViewById(R.id.et_acommName);
        et_contact1 = (EditText) findViewById(R.id.et_contact1);
        et_contact2 = (EditText) findViewById(R.id.et_contact2);
        tv_address = (TextView) findViewById(R.id.tv_address_on_map);
        tv_availability_start = (TextView) findViewById(R.id.availability_start);
        tv_availability_end = (TextView) findViewById(R.id.availability_end);

        tv_electricity_charges = (TextView) findViewById(R.id.tv_electricity_charges);
        tv_water_charges = (TextView) findViewById(R.id.tv_water_charges);

        et_properAddress = (EditText) findViewById(R.id.et_properAddress);
        et_landmark = (EditText) findViewById(R.id.et_landmark);
        //btn
        previewBtn = (Button) findViewById(R.id.preview_btn);

        //Spinners
        areaSpinner = (Spinner) findViewById(R.id.areaSpinner);
        acommTypeSpinner = (Spinner) findViewById(R.id.accomTypeSpinner);


        //----------------------------- adapters for spinners -------------------------
        String[] areas = getResources().getStringArray(R.array.areas);
        areaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, areas);
        areaAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);


        String[] acommTypes = getResources().getStringArray(R.array.acommTypes);
        acommTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, acommTypes);
        acommTypeAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        //-------------------------------------  End --------------------------------------------------

        createFacility_RecyclerviewData();
        //facilities_summery
        summery_facilities_ll = (LinearLayout) findViewById(R.id.summery_facilities);

        // RoomTypes Recyclerview
        //roomInstance_adapter = new RoomInstance_Adapter(this);
        /*roomInstance_Adapter2 = new RoomInstance_Adapter2(this);

        rec_room_instance_list = (RecyclerView) findViewById(R.id.rec_room_instance);
        rec_room_instance_list.setHasFixedSize(true);
        rec_room_instance_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));*/
        //rec_room_instance_list.setAdapter(roomInstance_Adapter2);


        // ----------------------- Facilities recyclerviews starts ------------------
        facility_adapter = new facility_adapter(this, electric_facility_list);
        electric_recyclerview = (RecyclerView) findViewById(R.id.rec_electric_facility);
        electric_recyclerview.setLayoutManager(new GridLayoutManager(this, 4));
        electric_recyclerview.setHasFixedSize(true);
        electric_recyclerview.setAdapter(facility_adapter);

        facility_adapter = new facility_adapter(this, service_facility_list);
        service_recyclerview = (RecyclerView) findViewById(R.id.rec__service_facility);
        service_recyclerview.setLayoutManager(new GridLayoutManager(this, 3));
        service_recyclerview.setHasFixedSize(true);
        service_recyclerview.setAdapter(facility_adapter);

        facility_adapter = new facility_adapter(this, other_facility_list);
        others_recycelrview = (RecyclerView) findViewById(R.id.rec__others_facility);
        others_recycelrview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        others_recycelrview.setHasFixedSize(true);
        others_recycelrview.setAdapter(facility_adapter);
        // ----------------------- Facilities recyclerviews ends ------------------


        // ------------------------------Booking block starts --------------------------
        et_booking_fee = (EditText) findViewById(R.id.et_Booking_Fee);
        tv_booking_startFrom = (TextView) findViewById(R.id.tv_booking_startFrom);
        rg_booking = (RadioGroup) findViewById(R.id.rg_booking);
        // ------------------------------Booking block ends --------------------------



        rg_isRentsNegotiable = (RadioGroup) findViewById(R.id.rg_isRentsNegotiable);
//        radio_isFacilityAvailWithNoCharges_yes = findViewById(R.id.radio_isFacilityAvailWithNoCharges_yes);
//        radio_isFacilityAvailWithNoCharges_no = findViewById(R.id.radio_isFacilityAvailWithNoCharges_no);
        radio_RAWECF_yes = (RadioButton) findViewById(R.id.radio_RAWECF_yes);
        radio_RAWECF_no = (RadioButton) findViewById(R.id.radio_RAWECF_no);
//        rg_isFacilityAvailWithNoCharges_yes = findViewById(R.id.rg_isFacilityAvailWithNoCharges);


        //advance & deposit
        et_deposit = (EditText) findViewById(R.id.et_deposit);
        et_advance = (EditText) findViewById(R.id.et_advance);


        String[] rules = getResources().getStringArray(R.array.rules);
        rules_adapter = new Rules_adapter(this, rules);
        rules_recyclerview = (RecyclerView) findViewById(R.id.rec_ad_rules);
        rules_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rules_recyclerview.setHasFixedSize(true);
        rules_recyclerview.setAdapter(rules_adapter);


        //upload images
        horizontal_recycler_view = (RecyclerView) findViewById(R.id.horizontal_recycler_view);
        horizontalAdapter = new HorizontalAdapter(rec_list);
        createRecList();


        //facility dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(AdActivity.this);
        View extra_charge_view = LayoutInflater.from(AdActivity.this).inflate(R.layout.custom_extra_charge, null);
        et_extra_charge = extra_charge_view.findViewById(R.id.et_extra_charge);
        tv_no_charges = extra_charge_view.findViewById(R.id.bt_no_charges);
        bt_as_per_meter_charges = extra_charge_view.findViewById(R.id.bt_as_per_meter_charges);
        bt_submit = extra_charge_view.findViewById(R.id.bt_submit);
        bt_exit = extra_charge_view.findViewById(R.id.bt_exit);
        builder.setView(extra_charge_view);
        extra_charge_dialog = builder.create();


        //hideKeyboard();

        showMoreRooms();
    }

    private void hideKeyboard() {
        View current_focused_view = this.getCurrentFocus();
        if (current_focused_view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(current_focused_view.getWindowToken(), 0);
        }
    }

    private void createFacility_RecyclerviewData() {
        electric_facility_list.add("Cooler");
        electric_facility_list.add("Duct");
        electric_facility_list.add("TV");
        electric_facility_list.add("Fridge");
        electric_facility_list.add("Geyser");
        electric_facility_list.add("Heater");
        electric_facility_list.add("Inverter");
        electric_facility_list.add("AquaGuard");
        electric_facility_list.add("WaterCooler");
        electric_facility_list.add("Washing Machine");
        electric_facility_list.add("Chair table");


        service_facility_list.add("Tifin");
        service_facility_list.add("Wifi");
        service_facility_list.add("Warden");
        service_facility_list.add("Mess");
        service_facility_list.add("Laundry");
        service_facility_list.add("Parking");
        service_facility_list.add("CCTV");
        service_facility_list.add("Newspaper");
        service_facility_list.add("Transport-Vehicle");
        service_facility_list.add("House-Keeping");

    }


    @Override
    protected void onStart() {
        super.onStart();

       /* final GetCurrenntLocation gcl = new GetCurrenntLocation(AdActivity.this);
        gcl.check();*/

       tv_booking_startFrom.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v)
           {
               final Calendar c = Calendar.getInstance();


               DatePickerDialog dpd = new DatePickerDialog(AdActivity.this,
                       new DatePickerDialog.OnDateSetListener() {

                           @Override
                           public void onDateSet(DatePicker view, int year,
                                                 int monthOfYear, int dayOfMonth) {

                               tv_booking_startFrom.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                               Toast.makeText(AdActivity.this, dayOfMonth + " " + monthOfYear + 1 + " " + year , Toast.LENGTH_LONG).show();
                           }
                       }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
               dpd.show();
           }
       });

        tv_electricity_charges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extra_charge_dialog.show();

                tv_no_charges.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tv_electricity_charges.setText("including in rent");
                        extra_charge_dialog.dismiss();
                    }
                });

                bt_as_per_meter_charges.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tv_electricity_charges.setText("as per submeter");
                        extra_charge_dialog.dismiss();
                    }
                });

                bt_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (et_extra_charge.getText().toString().isEmpty()) {
                            et_extra_charge.setError("enter charge before submit");
                        } else {
                            tv_electricity_charges.setText(et_extra_charge.getText().toString() + " Rs.");
                            extra_charge_dialog.dismiss();
                        }
                    }
                });

                extra_charge_dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        extra_charge_dialog.dismiss();
                    }
                });

                electricity_charge = tv_electricity_charges.getText().toString().trim();
            }
        });
        tv_water_charges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extra_charge_dialog.show();

                tv_no_charges.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tv_water_charges.setText("including in rent");
                        extra_charge_dialog.dismiss();
                    }
                });

                bt_as_per_meter_charges.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tv_water_charges.setText("as per submeter");
                        extra_charge_dialog.dismiss();
                    }
                });

                bt_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (et_extra_charge.getText().toString().isEmpty()) {
                            et_extra_charge.setError("enter charge before submit");
                        } else {
                            tv_water_charges.setText(et_extra_charge.getText().toString() + "Rs.");
                            extra_charge_dialog.dismiss();
                        }
                    }
                });

                extra_charge_dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        extra_charge_dialog.dismiss();
                    }
                });

                water_charge = tv_water_charges.getText().toString().trim();
            }
        });


        tv_availability_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AdActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tv_availability_start.setText(selectedHour + ":" + selectedMinute + "A.M.");
                    }
                }, hour, minute, false);//Yes 24 hour time
                // mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        tv_availability_end.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AdActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tv_availability_end.setText(selectedHour + ":" + selectedMinute + "P.M.");
                    }
                }, hour, minute, false);//Yes 24 hour time
                //mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        tv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence options[] = new CharSequence[]{"Get Your Current Location", "Choose Your Location on Map"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AdActivity.this);
                builder.setTitle("Select One Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            Toast.makeText(AdActivity.this, "map clicked", Toast.LENGTH_LONG).show();
                        } else if (i == 1) {
                            //launch of map
                        }
                    }
                });
                builder.show();
            }
        });


        rg_isRentsNegotiable.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                Log.i("onCheckedChanged", String.valueOf(id));
                switch (id) {
                    case R.id.radio_negotiable_yes:
                        isRentsNegotiable = "true";
                        break;
                    case R.id.radio_negotiable_no:
                        isRentsNegotiable = "false";
                        break;
                }
            }
        });


        /*rg_isFacilityAvailWithNoCharges_yes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId)
                {
                    case R.id.radio_isFacilityAvailWithNoCharges_yes:
                        isFacilityAvailWithNoCharges = "yes";
                        break;
                    case R.id.radio_isFacilityAvailWithNoCharges_no:
                        isFacilityAvailWithNoCharges = "no";
                        break;
                }
            }
        });*/


        facility_adapter.setOnClickForExtraCharges(new ExtraChargeDialogListener() {
            @Override
            public void onDialogBtnClick(String facility_name, String facility_charge) {
                //put selected facilities inside facilities_map
                facilities_map.put(facility_name, facility_charge);

                summery_facilities_view = AdActivity.this.getLayoutInflater().inflate(R.layout.ad_single_summery_layout, null);
                summery_facilities_view.setTag(facility_name);

                summery_facilities_ll.setVisibility(View.VISIBLE);
                TextView tv_facility_name = summery_facilities_view.findViewById(R.id.tv_facility_name);
                TextView tv_facility_charge = summery_facilities_view.findViewById(R.id.tv_facility_charge);
                tv_facility_name.setText(facility_name);
                tv_facility_charge.setText(facility_charge);

                summery_facilities_ll.addView(summery_facilities_view);    //adding summery to summery_facilities_view
            }
        });


        rules_adapter.setOnClick(new OnClickRulesInterface() {
            @Override
            public void onCheckBtnClick(String rule_name) {
                Toast.makeText(AdActivity.this, " rules" + rule_name, Toast.LENGTH_LONG).show();

                rules_list.add("" + rule_name);
                /*for(int i = 0; i < rules_list.size(); i++)
                    {
                        if(rules_list.get(i).equals(rule_name))
                        {
                            rules_list.remove(i);
                        }

                    }*/
            }

        });


        previewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //data
                owner_name = et_ownerName.getText().toString().trim();
                accom_name = et_accomName.getText().toString().trim();

                contact1 = et_contact1.getText().toString().trim();
                contact2 = et_contact2.getText().toString().trim();

                accom_address = et_properAddress.getText().toString().trim();
                accom_landmark = et_landmark.getText().toString().trim();
                accom_address_with_map = tv_address.getText().toString().trim();
                gender = genderChoosed();

                //booking start
                booking_fee = et_booking_fee.getText().toString().trim();
                booking_start_from = tv_booking_startFrom.getText().toString().trim();
                rb_booking_selected = (RadioButton) findViewById(rg_booking.getCheckedRadioButtonId());
                booking_including_rent = rb_booking_selected.getText().toString();
                time_availability = availaibility_time_chooosed();
                //booking end

                //accom_latLng = getLastLocation();
                //roomTypesList = roomTypeChoosed();

                starting_rate_map = getStartingRate();
                radio_RAWECF = (radio_RAWECF_yes.isChecked()) ? "yes" : "no";
                deposit = et_deposit.getText().toString().trim();
                advance = et_advance.getText().toString().trim();
                rules_list = Rules_adapter.getRulesFromAdapter(rules_recyclerview);

                if (validate() == true) {
                    Log.i("ad_data", owner_name);
                    Log.i("ad_data", accom_type);
                    Log.i("ad_data", accom_name);
                    Log.i("ad_data", accom_area);
                    Log.i("ad_data", accom_address);
                    Log.i("ad_data", accom_landmark);
                    Log.i("ad_data", contact1);
                    Log.i("ad_data", contact2);
                    Log.i("ad_data", gender);
                    Log.i("ad_data", deposit);
                    Log.i("ad_data", advance);
                    Log.i("ad_data", booking_fee);
                    Log.i("ad_data", booking_start_from);
                    Log.i("ad_data", booking_including_rent);
                    Log.i("ad_data", booking_fee);
                    Log.i("ad_data", String.valueOf(time_availability));

                    Log.i("accom_latLng", String.valueOf(accom_latLng));
                    Log.i("ad_data", uploadImagesMap.toString());
                    Log.i("ad_data", roomTypesList.toString());
                    Log.i("ad_data", isRentsNegotiable);
                    Log.i("ad_data", String.valueOf(rules_list));
                    // Log.i("ad_data", isFacilityAvailWithNoCharges);
                    Log.i("ad_data", facilities_map.toString());
                    Log.i("ad_data", radio_RAWECF + "     radio_RAWECF");
                    Log.i("ad_data", String.valueOf(time_availability));
                    Log.i("ad_data", String.valueOf(booking_fee));
                    Log.i("electricity_charge", electricity_charge);
                    Log.i("water_charge", water_charge);

                    Intent previewIntent = new Intent(AdActivity.this, PreviewActivity.class);
                    previewIntent.putExtra("owner_name", owner_name);
                    previewIntent.putExtra("accom_type", accom_type);
                    previewIntent.putExtra("accom_name", accom_name);
                    previewIntent.putExtra("accom_area", accom_area);
                    previewIntent.putExtra("accom_address", accom_address);
                    previewIntent.putExtra("accom_landmark", accom_landmark);
                    previewIntent.putExtra("accom_latLng", accom_latLng);
                    previewIntent.putExtra("accom_address_with_map", accom_address_with_map);
                    previewIntent.putExtra("accom_latLng", accom_latLng);
                    previewIntent.putExtra("contact1", contact1);
                    previewIntent.putExtra("contact2", contact2);
                    previewIntent.putExtra("gender", gender);
                    previewIntent.putExtra("deposit", deposit);
                    previewIntent.putExtra("advance", advance);

                    previewIntent.putExtra("booking_fee", booking_fee);
                    previewIntent.putExtra("booking_start_from", booking_start_from);
                    previewIntent.putExtra("booking_including_rent", booking_including_rent);

                    previewIntent.putExtra("electricity_charge", electricity_charge);
                    previewIntent.putExtra("water_charge", water_charge);

                    previewIntent.putExtra("time_availability", time_availability);


                    previewIntent.putExtra("roomTypesList", String.valueOf(roomTypesList));
                    previewIntent.putExtra("isRentsNegotiable", isRentsNegotiable);
                    previewIntent.putExtra("radio_RAWECF", radio_RAWECF);
                    // previewIntent.putExtra("isFacilityAvailWithNoCharges", isFacilityAvailWithNoCharges);
                    previewIntent.putExtra("facilities_map", String.valueOf(facilities_map));
//                    previewIntent.putExtra("starting_rate_map", (Serializable) starting_rate_map);
                    previewIntent.putExtra("uploadImagesMap", uploadImagesMap.toString());


                    startActivity(previewIntent);
                }
            }
        });


        //showMoreRooms();

        (findViewById(R.id.btn_create_room)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreRooms();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        hideKeyboard();
    }

    GridLayout installment_container;
    View room_view;
    ArrayAdapter roomTypeAdapter;
    RadioGroup rg_installment;

    private void showMoreRooms()
    {
        final boolean[] is_Add = {true};
        final int[] Installment = {1};
        final Room_last room_last = new Room_last();

        final LinearLayout ll_room_container = (LinearLayout) findViewById(R.id.rooms_container);

        room_view = getLayoutInflater().inflate(R.layout.single_room_instance_layout2, null);

        final Spinner spinner_roomType = room_view.findViewById(R.id.roomTypeSpinner);
        String[] roomTypes = getResources().getStringArray(R.array.roomTypes);
        roomTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roomTypes);
        roomTypeAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner_roomType.setAdapter(roomTypeAdapter);

        final RadioGroup rg_ac = room_view.findViewById(R.id.rg_acType);
        final RadioGroup rg_let_bath = room_view.findViewById(R.id.rg_let_bath);
        final RadioGroup rg_kitchen_type = room_view.findViewById(R.id.rg_kitchen);
        final EditText et_no_of_rooms = room_view.findViewById(R.id.et_no_of_rooms);
        final EditText et_rent = room_view.findViewById(R.id.et_rent);


        final Spinner spinner_rent_time = room_view.findViewById(R.id.spinner_rent_time);
        String[] room_times = getResources().getStringArray(R.array.room_times);
        roomTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, room_times);
        roomTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_rent_time.setAdapter(roomTypeAdapter);


        rg_installment = room_view.findViewById(R.id.rg_installment);


        //Installments
        final TextView tv_add_more_installment = room_view.findViewById(R.id.tv_add_more_installment);
        installment_container = room_view.findViewById(R.id.installment_container);


        rg_installment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.installment_yes:
                        Log.i("installment_yes", "installment_yes");
                        (room_view.findViewById(R.id.ll_installment)).setVisibility(View.VISIBLE);

                        tv_add_more_installment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Installment[0]++;
                                EditText et_installment = (EditText) getLayoutInflater().inflate(R.layout.dynamic_intallment_layout, null);
                                et_installment.setHint("Installment-" + Installment[0]);
                                installment_container.addView(et_installment);
                            }
                        });
                        break;

                    case R.id.installment_no:
                        Log.i("installment_no", "installment_no");
                        (room_view.findViewById(R.id.ll_installment)).setVisibility(View.GONE);
                        break;
                }
            }
        });


        final Button addThisRoom_btn = room_view.findViewById(R.id.addThisRoom_btn);
        addThisRoom_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                RadioButton rb_ac = room_view.findViewById(rg_ac.getCheckedRadioButtonId());
                String selected_ac_type = (String) rb_ac.getText();
                Log.i("selected_ac_type", selected_ac_type);

                RadioButton rb_let_bath = room_view.findViewById(rg_let_bath.getCheckedRadioButtonId());
                String selected_let_bath_type = (String) rb_let_bath.getText();
                Log.i("selected_let_bath_type", selected_let_bath_type);

                RadioButton rb_kitchen = room_view.findViewById(rg_kitchen_type.getCheckedRadioButtonId());
                String selected_kitchen_type = (String) rb_kitchen.getText();
                Log.i("selected_kitchen_type", selected_kitchen_type);

                String no_of_rooms = et_no_of_rooms.getText().toString().trim();
                Log.i("no_of_rooms", no_of_rooms);

                String rent_of_room = et_rent.getText().toString().trim();
                Log.i("rent_of_room", rent_of_room);

                String rent_time = (String) spinner_rent_time.getSelectedItem();
                Log.i("rent_time", rent_time);
                Toast.makeText(AdActivity.this, "Clicked", Toast.LENGTH_LONG).show();


                RadioButton rb_installment = room_view.findViewById(rg_installment.getCheckedRadioButtonId());
                String is_installment_system = (String) rb_installment.getText();
                Log.i("is_installment_system", is_installment_system);

                String selected_room_type = spinner_roomType.getSelectedItem().toString();


                //room object
                room_last.setFull_type(selected_room_type + selected_ac_type + selected_let_bath_type + "");
                room_last.setAc_type(selected_ac_type);
                room_last.setBed_type(selected_room_type);
                room_last.setKitchen_type(selected_kitchen_type);
                room_last.setLet_bath_type(selected_let_bath_type);
                room_last.setNo_of_rooms(no_of_rooms);
                room_last.setRent(rent_of_room);
                room_last.setTime(rent_time);

                Log.i("room_last", String.valueOf(Installment[0]));
                Log.i("room_last", String.valueOf(installment_container.getChildCount()));

                if ((findViewById(R.id.ll_installment)).getVisibility() == View.VISIBLE) {
                    for (int i = 0; i < Installment[0]; i++) {
                        EditText et_installment = (EditText) installment_container.getChildAt(i);
                        room_last.setInstallments_map(et_installment.getHint().toString(), et_installment.getText().toString());
                    }
                } else {
                    room_last.setClearInstallments_map();
                }

                Log.i("room_last", String.valueOf(room_last));


                if (is_Add[0] == true) {
                    addThisRoom_btn.setText("Remove This Room");
                    roomTypesList.add(room_last);
                    is_Add[0] = false;
                } else {
                    is_Add[0] = true;

                    addThisRoom_btn.setText("Add This Room");
                    roomTypesList.remove(room_last);
                    rb_ac.setChecked(false);
                    rb_let_bath.setChecked(false);
                    rb_kitchen.setChecked(false);
                    rb_installment.setChecked(false);
                    et_no_of_rooms.setText("");
                    et_rent.setText("");
                    spinner_roomType.setSelection(0);
                    spinner_rent_time.setSelection(0);
                    findViewById(R.id.ll_installment).setVisibility(View.GONE);

                    installment_container.removeAllViews();
                    getLayoutInflater().inflate(R.layout.dynamic_intallment_layout, installment_container);

                }
            }
        });

        ll_room_container.addView(room_view);

    }


    private Time_Availability availaibility_time_chooosed() {
        time_availability.setStart_time(tv_availability_start.getText().toString());
        time_availability.setEnd_time(tv_availability_end.getText().toString());
        return time_availability;
    }

    private String genderChoosed() {
        String genChoose = "";
        if (((RadioButton) findViewById(R.id.boy)).isChecked()) {
            genChoose = "Boy";
        }
        if (((RadioButton) findViewById(R.id.girl)).isChecked()) {
            genChoose = "Girl";
        }
        return genChoose;
    }

   /* private List<Room_final> roomTypeChoosed()
    {
        return roomInstance_Adapter2.getSelectedRooms();
    }*/

    public Map<String, String> getStartingRate() {
        int temp_starting_rate;
        for (int index = 0; index < roomTypesList.size(); index++) {
            temp_starting_rate = Integer.parseInt(roomTypesList.get(index).getRent());
            if (starting_rate > temp_starting_rate) {
                starting_rate = temp_starting_rate;
                time_of_starting_rent = roomTypesList.get(index).getTime();
            }
        }
        starting_rate_map.put(starting_rate + "", time_of_starting_rent);
        return starting_rate_map;
    }


    /*--------------------------------------- Validation Section Starts -----------------------------*/
    private boolean validate() {
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent = cd.isConnectingToInternet();
        if (!isInternetPresent) {
            Toast.makeText(this, "Please turn on Iternet", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!ownerName_Validation()) {
            return false;
        }
        if (!accomName_Validation()) {
            return false;
        }
        if (!contact_Validation()) {
            return false;
        }
        if (!address_Validation()) {
            return false;
        }
        if (!gender_Validation()) {
            return false;
        }
        if (!roomType_Validation()) {
            return false;
        }
        if (!uploadImages_Validation()) {
            return false;
        }
        return true;
    }

    private boolean ownerName_Validation() {
        if (owner_name.isEmpty()) {
            et_ownerName.setError("Owner Name Required");
            et_ownerName.requestFocus();
            return false;
        } else {
            if (owner_name.matches("^([A-Za-z]+(\\s[A-Za-z]+)?)$"))    //it allow single whitespace between 2(only) words ,where //s stands for sigle whitespace
            {                                                   // ye simple regx h -->   [A-Za-z ]?
                et_ownerName.setError(null);
                return true;
            } else {
                et_ownerName.setError("Invalid Name");
                return false;
            }
        }
    }

    private boolean accomName_Validation() {
        if (accom_name.isEmpty()) {
            et_accomName.setError("Accom_name Required");
            et_accomName.requestFocus();
            return false;
        }

        return true;
    }

    private boolean contact_Validation() {
        if (contact1.isEmpty()) {
            et_contact1.setError("Number Required");
            et_contact1.requestFocus();
            return false;
        } else if (contact1.length() != 10) {
            et_contact1.setError("Invalid Number");
            et_contact1.requestFocus();
            return false;
        }
        if (contact2.isEmpty()) {
            et_contact2.setError("Number Required");
            et_contact2.requestFocus();
            return false;
        } else if (contact2.length() != 10) {
            et_contact2.setError("Invalid Number");
            et_contact2.requestFocus();
            return false;
        }
        return true;
    }

    private boolean address_Validation() {
        if (accom_address.equals("")) {
            tv_address.setError("Address Required");
            tv_address.requestFocus();

            return false;
        }
        /*if (accom_address_with_map.equals(""))
        {
            et_properAddress.setError("Address on Map Required");
            et_properAddress.requestFocus();

            return false;
        }
*/
        return true;
    }

    private boolean gender_Validation() {

        if (gender.isEmpty()) {
            Toast.makeText(this, "gender Required", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private boolean roomType_Validation() {

        return true;
    }

    private boolean uploadImages_Validation() {
        if (IMG_UPLOAD_COUNT < 1) {
            Toast.makeText(this, "Atleast 1 images Required", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    /*--------------------------------------- Validation Section Ends ----------------------------------*/


    /*------------------------------------- Upload Images Section Starts ---------------------------------*/
    private void createRecList() {
        rec_list.add(new Rec_Item(R.drawable.image_up, "Room1"));
        rec_list.add(new Rec_Item(R.drawable.image_up, "Room2"));
        rec_list.add(new Rec_Item(R.drawable.image_up, "Kitchen"));
        rec_list.add(new Rec_Item(R.drawable.image_up, "Let-Bath"));
        rec_list.add(new Rec_Item(R.drawable.image_up, "Outside View"));
        //rec_list.add(new Rec_Item( R.drawable.more, ""));
        buildRecyclerView();
    }

    private void buildRecyclerView() {
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);
        horizontal_recycler_view.setHasFixedSize(true);
        horizontal_recycler_view.setAdapter(horizontalAdapter);

        horizontalAdapter.setOnClick(new HorizontalAdapter.OnItemClicked() {
            @Override
            public void onItemClick(int position, String from) {
                //ye method shi nhi h bcz each time onItemClick get called , findViewById() also  perform again & again
                //rec_list.get(position).changeImageResource();
                HorizontalAdapter.HorizontalViewHolder itemViewholder = (HorizontalAdapter.HorizontalViewHolder) horizontal_recycler_view.findViewHolderForAdapterPosition(position);
                rec_imgview = itemViewholder.img.findViewById(R.id.imgTag);
                rec_txtview = itemViewholder.txt.findViewById(R.id.txtTag);
                rec_imgDeleteBtn = itemViewholder.deleteBtn.findViewById(R.id.imgDeleteTag);
                rec_title = rec_txtview.getText().toString();

                switch (from) {
                    case "itemView":
                       /* Intent galleryIntent = new Intent();
                        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                        galleryIntent.setFull_type("image*//*");
                        startActivityForResult(galleryIntent, GALLERY_REQUEST);*/


                        // start picker to get image for cropping and then use the image in cropping activity
                        CropImage.activity()                                                //arthurhub cropping image library code
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .start(AdActivity.this);
                        break;

                    case "deleteBtn":
                        rec_imgDeleteBtn.setVisibility(View.GONE);
                        rec_imgview.setImageResource(R.drawable.image_up);
                        if (uploadImagesMap.containsKey(rec_title)) {
                            uploadImagesMap.remove(rec_title);
                        }
                        Log.i("uploadImagesMap", uploadImagesMap.toString());
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri).start(this);
        } else if (requestCode == GALLERY_REQUEST && resultCode != RESULT_OK) {
            Toast.makeText(this, "Please Try Again", Toast.LENGTH_LONG).show();
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri mImageUri = result.getUri();
                File thumb_filePath = new File(mImageUri.getPath());

                Log.i("mImageUri", mImageUri.toString());
                Log.i("thumb_filePath", thumb_filePath.toString());

                //--------- image compression code for selected image into a compressed bitmap image ---------
                try {
                    compressedImageBitmap = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(40)
                            .compressToBitmap(thumb_filePath);

                    setImageSelected();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e("Error", error.toString());
                Toast.makeText(this, "Please Try Again", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setImageSelected() {
        if (compressedImageBitmap != null) {
            rec_imgDeleteBtn.setVisibility(View.VISIBLE);              //Changing the visibility of selected image
            rec_imgview.setImageBitmap(compressedImageBitmap);
            uploadImagesMap.put(rec_title, compressedImageBitmap);      //putting changed image into uploadImagesMap

            Log.i("uploadImagesMap", uploadImagesMap.toString());

            IMG_UPLOAD_COUNT++;
        }
    }
    /*-------------------------------------- Upload Images Section Ends ------------------------------------*/


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        Spinner spinner = (Spinner) adapterView;
        switch (spinner.getId()) {
            case R.id.areaSpinner:
                accom_area = adapterView.getItemAtPosition(position).toString();
                Log.i("onItemSelected", accom_area);
                break;

            case R.id.accomTypeSpinner:
                accom_type = adapterView.getItemAtPosition(position).toString();
                Log.i("onItemSelected", accom_type);
                break;

            //-------------- Room layout spinners starts ----------------------------

            //-------------- Room layout spinners ends ----------------------------

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //this function never called because of default values already set on adapterViews(spinners)
    }


    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = getSharedPreferences("preview_state", MODE_PRIVATE).edit();
        editor.putString("accom_area", accom_area);
        editor.putString("owner_name", owner_name);
        editor.putString("accom_type", accom_type);
        editor.putString("accom_name", accom_name);
        editor.putString("contact1", contact1);
        editor.putString("contact2", contact2);
        editor.putString("accom_address", accom_address);
        editor.putString("accom_address_with_map", accom_address_with_map);
        editor.putString("gender", gender);
        editor.commit();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.i(TAG, "onRequestPermissionsResult");

        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                //If user interaction was interupted, the permission request is cancelled by user & you
                // recieve empty arrays.
                Log.i(TAG, "User interaction was cancelled");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permission granted
                getLastLocation();
            } else {
                //Permission denied.

                showSnackBar(R.string.textwarn, R.string.settings,
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {

                                //Build intent that displays the App settings screen.

                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        getPackageName(), null);
                                intent.setData(uri);
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            }
                        });
            }
        }
    }

    private void showSnackBar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }


    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        Log.i("permission1", String.valueOf(permissionState == PackageManager.PERMISSION_GRANTED));
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);

        if (shouldProvideRationale) {

            //Request permission
            startLocationPermissionRequest();
           /* Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackBar(R.string.textwarn,  android.R.string.ok,
                    new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view) {

                            //Request permission
                            startLocationPermissionRequest();
                        }
                    });*/
        } else {
            Log.i(TAG, "Reuesting permission");
            startLocationPermissionRequest();
        }
    }


    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }


    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();

                           /*mLatitudeText.setText(String.format(Locale.ENGLISH, "%s: %f",
                                    mLatitudeLabel,
                                    mLastLocation.getLatitude()));
                            mLongitudeText.setText(String.format(Locale.ENGLISH, "%s: %f",
                                    mLongitudeLabel,
                                    mLastLocation.getLongitude()));*/

                            //                            mFusedLocationClient_latLng = new LatLng();
//                            mFusedLocationClient_latLng.setLatitude(mLastLocation.getLatitude());
//                            mFusedLocationClient_latLng.setLongitude(mLastLocation.getLongitude());
                            accom_latLng.setLatitude(mLastLocation.getLatitude());
                            accom_latLng.setLongitude(mLastLocation.getLongitude());
                            Log.i("mFusedLocation=", String.valueOf(accom_latLng));

                        } else {
                            Log.w(TAG, "getLastLocation:exception", task.getException());
                            Log.i("mFusedLocatio==", String.valueOf(task.getException()));
                        }
                    }
                });



    }
}









