package com.shivam.complete_dashboard.Models;


import com.google.firebase.database.IgnoreExtraProperties;
import com.shivam.complete_dashboard.Advertise.Room;
import com.shivam.complete_dashboard.Advertise.Room_last;
import com.shivam.complete_dashboard.Advertise.Time_Availability;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shivam sharma on 10/20/2017.
 */
@IgnoreExtraProperties
public class Advertise implements Serializable
{
    private String verification_status;
    private String owner_name;
    private String accom_type;
    private String accom_name;
    private String accom_area;
    private String accom_address;
    private String accom_landmark;
    private String accom_address_with_map;
    private LatLng accom_latLng;
    private String contact1,contact2;
    private String gender;
    private String deposit;
    private String advance;
    private String booking_fee, booking_start_from, booking_including_rent;
    private String electricity_charge;
    private String water_charge;

    private Map<String, String> starting_rate;
    transient Map<String, Room_last> room_types = new HashMap<>();
    transient Map<String, String> thumbs = new HashMap<>();
    private Object timestamp;
    private String isRentsNegotiable;
//    public String isFacilityAvailWithNoCharges;
    private String radio_RAWECF;
    private Map<String, String> facilities_map;
    private List<String> rules_list;
    private Time_Availability time_availability;

    public Advertise()
    {
    }


    public Advertise(String verification_status, String owner_name, String accom_type, String accom_name, String accom_area, String accom_address, String accom_landmark, String accom_address_with_map, LatLng accom_latLng, String contact1,String contact2, String gender, String deposit, String advance, Map<String, String> starting_rate, String booking_fee,String booking_start_from,String booking_including_rent, String electricity_charge,String water_charge,  Time_Availability time_availability, Map<String, Room_last> room_types, Map<String, String> thumbs, Object timestamp, String isRentsNegotiable, String radio_RAWECF, Map<String , String> facilities_map,List<String> rules_list )
    {
        this.verification_status = verification_status;
        this.owner_name = owner_name;
        this.accom_type = accom_type;
        this.accom_name = accom_name;
        this.accom_area = accom_area;
        this.accom_address = accom_address;
        this.accom_landmark = accom_landmark;
        this.accom_address_with_map = accom_address_with_map;
        this.accom_latLng = accom_latLng;
        this.contact1 = contact1;
        this.contact2 = contact2;
        this.gender = gender;
        this.deposit = deposit;
        this.advance = advance;
        this.starting_rate = starting_rate;
        this.booking_fee = booking_fee;
        this.booking_start_from = booking_start_from;
        this.booking_including_rent = booking_including_rent;
        this.electricity_charge = electricity_charge;
        this.water_charge = water_charge;
        this.time_availability = time_availability;
        this.room_types = room_types;
        this.thumbs = thumbs;
        this.timestamp = timestamp;
        this.isRentsNegotiable = isRentsNegotiable;
        this.radio_RAWECF = radio_RAWECF;
        this.facilities_map = facilities_map;
        this.rules_list = rules_list;
    }

    public String getVerification_status() {
        return verification_status;
    }

    public void setVerification_status(String verification_status) {
        this.verification_status = verification_status;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getAccom_type() {
        return accom_type;
    }

    public void setAccom_type(String accom_type) {
        this.accom_type = accom_type;
    }

    public String getAccom_name() {
        return accom_name;
    }

    public void setAccom_name(String accom_name) {
        this.accom_name = accom_name;
    }

    public String getAccom_area() {
        return accom_area;
    }

    public void setAccom_area(String accom_area) {
        this.accom_area = accom_area;
    }

    public String getAccom_address() {
        return accom_address;
    }

    public void setAccom_address(String accom_address) {
        this.accom_address = accom_address;
    }

    public String getAccom_landmark() {
        return accom_landmark;
    }

    public void setAccom_landmark(String accom_landmark) {
        this.accom_landmark = accom_landmark;
    }

    public String getAccom_address_with_map() {
        return accom_address_with_map;
    }

    public void setAccom_address_with_map(String accom_address_with_map)
    {
        this.accom_address_with_map = accom_address_with_map;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public LatLng getAccom_latLng() {
        return accom_latLng;
    }

    public void setAccom_latLng(LatLng accom_latLng) {
        this.accom_latLng = accom_latLng;
    }

    public String getContact1() {
        return contact1;
    }

    public void setContact1(String contact1) {
        this.contact1 = contact1;
    }

    public String getContact2() {
        return contact2;
    }

    public void setContact2(String contact2) {
        this.contact2 = contact2;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getAdvance() {
        return advance;
    }

    public void setAdvance(String advance) {
        this.advance = advance;
    }

    public Map<String, String> getStarting_rate()
    {
        return starting_rate;
    }

    public void setStarting_rate(Map<String, String> starting_rate)
    {
        this.starting_rate = starting_rate;
    }

    public String getBooking_fee() {
        return booking_fee;
    }

    public void setBooking_fee(String booking_fee) {
        this.booking_fee = booking_fee;
    }

    public String getBooking_start_from() {
        return booking_start_from;
    }

    public void setBooking_start_from(String booking_start_from) {
        this.booking_start_from = booking_start_from;
    }

    public String getBooking_including_rent() {
        return booking_including_rent;
    }

    public void setBooking_including_rent(String booking_including_rent) {
        this.booking_including_rent = booking_including_rent;
    }

    public String getElectricity_charge() {
        return electricity_charge;
    }

    public void setElectricity_charge(String electricity_charge) {
        this.electricity_charge = electricity_charge;
    }

    public String getWater_charge() {
        return water_charge;
    }

    public void setWater_charge(String water_charge) {
        this.water_charge = water_charge;
    }

    public Time_Availability getTime_availability() {
        return time_availability;
    }

    public void setTime_availability(Time_Availability time_availability) {
        this.time_availability = time_availability;
    }

    public Map<String, Room_last> getRoom_types()
    {
        return room_types;
    }

    public void setRoom_types(Map<String, Room_last> room_types) {
        this.room_types = room_types;
    }

    public Map<String, String> getThumbs()
    {
        return thumbs;
    }



    public void setThumbs(Map<String, String> thumbs)
    {
        this.thumbs = thumbs;
    }

    public String getFirstThumbUrl()
    {
        return thumbs.get("Room1");
    }


    public String getIsRentsNegotiable() {
        return isRentsNegotiable;
    }

    public void setIsRentsNegotiable(String isRentsNegotiable) {
        this.isRentsNegotiable = isRentsNegotiable;
    }


    public String getRadio_RAWECF() {
        return radio_RAWECF;
    }

    public void setRadio_RAWECF(String radio_RAWECF) {
        this.radio_RAWECF = radio_RAWECF;
    }

    public Map<String, String> getFacilities_map() {
        return facilities_map;
    }

    public void setFacilities_map(Map<String, String> facilities_map)
    {
        this.facilities_map = facilities_map;
    }

    public List<String> getRules_list() {
        return rules_list;
    }

    public void setRules_list(List<String> rules_list) {
        this.rules_list = rules_list;
    }

    @Override
    public String toString()
    {
        return accom_latLng + "\n" + owner_name + "\n" + accom_type + "\n" + accom_name + "\n" + accom_area
                + "\n" + accom_address +  "\n" + accom_landmark + "\n" + accom_address_with_map + "\n"
                 + contact1 + "\n"+  contact2 + "\n" + gender +  "\n" + deposit + "\n" + advance +  "\n" + accom_type + "\n" + booking_fee + "\n" + booking_start_from + "\n" + booking_including_rent + "\n" +
                room_types + "\n" + "\n" + starting_rate + "\n" + thumbs + "\n" + isRentsNegotiable + "\n"+   radio_RAWECF + "\n" +facilities_map + "\n" + rules_list;
    }
}












