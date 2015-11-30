package com.mychauffeurapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListPopupWindow;
import android.widget.Toast;

import com.mychauffeurapp.R;
import com.mychauffeurapp.model.ButtonText;
import com.mychauffeurapp.model.MyEditTextView;
import com.mychauffeurapp.model.MyTextviews;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class Vehicle_types extends Activity {

    MyTextviews Request_quote;
    MyEditTextView details;
    ButtonText driver_car;

    private CheckBox car, van;

    ProgressDialog pdialog;

    ListPopupWindow listPopupWindow;
    String[] types = {"Driver and Car", "Driver and Van"};
    /*Calendar mcurrentTime = Calendar.getInstance();
    final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    final int minute = mcurrentTime.get(Calendar.MINUTE);
    final int seconds=mcurrentTime.get(Calendar.SECOND);
    final int milliseconds=mcurrentTime.get(Calendar.MILLISECOND);
    final int date=mcurrentTime.get(Calendar.DATE);*/
    String type,text;
    String estSDistanceCost, durationText,sname, destination,slat,slong,dlat,dlong,time,dates,months,years,userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_types);
      /*  try {*/
            //car_van1 = (MyTextviews) findViewById(R.id.car_van);
            details = (MyEditTextView) findViewById(R.id.driver_text);
            car = (CheckBox) findViewById(R.id.car);
            van = (CheckBox) findViewById(R.id.van);
            driver_car = (ButtonText) findViewById(R.id.driver_car);
          //  Request_quote = ( MyTextviews) findViewById(R.id.requestquote);

            //car_van1.setText("Select Driver and vehicle");
            pdialog = new ProgressDialog(Vehicle_types.this, AlertDialog.THEME_HOLO_LIGHT);
            pdialog.setMessage("Loading ...");
            pdialog.setCancelable(false);

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

            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    details.setHint("");
                }
            });
           /* details.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    details.setHint("Please enter more details about \nthe type of vehicle and the \n number of passengers");
                    details.setHintTextColor(Color.rgb(0, 161, 228));
                }
            });*/

            if(!details.isFocused()){
                details.setHint("Please enter more details about \nthe type of vehicle and the \n number of passengers");
                details.setHintTextColor(Color.rgb(0, 161, 228));
            }

/*        listPopupWindow = new ListPopupWindow(Vehicle_types.this);
        listPopupWindow.setAdapter(new ArrayAdapter(Vehicle_types.this, R.layout.list_item, types));
        listPopupWindow.setAnchorView(car_van1);
        listPopupWindow.setWidth(600);
        listPopupWindow.setHeight(400);
        listPopupWindow.setModal(true);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                type = types[position];
                car_van1.setText(types[position]);
                listPopupWindow.dismiss();*/
                /*RequestQueue queue = MyVolley.getRequestQueue();

                StringRequest req = new StringRequest(urlJsonArry + slat + "/" + slong + "/" + dlat + "/" + dlong + "/" + time + ":00" + "/" + duration,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject busStops = new JSONObject(response);
                                    Log.e("Booking", busStops + "\n" + response);
                                    JSONObject res = (JSONObject) busStops.get("result");
                                    try {
                                        estSDistanceCost = res.getString("acutalcost");
                                        //estDistanceHour
                                        estDistanceHour.setText(duration+ " Hours");

                                        estDistanceCost.setText("Rs. " + estSDistanceCost);
                                        //duration

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                hidepDialog();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidepDialog();
                        error.printStackTrace();
                    }
                });

                // Adding request to request queue
                queue.add(req);*/
       /*     }
        });*/

        /*car_van1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listPopupWindow.show();
            }
        });*/

            driver_car.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //showpDialog();
                    text = details.getText().toString();
                    Intent bookIntent = new Intent(Vehicle_types.this, BookingInformation.class);
                    if (!car.isChecked() && !van.isChecked() && text.equalsIgnoreCase("")) {
                        type = "0";
                        text = "0";
                    } else if (!car.isChecked() && !van.isChecked()) {
                        type = "0";
                    } else if (text.equalsIgnoreCase("")) {
                        text = "0";
                    }
                    //bookIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    bookIntent.putExtra("Cost", estSDistanceCost);
                    bookIntent.putExtra("Time", durationText);
                    bookIntent.putExtra("SAddress", sname);
                    bookIntent.putExtra("DAddress", destination);
                    bookIntent.putExtra("slat", slat);
                    bookIntent.putExtra("slong", slong);
                    bookIntent.putExtra("dlat", dlat);
                    bookIntent.putExtra("dlong", dlong);
                    bookIntent.putExtra("time", time);
                    bookIntent.putExtra("dates", dates);
                    bookIntent.putExtra("months", months);
                    bookIntent.putExtra("years", years);
                    bookIntent.putExtra("reg_num", "0");
                    bookIntent.putExtra("make_of_car", "0");
                    bookIntent.putExtra("type_of_insurance","Comprehensive");
                    bookIntent.putExtra("age_of_vehicle", "0");
                    bookIntent.putExtra("UserId", userid);
                    bookIntent.putExtra("bvehicle_enq", text);
                    bookIntent.putExtra("bvehicle", type);

                    Log.e("The user id", userid);
                    Log.e("The vehicle details ", text + type);

                    startActivity(bookIntent);
                    Vehicle_types.this.finish();
                }
            });
            car.setOnCheckedChangeListener(listener);
            van.setOnCheckedChangeListener(listener);

           /*Ab Request_quote.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    String[] recipients = {"mychauffeur@rht.mu"};
                    intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Request for quote");
                    intent.putExtra(Intent.EXTRA_TEXT, "Request for quote");
                    intent.setType("text/html");
                    startActivity(Intent.createChooser(intent, "Send mail"));

                }
            });*/

       /* car.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (buttonView.isChecked()) {
                    type = car.getText().toString();
                    //van.setEnabled(false);
                    //driver_car.setEnabled(true);
                } else {
                    //driver_car.setEnabled(false);
                    //van.setEnabled(true);
                }

            }

        });
        van.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (buttonView.isChecked()) {
                    type = van.getText().toString();
                    car.isChecked()=false;
                    //car.setEnabled(false);
                    //driver_car.setEnabled(true);
                } else {
                    //driver_car.setEnabled(false);
                    //car.setEnabled(true);
                }

            }

        });*/
        /*}catch (Exception e){
            generateNoteOnSD("Vehicles_types_log"+date+":"+hour+":"+minute+":"+seconds+":"+milliseconds, e.toString());
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
    private CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {

        public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
            if(isChecked){
                switch(arg0.getId())
                {
                    case R.id.car:
                        car.setChecked(true);
                        van.setChecked(false);
                        type = car.getText().toString();
                        break;
                    case R.id.van:
                        van.setChecked(true);
                        car.setChecked(false);
                        type = van.getText().toString();

                }
            }

        }
    };

    @Override
    public void onBackPressed() {
        Intent in = new Intent(Vehicle_types.this,Booking.class);
        in.putExtra("UserId",userid);
        in.putExtra("SourceName",sname);
        in.putExtra("SLatitude",slat);
        in.putExtra("SLongitude",slong);
        startActivity(in);
        finish();
    }




}
