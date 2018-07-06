package com.shivam.complete_dashboard.Models;

/**
 * Created by shivam on 31/1/18.
 */

public class Ammenity
{
    private int img;
    private String txt;

    public Ammenity(int img, String txt)
    {
        this.img = img;
        this.txt = txt;
    }

    public int getImg()
    {
        return img;
    }

    public void setImg(int img)
    {
        this.img = img;
    }

    public String getTxt()
    {
        return txt;
    }

    public void setTxt(String txt)
    {
        this.txt = txt;
    }

}
