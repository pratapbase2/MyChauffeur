package com.mychauffeurapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mychauffeurapp.R;
import com.mychauffeurapp.app.MyVolley;
import com.mychauffeurapp.model.ButtonText;
import com.mychauffeurapp.model.MyEditTextView;
import com.mychauffeurapp.model.MyTextview;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Book_driver_and_car extends Activity {

    String urlJson = "http://54.169.81.215/mychauffeurapi/mcapi/bookdriverandcar";//54.251.178.3
    ButtonText driver_and_car;
    MyEditTextView driver_text;

    String driver;
    String text;

   /* Calendar mcurrentTime = Calendar.getInstance();
    final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    final int minute = mcurrentTime.get(Calendar.MINUTE);
    final int seconds=mcurrentTime.get(Calendar.SECOND);
    final int milliseconds=mcurrentTime.get(Calendar.MILLISECOND);
    final int date=mcurrentTime.get(Calendar.DATE);*/
    ProgressDialog pdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_driver_and_car);


        //this.getActionBar().setDisplayHomeAsUpEnabled(true);
        this.getActionBar().setDisplayShowCustomEnabled(true);
        this.getActionBar().setDisplayShowTitleEnabled(false);

        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.titleview, null);

        ((MyTextview) v.findViewById(R.id.title)).setText(this.getTitle());
        this.getActionBar().setCustomView(v);


        driver_and_car = (ButtonText) findViewById(R.id.driver_car);
        driver_text = (MyEditTextView) findViewById(R.id.driver_text);

        pdialog = new ProgressDialog(Book_driver_and_car.this, AlertDialog.THEME_HOLO_LIGHT);
        pdialog.setMessage("Loading ...");

        driver_and_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                driver = driver_text.getText().toString();


                showpDialog();
                RequestQueue queue = MyVolley.getRequestQueue();

                StringRequest postReq = new StringRequest(Request.Method.POST,
                        urlJson,
                        new Response.Listener<String>() {
                            public void onResponse(String response) {

                                Log.e("BookingInformation", response);
                                Log.e("The text typed{{{{{{{", driver);
                                try {
                                    JSONObject orderid1 = new JSONObject(response);

                                    JSONObject orderid = (JSONObject) orderid1.get("result");

                                    for (int i = 0; i < orderid.length(); i++) {
                                        try {
                                            text = orderid.getString("message");
                                            AlertDialog.Builder helpBuilder = new AlertDialog.Builder(Book_driver_and_car.this, AlertDialog.THEME_HOLO_LIGHT);
                                            helpBuilder.setTitle("Enquiry Submitted");
                                            helpBuilder.setMessage(text);
                                            hidepDialog();
                                            helpBuilder.setPositiveButton("OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                            Intent checkIntent = new Intent(Book_driver_and_car.this, SplashScreen.class);
                                                            startActivity(checkIntent);
                                                            finish();
                                                        }
                                                    });
                                            AlertDialog helpDialog = helpBuilder.create();
                                            helpDialog.show();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }


                                    //Toast.makeText(BookingInformation.this, ""+orderid.getString("message"), Toast.LENGTH_LONG).show();

                                    // hidepDialog();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error [" + error + "]");
                        hidepDialog();
                    }

                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        HashMap<String, String> params = new HashMap<>();
                        params.put("text_enq", driver);

                        return params;
                    }
                };

                queue.add(postReq);

            }
        });

    }




    private void showpDialog(){
        if (!pdialog.isShowing())
            pdialog.show();
    }

    private void hidepDialog(){
        if (pdialog.isShowing())
            pdialog.dismiss();
    }


}
