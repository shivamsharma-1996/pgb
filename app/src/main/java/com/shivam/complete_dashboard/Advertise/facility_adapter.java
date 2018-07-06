package com.shivam.complete_dashboard.Advertise;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shivam.complete_dashboard.R;

import java.util.List;

/**
 * Created by shivam sharma on 2/19/2018.
 */

public class facility_adapter extends RecyclerView.Adapter<facility_adapter.facility_holder>
{
    Context ctx;
    List<String> facilityList;

    private static AlertDialog extra_charge_dialog;
    private static View extra_charge_view;
    private static TextView extra_charge_tv_facility_title;
    private static Button bt_submit, bt_exit;
    private static TextView tv_no_charges, bt_as_per_meter_charges;
    private static EditText et_extra_charge;

    private static LinearLayout summery_facilities_ll;


    // private static Room room_instance;

    static ExtraChargeDialogListener mListener;

    public facility_adapter(Context ctx, List<String> facilityList)
    {
        this.ctx = ctx;
        this.facilityList = facilityList;

        extra_charge_view = LayoutInflater.from(this.ctx).inflate(R.layout.custom_extra_charge, null);
        extra_charge_tv_facility_title = extra_charge_view.findViewById(R.id.extra_charge_title);
        et_extra_charge = extra_charge_view.findViewById(R.id.et_extra_charge);
        tv_no_charges = extra_charge_view.findViewById(R.id.bt_no_charges);
        bt_as_per_meter_charges =  extra_charge_view.findViewById(R.id.bt_as_per_meter_charges);
        bt_submit = extra_charge_view.findViewById(R.id.bt_submit);
        bt_exit = extra_charge_view.findViewById(R.id.bt_exit);

        AlertDialog.Builder builder = new AlertDialog.Builder(this.ctx);
        builder.setView(extra_charge_view);
        extra_charge_dialog = builder.create();

        //facilities_summery
       /* View summery_view = LayoutInflater.from(this.ctx).inflate(R.layout.activity_main, null);
        summery_facilities_ll = summery_view.findViewById(R.id.summery_facilities);*/

        summery_facilities_ll = new AdActivity().getSummery_facilities_ll();
        Log.i("child_views_adapter", String.valueOf(summery_facilities_ll.getChildCount()));
    }

    public void setOnClickForExtraCharges(ExtraChargeDialogListener mListener)
    {
        try
        {
        this.mListener = mListener;
        }
        catch (final ClassCastException e)
        {
            throw new ClassCastException(this.ctx.toString() + " must implement OnCompleteListener" + " " + e);
        }
    }

    @Override
    public facility_holder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view  = LayoutInflater.from(ctx).inflate(R.layout.facility_single_item, null);
        return new facility_holder(view);
    }

    @Override
    public void onBindViewHolder(facility_holder facility_holder, int position)
    {
        facility_holder.facility_title.setText(facilityList.get(position));
    }

    @Override
    public int getItemCount() {
        return facilityList.size();
    }

    public static class facility_holder extends RecyclerView.ViewHolder
    {
        TextView facility_title;

        public facility_holder(final View itemView)
        {
            super(itemView);
            facility_title = itemView.findViewById(R.id.facility_title);



            facility_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Log.i("child_viewsonclick", String.valueOf(summery_facilities_ll.getChildCount()));

                    et_extra_charge.setText("");

                    if(((itemView.findViewById(R.id.title_ll)).getBackground()) == null)
                    {
                        (itemView.findViewById(R.id.title_ll)).setBackgroundResource(R.drawable.selection_bg);

                        extra_charge_tv_facility_title.setText("Extra Charges for " + facility_title.getText().toString());
                        extra_charge_dialog.show();

                        bt_exit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            extra_charge_dialog.dismiss();
                            (itemView.findViewById(R.id.title_ll)).setBackgroundResource(0);
                        }
                    });

                        tv_no_charges.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                mListener.onDialogBtnClick(facility_title.getText().toString(),"including in rent" );
                                extra_charge_dialog.dismiss();
                            }
                        });

                        bt_as_per_meter_charges.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                mListener.onDialogBtnClick(facility_title.getText().toString(),"as per submeter" );
                                extra_charge_dialog.dismiss();
                            }
                        });

                        bt_submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                if(et_extra_charge.getText().toString().isEmpty())
                                {
                                    et_extra_charge.setError("enter charge before submit");
                                }
                                else
                                {
                                    mListener.onDialogBtnClick(facility_title.getText().toString(), et_extra_charge.getText().toString());
                                    extra_charge_dialog.dismiss();
                                }

                            }
                        });


                        extra_charge_dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                extra_charge_dialog.dismiss();
                                (itemView.findViewById(R.id.title_ll)).setBackgroundResource(0);
                            }
                        });


                    }
                    else if(((itemView.findViewById(R.id.title_ll)).getBackground()) != null)
                    {
                        (itemView.findViewById(R.id.title_ll)).setBackgroundResource(0);

                        //Log.i("child_views", String.valueOf(summery_facilities_ll.getChildCount()));
                        for(int index = 1; index< summery_facilities_ll.getChildCount(); index++)
                        {
                            View child_view = summery_facilities_ll.getChildAt(index);
                            Log.i("child_view", String.valueOf(child_view));
                            Log.i("child_view", String.valueOf(child_view.getTag()));

                            if(child_view.getTag().equals(facility_title.getText().toString()))
                            {
                                summery_facilities_ll.removeViewAt(index);
                                if(summery_facilities_ll.getChildCount() == 1)
                                {
                                    summery_facilities_ll.setVisibility(View.GONE);
                                }
                                break;
                            }
                        }

                    }

                }
            });
        }
    }
}
