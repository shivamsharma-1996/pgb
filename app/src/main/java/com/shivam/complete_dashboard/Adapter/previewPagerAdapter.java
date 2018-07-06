package com.shivam.complete_dashboard.Adapter;

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
import android.widget.Toast;

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

public class previewPagerAdapter extends PagerAdapter {
    private static List<Bitmap> bitmapImgList = new ArrayList<>();


    private LayoutInflater inflater;
    private Context ctx;
    ImageView imageView;

    public previewPagerAdapter(Context ctx, HashMap<String, Bitmap> uploadImagesMap) {
        this.ctx = ctx;

        for (String key : uploadImagesMap.keySet())
        {
            bitmapImgList.add(uploadImagesMap.get(key));
        }
    }

    @Override
    public int getCount()
    {
        return bitmapImgList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return (view == (LinearLayout) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position)
    {
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.swipe, container, false);
        imageView = v.findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmapImgList.get(position));

        container.addView(v);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(ctx, (position+1) + " clicked", Toast.LENGTH_LONG).show();
                Log.i("tag",container.toString());

                Intent fullscreenIntent = new Intent(ctx, FullScreenActivity.class);
                fullscreenIntent.putExtra("from", "PreviewActivity");
                ctx.startActivity(fullscreenIntent);
            }
        });
        return v;
    }


    @Override
    public void destroyItem (ViewGroup container,int position, Object object)
    {
        container.removeView((View) object);
    }

    public static List<Bitmap> getBitmapImgList()
    {
        return bitmapImgList;
    }


}



