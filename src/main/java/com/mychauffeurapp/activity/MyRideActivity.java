package com.mychauffeurapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mychauffeurapp.R;
import com.mychauffeurapp.adapter.myRideAdapter;
import com.mychauffeurapp.common.AlertDialogManager;
import com.mychauffeurapp.common.ConnectionDetector;
import com.mychauffeurapp.app.MyVolley;
import com.mychauffeurapp.model.MyRide;
import com.mychauffeurapp.model.MyTextview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class MyRideActivity extends Activity {


    String urlJsonArry="http://54.169.81.215/mychauffeurapi/mcapi/bookinghistory/167";
    String jsonResponse;
    //public static String[] frag = {"All Rides"};
    AlertDialogManager alert = new AlertDialogManager();
    ConnectionDetector cd;
    ProgressDialog pdialog;
    ListView listView;

    private myRideAdapter rideAdapter;
   /* Calendar mcurrentTime = Calendar.getInstance();
    final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    final int minute = mcurrentTime.get(Calendar.MINUTE);
    final int seconds=mcurrentTime.get(Calendar.SECOND);
    final int milliseconds=mcurrentTime.get(Calendar.MILLISECOND);
    final int date1=mcurrentTime.get(Calendar.DATE);*/
    public static String booking_id_list[];

    public static String bid[],date[],status[],src_address[],dest_address[],time[],bactual_cost[], bfinal_cost[],bdiscount_price[],bperiod[];
    String source,btime,bcost,bstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_rides);
       /* try {*/
            //this.getActionBar().setDisplayHomeAsUpEnabled(true);
            this.getActionBar().setDisplayShowCustomEnabled(true);
            this.getActionBar().setDisplayShowTitleEnabled(false);
            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.titleview, null);

            ((MyTextview) v.findViewById(R.id.title)).setText(this.getTitle());
            this.getActionBar().setCustomView(v);

            cd = new ConnectionDetector(getApplicationContext());
            pdialog = new ProgressDialog(MyRideActivity.this, AlertDialog.THEME_HOLO_LIGHT);
            pdialog.setMessage("Loading ...");

            listView = (ListView) findViewById(R.id.card_listView);

            rideAdapter = new myRideAdapter(getApplicationContext(), R.layout.ride_history_list);
            listView.setAdapter(rideAdapter);

            if (!cd.isConnectingToInternet()) {
                alert.showAlertDialog(MyRideActivity.this,
                        "Internet Connection Lost",
                        "Please connect to Internet and Try again..", false);
            } else {
                showpDialog();

                RequestQueue queue = MyVolley.getRequestQueue();

                JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {

                                try {
                                    jsonResponse = "";
                                    booking_id_list = new String[response.length()];
                                    bid = new String[response.length()];
                                    date = new String[response.length()];
                                    src_address = new String[response.length()];
                                    dest_address = new String[response.length()];
                                    bactual_cost = new String[response.length()];
                                    bfinal_cost = new String[response.length()];
                                    time = new String[response.length()];
                                    status = new String[response.length()];
                                    bdiscount_price = new String[response.length()];
                                    bperiod = new String[response.length()];
                                    for (int i = 0; i < response.length(); i++) {

                                        JSONObject person = (JSONObject) response
                                                .get(i);

                                        bid[i] = person.getString("bunique_id");
                                        date[i] = person.getString("bdate");
                                        status[i] = person.getString("bstatus");
                                        src_address[i] = person.getString("bsource_address");
                                        dest_address[i] = person.getString("bdest_address");
                                        time[i] = person.getString("btime");
                                        bactual_cost[i] = person.getString("bactual_cost");
                                        bfinal_cost[i] = person.getString("bfinal_cost");
                                        bdiscount_price[i] = person.getString("bdiscount_price");

                                        booking_id_list[i] = person.getString("booking_id");

                                        source = person.getString("bsource_address");
                                        btime = person.getString("btime");
                                        bcost = person.getString("bfinal_cost");
                                        bstatus = person.getString("bstatus");
                                        MyRide card = new MyRide(source, btime, bcost, bstatus);
                                        rideAdapter.add(card);

                                    }

                                    //mTvResult.setText(jsonResponse);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    /*generateNoteOnSD("My_ride_activity_log", e.toString());*/

                                }
                                hidepDialog();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //tv_no_result.setText("No buses avaiable");
                        listView.setVisibility(View.GONE);
                        hidepDialog();
					/*Toast.makeText(getApplicationContext(),
							error.getMessage(), Toast.LENGTH_SHORT).show();*/
                    }


                });
                // Adding request to request queue
                queue.add(req);

                listView.setAdapter(rideAdapter);
            /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent booking_details = new Intent(MyRideActivity.this, BookingDetails.class);
                    booking_details.putExtra("bid",bid);
                    booking_details.putExtra("bdate",date);
                    booking_details.putExtra("source",src_address);
                    booking_details.putExtra("time",time);

                    booking_details.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(booking_details);
                    finish();
                }
            });*/

            }
       /* }catch(Exception e){
            generateNoteOnSD("My_ride_activity_log"+date+":"+hour+":"+minute+":"+seconds+":"+milliseconds, e.toString());

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
    private void showpDialog(){
        if (!pdialog.isShowing())
            pdialog.show();
    }

    private void hidepDialog(){
        if (pdialog.isShowing())
            pdialog.dismiss();
    }
}
