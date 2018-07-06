package com.shivam.complete_dashboard.Advertise;

/**
 * Created by Shivam on 13-03-2018.
 */

import java.io.Serializable;
import java.util.Map;

public class Room_final implements Serializable
{
    private String full_type, bed_type, ac_type, let_bath_type;
    private String rent;
    private String time;
    private String no_of_installments;
    private Map<String, String> installments;
    private String tag_of_room;


    public Room_final()
    {

    }


    public Room_final(String full_type, String rent, String time, String let_bath_type, Map<String, String> installments)
    {
        this.full_type = full_type;
        this.rent = rent;
        this.time = time;
        this.let_bath_type = let_bath_type;
        this.installments = installments;
    }

    public String getFull_type() {
        return full_type;
    }

    public void setFull_type(String full_type) {
        this.full_type = full_type;
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

    public String getNo_of_installments() {
        return no_of_installments;
    }

    public void setNo_of_installments(String no_of_installments) {
        this.no_of_installments = no_of_installments;
    }

    public Map<String, String> getInstallments() {
        return installments;
    }

    public void setInstallments(Map<String, String> installments) {
        this.installments = installments;
    }

    public String getTag_of_room()
    {
        return tag_of_room;
    }

    public void setTag_of_room(String tag_of_room) {
        this.tag_of_room = tag_of_room;
    }

    @Override
    public String toString()
    {
        return full_type + "\n"  +  bed_type +  "\n" +  let_bath_type+ "\n"  + ac_type + "\n"+ rent + "\n" + time + "\n" + installments + "\n" + no_of_installments + "\n" + tag_of_room;
    }
}
