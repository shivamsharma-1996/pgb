package com.shivam.complete_dashboard.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.shivam.complete_dashboard.Activities.DetailViewActivity2;
import com.shivam.complete_dashboard.Models.Advertise;
import com.shivam.complete_dashboard.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shivam on 27/1/18.
 */

public class adcard_viewPagerAdapter extends PagerAdapter
{
    private List<String> imagesUrlList = new ArrayList<>();
    private LayoutInflater inflater;
    private Context ctx;
    ImageView imageView;

    Advertise ad_clicked;
    Serializable room_types, thumbs, facilities_map;

    public adcard_viewPagerAdapter(Context ctx, List<String> imgUrlList, Advertise ad_clicked, Serializable room_types, Serializable thumbs, Serializable facilities_map)
    {
        this.ctx = ctx;
        this.imagesUrlList = imgUrlList;
        this.ad_clicked = ad_clicked;
        this.room_types = room_types;
        this.thumbs = thumbs;
        this.facilities_map = facilities_map;
    }

    @Override
    public int getCount()
    {
        return imagesUrlList.size();
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

        //imageView.setImageResource(images[position]);

        for(int i = 0 ; i < imagesUrlList.size() ; i++)
        {
            Picasso.with(imageView.getContext()).load(imagesUrlList.get(position)).placeholder(R.drawable.room).into(imageView, new Callback()
            {
                @Override
                public void onSuccess()
                {
                    //Toast.makeText(imageView.getContext(),"image loded",Toast.LENGTH_LONG).show();
                }
                @Override
                public void onError()
                {
                    Picasso.with(imageView.getContext()).load(imagesUrlList.get(position)).placeholder(R.drawable.room).into(imageView);
                }
            });
        }
        container.addView(v);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent fullscreenIntent = new Intent(ctx, DetailViewActivity2.class);
                fullscreenIntent.putExtra("Ad",  ad_clicked);
                fullscreenIntent.putExtra("pos",position);
                fullscreenIntent.putExtra("Ad_rooms",room_types);
                fullscreenIntent.putExtra("Ad_thumbs",thumbs);
                fullscreenIntent.putExtra("Ad_facilities",facilities_map);
                ctx.startActivity(fullscreenIntent);
            }
        });
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.invalidate();
       // container.removeView((View) object);
    }
}


