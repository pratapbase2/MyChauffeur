package com.mychauffeurapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
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
import com.mychauffeurapp.app.App_VolleyExamples;
import com.mychauffeurapp.app.MyVolley;
import com.mychauffeurapp.model.ButtonText;
import com.mychauffeurapp.model.MyTextview;
import com.mychauffeurapp.model.MyTextviews;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BookingInformation extends Activity {

    String bsource_long,bsource_address,bdest_lat,bdest_long,bdest_address,bactual_cost,bsource_lat,btime,bdate,bperiod,uid;

    String final_cost, discount_price,status;
    Calendar mcurrentTime = Calendar.getInstance();
    String urlJson="http://54.169.81.215/mychauffeurapi/mcapi/driverbooking";

    String unique_id,source,destination,duration,cost,date;
    String breg_num, bmake_of_car,btype_of_insurance,bage_of_vehicle, vtype, text_enq;

    private MyTextview code;
    private ButtonText button2;

    String coupon_code="0";
    String userid;

    SQLiteOpenHelper dbHelper;
    SQLiteDatabase mydb;

    MyTextviews textCost,textDuration,textView16,textView18;

    ProgressDialog pdialog;
    String months;
    String years ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookinginformation);

            //this.getActionBar().setDisplayHomeAsUpEnabled(true);
            this.getActionBar().setDisplayShowCustomEnabled(true);
            this.getActionBar().setDisplayShowTitleEnabled(false);
            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.titleview, null);

            ((MyTextview) v.findViewById(R.id.title)).setText(this.getTitle());
            this.getActionBar().setCustomView(v);

            code = (MyTextview) findViewById(R.id.code);
            button2 = (ButtonText) findViewById(R.id.button2);

            textCost = (MyTextviews) findViewById(R.id.textCost);
            textDuration = (MyTextviews) findViewById(R.id.textDuration);
            textView16 = (MyTextviews) findViewById(R.id.textView16);
            textView18 = (MyTextviews) findViewById(R.id.textView18);

            pdialog = new ProgressDialog(BookingInformation.this, AlertDialog.THEME_HOLO_LIGHT);
            pdialog.setMessage("Loading ...");

            dbHelper = new SQLiteOpenHelper(getApplicationContext(), "MychauffeurAppProfile", null, 1) {

                @Override
                public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

                }

                @Override
                public void onCreate(SQLiteDatabase db) {

                }
            };

            String col[] = {"Login", "Password", "UserId"};

            Cursor cursor = dbHelper.getReadableDatabase().query("MychauffeurAppTable", col, null, null, null, null, null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                String mEmail = cursor.getString(0);
                String mPassword = cursor.getString(1);
                uid = cursor.getString(2);

                cursor.moveToNext();

            }
            cursor.close();
            dbHelper.close();


            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                textCost.setText("Rs. " + extras.getString("Cost"));
                textDuration.setText(extras.getString("Time") + " hrs");
                textView16.setText(extras.getString("SAddress"));
                textView18.setText(extras.getString("DAddress"));
                bsource_address = extras.getString("SAddress");
                bdest_long = extras.getString("dlong");
                bdest_address = extras.getString("DAddress");
                bperiod = extras.getString("Time");
                bactual_cost = extras.getString("Cost");
                bsource_lat = extras.getString("slat");
                btime = extras.getString("time");
                bdate = extras.getString("dates");
                months = extras.getString("months");
                years = extras.getString("years");
                bsource_long = extras.getString("slong");
                bdest_lat = extras.getString("dlat");
                userid = extras.getString("UserId");

                breg_num = extras.getString("reg_num");
                bmake_of_car = extras.getString("make_of_car");
                btype_of_insurance = extras.getString("type_of_insurance");
                bage_of_vehicle = extras.getString("age_of_vehicle");

                vtype = extras.getString("bvehicle");
                text_enq = extras.getString("bvehicle_enq");

                String date = years + "-" + months + "-" + bdate;
                bdate = date;

                //Toast.makeText(BookingInformation.this, vtype + text_enq , Toast.LENGTH_SHORT).show();

            }


            code.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent bookIntent = new Intent(BookingInformation.this, OfferActivity.class);
                    startActivityForResult(bookIntent, 1);
                    //BookingInformation.this.finish();

                }
            });

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    button2.setVisibility(View.GONE);


                    pdialog.setIndeterminate(false);
                    showpDialog();
                    RequestQueue queue = MyVolley.getRequestQueue();

                    App_VolleyExamples.getInstance().trackEvent("Button", "Clicked", "Confirm Booking information");

                    StringRequest postReq = new StringRequest(Request.Method.POST,
                            urlJson,
                            new Response.Listener<String>() {
                                public void onResponse(String response) {
                                    //Toast.makeText(getApplicationContext(), "response " + response, Toast.LENGTH_LONG).show();
                                    Log.e("BookingInformation", response);
                                    try {
                                        JSONObject orderid1 = new JSONObject(response);
                                        JSONObject orderid = (JSONObject) orderid1.get("result");

                                        try {

                                            unique_id = orderid.getString("unique_id");
                                            source = orderid.getString("src_addrs");
                                            destination = orderid.getString("dest_addr");
                                            duration = orderid.getString("duration");
                                            final_cost = orderid.getString("cost");
                                            date = orderid.getString("date");
                                            cost = orderid.getString("actual_cost");
                                            discount_price = orderid.getString("discount_cost");
                                            status = orderid.getString("status");

                                            //Toast.makeText(BookingInformation.this, ""+orderid.getString("message"), Toast.LENGTH_LONG).show();

                                            Intent checkIntent = new Intent(getApplicationContext(), BookingDetails.class);
                                            checkIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            checkIntent.putExtra("unique_id", unique_id);
                                            checkIntent.putExtra("source", source);
                                            checkIntent.putExtra("destination", destination);
                                            checkIntent.putExtra("duration", duration);
                                            checkIntent.putExtra("actual_cost", cost);
                                            checkIntent.putExtra("final_cost", final_cost);
                                            checkIntent.putExtra("discount_price", discount_price);
                                            checkIntent.putExtra("date", date);
                                            checkIntent.putExtra("status", status);
                                            checkIntent.putExtra("UserId", userid);
                                            //Toast.makeText(BookingInformation.this, userid, Toast.LENGTH_SHORT).show();
                                            startActivity(checkIntent);
                                            finish();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            //Analytics catching exceptions
                                            App_VolleyExamples.getInstance().trackException(e);
                                        }
                                        //Toast.makeText(CheckoutFoodCart.this, "order_id " + order_id, Toast.LENGTH_SHORT).show();
                                        //String order_id = response;//new String(response.getBytes("order_id"));
                                        //}

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
                            System.out.println("Error [" + error + "]");
                            hidepDialog();
                        }

                    }) {

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            if (bperiod.length() == 1) {
                                bperiod = "0" + bperiod;
                            }

                            HashMap<String, String> params = new HashMap<>();
                            params.put("bdate", bdate);
                            params.put("btime", btime);
                            params.put("bperiod", bperiod);
                            params.put("bslot", "0");
                            params.put("bsource_lat", bsource_lat);
                            params.put("bsource_long", bsource_long);
                            params.put("bsource_address", bsource_address);
                            params.put("bdest_lat", bdest_lat);
                            params.put("bdest_long", bdest_long);
                            params.put("bdest_address", bdest_address);
                            params.put("uid", uid);
                            params.put("bcar_reg_num", breg_num);
                            params.put("bdiscount_code", coupon_code);
                            params.put("bdiscount_id", "0");
                            params.put("bdiscount_price", "0");
                            params.put("bdiscount_percentage", "0");
                            params.put("bactual_cost", bactual_cost);
                            params.put("bfinal_cost", "0");
                            params.put("bstatus", "0");
                            params.put("bdriver_id", "0");
                            params.put("breview_rating", "0");
                            params.put("bcomments", "0");
                            params.put("bdriver_rating", "0");
                            params.put("bcar_make", bmake_of_car);
                            params.put("bcar_age", bage_of_vehicle);
                            params.put("bcar_insurance", btype_of_insurance);

                            params.put("bvehicle", vtype);
                            params.put("bvehicle_enq", text_enq);

                            Log.e("BookingInformation", bsource_long + bsource_address + bdest_lat + bdest_long + bdest_address + bactual_cost + bsource_lat + btime + bdate + bperiod + uid + bmake_of_car + bage_of_vehicle + btype_of_insurance + breg_num + coupon_code + vtype + text_enq);


                            return params;
                        }
                    };

                    //AppController.getInstance().addToRequestQueue(postReq);
                    queue.add(postReq);



                /*Intent bookIntent = new Intent(BookingInformation.this, SuccessfulActivity.class);
                startActivity(bookIntent);
                BookingInformation.this.finish();*/

                }
            });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            //code.setText("");
            coupon_code = data.getStringExtra("CouponCode");
           /* if(coupon_code.equals(null)){
                coupon_code="0";
            }
            Log.e("BookingInformation", coupon_code);
            Toast.makeText(BookingInformation.this, "coupon code "+coupon_code , Toast.LENGTH_SHORT).show();
            code.setText("Coupon applied : " + coupon_code + " | Change coupon");
            code.setTextColor(Color.rgb(30, 62, 102));
*/
            /*if (coupon_code.equals("0")) {
                coupon_code = "0";
                code.setText("No coupon | Add coupon if any");
                code.setTextColor(Color.rgb(30, 62, 102));
            } else {*/
                Log.e("BookingInformation", coupon_code);
                code.setText("Coupon applied : " + coupon_code + " | Change coupon");
                code.setTextColor(Color.rgb(30, 62, 102));
            //}
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

    @Override
    public void onBackPressed() {
        if (getFragmentManager().findFragmentByTag("Frag1") != null) {
            getFragmentManager().popBackStackImmediate("Frag1",0);
        } else {
            Intent in = new Intent(BookingInformation.this,Vehicle_details_tab.class);
            in.putExtra("UserId",userid);
            in.putExtra("Cost",bactual_cost);
            in.putExtra("Time",bperiod);
            in.putExtra("SAddress",bsource_address);
            in.putExtra("DAddress",bdest_address);
            in.putExtra("slat",bsource_lat);
            in.putExtra("slong",bsource_long);
            in.putExtra("dlat",bdest_lat);
            in.putExtra("dlong",bdest_long);
            in.putExtra("time",btime);
            in.putExtra("dates",bdate);
            in.putExtra("months",months);
            in.putExtra("years",years);
            /*bookIntent.putExtra("Cost", estSDistanceCost);
            bookIntent.putExtra("Time", durationText.getText().toString());
            bookIntent.putExtra("SAddress", sname);
            bookIntent.putExtra("DAddress", destination);

            bookIntent.putExtra("slat", slat);
            bookIntent.putExtra("slong", slong);
            bookIntent.putExtra("dlat", dlat);
            bookIntent.putExtra("dlong", dlong);
            bookIntent.putExtra("time", time);
            bookIntent.putExtra("dates", dates);
            bookIntent.putExtra("months", months);
            bookIntent.putExtra("years", years);*/

            startActivity(in);
            finish();
        }
    }
}

