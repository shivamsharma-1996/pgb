package com.shivam.complete_dashboard.Advertise;

import java.io.Serializable;

/**
 * Created by shivam sharma on 2/23/2018.
 */

public class Room implements Serializable
{
    private String type;
    private String rent;
    private String time;
    private String let_bath;
    private String installment1, installment2;
    private String date;

    public Room()
    {
    }


    public Room(String type, String rent, String time, String let_bath, String installment1, String installment2, String date)
    {
        this.type = type;
        this.rent = rent;
        this.time = time;
        this.let_bath = let_bath;
        this.installment1 = installment1;
        this.installment2 = installment2;
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getLet_bath() {
        return let_bath;
    }

    public void setLet_bath(String let_bath) {
        this.let_bath = let_bath;
    }

    public String getInstallment1()
    {
        return installment1;
    }

    public void setInstallment1(String installment1)
    {
        this.installment1 = installment1;
    }

    public String getInstallment2()
    {
        return installment2;
    }

    public void setInstallment2(String installment2)
    {
        this.installment2 = installment2;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getDate()
    {
        return date;
    }

    @Override
    public String toString()
    {
        return type + "\n" + rent + "\n" + time + "\n" +  let_bath + "\n" + installment1 + "\n" + installment2 + "\n" + date;
    }
}
