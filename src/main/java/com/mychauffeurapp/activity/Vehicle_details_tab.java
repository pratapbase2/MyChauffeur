package com.mychauffeurapp.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import com.mychauffeurapp.R;
import com.mychauffeurapp.model.MyTextview;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class Vehicle_details_tab extends TabActivity {

    TabHost tabHost;
    String estSDistanceCost, durationText,sname, destination,slat,slong,dlat,dlong,time,dates,months,years,userid;
   /* Calendar mcurrentTime = Calendar.getInstance();
    final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    final int minute = mcurrentTime.get(Calendar.MINUTE);
    final int seconds=mcurrentTime.get(Calendar.SECOND);
    final int milliseconds=mcurrentTime.get(Calendar.MILLISECOND);
    final int date=mcurrentTime.get(Calendar.DATE);*/
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details_tab);
       /* try {*/
            this.getActionBar().setDisplayShowCustomEnabled(true);
            this.getActionBar().setDisplayShowTitleEnabled(false);

            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.titleview, null);

            ((MyTextview) v.findViewById(R.id.title)).setText(this.getTitle());
            this.getActionBar().setCustomView(v);

            Resources resources = getResources();
            tabHost = getTabHost();


            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                estSDistanceCost = extras.getString("Cost");
                durationText = extras.getString("Time");
                sname = extras.getString("SAddress");
                destination = extras.getString("DAddress");
                slat = extras.getString("slat");
                slong = extras.getString("slong");
                dlat = extras.getString("dlat");
                dlong = extras.getString("dlong");
                time = extras.getString("time");
                dates = extras.getString("dates");
                months = extras.getString("months");
                years = extras.getString("years");
                userid = extras.getString("UserId");
            }

       /* Bundle b1 = new Bundle();
        b1.putString("UserId",u);*/

            // Vehicle Details tab
            Intent intentDetails = new Intent().setClass(this, Vehicle_details.class);
            intentDetails.putExtra("Cost", estSDistanceCost);
            intentDetails.putExtra("Time", durationText);
            intentDetails.putExtra("SAddress", sname);
            intentDetails.putExtra("DAddress", destination);
            intentDetails.putExtra("slat", slat);
            intentDetails.putExtra("slong", slong);
            intentDetails.putExtra("dlat", dlat);
            intentDetails.putExtra("dlong", dlong);
            intentDetails.putExtra("time", time);
            intentDetails.putExtra("dates", dates);
            intentDetails.putExtra("months", months);
            intentDetails.putExtra("years", years);
            intentDetails.putExtra("UserId", userid);

            TabHost.TabSpec tabSpecAndroid = tabHost
                    .newTabSpec("Own Vehicle").setIndicator("Own Vehicle")
                    .setContent(intentDetails);

            // Vehicle Type
            Intent intentApple = new Intent().setClass(this, Vehicle_types.class);
            intentApple.putExtra("Cost", estSDistanceCost);
            intentApple.putExtra("Time", durationText);
            intentApple.putExtra("SAddress", sname);
            intentApple.putExtra("DAddress", destination);
            intentApple.putExtra("slat", slat);
            intentApple.putExtra("slong", slong);
            intentApple.putExtra("dlat", dlat);
            intentApple.putExtra("dlong", dlong);
            intentApple.putExtra("time", time);
            intentApple.putExtra("dates", dates);
            intentApple.putExtra("months", months);
            intentApple.putExtra("years", years);
            intentApple.putExtra("UserId", userid);

            TabHost.TabSpec tabSpecApple = tabHost
                    .newTabSpec("Need Vehicle").setIndicator("Need Vehicle")
                    .setContent(intentApple);

            // tabSpecApple.setAllCaps(false);
            // add all tabs
            tabHost.addTab(tabSpecAndroid);
            tabHost.addTab(tabSpecApple);

            //set Windows tab as default (zero based)
            tabHost.setCurrentTab(2);


        /*tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            public void onTabChanged(String arg0) {
                for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                    tabHost.getTabWidget().getChildAt(i)
                            .setBackgroundColor(Color.parseColor("#1e3e66")); // unselected
                }
                tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab())
                        .setBackgroundColor(Color.parseColor("#18aae6")); // selected

            }
        });*/
      /*  }catch (Exception e) {
            generateNoteOnSD("Vehicles_details_tab_log"+date+":"+hour+":"+minute+":"+seconds+":"+milliseconds, e.toString());
        }*/
    }
    /*public void generateNoteOnSD(String sFileName, String sBody){
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
}