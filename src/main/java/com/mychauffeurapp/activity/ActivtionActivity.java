package com.mychauffeurapp.activity;

import com.mychauffeurapp.R;
import com.mychauffeurapp.app.App_VolleyExamples;
import com.mychauffeurapp.model.MyTextview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActivtionActivity  extends Activity implements View.OnClickListener{

    String acterror1, actmassage1;
    private Button mBtnVerify;
    private EditText mEtOTP, et_email;
    private String mOTP,mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activation_activity);

        //this.getActionBar().setDisplayHomeAsUpEnabled(true);
        this.getActionBar().setDisplayShowCustomEnabled(true);
        this.getActionBar().setDisplayShowTitleEnabled(false);


        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.titleview, null);

        ((MyTextview) v.findViewById(R.id.title)).setText(this.getTitle());
        this.getActionBar().setCustomView(v);

        et_email = (EditText) findViewById(R.id.et_em);
        mEtOTP = (EditText) findViewById(R.id.et_otp);
        mBtnVerify = (Button) findViewById(R.id.btn_submit);

        //mEmail = getIntent().getStringExtra("Email");
        mBtnVerify.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_submit:

                App_VolleyExamples.getInstance().trackEvent("verifybutton", "Clicked", "Getting OTP for SignUp");
                if(mEtOTP.getText().toString().trim().equalsIgnoreCase("")){
                    Toast.makeText(ActivtionActivity.this, "Please enter the OTP!!!", Toast.LENGTH_LONG).show();

                }else
                {
                    mEmail=et_email.getText().toString();
                    mOTP = mEtOTP.getText().toString();

                    //Toast.makeText(getApplicationContext(), "OTP : " + mOTP +"" + mEmail, Toast.LENGTH_LONG).show();
                    new Verify().execute();
                }
                break;
        }
    }



            class Verify extends AsyncTask<String, Void, String> {
                ProgressDialog pDialog;

                protected void onPreExecute() {
                    super.onPreExecute();
                    pDialog = new ProgressDialog(ActivtionActivity.this);
                    pDialog.setMessage("Verifying....");
                    pDialog.setCancelable(false);
                    pDialog.show();
                }

                @Override
                protected String doInBackground(String... arg0) {

                    try {
                        verify(mOTP, mEmail);
                    } catch (JSONException e) {
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

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

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
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivtionActivity.this);
            alertDialogBuilder.setMessage("Your account is activated, Please login");
            alertDialogBuilder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    /*if(doesDatabaseExist((ContextWrapper)getApplicationContext(),"MychauffeurAppProfile")) {
                        deleteDatabase("MychauffeurAppProfile");

                        if(!doesDatabaseExist((ContextWrapper)getApplicationContext(),"MychauffeurAppProfile")) {*/
                            Intent checkIntent = new Intent(getApplicationContext(), LoginScreen.class);
                            checkIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(checkIntent);
                            finish();
                        //}
                    //}


                }
            });
            /*alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    deleteDatabase("MychauffeurAppProfile");

                    Intent checkIntent = new Intent(getApplicationContext(), LoginScreen.class);
                    checkIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(checkIntent);
                    finish();
                }
            });
            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });*/

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            /*Intent Drin = new Intent(ActivtionActivity.this,LoginScreen.class);
            startActivity(Drin);
            Toast.makeText(ActivtionActivity.this, "Please login again", Toast.LENGTH_SHORT).show();
            this.finish();*/

        } else
        {
            Toast.makeText(getApplicationContext(), actmassage1, Toast.LENGTH_SHORT).show();
        }


    }

    private boolean doesDatabaseExist(ContextWrapper applicationContext,
                                      String string) {

        File dbFile = applicationContext.getDatabasePath(string);
        return dbFile.exists();
    }

}

