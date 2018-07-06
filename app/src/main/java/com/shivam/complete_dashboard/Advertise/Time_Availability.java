package com.shivam.complete_dashboard.Advertise;

import java.io.Serializable;

/**
 * Created by Shivam on 20-03-2018.
 */

public class Time_Availability implements Serializable
{
    private String start_time;
    private String end_time;

    public Time_Availability() {
    }

    public Time_Availability(String start_time, String end_time) {
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}
