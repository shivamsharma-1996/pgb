package com.shivam.complete_dashboard.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.shivam.complete_dashboard.Models.Rec_Item;
import com.shivam.complete_dashboard.R;

import java.util.List;

/**
 * Created by shivam on 14/1/18.
 */

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.HorizontalViewHolder>
{
    private List<Rec_Item> horizontalList;
    private OnItemClicked mListener;

    public interface OnItemClicked
    {
        void onItemClick(int position, String from);
    }

    public void setOnClick(OnItemClicked listner)
    {
        this.mListener=listner;
    }

    public HorizontalAdapter(List<Rec_Item> List)
    {
        this.horizontalList = List;
    }


    public static class HorizontalViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView img;
        public ImageView deleteBtn;
        public TextView txt;
        public HorizontalViewHolder(View itemView, final OnItemClicked listner)
        {
            super(itemView);
            img = itemView.findViewById(R.id.imgTag);
            txt = itemView.findViewById(R.id.txtTag);
            deleteBtn = itemView.findViewById(R.id.imgDeleteTag);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(listner != null)
                    {

                    }
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION)
                    {
                        listner.onItemClick(position,"itemView");
                    } }});

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if(listner != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            listner.onItemClick(position,"deleteBtn");
                        }
                    }
                }});
        }
    }

    @Override
    public HorizontalViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.rec_item_layout, parent, false);
        HorizontalViewHolder hvh = new HorizontalViewHolder(view, mListener);
        return hvh;
    }

    @Override
    public void onBindViewHolder(HorizontalViewHolder holder, int position)
    {
        holder.img.setImageResource(horizontalList.get(position).getImageResource());
        holder.txt.setText(horizontalList.get(position).getText());
    }

    @Override
    public int getItemCount()
    {
        return horizontalList.size();
    }




}


/*             To handle click events for recyclerView inside the Activity we have to pass the position of clicked item
                    from Adapter class to the Activity, which is done by onItemClick interface method                          */

                     // Step 1: Create interface onItemClicked with a onItemClick() method
                     // Step 2: Create an interface variable mListener for onItemClicked
                     // Step 3: Create a method - setOnClick(onItemClicked listner) that is to be called from activity to pass a listener object from activity to class
                     // Step 4 : setOnClicklistner() on itemView to handle click Events.
                     // Step 5: Change parameters of HorizontalViewHolder() from -->
                                /*HorizontalViewHolder(View itemView)  to HorizontalViewHolder(View itemView,OnItemClicked listner )*/

                     // Step 6: Change   new HorizontalViewHolder(view) object inside onCreateViewHolder() to new HorizontalViewHolder(view, mListener).

                    // Step 7: add code to itemView clickListner ---->


                           /* if(listner != null)
                              {
                              int position = getAdapterPosition();
                              if(position != RecyclerView.NO_POSITION)
                              {
                              listner.onItemClick(position);
                              }
                              }*/



                     // Step 8: inside the mainActivity, adding the code:
                          /*horizontalAdapter.setOnClick(new HorizontalAdapter.OnItemClicked()
                             {
                                 @Override
                                public void onItemClick(int position)
                               {
                                    rec_list.get(position).changeImageResource(R.drawable.image_up);
                               }
                             });*/
