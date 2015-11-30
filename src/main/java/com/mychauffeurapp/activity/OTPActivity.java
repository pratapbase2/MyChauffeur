package com.mychauffeurapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mychauffeurapp.R;
import com.mychauffeurapp.app.App_VolleyExamples;
import com.mychauffeurapp.common.AppConstants;
import com.mychauffeurapp.model.MyTextview;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OTPActivity extends Activity implements View.OnClickListener{

    private EditText mEtOTP;
    private Button mBtnVerify;
    private String mOTP,mEmail;
    String userId;

  /*  Calendar mcurrentTime = Calendar.getInstance();
    final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    final int minute = mcurrentTime.get(Calendar.MINUTE);
    final int seconds=mcurrentTime.get(Calendar.SECOND);
    final int milliseconds=mcurrentTime.get(Calendar.MILLISECOND);
    final int date=mcurrentTime.get(Calendar.DATE);*/
    String acterror1,actmassage1;
    private static final String TAG = OTPActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
       /* try {*/
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_otp);

            //this.getActionBar().setDisplayHomeAsUpEnabled(true);
            this.getActionBar().setDisplayShowCustomEnabled(true);
            this.getActionBar().setDisplayShowTitleEnabled(false);

            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.titleview, null);

            ((MyTextview)v.findViewById(R.id.title)).setText(this.getTitle());
            this.getActionBar().setCustomView(v);

            mEtOTP = (EditText) findViewById(R.id.et_otp_num);
            mBtnVerify = (Button) findViewById(R.id.btn_otp_submit);

            userId = getIntent().getStringExtra("UserId");
            mEmail = getIntent().getStringExtra("Email");
            mBtnVerify.setOnClickListener(this);
     /*   }catch (Exception e){
            generateNoteOnSD("OTP_activity_log"+date+":"+hour+":"+minute+":"+seconds+":"+milliseconds, e.toString());
        }*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        deleteDatabase("MychauffeurAppProfile");

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

    private void startMainActivity(){
        SharedPreferences mPrefrences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor mEditor = mPrefrences.edit();
        mEditor.putBoolean(AppConstants.IS_USER_LOGGED_IN,true);
        mEditor.commit();
        Intent intent = new Intent();
        intent.setClass(this, Booking.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_otp_submit:

                App_VolleyExamples.getInstance().trackEvent("SignUpOTPButton", "Clicked", "Getting OTP for SignUp");
                if(mEtOTP.getText().toString().trim().equalsIgnoreCase("")){
                    Toast.makeText(OTPActivity.this, "Please enter the OTP!!!", Toast.LENGTH_LONG).show();

                }else
                {
                    mOTP = mEtOTP.getText().toString();
                    //Toast.makeText(getApplicationContext(), "OTP : " + mOTP +"" + mEmail, Toast.LENGTH_LONG).show();
                    new Verify().execute();
                }
                break;
        }
    }



    class Verify extends AsyncTask<String, Void, String>
    {
        ProgressDialog pDialog;
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(OTPActivity.this);
            pDialog.setMessage("Verifying....");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... arg0) {

            try {
                verify(mOTP,mEmail);
            }

            catch (JSONException e) {
                e.printStackTrace();
                //Analytics catching exceptions
                App_VolleyExamples.getInstance().trackException(e);
            }

            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();


            fuct_verification();
        }

    }


    public void verify(String code,String mEmail) throws JSONException {


        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://54.169.81.215/mychauffeurapi/mcapi/verification");

        try {

            List<NameValuePair> nameValuePairs = new ArrayList<>(2);

            nameValuePairs.add(new BasicNameValuePair("email", mEmail));
            nameValuePairs.add(new BasicNameValuePair("verification", code));


            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

            String activestr = EntityUtils.toString(response.getEntity());

            Log.d("OTP JSON Response : ", activestr);
            JSONObject actobj = new JSONObject(activestr);

            JSONObject objact = actobj.getJSONObject("result");
            acterror1 = objact.getString("error");

            actmassage1 = objact.getString("message");



        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }


    }

    public void fuct_verification()
    {
        if (acterror1.equalsIgnoreCase("false"))
        {
            Intent Drin = new Intent(OTPActivity.this,MainActivity.class);
            Drin.putExtra("UserId",userId);
            startActivity(Drin);

            this.finish();

        } else
        {
            deleteDatabase("MychauffeurAppProfile");
            Toast.makeText(getApplicationContext(), actmassage1, Toast.LENGTH_SHORT).show();
        }


    }


}
