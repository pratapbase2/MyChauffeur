package com.mychauffeurapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mychauffeurapp.R;
import com.mychauffeurapp.app.App_VolleyExamples;
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

public class ForgotPasswordOtp extends Activity {

    private EditText mEtOTP;
    private Button mBtnVerify;
    private String mOTP,mUserid,mEmailid;
    String acterror1,actmassage1;

   /* Calendar mcurrentTime = Calendar.getInstance();
    final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    final int minute = mcurrentTime.get(Calendar.MINUTE);
    final int seconds=mcurrentTime.get(Calendar.SECOND);
    final int milliseconds=mcurrentTime.get(Calendar.MILLISECOND);
    final int date=mcurrentTime.get(Calendar.DATE);*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
      /*  try {*/
            this.getActionBar().setDisplayShowCustomEnabled(true);
            this.getActionBar().setDisplayShowTitleEnabled(false);

            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.titleview, null);

            ((MyTextview) v.findViewById(R.id.title)).setText(this.getTitle());
            this.getActionBar().setCustomView(v);
            mEtOTP = (EditText) findViewById(R.id.et_otp_num);
            mBtnVerify = (Button) findViewById(R.id.btn_otp_submit);

            mUserid = getIntent().getStringExtra("Userid");
            mEmailid = getIntent().getStringExtra("EmailiId");
            mBtnVerify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (mEtOTP.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(ForgotPasswordOtp.this, "Please enter the OTP!!!", Toast.LENGTH_LONG).show();

                    } else {
                        mOTP = mEtOTP.getText().toString();

                        //Toast.makeText(getApplicationContext(), "OTP : " + mOTP, Toast.LENGTH_LONG).show();
                        new Verify().execute();
                    }

                }
            });
       /* }catch (Exception e){
            generateNoteOnSD("Forgot_password_otp_log"+date+":"+hour+":"+minute+":"+seconds+":"+milliseconds, e.toString());
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

    class Verify extends AsyncTask<String, Void, String>
    {
        ProgressDialog pDialog;
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pDialog = new ProgressDialog(ForgotPasswordOtp.this);
            pDialog.setMessage("Verifying....");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub

            try {
                verify(mOTP,mUserid);
            }

            catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Analytics catching exceptions
                App_VolleyExamples.getInstance().trackException(e);
            }

            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            fuct_verification();
        }

    }


    public void verify(String code,String mUserid) throws JSONException {


        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://54.169.81.215/mychauffeurapi/mcapi/passwordactivate");

        try {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

            nameValuePairs.add(new BasicNameValuePair("user_id", mUserid));
            nameValuePairs.add(new BasicNameValuePair("verification", code));


            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

            String activestr = EntityUtils.toString(response.getEntity());

            Log.d("OTP JSON Response : ",activestr);
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
            //Toast.makeText(getApplicationContext(), actmassage1,Toast.LENGTH_SHORT).show();
            Intent Drin = new Intent(ForgotPasswordOtp.this,ResetPasswordActivity.class);

            Drin.putExtra("Userid", mUserid);
            Drin.putExtra("Emailid",mEmailid);
            startActivity(Drin);

            this.finish();

        } else
        {

            Toast.makeText(getApplicationContext(), actmassage1,Toast.LENGTH_SHORT).show();
        }


    }
}
