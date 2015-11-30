package com.mychauffeurapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.mychauffeurapp.R;
import com.mychauffeurapp.common.AlertDialogManager;
import com.mychauffeurapp.common.ConnectionDetector;
import com.mychauffeurapp.fragments.BookingFragment;

import java.util.Calendar;

public class BookingDetails_old extends Activity {

    String bid, btime,bdate,bsrc_address,bdest_address,bcost,bperiod;
    /*Calendar mcurrentTime = Calendar.getInstance();
    final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    final int minute = mcurrentTime.get(Calendar.MINUTE);
    final int seconds=mcurrentTime.get(Calendar.SECOND);
    final int milliseconds=mcurrentTime.get(Calendar.MILLISECOND);
    final int date1=mcurrentTime.get(Calendar.DATE);*/
    ConnectionDetector cd;
    AlertDialogManager alert = new AlertDialogManager();

    TextView booking_id;
    TextView src_address,dest_address,date,time,cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookingdetails);

        booking_id = (TextView) findViewById(R.id.booking_id);
        src_address = (TextView) findViewById(R.id.textView16);
        dest_address = (TextView) findViewById(R.id.textView18);
        date = (TextView) findViewById(R.id.date);
        time = (TextView) findViewById(R.id.textDuration);
        cost = (TextView) findViewById(R.id.cost);

        cd = new ConnectionDetector(getApplicationContext());
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            //Toast.makeText(BusRoutesListView.this, "Provide source & destination", Toast.LENGTH_LONG).show();
            Intent homeIntent = new Intent(getApplicationContext(), BookingFragment.class);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            finish();

        } else {

            bid = extras.getString("bid");
            btime = extras.getString("btime");
            bdate = extras.getString("bdate");
            bsrc_address = extras.getString("source");
            bdest_address = extras.getString("destination");
            bcost = extras.getString("bcost");
            bperiod = extras.getString("period");

        }

        if(!cd.isConnectingToInternet())
        {
            alert.showAlertDialog(BookingDetails_old.this,
                    "Internet Connection Lost",
                    "Please connect to Internet and Try again..", false);
        }
        else
        {
            booking_id.setText(bid);
            time.setText(bperiod+ " Hours");
            date.setText(bdate);
            src_address.setText(bsrc_address);
            dest_address.setText(bdest_address);
            cost.setText("Rs." +bcost);
        }
    }

}
