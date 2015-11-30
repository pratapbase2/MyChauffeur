package com.mychauffeurapp.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mychauffeurapp.R;
import com.mychauffeurapp.activity.Ride_History;
import com.mychauffeurapp.fragments.BookingFragment;
import com.mychauffeurapp.model.MyRide;

import java.util.ArrayList;
import java.util.List;

public class myRideAdapter extends ArrayAdapter<MyRide> {
    private static final String TAG = "MyRideAdapter";
    private List<MyRide> rideList = new ArrayList<MyRide>();

    String ride_item;
    String bid,date,status,src_address,time,actual_cost,final_cost, discount_price,dest_address,period,rating;
    String cost,uid;
    String image;

    int textViewResourceId;

    public myRideAdapter(Context context, int textViewResourceId) {

        super(context, textViewResourceId);
        this.textViewResourceId=textViewResourceId;
    }

    @Override
    public void add(MyRide object) {
        rideList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.rideList.size();
    }

    @Override
    public MyRide getItem(int index) {
        return this.rideList.get(index);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(textViewResourceId, parent, false);
            viewHolder = new CardViewHolder();
            viewHolder.line1 = (TextView) row.findViewById(R.id.unique_id);
            viewHolder.line2 = (TextView) row.findViewById(R.id.cost);
            viewHolder.line3 = (TextView) row.findViewById(R.id.date);
            viewHolder.line4 = (TextView) row.findViewById(R.id.status);
            viewHolder.image = (ImageView) row.findViewById(R.id.icon);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder) row.getTag();
        }

        MyRide card = getItem(position);
        viewHolder.line1.setText(card.getUniqueId());
        viewHolder.line2.setText("Rs."+card.getCost());
        viewHolder.line3.setText(card.getDate());
        if(card.getStatus().equals("0")){
            viewHolder.line4.setText("Booked");
            viewHolder.image.setImageResource(R.drawable.tick);
        }
        else{
            viewHolder.line4.setText("Pending");
            viewHolder.image.setImageResource(R.drawable.pending1);
        }

        viewHolder.line1.setSelected(true);

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ride_item= BookingFragment.booking_id_list[position];
                bid = BookingFragment.bid[position];
                date = BookingFragment.date[position];
                src_address = BookingFragment.src_address[position];
                time = BookingFragment.time[position];
                status = BookingFragment.status[position];
               // cost = BookingFragment.cost[position];
                uid = BookingFragment.uid[position];
                dest_address = BookingFragment.dest_address[position];
                period = BookingFragment.period[position];
                actual_cost = BookingFragment.bactual_cost[position];
                final_cost = BookingFragment.bfinal_cost[position];
                discount_price = BookingFragment.bdiscount_price[position];
                rating = BookingFragment.brating[position];

                //Toast.makeText(getContext(),position +" "+bus_id_item, Toast.LENGTH_SHORT).show();
                Intent homeIntent = new Intent(getContext(), Ride_History.class);
                homeIntent.putExtra("booking_id", ride_item);

                homeIntent.putExtra("unique_id",bid);
                homeIntent.putExtra("date",date);
                homeIntent.putExtra("source",src_address);
                homeIntent.putExtra("btime",time);
                homeIntent.putExtra("status",status);
                //homeIntent.putExtra("cost",cost);
                homeIntent.putExtra("UserId",uid);
                homeIntent.putExtra("actual_cost",actual_cost);
                homeIntent.putExtra("final_cost",final_cost);
                homeIntent.putExtra("discount_price",discount_price);
                homeIntent.putExtra("destination",dest_address);
                homeIntent.putExtra("duration",period);
                homeIntent.putExtra("bid",ride_item);
                homeIntent.putExtra("brating",rating);

                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(homeIntent);
            }
        });

        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }



    static class CardViewHolder {
        TextView line1;
        TextView line2;
        TextView line3;
        TextView line4;
        ImageView image;
        //TextView line3;
    }

}
