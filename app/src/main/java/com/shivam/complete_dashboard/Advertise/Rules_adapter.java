package com.shivam.complete_dashboard.Advertise;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.shivam.complete_dashboard.Adapter.HorizontalAdapter;
import com.shivam.complete_dashboard.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shivam sharma on 2/27/2018.
 */

public class Rules_adapter extends RecyclerView.Adapter<Rules_adapter.rules_holder>
{
    private String[] rules;
    private List<String> rules_list;
    private Context ctx;


    public Rules_adapter(Context ctx, String[] rules)
    {
        this.ctx = ctx;
        this.rules  = rules;
    }



    OnClickRulesInterface mListener;
    public void setOnClick(OnClickRulesInterface listner)
    {
        this.mListener=listner;
    }


    @Override
    public rules_holder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View rulles_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_rule_item_layout, null);
        return new rules_holder(rulles_view, mListener);
    }

    @Override
    public void onBindViewHolder(rules_holder holder, int position)
    {
        holder.rules_title.setText(rules[position]);
    }

    @Override
    public int getItemCount() {
        return rules.length;
    }

    public static class rules_holder extends RecyclerView.ViewHolder
    {
        TextView rules_title;
        CheckBox rules_check;
        public rules_holder(View itemView, final OnClickRulesInterface mListener)
        {
            super(itemView);
            rules_title = itemView.findViewById(R.id.rules_title);
            rules_check = itemView.findViewById(R.id.rules_check);


            rules_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION)
                    {
                        mListener.onCheckBtnClick(rules_title.getText().toString());
                    }

                }
            });
        }
    }

    public static List<String> getRulesFromAdapter(RecyclerView rules_recyclerview)
    {
        List<String> selectedRulessList = new ArrayList<>();

        int itemCount = rules_recyclerview.getAdapter().getItemCount();
        CheckBox cb_ruleCheck;
        TextView tv_rule_title;
        for (int index = 0; index < itemCount; index++)
        {
            RecyclerView.ViewHolder child_viewHolder = rules_recyclerview.findViewHolderForAdapterPosition(index);
            cb_ruleCheck = child_viewHolder.itemView.findViewById(R.id.rules_check);
            tv_rule_title = child_viewHolder.itemView.findViewById(R.id.rules_title);


            if (cb_ruleCheck.isChecked())
            {
                selectedRulessList.add(tv_rule_title.getText().toString().trim());
            }
        }
        return selectedRulessList;
    }
}
