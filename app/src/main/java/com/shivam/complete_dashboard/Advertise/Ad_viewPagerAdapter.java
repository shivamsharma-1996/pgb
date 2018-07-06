package com.shivam.complete_dashboard.Advertise;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shivam.complete_dashboard.Activities.FullScreenActivity;
import com.shivam.complete_dashboard.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by shivam on 27/1/18.
 */

public class Ad_viewPagerAdapter extends PagerAdapter
{
    private static List<String> ImgUrlList = new ArrayList<>();


    private LayoutInflater inflater;
    private Context ctx;
    ImageView imageView;


    public Ad_viewPagerAdapter(Context ctx, Map<String, String> uploadImagesMap)
    {
        this.ctx = ctx;

        Log.i("detailAd.getThumbs()", String.valueOf(uploadImagesMap));


        for( String key : uploadImagesMap.keySet())
        {
            ImgUrlList.add(uploadImagesMap.get(key));
        }
    }

    @Override
    public int getCount()
    {
        return ImgUrlList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (LinearLayout) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position)
    {
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.swipe, container, false);
        imageView = v.findViewById(R.id.imageView);


                Picasso.with(imageView.getContext()).load(ImgUrlList.get(position)).placeholder(R.drawable.room).into(imageView, new Callback()
                {
                    @Override
                    public void onSuccess()
                    {

                    }

                    @Override
                    public void onError()
                    {
                        Picasso.with(ctx).load(ImgUrlList.get(position)).placeholder(R.drawable.room).into(imageView);
                    }
                });

        container.addView(v);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

            }
        });
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
      container.removeView((View) object);
    }

    public static List<String> getImgUrlList()
    {
        return ImgUrlList;
    }

}


