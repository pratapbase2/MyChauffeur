package com.mychauffeurapp.activity;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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
import com.mychauffeurapp.fragments.BookingFragment;
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

public class RatingActivity extends FragmentActivity {

    String jsonUrl = "http://54.169.81.215/mychauffeurapi/mcapi/addrating";
    private ButtonText review;
    private RatingBar ratingBar;
    String text_review,userid, bookingId,message;
    String date,time,unique_id;
    /*Calendar mcurrentTime = Calendar.getInstance();
    final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    final int minute = mcurrentTime.get(Calendar.MINUTE);
    final int seconds=mcurrentTime.get(Calendar.SECOND);
    final int milliseconds=mcurrentTime.get(Calendar.MILLISECOND);
    final int date1=mcurrentTime.get(Calendar.DATE);*/
    MyTextviews bookid, bdate, btime;

    RelativeLayout layout;
    Fragment frag;

    FrameLayout frameLayout;
    FragmentTransaction transaction ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating);
        /*try {*/
            this.getActionBar().setDisplayShowCustomEnabled(true);
            this.getActionBar().setDisplayShowTitleEnabled(false);

            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.titleview, null);

            ((MyTextview) v.findViewById(R.id.title)).setText(this.getTitle());
            this.getActionBar().setCustomView(v);

            addListenerOnRatingBar();
            addListenerOnButton();
            layout = (RelativeLayout) findViewById(R.id.layout);

            review = (ButtonText) findViewById(R.id.review);

            bookid = (MyTextviews) findViewById(R.id.booking_id);
            bdate = (MyTextviews) findViewById(R.id.date);
            btime = (MyTextviews) findViewById(R.id.time);
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                userid = extras.getString("UserId");
                bookingId = extras.getString("booking_id");
                time = extras.getString("duration");
                date = extras.getString("date");
                unique_id = extras.getString("uniqueId");

            }

            bookid.setText(unique_id);
            bdate.setText(date);
            btime.setText("for " + time + " hours");
        /*review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rateIntent = new Intent(RatingActivity.this, MainActivity.class);
                startActivity(rateIntent);
                RatingActivity.this.finish();
            }

        });*/
            LayerDrawable stars = (LayerDrawable) ratingBar
                    .getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.parseColor("#1e3e55"),
                    PorterDuff.Mode.SRC_ATOP); // for filled stars
        /*stars.getDrawable(1).setColorFilter(Color.parseColor("#FFFF00"),
                PorterDuff.Mode.SRC_ATOP); // for half filled stars*/
            stars.getDrawable(0).setColorFilter(Color.parseColor("#a0a0a0"),
                    PorterDuff.Mode.SRC_ATOP); // for empty stars

       /* }catch (Exception e){

        }*/
        Bundle bundle=new Bundle();
        bundle.putString("UserId", userid);
        Log.e("The Userid {{{{{{{{{{", userid);
        frag = new BookingFragment();
        frag.setArguments(bundle);
        frameLayout = (FrameLayout) findViewById(R.id.frame_container1);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container1, frag);
        //transaction.addToBackStack("Frag1");
        transaction.addToBackStack(null);
        transaction.commit();
        frameLayout.setVisibility(View.GONE);
    }

    public void addListenerOnRatingBar() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar2);
        //txtRatingValue = (TextView) findViewById(R.id.txtRatingValue);

        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                if(rating<1)
                    text_review = "0";
                else
                    text_review = String.valueOf(rating);

                //txtRatingValue.setText(String.valueOf(rating));

            }
        });
    }

    public void addListenerOnButton() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar2);
        review = (ButtonText) findViewById(R.id.review);

        //if click on me, then display the current rating value.
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                review.setVisibility(View.GONE);
                RequestQueue queue = MyVolley.getRequestQueue();

                StringRequest postReq = new StringRequest(Request.Method.POST,
                        jsonUrl,
                        new Response.Listener<String>() {
                            public void onResponse(String response) {
                                //Toast.makeText(getApplicationContext(), "response " + response, Toast.LENGTH_LONG).show();
                                Log.e("BookingInformation", response);
                                try {
                                    JSONObject orderid1 = new JSONObject(response);
                                    //JSONObject orderid = (JSONObject) orderid1.get("result");

                                    try {

                                        message = orderid1.getString("message");

                                        LayoutInflater inflater = getLayoutInflater();
                                        // Inflate the Layout
                                        View layout = inflater.inflate(R.layout.toast_drawable,
                                                (ViewGroup) findViewById(R.id.custom_toast_layout));

                                        MyTextviews text = (MyTextviews) layout.findViewById(R.id.textToShow);
                                        text.setText(message);
                                        text.setTextColor(Color.parseColor("#1e3e66"));
                                        Toast toast = new Toast(getApplicationContext());
                                        //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                        toast.setDuration(Toast.LENGTH_LONG);
                                        toast.setView(layout);
                                        toast.show();

                                        //Toast.makeText(RatingActivity.this, message, Toast.LENGTH_LONG).show();

                                        Intent checkIntent = new Intent(getApplicationContext(), MainActivity.class);
                                        checkIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        checkIntent.putExtra("UserId", userid);
                                        //checkIntent.putExtra("source", source);
                                        //Toast.makeText(RatingActivity.this,message + String.valueOf(ratingBar.getRating()), Toast.LENGTH_SHORT).show();
                                        startActivity(checkIntent);
                                        finish();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                       /* generateNoteOnSD("Rating_activity_log", e.toString());*/
                                        //Analytics catching exceptions
                                        App_VolleyExamples.getInstance().trackException(e);
                                    }
                                    //Toast.makeText(CheckoutFoodCart.this, "order_id " + order_id, Toast.LENGTH_SHORT).show();
                                    //String order_id = response;//new String(response.getBytes("order_id"));
                                    //}

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    /*generateNoteOnSD("Rating_activity_log"+new Date().getTime(), e.toString());*/

                                    //Analytics catching exceptions
                                    App_VolleyExamples.getInstance().trackException(e);
                                }
                                //hidepDialog();
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error [" + error + "]");
                        //hidepDialog();
                    }

                }) {

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {


                        HashMap<String, String> params = new HashMap<>();
                        params.put("bid", bookingId);
                        params.put("brating", text_review);
                        params.put("breview", "0");

                        //Log.e("BookingInformation", bsource_long + bsource_address + bdest_lat + bdest_long + bdest_address + bactual_cost + bsource_lat + btime + bdate + bperiod + uid + bmake_of_car + bage_of_vehicle + btype_of_insurance + breg_num + coupon_code + vtype + text_enq);


                        return params;
                    }
                };

                //AppController.getInstance().addToRequestQueue(postReq);
                queue.add(postReq);
            }
        });

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

        /*int fragments = getFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            // make layout invisible since last fragment will be removed
        }
        super.onBackPressed();*/

        /*int fragments = getFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            // make layout invisible since last fragment will be removed
            frameLayout.setVisibility(View.GONE);
        }
        super.onBackPressed();
*/
        /*Bundle bundle=new Bundle();
        bundle.putString("UserId", userid);
        Log.e("The Userid {{{{{{{{{{", userid);
        frag = new BookingFragment();
        frag.setArguments(bundle);*/
        /*if (getFragmentManager().findFragmentByTag("Frag1") != null) {
            getFragmentManager().popBackStackImmediate("Frag1", 0);

        }*/
        Intent in = new Intent(RatingActivity.this,MainActivity.class);
        in.putExtra("UserId", userid);
        startActivity(in);
        this.finish();

        /*if (getFragmentManager().getBackStackEntryCount() == 0) {
            //
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }*/
       // layout.setVisibility(View.GONE);
        super.onBackPressed();
        //frameLayout.setVisibility(View.VISIBLE);
            //FragmentTransaction transaction = getFragmentManager().beginTransaction();

        /*transaction.replace(R.id.frame_container1, frag);
        transaction.addToBackStack("Frag1");
        transaction.commit();*/
            //layout.setVisibility(View.GONE);
        //RatingActivity.this.finish();
           // super.onBackPressed();
            //RatingActivity.this.finish();
            //booking_history.setVisibility(View.GONE);

            //   RatingActivity.this.finish();
            //}


    }

}
