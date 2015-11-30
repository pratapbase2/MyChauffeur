package com.mychauffeurapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mychauffeurapp.R;
import com.mychauffeurapp.app.MyVolley;
import com.mychauffeurapp.common.AlertDialogManager;
import com.mychauffeurapp.common.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class ProfileActivity extends Activity {

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
    }*/
    private String urlJsonArry = "http://54.169.81.215/mychauffeurapi/mcapi/bookinghistory/";

    String hours, rupee_per_hour, charged_price,discount,discount_amt,total_amt;
   /* Calendar mcurrentTime = Calendar.getInstance();
    final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    final int minute = mcurrentTime.get(Calendar.MINUTE);
    final int seconds=mcurrentTime.get(Calendar.SECOND);
    final int milliseconds=mcurrentTime.get(Calendar.MILLISECOND);
    final int date=mcurrentTime.get(Calendar.DATE);*/
    AlertDialogManager alert = new AlertDialogManager();
    ConnectionDetector cd;

    String User_id;


    ProgressDialog pdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookinghistory);
       /* try {*/
            cd = new ConnectionDetector(this);
            Bundle extras = getIntent().getExtras();
            if (extras == null) {

            } else {

                User_id = extras.getString("UserId");
                //Toast.makeText(OrderFoodReceipt.this, "ORDER_ID "+ORDER_ID, Toast.LENGTH_SHORT).show();
            }


            pdialog = new ProgressDialog(ProfileActivity.this, AlertDialog.THEME_HOLO_LIGHT);
            pdialog.setMessage("Loading ...");

            if (!cd.isConnectingToInternet()) {
                alert.showAlertDialog(ProfileActivity.this,
                        "Internet Connection Issue",
                        "Please connect Internet connection and Try again..", false);
            } else {

                RequestQueue queue = MyVolley.getRequestQueue();

                JsonArrayRequest req = new JsonArrayRequest(urlJsonArry + User_id,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                showpDialog();
                                try {
                                    for (int i = 0; i < response.length(); i++) {

                                        JSONObject json = (JSONObject) response.get(i);
                                    /*discount = json.getString("bdiscount_code");
                                    discount_amt = json.getString("bfinal_cost");

                                    discount_code.setText(discount);
                                    discount_price.setText(discount_amt);*/

                                        Log.e("Booking history", discount + discount_amt);

                                    }

                                    Log.e("Booking history", discount + discount_amt);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                hidepDialog();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //hidepDialog();
                    }
                });

                // Adding request to request queue
                queue.add(req);
            }
        /*agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reviewIntent = new Intent(getApplicationContext(), Booking.class);
                //reviewIntent.putExtra("OrderId", ORDER_ID);
                startActivity(reviewIntent);
                finish();
            }
        });*/
      /*  }catch (Exception e) {
            generateNoteOnSD("Profile_activity_log"+date+":"+hour+":"+minute+":"+seconds+":"+milliseconds, e.toString());
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
