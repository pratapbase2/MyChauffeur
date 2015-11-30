package com.mychauffeurapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mychauffeurapp.R;
import com.mychauffeurapp.app.App_VolleyExamples;
import com.mychauffeurapp.app.MyVolley;
import com.mychauffeurapp.model.MyEditTextView;
import com.mychauffeurapp.model.MyTextview;
import com.mychauffeurapp.model.MyTextviews;
import com.mychauffeurapp.navdrawer.NavDrawerItem;
import com.mychauffeurapp.navdrawer.NavDrawerListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Booking extends Activity {

    private String urlJsonArry = "http://54.169.81.215/mychauffeurapi/mcapi/getestimate/";

    private Button proceed;
    ImageView imageDatePicker;
    MyTextviews textTime,textDate,textMonth,destinationAddressText;
   /* Calendar mcurrentTime = Calendar.getInstance();
    final int seconds=mcurrentTime.get(Calendar.SECOND);
    final int milliseconds=mcurrentTime.get(Calendar.MILLISECOND);
    final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    final int minute = mcurrentTime.get(Calendar.MINUTE);
    final int date=mcurrentTime.get(Calendar.DATE);*/
    MyTextview logout;

    MyTextviews estDistanceCost;//estTime,estPerUnit,estDistance,
    MyTextviews constant_txt;
    LinearLayout estDistanceLayout;//estTimeLayout,
    MyTextviews hour_txt;

    MyTextviews durationText;

    MyTextviews destinationText,estDistanceHour;
    MyEditTextView destinationAddressET;
    LinearLayout dateTitle,timeTitle,timeText;
    RelativeLayout dateText;

    ProgressDialog pdialog;

    String destination;
    String estSDistanceCost;

    String sname,slat,slong;

    String dlat,dlong;
    String time,dates,months,years;
    String edithour,editminutes;
    String userid;

    //AB NavDrawer
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    ListPopupWindow listPopupWindow;
    String[] num={"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"};

    String duration,view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking);
       /* try {*/

            this.getActionBar().setDisplayHomeAsUpEnabled(false);
            this.getActionBar().setDisplayShowCustomEnabled(true);
            this.getActionBar().setDisplayShowTitleEnabled(false);

            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.titleview, null);

            ((MyTextview) v.findViewById(R.id.title)).setText(this.getTitle());
            //   ((TextView)v.findViewById(R.id.logout)).setText("Logout");
            this.getActionBar().setCustomView(v);

            proceed = (Button) findViewById(R.id.proceed);
            imageDatePicker = (ImageView) findViewById(R.id.imageDatePicker);
            textTime = (MyTextviews) findViewById(R.id.textTime);
            textDate = (MyTextviews) findViewById(R.id.textDate);
            textMonth = (MyTextviews) findViewById(R.id.textMonth);
            destinationAddressText = (MyTextviews) findViewById(R.id.destinationAddressText);
            destinationAddressET = (MyEditTextView) findViewById(R.id.destinationAddressET);
            destinationText = (MyTextviews) findViewById(R.id.destinationText);
            estDistanceHour = (MyTextviews) findViewById(R.id.estDistanceHour);
            hour_txt = (MyTextviews) findViewById(R.id.view);

            //durationText= (MyTextviews) findViewById(R.id.durationText);

            durationText = (MyTextviews) findViewById(R.id.durationText);

            dateTitle = (LinearLayout) findViewById(R.id.dateTitle);
            dateText = (RelativeLayout) findViewById(R.id.dateText);

            timeTitle = (LinearLayout) findViewById(R.id.timeTitle);
            timeText = (LinearLayout) findViewById(R.id.timeText);

            durationText.setText("Select Duration");

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                //bus_names=extras.getStringArray("BusNames");
                sname = extras.getString("SourceName");
                slat = String.valueOf(extras.getDouble("SLatitude"));
                slong = String.valueOf(extras.getDouble("SLongitude"));
                userid = extras.getString("UserId");

                //Toast.makeText(Booking.this,sname+"\n"+slat+"\n"+slong, Toast.LENGTH_SHORT).show();
            }

            listPopupWindow = new ListPopupWindow(Booking.this);
            listPopupWindow.setAdapter(new ArrayAdapter(Booking.this, R.layout.list_item, num));
            listPopupWindow.setAnchorView(durationText);
            listPopupWindow.setWidth(300);
            listPopupWindow.setHeight(800);
            listPopupWindow.setModal(true);
            listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    duration = num[position];
                    durationText.setText(num[position]);
                    durationText.setTextSize(36);
                    hour_txt.setVisibility(View.VISIBLE);
                    listPopupWindow.dismiss();

                    if (durationText.getText().toString().equals("01")) {
                        hour_txt.setText("Hour");
                    } else {
                        hour_txt.setText("Hours");
                    }
                    // showpDialog();
                    RequestQueue queue = MyVolley.getRequestQueue();

                    StringRequest req = new StringRequest(urlJsonArry + slat + "/" + slong + "/" + dlat + "/" + dlong + "/" + time + ":00" + "/" + duration,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject busStops = new JSONObject(response);
                                        Log.e("Booking", busStops + "\n" + response);
                                        JSONObject res = (JSONObject) busStops.get("result");
                                        try {

                                        /*constant_txt.setVisibility(View.VISIBLE);
                                        //estTimeLayout.setVisibility(View.VISIBLE);
                                        estDistanceLayout.setVisibility(View.VISIBLE);*/

                                            estSDistanceCost = res.getString("acutalcost");

                                            //estDistanceHour
                                            if (duration.equals("01")) {
                                                estDistanceHour.setText(duration + " Hour");
                                            } else {
                                                estDistanceHour.setText(duration + " Hours");
                                            }

                                            estDistanceCost.setText("Rs. " + estSDistanceCost);
                                            //duration

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();

                                        //Analytics catching exceptions
                                        App_VolleyExamples.getInstance().trackException(e);
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
                    queue.add(req);
                }
            });

            durationText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dest = destinationAddressET.getText().toString();
                    Log.e("The duration value", dest);
                    if (dest.equals("Drop location")) {
                        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(Booking.this, AlertDialog.THEME_HOLO_LIGHT);
                        helpBuilder.setTitle("Drop location");
                        helpBuilder.setMessage("Please select drop location");

                        helpBuilder.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog helpDialog = helpBuilder.create();
                        helpDialog.show();
                        //durationText.setEnabled(false);
                    } else {
                        //durationText.setEnabled(true);
                        listPopupWindow.show();
                    }

                }
            });

            //estTime=(MyTextviews) findViewById(R.id.estTime);
            //estPerUnit=(MyTextviews) findViewById(R.id.estPerUnit);
            //estDistance=(MyTextviews) findViewById(R.id.estDistance);
            estDistanceCost = (MyTextviews) findViewById(R.id.estDistanceCost);
            constant_txt = (MyTextviews) findViewById(R.id.estConstantTxt);

            //estTimeLayout= (LinearLayout) findViewById(R.id.estTimeLayout);
            estDistanceLayout = (LinearLayout) findViewById(R.id.estDistanceLayout);

            //estPerUnit.setVisibility(View.GONE);
            constant_txt.setVisibility(View.GONE);
            //estTimeLayout.setVisibility(View.GONE);
            estDistanceLayout.setVisibility(View.GONE);

            Calendar mcurrentTime = Calendar.getInstance();
            final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            final int minute = mcurrentTime.get(Calendar.MINUTE);
            final int year = mcurrentTime.get(Calendar.YEAR);
            final int month = mcurrentTime.get(Calendar.MONTH);
            final int dom = mcurrentTime.get(Calendar.DAY_OF_MONTH);

            dates = dom + "";
            months = (month + 1) + "";
            years = year + "";

            textDate.setText(dom + "");
            switch (month + 1) {
                case 1:
                    textMonth.setText("JAN");
                    break;
                case 2:
                    textMonth.setText("FEB");
                    break;
                case 3:
                    textMonth.setText("MAR");
                    break;
                case 4:
                    textMonth.setText("APR");
                    break;
                case 5:
                    textMonth.setText("MAY");
                    break;
                case 6:
                    textMonth.setText("JUN");
                    break;
                case 7:
                    textMonth.setText("JUL");
                    break;
                case 8:
                    textMonth.setText("AUG");
                    break;
                case 9:
                    textMonth.setText("SEP");
                    break;
                case 10:
                    textMonth.setText("OCT");
                    break;
                case 11:
                    textMonth.setText("NOV");
                    break;
                case 12:
                    textMonth.setText("DEC");
                    break;


            }

        textTime.setText(pad(hour) + ":" + pad(minute));

        /*if (String.valueOf(hour).trim().length()==1){
            edithour="0"+hour;
            textTime.setText(edithour + ":" + minute);
        }

        if (String.valueOf(minute).trim().length()==1){
            editminutes="0"+minute;
            textTime.setText(hour + ":" + editminutes);
        }*/



            time = hour + ":" + minute;

            timeTitle.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(Booking.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            textTime.setText(pad(selectedHour) + ":" + pad(selectedMinute));

                            /*if (String.valueOf(selectedHour).trim().length()==1){
                                edithour="0"+selectedHour;
                                textTime.setText(edithour + ":" + editminutes);
                            }

                            if (String.valueOf(selectedMinute).trim().length()==1){
                                editminutes="0"+selectedMinute;
                                textTime.setText(selectedHour + ":" + editminutes);
                            }*/

                            time = selectedHour + ":" + selectedMinute;
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                    //Toast.makeText(Booking.this,time, Toast.LENGTH_SHORT).show();
                }
            });

            String hours, minutes;
            timeText.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(Booking.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                            //    pad(hourOfDay) + ":" + pad(minute)

                            textTime.setText(pad(selectedHour) + ":" + pad(selectedMinute));
                            time = selectedHour + ":" + selectedMinute;
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                    // Toast.makeText(Booking.this,time, Toast.LENGTH_SHORT).show();

                }
            });

            dateTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog mDatePicker;
                    mDatePicker = new DatePickerDialog(Booking.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            textDate.setText(dayOfMonth + "");
                            dates = dayOfMonth + "";
                            years = year + "";
                            months = (monthOfYear + 1) + "";
                            //textMonth.setText((monthOfYear + 1) + "");

                            switch (monthOfYear + 1) {
                                case 1:
                                    textMonth.setText("JAN");
                                    break;
                                case 2:
                                    textMonth.setText("FEB");
                                    break;
                                case 3:
                                    textMonth.setText("MAR");
                                    break;
                                case 4:
                                    textMonth.setText("APR");
                                    break;
                                case 5:
                                    textMonth.setText("MAY");
                                    break;
                                case 6:
                                    textMonth.setText("JUN");
                                    break;
                                case 7:
                                    textMonth.setText("JUL");
                                    break;
                                case 8:
                                    textMonth.setText("AUG");
                                    break;
                                case 9:
                                    textMonth.setText("SEP");
                                    break;
                                case 10:
                                    textMonth.setText("OCT");
                                    break;
                                case 11:
                                    textMonth.setText("NOV");
                                    break;
                                case 12:
                                    textMonth.setText("DEC");
                                    break;


                            }
                        }
                    }, year, month, dom);
                    mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    mDatePicker.setTitle("Select Date");
                    mDatePicker.show();

                }
            });

            dateText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog mDatePicker;
                    mDatePicker = new DatePickerDialog(Booking.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            textDate.setText(dayOfMonth + "");
                            dates = dayOfMonth + "";
                            years = year + "";
                            months = (monthOfYear + 1) + "";
                            //textMonth.setText((monthOfYear + 1) + "");

                            switch (monthOfYear + 1) {
                                case 1:
                                    textMonth.setText("JAN");
                                    break;
                                case 2:
                                    textMonth.setText("FEB");
                                    break;
                                case 3:
                                    textMonth.setText("MAR");
                                    break;
                                case 4:
                                    textMonth.setText("APR");
                                    break;
                                case 5:
                                    textMonth.setText("MAY");
                                    break;
                                case 6:
                                    textMonth.setText("JUN");
                                    break;
                                case 7:
                                    textMonth.setText("JUL");
                                    break;
                                case 8:
                                    textMonth.setText("AUG");
                                    break;
                                case 9:
                                    textMonth.setText("SEP");
                                    break;
                                case 10:
                                    textMonth.setText("OCT");
                                    break;
                                case 11:
                                    textMonth.setText("NOV");
                                    break;
                                case 12:
                                    textMonth.setText("DEC");
                                    break;


                            }
                        }
                    }, year, month, dom);
                    mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    mDatePicker.setTitle("Select Date");
                    mDatePicker.show();
                }
            });

            destinationAddressText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*int time = Integer.parseInt(durationText.getText().toString());

                    if (time <= 0) {
                        Toast.makeText(Booking.this, "Time can't be 0 hour or less than 0 hour", Toast.LENGTH_SHORT).show();
                    } else if (time > 24) {
                        Toast.makeText(Booking.this, "Time can't be greater than 24 hours", Toast.LENGTH_SHORT).show();
                    } else {*/

                        Intent destinationMap = new Intent(Booking.this, DestinationMap.class);
                        destinationMap.putExtra("UserId",userid);
                        startActivityForResult(destinationMap, 1);
                        //Booking.this.finish();
                    //}
                }
            });


            proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String dest = destinationAddressET.getText().toString();
                    String dur = durationText.getText().toString();
                    Log.e("The duration value", dest);
                    if(dur.equals("Select Duration")){
                        duration = "01";
                    }
                    if (dest.equals("Drop location")) {
                        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(Booking.this, AlertDialog.THEME_HOLO_LIGHT);
                        helpBuilder.setTitle("Drop location");
                        helpBuilder.setMessage("Please select drop location");

                        helpBuilder.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog helpDialog = helpBuilder.create();
                        helpDialog.show();
                    } else if ((dlat) != null && dlong != null)
                        if (dlat.length() != 0 && dlong.length() != 0
                                && Double.parseDouble(dlat) != 0
                                && Double.parseDouble(dlong) != 0) {

                            App_VolleyExamples.getInstance().trackEvent("ProceedButton", "Clicked", "Proceed to Booking");
                            //Intent bookIntent = new Intent(Booking.this, Vehicle_details.class);
                            Intent bookIntent = new Intent(Booking.this, Vehicle_details_tab.class);
                            bookIntent.putExtra("Cost", estSDistanceCost);
                            bookIntent.putExtra("Time", duration);
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

                            bookIntent.putExtra("UserId", userid);

                            startActivity(bookIntent);
                            Booking.this.finish();

                        }



                /*Intent bookIntent = new Intent(Booking.this, BookingInformation.class);
                startActivity(bookIntent);
                Booking.this.finish();*/
                }
            });

        /*}catch (Exception e) {
            generateNoteOnSD("Booking_log"+date+":"+hour+":"+minute+":"+seconds+":"+milliseconds, e.toString());

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
    }
*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==1){
            try {
                destination = data.getStringExtra("DestAddress");
                dlat = String.valueOf(data.getDoubleExtra("DLatitude", 0));
                dlong = String.valueOf(data.getDoubleExtra("DLongitude",0));

                //.makeText(Booking.this,destination+ "\n"+dlat+"\n"+dlong, Toast.LENGTH_SHORT).show();

                destinationAddressText.setVisibility(View.GONE);
                destinationAddressET.setVisibility(View.VISIBLE);
                destinationText.setVisibility(View.VISIBLE);
                destinationAddressET.setText(destination);

                pdialog = new ProgressDialog(Booking.this, AlertDialog.THEME_HOLO_LIGHT);
                pdialog.setMessage("Loading ...");

                pdialog.setCancelable(false);
                showpDialog();

                Log.e("Booking",urlJsonArry + slat + "/" + slong + "/" + dlat + "/" + dlong + "/" + time + ":00"+"/"+01);
                RequestQueue queue = MyVolley.getRequestQueue();

                StringRequest req = new StringRequest(urlJsonArry + slat + "/" + slong + "/" + dlat + "/" + dlong + "/" + time + ":00"+"/"+01,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject busStops =  new JSONObject(response);
                                    Log.e("Booking", busStops + "\n" + response);
                                    JSONObject res = (JSONObject) busStops.get("result");
                                try {
                                    //for (int i = 0; i < orderid1.length(); i++) {

                                        //JSONObject busStops = (JSONObject) orderid1;

                                        //estPerUnit.setVisibility(View.VISIBLE);
                                        constant_txt.setVisibility(View.VISIBLE);
                                        //estTimeLayout.setVisibility(View.VISIBLE);
                                        estDistanceLayout.setVisibility(View.VISIBLE);

                                        String estSPerUnit = res.getString("unitprice");
                                        String estSTime = res.getString("btime");
                                        String estSDistance = res.getString("distance_value");
                                        estSDistanceCost = res.getString("acutalcost");

                                        //estTime.setText(estSTime);
                                        //estPerUnit.setText("Rs. "+estSPerUnit+" /Km.");
                                        //estDistance.setText(estSDistance+" Km.");
                                        estDistanceCost.setText("Rs. "+estSDistanceCost);


                                        //Toast.makeText(getApplicationContext(), res.getString("bus_stop_name"), Toast.LENGTH_SHORT).show();
                                    //}
                                    //Toast.makeText(getApplicationContext(), busNameList.size()+" ", Toast.LENGTH_SHORT).show();

                                    //listView.setAdapter(new BusStopListAdapter1(ListPopUp1.this, busNameList));

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
                queue.add(req);
            }
            catch (Exception e){
                hidepDialog();
                e.printStackTrace();
            }
        }
    }

    private void showpDialog(){
        if (!pdialog.isShowing())
            pdialog.show();
    }

    private void hidepDialog(){
        if (pdialog.isShowing())
            pdialog.dismiss();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
       *//* LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.titleview, null);

        ((TextView)v.findViewById(R.id.title)).setText(this.getTitle());
        //   ((TextView)v.findViewById(R.id.logout)).setText("Logout");
        this.getActionBar().setCustomView(v);*//*

        getMenuInflater().inflate(R.menu.main, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title

        // Handle action bar actions click
        int id = item.getItemId();
        *//*AB if (id == R.id.logout) {
            Intent passIntent = new Intent(Booking.this, LoginScreen.class);
            startActivity(passIntent);
            Booking.this.finish();
            return true;
        }*//*
        return super.onOptionsItemSelected(item);
    }*/

    public String pad(int input) {
        String str = "";
        if (input > 9) {
            str = Integer.toString(input);
        } else {
            str = "0" + Integer.toString(input);
        }
        return str;
    }

    @Override
    public void onBackPressed() {
            //super.onBackPressed();
        String dest = destinationAddressET.getText().toString();
        if(dest.equals("Drop location")){
            Intent in1 = new Intent(Booking.this,MainActivity.class);
            in1.putExtra("UserId",userid);
            startActivity(in1);
            finish();
        }
        else {
            Intent in2 = new Intent(Booking.this, DestinationMap.class);
            in2.putExtra("UserId",userid);
            startActivityForResult(in2, 1);
        }
        /*Intent in = new Intent(Booking.this,DestinationMap.class);
        startActivity(in);*/
        //    finish();
    }
}

