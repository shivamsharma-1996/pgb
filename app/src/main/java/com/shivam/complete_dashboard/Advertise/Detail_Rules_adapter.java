package com.shivam.complete_dashboard.Advertise;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.shivam.complete_dashboard.R;

import java.util.List;

/**
 * Created by shivam sharma on 2/27/2018.
 */

public class Detail_Rules_adapter extends RecyclerView.Adapter<Detail_Rules_adapter.rules_holder>
{

    private List<String> rules_list;
    private Context ctx;


    public Detail_Rules_adapter(Context ctx, List<String> rules_list)
    {
        this.ctx = ctx;
        this.rules_list  = rules_list;
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
        holder.rules_title.setText(rules_list.get(position));
    }

    @Override
    public int getItemCount() {
        return rules_list.size();
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

            rules_check.setChecked(true);
            rules_check.setClickable(false);

          /*  rules_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION)
                    {
                        mListener.onCheckBtnClick(rules_title.getText().toString());
                    }

                }
            });*/
        }
    }
}
