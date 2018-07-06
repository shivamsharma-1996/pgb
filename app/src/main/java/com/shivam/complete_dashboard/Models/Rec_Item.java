package com.shivam.complete_dashboard.Models;


/**
 * Created by shivam sharma on 8/30/2017.
 */

public class Rec_Item
{
    public int mImageResource;
    public String mText;


    public Rec_Item(int mImageResource, String mText)
    {
        this.mImageResource = mImageResource;
        this.mText = mText;
    }


/*
    public void changeImageResource(int newImage)
    {
        this.mImageResource = newImage;
    }
*/

    public int getImageResource()
    {
        return mImageResource;
    }

    public void setImageResource(int imageResource)
    {
        this.mImageResource = imageResource;
    }

    public String getText()
    {
        return mText;
    }

    public void setText(String text)
    {
        this.mText = text;
    }

    public String toString()
    {
        return  mText + " " + String.valueOf(mImageResource);
    }
}


