package com.shivam.complete_dashboard.Advertise;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.shivam.complete_dashboard.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shivam sharma on 2/24/2018.
 */

public class RoomInstance_Adapter2 extends RecyclerView.Adapter<RoomInstance_Adapter2.holder> {

    private static Context ctx;
    private String[] room_types, room_times, installments;
    private static ArrayAdapter<String> roomTimesAdapter, installmentTypeAdapter;


    ArrayList<Room_final> selectedRoomsList = new ArrayList<>();
    int counter = 0;




    //Device_location
    private static AdActivity mainActivity;


    public RoomInstance_Adapter2(Context ctx)
    {
        this.ctx = ctx;
        mainActivity = (AdActivity) ctx;

        //this.room_types = ctx.getResources().getStringArray(R.array.room_types);
        this.room_times = ctx.getResources().getStringArray(R.array.room_times);
        this.installments = ctx.getResources().getStringArray(R.array.installment);


        this.roomTimesAdapter = new ArrayAdapter(this.ctx, android.R.layout.simple_spinner_item, room_times);
        this.roomTimesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        this.installmentTypeAdapter = new ArrayAdapter(this.ctx, android.R.layout.simple_spinner_item, installments);
        this.installmentTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public holder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View room_instance_view = LayoutInflater.from(this.ctx).inflate(R.layout.single_room_instance_layout2, null);
        return new holder(room_instance_view);
    }

    @Override
    public void onBindViewHolder(final holder holder, int position) {

        holder.spinner_rent_time.setAdapter(roomTimesAdapter);
        holder.spinner_rent_time.setSelection(2);
        holder.spinner_installment.setAdapter(installmentTypeAdapter);
        holder.tv_room_type.setText(room_types[position]);

       /* room_rent = holder.et_room_rent.getText().toString().trim();
        room_type = holder.tv_room_type.getText().toString();
        room_time = holder.spinner_room_time.getSelectedItem().toString();*/
    }

    @Override
    public int getItemCount() {
        return 16;
    }


    public class holder extends RecyclerView.ViewHolder
    {
        TextView tv_room_type, tv_note_default, tv_installment_equal, tv_note1, tv_note2;
        Button btn_Add, btn_Remove;
        EditText et_room_rent, et_installment1, et_installment2;
        Spinner spinner_rent_time, spinner_installment;
        LinearLayout room_layout_expandable, room_layout_collapse, ll_notes, ll_installment;

        boolean isBtn_add = true;

        public holder(final View itemView)
        {
            super(itemView);

            tv_room_type = itemView.findViewById(R.id.tv_type);
            tv_note_default = itemView.findViewById(R.id.tv_note_default);
            tv_note1 = itemView.findViewById(R.id.tv_note1);
            tv_note2 = itemView.findViewById(R.id.tv_note2);
            tv_installment_equal = (itemView.findViewById(R.id.tv_installment_equal));
            btn_Add = itemView.findViewById(R.id.btn_Add);
            //btn_Remove = itemView.findViewById(R.id.btn_Remove);
            et_room_rent = itemView.findViewById(R.id.et_rent);
            et_installment1 = itemView.findViewById(R.id.et_installment1);
            et_installment2 = itemView.findViewById(R.id.et_installment2);

            spinner_rent_time = itemView.findViewById(R.id.spinner_rent_time);
            spinner_installment = itemView.findViewById(R.id.spinner_Installment);

            room_layout_expandable = itemView.findViewById(R.id.room_layout_expandable);
            room_layout_collapse = itemView.findViewById(R.id.room_layout_collapse);
            ll_notes = itemView.findViewById(R.id.ll_notes);
            ll_installment = itemView.findViewById(R.id.ll_installment);


            btn_Add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Room_final room_final = new Room_final();
                    if (isBtn_add)
                    {
                        isBtn_add = false;
                        btn_Add.setText("Remove");

                        String room_type = tv_room_type.getText().toString().trim();

                        String ac_type = "", let_bath_type = "";
                        List<String> contains_list = new ArrayList();
                        contains_list.add("Ac");
                        contains_list.add("NonAc");
                        contains_list.add("Attached let-Bath");
                        contains_list.add("Common let-Bath");

                        for (String contains : contains_list) {
                            if (room_type.contains(contains)) {
                                switch (contains)
                                {
                                    case "Ac":
                                        ac_type = "Ac";
                                        break;
                                    case "NonAc":
                                        ac_type = "NonAc";
                                        break;
                                    case "Attached let-Bath":
                                        let_bath_type = "Attached let-Bath";
                                        break;
                                    case "Common let-Bath":
                                        let_bath_type = "Common let-Bath";
                                        break;
                                }
                            }
                        }
                        String bed_type = room_type.substring(room_type.lastIndexOf("(") + 1);
                        bed_type = bed_type.replace(")", "");


                        room_final.setFull_type(room_type);
                        room_final.setBed_type(bed_type);
                        room_final.setAc_type(ac_type);
                        room_final.setLet_bath_type(let_bath_type);

                        room_final.setNo_of_installments((String) spinner_installment.getSelectedItem());
                        room_final.setRent(et_room_rent.getText().toString().trim());
                        room_final.setTime((String) spinner_rent_time.getSelectedItem());
                        if (spinner_installment.getSelectedItem().toString().equals("2"))
                        {
                            Map<String, String> installments_map = new HashMap<>();
                            installments_map.put("first_installment", et_installment1.getText().toString());
                            installments_map.put("second_installment", et_installment2.getText().toString());
                            room_final.setInstallments(installments_map);
                        } else {
                            Map<String, String> installments_map = new HashMap<>();
                            if (spinner_installment.getSelectedItem().toString().equals("0")) {
                                installments_map.put("installment", "0");
                            } else {
                                installments_map.put("installment", String.valueOf(Integer.parseInt(et_room_rent.getText().toString()) / Integer.parseInt(spinner_installment.getSelectedItem().toString())));
                            }
                            room_final.setInstallments(installments_map);
                        }

                        Log.i("itemView.getTag()", String.valueOf(itemView.getTag()));

                        //FINALLY , adding tag_of_room to selected room
                        room_final.setTag_of_room(String.valueOf(itemView.getTag()));
                        selectedRoomsList.add(room_final);
                    }
                    else
                    {
                        //remove button clicked
                        isBtn_add = true;
                        btn_Add.setText("Add");

                        for(Room_final room_final1 : selectedRoomsList)
                        {
                            if(room_final1.getFull_type().equals(tv_room_type))
                            {
                                selectedRoomsList.remove(room_final);
                            }
                        }
                    }

                    Log.i("selectedRoomsList", String.valueOf(selectedRoomsList));

                    Log.i("room_final", String.valueOf(room_final));
                }
            });


            room_layout_collapse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (room_layout_expandable.getVisibility() == View.VISIBLE) {
                        room_layout_expandable.setVisibility(View.GONE);
                    } else {
                        room_layout_expandable.setVisibility(View.VISIBLE);
                    }

                }
            });
            spinner_rent_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            spinner_installment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    if (adapterView.getItemAtPosition(position).toString().equals("0")) {
                        tv_note_default.setVisibility(View.VISIBLE);
                        ll_notes.setVisibility(View.GONE);
                        ll_installment.setVisibility(View.GONE);
                        tv_installment_equal.setVisibility(View.GONE);
                    } else {
                        switch (adapterView.getItemAtPosition(position).toString()) {
                            case "2":
                                ll_installment.setVisibility(View.VISIBLE);
                                tv_installment_equal.setVisibility(View.GONE);
                                tv_note1.setText("Second installment to be paid after ");
                                tv_note2.setText("6 months");

                                break;

                            default:
                                ll_installment.setVisibility(View.GONE);
                                tv_installment_equal.setText("Installments are of : " + Integer.parseInt(et_room_rent.getText().toString()) / Integer.parseInt(spinner_installment.getSelectedItem().toString()));
                                tv_installment_equal.setVisibility(View.VISIBLE);
                                tv_note1.setText("Further installement to paid after ");
                                tv_note2.setText(12 / Integer.parseInt(spinner_installment.getSelectedItem().toString()) + " months");
                        }
                        tv_note_default.setVisibility(View.GONE);
                        ll_notes.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }

    }

    public List<Room_final> getSelectedRooms()
    {
        return selectedRoomsList;
    }


//    public List<Room> getSelectedRooms(RecyclerView main_roomType_rec_container) {
//        //        int itemCount = main_roomType_rec_container.getAdapter().getItemCount();
////        TextView tv_room_type, tv_let_bath, tv_select_date;
////        EditText et_room_rent, et_installment1, et_installment2;
////
////        Spinner spinner_room_time;
////        RadioButton rg_yes;
////
////        for (int index = 0; index < itemCount; index++) {
////            RecyclerView.ViewHolder child_viewHolder = main_roomType_rec_container.findViewHolderForAdapterPosition(index);
////            //cb_roomCheck = child_viewHolder.itemView.findViewById(R.id.cb_roomCheck);
////            tv_room_type = child_viewHolder.itemView.findViewById(R.id.tv_type);
////            et_room_rent = child_viewHolder.itemView.findViewById(R.id.et_rent);
////            et_installment1 = child_viewHolder.itemView.findViewById(R.id.et_installment1);
////            et_installment2 = child_viewHolder.itemView.findViewById(R.id.et_installment2);
////            spinner_room_time = child_viewHolder.itemView.findViewById(R.id.spinner_rent_time);
////
////
////                if (rg_yes.isChecked())
////                {
////                    if (et_room_rent.getText().toString().trim().isEmpty()) {
////                        et_room_rent.setError("enter rent");
////                        et_room_rent.requestFocus();
////                        break;
////                    }
////                    Room room_instance = new Room();
////                    room_instance.setRent(et_room_rent.getText().toString().trim());
////                    room_instance.setFull_type(tv_room_type.getText().toString());
////                    room_instance.setLet_bath_type("Toilet-Bath : Attach / Seperate");
////                    room_instance.setDate(tv_select_date.getText().toString().trim());
////                    room_instance.setTime(spinner_room_time.getSelectedItem().toString());
////                    room_instance.setInstallment1(et_installment1.getText().toString().trim());
////                    room_instance.setInstallment2(et_installment2.getText().toString().trim());
////
////                    selectedRoomsList.add(room_instance);
////                }
////
//        //return selectedRoomsList;
//    }






}





