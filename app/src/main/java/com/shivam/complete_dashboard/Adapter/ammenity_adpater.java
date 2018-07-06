package com.shivam.complete_dashboard.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.shivam.complete_dashboard.Models.Ammenity;
import com.shivam.complete_dashboard.R;

import java.util.List;

/**
 * Created by shivam on 31/1/18.
 */

public class ammenity_adpater extends RecyclerView.Adapter<ammenity_adpater.myHolder>
{
    private List<Ammenity> ammenities_list;

    public ammenity_adpater(List<Ammenity> ammenities_list)
    {
        this.ammenities_list = ammenities_list;
    }

    public static class myHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView textView;
        public myHolder(View itemView)
        {
            super(itemView);
            imageView = itemView.findViewById(R.id.ammenity_img);
            textView = itemView.findViewById(R.id.ammenity_txt);
        }
    }
    @Override
    public myHolder onCreateViewHolder(ViewGroup recylerview, int viewType)
    {
        Log.i("viewGroup",recylerview.toString());

        LayoutInflater inflater = LayoutInflater.from(recylerview.getContext());
        View view = inflater.inflate(R.layout.single_ammenity_layout, recylerview, false);
        return new myHolder(view);
    }

    @Override
    public void onBindViewHolder(myHolder holder, int position)
    {
        holder.imageView.setImageResource(ammenities_list.get(position).getImg());
        //rules_holder.imageView.setImageDrawable(new ColorDrawable(Coltor.parseColor("#f435")));
        holder.textView.setText(ammenities_list.get(position).getTxt());
    }


    @Override
    public int getItemCount()
    {
        return ammenities_list.size();
    }
}
