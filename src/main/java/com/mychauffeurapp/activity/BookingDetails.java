package com.mychauffeurapp.activity;

import android.app.Activity;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mychauffeurapp.R;
import com.mychauffeurapp.app.App_VolleyExamples;
import com.mychauffeurapp.common.AlertDialogManager;
import com.mychauffeurapp.common.ConnectionDetector;
import com.mychauffeurapp.fragments.BookingFragment;
import com.mychauffeurapp.model.ButtonText;
import com.mychauffeurapp.model.MyTextview;
import com.mychauffeurapp.model.MyTextviews;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class BookingDetails extends FragmentActivity {

    String bid, bduration,bdate,bsrc_address,bdest_address,bstatus, bactual_cost,bfinal_cost, bdiscount_price;
   /* Calendar mcurrentTime = Calendar.getInstance();
    final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    final int minute = mcurrentTime.get(Calendar.MINUTE);
    final int seconds=mcurrentTime.get(Calendar.SECOND);
    final int milliseconds=mcurrentTime.get(Calendar.MILLISECOND);
    final int date1=mcurrentTime.get(Calendar.DATE);*/
    ConnectionDetector cd;
    AlertDialogManager alert = new AlertDialogManager();

    MyTextviews booking_id;
    MyTextviews src_address,dest_address,date,time,status,actual_cost,final_cost, discount_price;
    ButtonText booking_history;
    String  uid;
    Fragment frag;
    RelativeLayout Layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_completed);
       /* try {*/
            //this.getActionBar().setDisplayHomeAsUpEnabled(true);
            this.getActionBar().setDisplayShowCustomEnabled(true);
            this.getActionBar().setDisplayShowTitleEnabled(false);
            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.titleview, null);

            ((MyTextview) v.findViewById(R.id.title)).setText(this.getTitle());
            this.getActionBar().setCustomView(v);

            booking_id = (MyTextviews) findViewById(R.id.booking_id);
            src_address = (MyTextviews) findViewById(R.id.src_address);
            dest_address = (MyTextviews) findViewById(R.id.dest_address);
            date = (MyTextviews) findViewById(R.id.date);
            time = (MyTextviews) findViewById(R.id.time);
            status = (MyTextviews) findViewById(R.id.status);
            actual_cost = (MyTextviews) findViewById(R.id.actual_cost);
            final_cost = (MyTextviews) findViewById(R.id.final_cost);
            discount_price = (MyTextviews) findViewById(R.id.discount_amount);
            booking_history = (ButtonText) findViewById(R.id.booking_history);

            Layout = (RelativeLayout) findViewById(R.id.layout);

            cd = new ConnectionDetector(getApplicationContext());
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                //Toast.makeText(BusRoutesListView.this, "Provide source & destination", Toast.LENGTH_LONG).show();
                Intent homeIntent = new Intent(getApplicationContext(), MyRideActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                finish();

            } else {

                bid = extras.getString("unique_id");
                bduration = extras.getString("duration");
                bdate = extras.getString("date");
                bsrc_address = extras.getString("source");
                bdest_address = extras.getString("destination");
                bstatus = extras.getString("status");
                bactual_cost = extras.getString("actual_cost");
                bfinal_cost = extras.getString("final_cost");
                bdiscount_price = extras.getString("discount_price");
                uid = extras.getString("UserId");
            }

            if (!cd.isConnectingToInternet()) {
                alert.showAlertDialog(BookingDetails.this,
                        "Internet Connection Lost",
                        "Please connect to Internet and Try again..", false);
            } else {
                booking_id.setText(bid);
                time.setText("Duration " + bduration + " Hours");
                date.setText(" on " + bdate);
                src_address.setText(bsrc_address);
                dest_address.setText(bdest_address);
                actual_cost.setText("Rs." + bactual_cost);
                final_cost.setText("Rs." + bfinal_cost);
                discount_price.setText("Rs." + bdiscount_price);
                if (bstatus.equals("0")) {
                    status.setText("Booked");
                } else if (bstatus.equals("1")) {
                    status.setText("Processing");
                } else if (bstatus.equals("2")) {
                    status.setText("Driver assigned");
                } else if (bstatus.equals("3")) {
                    status.setText("Started");
                } else if (bstatus.equals("4")) {
                    status.setText("Completed");
                } else {
                    status.setText("Booking");
                }
            }
            booking_history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    App_VolleyExamples.getInstance().trackEvent("Button", "Clicked", "To See Booking History");
                    Bundle bundle = new Bundle();
                    bundle.putString("UserId", uid);
                    Log.e("The Userid {{{{{{{{{{", uid);
                    frag = new BookingFragment();
                    frag.setArguments(bundle);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                    transaction.replace(R.id.frame_container1, frag);
                    transaction.addToBackStack("Frag1");
                    transaction.commit();
                    Layout.setVisibility(View.GONE);
                    booking_history.setVisibility(View.GONE);

               /*Intent in = new Intent(BookingDetails.this,Vehicle_details_tab.class);
                startActivity(in);
                finish();*/


                }
            });
       /* }catch (Exception e) {
            generateNoteOnSD("Booking_details_log"+date+":"+hour+":"+minute+":"+seconds+":"+milliseconds, e.toString());
        }*/
    }
   /* public void generateNoteOnSD(String sFileName, String sBody){
        try
        {
            File root = new File(Environment.getExternalStorageDirectory(), "Mychauffeur Logs");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }*/
    /*@Override
    public void onBackPressed() {
        finish();
    }*/

    @Override
    public void onBackPressed() {
        if (getFragmentManager().findFragmentByTag("Frag1") != null) {
            getFragmentManager().popBackStackImmediate("Frag1",0);
        } else {
            //super.onBackPressed();
            Intent in = new Intent(BookingDetails.this,MainActivity.class);
            in.putExtra("UserId",uid);
            startActivity(in);
            finish();
        }
    }

}
