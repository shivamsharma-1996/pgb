package com.shivam.complete_dashboard.Database;

/**
 * Created by shivam sharma on 11/9/2017.
 */

public class AdImage
{
    private Object Room1;
    private Object Room2;
    private Object Kitchen;
    private Object Let_Bath;
    private Object Outside_View;

    public AdImage()
    {
        //empty contructor
    }

    public void setRoom1(Object room1) {
        Room1 = room1;
    }

    public void setRoom2(Object room2) {
        Room2 = room2;
    }

    public void setKitchen(Object kitchen) {
        Kitchen = kitchen;
    }

    public void setLet_Bath(Object let_Bath) {
        Let_Bath = let_Bath;
    }

    public void setOutside_View(Object outside_View) {
        Outside_View = outside_View;
    }
}
