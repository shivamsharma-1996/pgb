package com.shivam.complete_dashboard.Advertise;

/**
 * Created by Shivam on 13-03-2018.
 */

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Room_last implements Serializable
{
    private String  bed_type, ac_type, let_bath_type, kitchen_type, full_type;
    private String rent;
    private String time;
    private String no_of_rooms;
    private Map<String, String> installments_map = new HashMap<>();


    public Room_last()
    {

    }


    public Room_last( String rent, String time, String let_bath_type, Map<String, String> installments_map)
    {
        this.rent = rent;
        this.time = time;
        this.let_bath_type = let_bath_type;
        this.installments_map = installments_map;
    }



    public String getBed_type() {
        return bed_type;
    }

    public void setBed_type(String bed_type) {
        this.bed_type = bed_type;
    }

    public String getAc_type() {
        return ac_type;
    }

    public void setAc_type(String ac_type) {
        this.ac_type = ac_type;
    }

    public String getLet_bath_type() {
        return let_bath_type;
    }

    public void setLet_bath_type(String let_bath_type) {
        this.let_bath_type = let_bath_type;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    public Map<String, String> getInstallments_map() {
        return installments_map;
    }

    public void setInstallments_map(String key, String value)
    {
        this.installments_map.put(key, value);
    }
    public void setClearInstallments_map()
    {
        this.installments_map.clear();
    }


    public String getKitchen_type() {
        return kitchen_type;
    }

    public void setKitchen_type(String kitchen_type) {
        this.kitchen_type = kitchen_type;
    }

    public String getNo_of_rooms() {
        return no_of_rooms;
    }

    public void setNo_of_rooms(String no_of_rooms) {
        this.no_of_rooms = no_of_rooms;
    }

    public String getFull_type() {
        return full_type;
    }

    public void setFull_type(String full_type) {
        this.full_type = full_type;
    }

    @Override
    public String toString()
    {
        return   bed_type +  "\n" +  let_bath_type+ "\n"  + ac_type + "\n"+ rent + "\n" + time + "\n" + installments_map + "\n" + kitchen_type + "\n" + no_of_rooms +"\n" + full_type;
    }
}
