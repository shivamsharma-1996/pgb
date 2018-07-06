package com.shivam.complete_dashboard.Advertise;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shivam.complete_dashboard.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shivam sharma on 2/27/2018.
 */

public class Detail_Facility_Adapter extends RecyclerView.Adapter<Detail_Facility_Adapter.detail_facility_holder>
{
    Context ctx;
    Map<String, String > facilityMap;
    private List<Facility> facilityList = new ArrayList<>();

    public Detail_Facility_Adapter()
    {

    }

    public Detail_Facility_Adapter(Context ctx, Map<String, String> facilityMap)
    {
        this.ctx = ctx;
        this.facilityMap = facilityMap;


        for(String key : facilityMap.keySet())
        {
            Facility facility = new Facility();
            facility.setTitle(key);
            facility.setCharge(facilityMap.get(key));
            facilityList.add(facility);
        }
    }

    @Override
    public detail_facility_holder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View detail_facility_view = LayoutInflater.from(ctx).inflate(R.layout.single_summery_layout, null);
        return new detail_facility_holder(detail_facility_view);
    }

    @Override
    public void onBindViewHolder(detail_facility_holder holder, int position)
    {
        holder.facility_title.setText(facilityList.get(position).getTitle());
        holder.facility_charge.setText(facilityList.get(position).getCharge());
    }

    @Override
    public int getItemCount()
    {
        return facilityList.size();
    }

    public static class detail_facility_holder extends RecyclerView.ViewHolder
    {
        TextView facility_title;
        TextView facility_charge;

        public detail_facility_holder(View itemView)
        {
            super(itemView);
            facility_title = itemView.findViewById(R.id.tv_facility_name);
            facility_charge = itemView.findViewById(R.id.tv_facility_charge);

        }
    }
}
