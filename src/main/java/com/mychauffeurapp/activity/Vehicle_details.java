package com.mychauffeurapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;
import android.widget.Toast;

import com.mychauffeurapp.R;
import com.mychauffeurapp.model.ButtonText;
import com.mychauffeurapp.model.MyEditTextView;
import com.mychauffeurapp.model.MyTextviewBlack;
import com.mychauffeurapp.model.MyTextviews;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class Vehicle_details extends Activity {

    String jsonUrl = "http://54.169.81.215/mychauffeurapi/mcapi/driverbooking";
    ListPopupWindow listPopupWindow1;
    ListPopupWindow listPopupWindow2;

    String[] insurance_type={"type1", "type2"};
    String[] vehicle_age = {"1","2","3","4","5"};
    String name[]={"Tata","Maruti","Honda","Hero","Suzuki"};
   /* Calendar mcurrentTime = Calendar.getInstance();
    final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    final int minute = mcurrentTime.get(Calendar.MINUTE);
    final int seconds=mcurrentTime.get(Calendar.SECOND);
    final int milliseconds=mcurrentTime.get(Calendar.MILLISECOND);
    final int date=mcurrentTime.get(Calendar.DATE);*/
    String age;

    String estSDistanceCost, durationText,sname, destination,slat,slong,dlat,dlong,time,dates,months,years,userid;


    MyEditTextView reg_num1 ,age_of_vehicle1,make_of_car1;;

    MyTextviewBlack type_of_insurance1;
    ButtonText proceed;

    String breg_num, bmake_of_car,btype_of_insurance,bage_of_vehicle;
    String type, text;
    //ProgressDialog pdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);
       /* try {*/
            reg_num1 = (MyEditTextView) findViewById(R.id.reg_num);
            make_of_car1 = (MyEditTextView) findViewById(R.id.make_of_car);
            type_of_insurance1 = (MyTextviewBlack) findViewById(R.id.type_of_insurance);
           // age_of_vehicle1 = (MyTextviews) findViewById(R.id.age_of_vehicle);
            age_of_vehicle1 = (MyEditTextView) findViewById(R.id.age_of_vehicle);
            proceed = (ButtonText) findViewById(R.id.proceed);

        /*pdialog = new ProgressDialog(Vehicle_details.this, AlertDialog.THEME_HOLO_LIGHT);
        pdialog.setMessage("Loading...");
*/
        /*this.getActionBar().setDisplayShowCustomEnabled(true);
        this.getActionBar().setDisplayShowTitleEnabled(false);

        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.titleview, null);

        ((MyTextview)v.findViewById(R.id.title)).setText(this.getTitle());
        //   ((TextView)v.findViewById(R.id.logout)).setText("Logout");
        this.getActionBar().setCustomView(v);
*/
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
//            listPopupWindow1 = new ListPopupWindow(Vehicle_details.this);
//            listPopupWindow1.setAdapter(new ArrayAdapter(Vehicle_details.this, R.layout.list_item, name));
//            listPopupWindow1.setAnchorView(make_of_car1);
//            listPopupWindow1.setWidth(820);
//            listPopupWindow1.setHeight(800);
            //listPopupWindow1.
//            listPopupWindow1.setModal(true);
//            listPopupWindow1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    //*duration = num[position];*//*
//                    make_of_car1.setText(name[position]);
//                    listPopupWindow1.dismiss();
//                }
//            });
           /* listPopupWindow2 = new ListPopupWindow(Vehicle_details.this);
            listPopupWindow2.setAdapter(new ArrayAdapter(Vehicle_details.this, R.layout.list_item, vehicle_age));
            listPopupWindow2.setAnchorView(type_of_insurance1);
            listPopupWindow2.setWidth(500);
            listPopupWindow2.setHeight(600);
            listPopupWindow2.setModal(true);
            listPopupWindow2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    age = vehicle_age[position];
                    if (age.equals("1")) {
                        age_of_vehicle1.setText(vehicle_age[position] + " year");
                    } else {
                        age_of_vehicle1.setText(vehicle_age[position] + " years");
                    }

                    listPopupWindow2.dismiss();
                }
            });

            age_of_vehicle1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listPopupWindow2.show();
                }
            });*/
            age_of_vehicle1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    age_of_vehicle1.setHint(" ");
                    age = age_of_vehicle1.getText().toString();


                }
            });
            age_of_vehicle1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    age_of_vehicle1.setHint("Age of Vehicle");
                    age_of_vehicle1.setHintTextColor(Color.rgb(30, 62, 102));
                }
            });

            reg_num1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reg_num1.setHint("");
                }
            });
            reg_num1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    reg_num1.setHint("Registration Number");
                    reg_num1.setHintTextColor(Color.rgb(30, 62, 102));
                }
            });

            make_of_car1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    make_of_car1.setHint("");
                }
            });
            make_of_car1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    make_of_car1.setHint("Make of Car/ Van");
                    make_of_car1.setHintTextColor(Color.rgb(30, 62, 102));
                }
            });

            type_of_insurance1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    type_of_insurance1.setHint("");
                }
            });
            type_of_insurance1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    type_of_insurance1.setHint("Type of Insurance");
                    type_of_insurance1.setHintTextColor(Color.rgb(30, 62, 102));
                }
            });


            Log.e("Before proceed{{{{{{{", breg_num + bmake_of_car + btype_of_insurance + bage_of_vehicle);

            proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //showpDialog();
                    breg_num = reg_num1.getText().toString();
                    bmake_of_car = make_of_car1.getText().toString();
                    btype_of_insurance = type_of_insurance1.getText().toString();
                    bage_of_vehicle = age_of_vehicle1.getText().toString();

                    type = "0";
                    text = "0";
               /* if (reg_num1.getText().toString().equalsIgnoreCase("") && make_of_car1.getText().toString().equalsIgnoreCase("") && type_of_insurance1.getText().toString().equalsIgnoreCase("") && age_of_vehicle1.getText().toString().equals("Age of vehicle")) {
                    reg_num1.requestFocus();
                    reg_num1.setError("Pls Enter Your Registration Number ?");

                    make_of_car1.requestFocus();
                    make_of_car1.setError("Pls Enter Your Make of Car ?");

                    type_of_insurance1.requestFocus();
                    type_of_insurance1.setError("Pls Enter Your Type of Insurance ?");

                    age_of_vehicle1.setText("Pls Enter the Age of Vehicle ?");
                    *//*age.requestFocus();
                    age.setError("Pls Enter Your Name ?");*//*

                } else if (reg_num1.getText().toString().equalsIgnoreCase("")) {
                    reg_num1.requestFocus();
                    reg_num1.setError("Pls Enter Your Registration Number ?");

                } else if (make_of_car1.getText().toString().equalsIgnoreCase("")) {
                    make_of_car1.requestFocus();
                    make_of_car1.setError("Pls Enter Your Make of Car ?");
                } else if (type_of_insurance1.getText().toString().equalsIgnoreCase("")) {
                    type_of_insurance1.requestFocus();
                    type_of_insurance1.setError("Pls Enter Your Type of Insurance ?");
                } else if (age_of_vehicle1.getText().toString().equals("Age of vehicle")) {
                    age_of_vehicle1.setText("Pls Enter the Age of Vehicle ?");
                } */
                    if (reg_num1.getText().toString().equalsIgnoreCase("") && make_of_car1.getText().toString().equalsIgnoreCase("") && type_of_insurance1.getText().toString().equalsIgnoreCase("") && age_of_vehicle1.getText().toString().equals("Age of vehicle")) {

                        breg_num = "0";
                        bmake_of_car = "0";
                        btype_of_insurance = "0";
                        bage_of_vehicle = "0";
                    }
                    if (reg_num1.getText().toString().equalsIgnoreCase("")) {
                        breg_num = "0";
                    }
                    if (make_of_car1.getText().toString().equalsIgnoreCase("")) {
                        bmake_of_car = "0";
                    }
                    if (type_of_insurance1.getText().toString().equalsIgnoreCase("")) {
                        btype_of_insurance = "0";
                    }
                    if (age_of_vehicle1.getText().toString().equals("")) {
                        bage_of_vehicle = "0";
                    }

                /*} else if (reg_num1.getText().toString().equalsIgnoreCase("")) {
                    breg_num = "0";

                } else if (make_of_car1.getText().toString().equalsIgnoreCase("")) {

                    bmake_of_car = "0";

                } else if (type_of_insurance1.getText().toString().equalsIgnoreCase("")) {

                    btype_of_insurance = "0";

                } else if (age_of_vehicle1.getText().toString().equals("Age of vehicle")) {

                    bage_of_vehicle = "0";

                }*/

                    Intent bookIntent = new Intent(Vehicle_details.this, BookingInformation.class);
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
                    bookIntent.putExtra("reg_num", breg_num);
                    bookIntent.putExtra("make_of_car", bmake_of_car);
                    bookIntent.putExtra("type_of_insurance", "Comprehensive");//btype_of_insurance
                    bookIntent.putExtra("age_of_vehicle", bage_of_vehicle);
                    bookIntent.putExtra("UserId", userid);
                    bookIntent.putExtra("bvehicle_enq", text);
                    bookIntent.putExtra("bvehicle", type);

                    //  Toast.makeText(Vehicle_details.this, breg_num + bmake_of_car + btype_of_insurance + bage_of_vehicle, Toast.LENGTH_SHORT).show();
                    Log.e("The user id", userid);
                    Log.e("The vehicle details ", estSDistanceCost + durationText + sname + destination + slat + slong + dlat + dlong + time + dates + months + years + breg_num + bmake_of_car + btype_of_insurance + bage_of_vehicle);

                    startActivity(bookIntent);
                    Vehicle_details.this.finish();
                }
            });

      /*  }catch (Exception e) {
            generateNoteOnSD("Vehicle_details_log"+date+":"+hour+":"+minute+":"+seconds+":"+milliseconds, e.toString());
        }*/
    }

  /*  public void generateNoteOnSD(String sFileName, String sBody){
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

    @Override
    public void onBackPressed() {
        Intent in = new Intent(Vehicle_details.this,Booking.class);
        in.putExtra("UserId",userid);
        in.putExtra("SourceName",sname);
        in.putExtra("SLatitude",slat);
        in.putExtra("SLongitude",slong);
        startActivity(in);
        finish();
    }

    /*private void showpDialog(){
        if (!pdialog.isShowing())
            pdialog.show();
    }

    private void hidepDialog(){
        if (pdialog.isShowing())
            pdialog.dismiss();
    }
*/
}
