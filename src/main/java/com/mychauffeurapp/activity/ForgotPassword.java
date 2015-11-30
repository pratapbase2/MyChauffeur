package com.mychauffeurapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mychauffeurapp.R;
import com.mychauffeurapp.app.App_VolleyExamples;
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
import java.util.Timer;
import java.util.TimerTask;

public class ForgotPassword extends Activity {

    EditText et_email;
    Button btn_submit_email;
    String email;
    ProgressDialog pdialog;
    String acterror1,actmassage1,actuserid;
   /* Calendar mcurrentTime = Calendar.getInstance();
    final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    final int minute = mcurrentTime.get(Calendar.MINUTE);
    final int seconds=mcurrentTime.get(Calendar.SECOND);
    final int milliseconds=mcurrentTime.get(Calendar.MILLISECOND);
    final int date=mcurrentTime.get(Calendar.DATE);*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpassword);
       /* try {*/
            //this.getActionBar().setDisplayHomeAsUpEnabled(true);
            this.getActionBar().setDisplayShowCustomEnabled(true);
            this.getActionBar().setDisplayShowTitleEnabled(false);
            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.titleview, null);

            ((MyTextview) v.findViewById(R.id.title)).setText(this.getTitle());
            this.getActionBar().setCustomView(v);

            et_email = (EditText) findViewById(R.id.et_email);
            btn_submit_email = (Button) findViewById(R.id.forgot);

            btn_submit_email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    email = et_email.getText().toString();
                    if (et_email.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(ForgotPassword.this, "Please enter a valid Email", Toast.LENGTH_LONG).show();

                    } else {
                        BigDeal();
                    }
                }
            });

        /*btn_submit_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent passIntent = new Intent(ForgotPassword.this, ResetPassword.class);
                startActivity(passIntent);
                ForgotPassword.this.finish();
            }
        });*/

       /* }catch (Exception e) {
            generateNoteOnSD("Forgot_password_log"+date+":"+hour+":"+minute+":"+seconds+":"+milliseconds, e.toString());

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

    public void BigDeal() {
        pdialog = new ProgressDialog(ForgotPassword.this);
        pdialog.setMessage("Loading ...");

        long delayInMillis = 2000;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(pdialog !=null)
                {
                    if(pdialog.isShowing())
                    {
                        pdialog.dismiss();
                    }
                    pdialog=null;
                }
            }
        }, delayInMillis);


        RequestQueue rq = Volley.newRequestQueue(ForgotPassword.this);
			/*
			 * JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
			 * url, null, new Response.Listener<JSONObject>() {
			 */
        StringRequest postReq = new StringRequest(
                Request.Method.POST,
                "http://54.169.81.215/mychauffeurapi/mcapi/forgotuserpassword",
                new Response.Listener<String>() {

                    public void onResponse(String response) {
                        //	pdialog.dismiss();
                        // parseJSON(response);

                        Log.d("JSON POI Response", "{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{");
                        Log.d("JSON POI Response", response);

                        // BigList.clear();
                        Log.d("response",response);
                        JsonParse(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error [" + error + "]");

            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                // Map<String, String> params = new HashMap<String, String>();
                headers.put("email", email);  // User_id
                headers.put("flag", "1");

                return headers;
            }

        };

        rq.add(postReq);

        //AppController.getInstance().addToRequestQueue(postReq);

    }

    public void JsonParse(String page) {

        try {
            JSONObject actobj = new JSONObject(page);

            JSONObject objact = actobj.getJSONObject("result");
            acterror1 = objact.getString("error");
            actuserid = objact.getString("user_id");
            actmassage1 = objact.getString("message");

            Log.d("Json", acterror1 + actmassage1 + actmassage1);

        } catch (JSONException e) {
            e.printStackTrace();
            //Analytics catching exceptions
            App_VolleyExamples.getInstance().trackException(e);
        }

        Fun_POI_list();
    }

    public void Fun_POI_list(){

        if (acterror1.equalsIgnoreCase("false"))
        {
            Toast.makeText(getApplicationContext(), "Please check your email for OTP",Toast.LENGTH_SHORT).show();
            Intent Drin = new Intent(ForgotPassword.this,ForgotPasswordOtp.class);

            Drin.putExtra("Userid", actuserid);
            Drin.putExtra("EmailiId",email);
            startActivity(Drin);

            this.finish();

        } else
        {
            Toast.makeText(getApplicationContext(), "The Email id is Invalid Pls Enter Correct ID", Toast.LENGTH_SHORT).show();
        }
    }
}



