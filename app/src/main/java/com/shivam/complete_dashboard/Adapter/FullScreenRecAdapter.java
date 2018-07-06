package com.shivam.complete_dashboard.Adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.shivam.complete_dashboard.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by shivam sharma on 2/7/2018.
 */

public class FullScreenRecAdapter extends RecyclerView.Adapter<FullScreenRecAdapter.imgHolder>
{
    private List<Bitmap> bitmapImgList;
    private List<String> ImgUrlList;

    public FullScreenRecAdapter()
    {
        //empty constructor
    }

    public void setBitmapImgList(List<Bitmap> bitmapImgList)
    {
        this.bitmapImgList = bitmapImgList;
    }

    public void setImgUrlList(List<String> imgUrlList) {
        ImgUrlList = imgUrlList;
    }

    public static class imgHolder extends RecyclerView.ViewHolder
    {
        ImageView full_img;
        public imgHolder(View itemView)
        {
            super(itemView);
            full_img = itemView.findViewById(R.id.full_img);

            full_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION)
                    {
                        Toast.makeText(full_img.getContext(), " clicked" + position, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public imgHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fullscreen_rec_item_layout, parent, false);
        return new imgHolder(view);
    }

    @Override
    public void onBindViewHolder(final imgHolder holder, final int position) {
        if (bitmapImgList != null) {
            holder.full_img.setImageBitmap(bitmapImgList.get(position));
        } else {
            Picasso.with(holder.full_img.getContext()).load(ImgUrlList.get(position)).placeholder(R.drawable.room).into(holder.full_img, new Callback() {
                @Override
                public void onSuccess() {
                    //Toast.makeText(ctx,"image loded",Toast.LENGTH_LONG).show();}
                }

                @Override
                public void onError() {
                    Picasso.with(holder.full_img.getContext()).load(ImgUrlList.get(position)).placeholder(R.drawable.room).into(holder.full_img);
                }
            });

        }



    }

    @Override
    public int getItemCount ()
    {
        if (bitmapImgList != null) {
            return bitmapImgList.size();
        } else {
            return ImgUrlList.size();
        }
    }
}
