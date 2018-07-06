package com.shivam.complete_dashboard.Holders;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.shivam.complete_dashboard.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class myAdd_viewholder extends RecyclerView.ViewHolder
{
    public TextView tv_property_name;
    public TextView tv_property_status;
    public TextView tv_property_date;

    public myAdd_viewholder(View itemView)
    {
        super(itemView);

        tv_property_name = itemView.findViewById(R.id.tv_property_name);
        tv_property_status = itemView.findViewById(R.id.tv_property_status);
        tv_property_date = itemView.findViewById(R.id.tv_property_date);
    }

    public void setName(String accomName)
    {
        tv_property_name.setText(accomName);
    }
    public void setStatus(String status)
    {
        if(status.equals("pending"))
        {
            tv_property_status.setTextColor(Color.RED);
        }
        else if(status.equals("complete"))
        {
            tv_property_status.setTextColor(Color.GREEN);
        }
        tv_property_status.setText(status);
    }
    public void setDate(Object timestamp)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        tv_property_date.setText(sdf.format(new Date((Long) timestamp)));
    }
}