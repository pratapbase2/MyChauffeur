package com.mychauffeurapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mychauffeurapp.R;
import com.mychauffeurapp.app.App_VolleyExamples;
import com.mychauffeurapp.callbacks.Config;
import com.mychauffeurapp.common.AlertDialogManager;
import com.mychauffeurapp.common.ConnectionDetector;
import com.mychauffeurapp.common.SessionManager;
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
import java.util.HashMap;
import java.util.List;


public class SignUpActivity extends Activity implements View.OnClickListener{

    private EditText etName;
    private EditText etEmail;
    private EditText etPhoneNumber;
    private EditText etPassWord;
    private Button btnSignup;

    String   Name, Email,Password,Mobile,Deviceid;
    String error1,userid1,message1, name1,email1,mobile1;

    GoogleCloudMessaging gcm;
    Context context;
    String regId;
    // Session Manager Class
    SessionManager session;

   /* Calendar mcurrentTime = Calendar.getInstance();
    final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    final int minute = mcurrentTime.get(Calendar.MINUTE);
    final int seconds=mcurrentTime.get(Calendar.SECOND);
    final int milliseconds=mcurrentTime.get(Calendar.MILLISECOND);
    final int date=mcurrentTime.get(Calendar.DATE);*/
    //GCM Declarations
    private final String TAG = getClass().getSimpleName();

    AlertDialog.Builder dialog1;
    public static final String REG_ID = "regId";
    private static final String APP_VERSION = "appVersion";

    String Reg_id,App_ver;
    Dialog dialog;

    AlertDialogManager alert = new AlertDialogManager();
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        /*try {*/
            this.getActionBar().setDisplayShowCustomEnabled(true);
            this.getActionBar().setDisplayShowTitleEnabled(false);

            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.titleview, null);

            ((MyTextview) v.findViewById(R.id.title)).setText(this.getTitle());
            this.getActionBar().setCustomView(v);

            etName = (EditText) findViewById(R.id.name);
            etEmail = (EditText) findViewById(R.id.email_id);
            etPhoneNumber = (EditText) findViewById(R.id.phone_number);
            etPassWord = (EditText) findViewById(R.id.password);
            btnSignup = (Button) findViewById(R.id.sign_up_button);

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            context = getApplicationContext();

            // Session Manager
            session = new SessionManager(getApplicationContext());

            cd = new ConnectionDetector(getApplicationContext());


            if (!cd.isConnectingToInternet()) {
                alert.showAlertDialog(SignUpActivity.this,
                        "Internet Connection Issue",
                        "Please Check the Internet Connection", false);
            }

            //Google Cloud Messaging Functionality
            if (TextUtils.isEmpty(regId)) {
                regId = registerGCM();

            } else {
                Toast.makeText(getApplicationContext(),
                        "Already Registered with GCM Server!",
                        Toast.LENGTH_LONG).show();
            }

        /*etEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp2();
            }
        });*/

            etEmail.setClickable(true);

            etEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog1 = new AlertDialog.Builder(SignUpActivity.this);
                    dialog1.setMessage("Please enter a valid email id to verify your account.\n You will recieve a  OTP Verification Code in email  for verifying your account");
                    dialog1.setPositiveButton("Dismiss",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                }
            });
       /* etEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showPopUp2();
                return true; // return is important...
            }
        });*/
            btnSignup.setOnClickListener(this);

        /*btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent = new Intent(SignUpActivity.this,MainActivity.class);
                startActivity(signupIntent);
                SignUpActivity.this.finish();
            }
        });*/
       /* }catch (Exception e){
            generateNoteOnSD("Sign_up_activity_log"+date+":"+hour+":"+minute+":"+seconds+":"+milliseconds, e.toString());
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

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.sign_up_button:

                if(etEmail.getText().toString().equalsIgnoreCase("") && etName.getText().toString().equalsIgnoreCase("") && etPassWord.getText().toString().equalsIgnoreCase("") && etPhoneNumber.getText().toString().equalsIgnoreCase(""))
                {
                    etPassWord.requestFocus();
                    etPassWord.setError("Pls Enter Your Password ?");

                    etPhoneNumber.requestFocus();
                    etPhoneNumber.setError("Pls Enter Your Number ?");

                    etEmail.requestFocus();
                    etEmail.setError("Pls Enter Your Email ?");

                    etName.requestFocus();
                    etName.setError("Pls Enter Your Name ?");

                }
                else if(etEmail.getText().toString().equalsIgnoreCase("") )
                {
                    etEmail.requestFocus();
                    etEmail.setError("Pls Enter Your Email ?");

                }
                else if(etPassWord.getText().toString().equalsIgnoreCase(""))
                {
                    etPassWord.requestFocus();
                    etPassWord.setError("Pls Enter Your Password ?");
                }
                else if(etName.getText().toString().equalsIgnoreCase(""))
                {
                    etName.requestFocus();
                    etName.setError("Pls Enter Your Name ?");
                }
                else if(etPhoneNumber.getText().toString().equalsIgnoreCase(""))
                {
                    etPhoneNumber.requestFocus();
                    etPhoneNumber.setError("Pls Enter Your Number ?");
                }

                else
                {
                    if (!cd.isConnectingToInternet()) {
                        alert.showAlertDialog(SignUpActivity.this,
                                "Internet Connection Issue",
                                "Please Check the Internet Connection", false);
                    }else {
                        new Register().execute();
                    }
                }
                break;
        }
    }

    class Register extends AsyncTask<String, Void, String>
    {
        ProgressDialog pDialog;
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pDialog = new ProgressDialog(SignUpActivity.this);
            pDialog.setMessage("Signing up....");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... arg0)
        {
            // TODO Auto-generated method stub

            try {
                RegistrationData();
            } catch (JSONException e) {
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
            func_Register();

        }

    }

    public void RegistrationData() throws JSONException
    {
        Name = etName.getText().toString();
        Email = etEmail.getText().toString();
        Password = etPassWord.getText().toString();
        Mobile = etPhoneNumber.getText().toString();


        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://54.169.81.215/mychauffeurapi/mcapi/userregister");

        try {

            List<NameValuePair> nameValuePairs = new ArrayList<>(2);
            nameValuePairs.add(new BasicNameValuePair("name", Name));
            nameValuePairs.add(new BasicNameValuePair("email", Email));
            nameValuePairs.add(new BasicNameValuePair("password", Password));
            nameValuePairs.add(new BasicNameValuePair("mobile", Mobile));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);

            String str = EntityUtils.toString(response.getEntity());

            JSONObject jsonobj = new JSONObject(str);
            JSONObject objjson = jsonobj.getJSONObject("result");

            error1 = objjson.getString("error");
            message1 = objjson.getString("message");
            userid1 = objjson.getString("user_id");
            name1 = objjson.getString("name");
            email1 = objjson.getString("email");
            mobile1 = objjson.getString("mobile");

            session.createUserLoginSession(name1,userid1,email1,mobile1);

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }

    }


    public void func_Register()
    {
        if (error1 == "false")
        {
            //Creating Database
            SQLiteDatabase mydb;
            mydb=openOrCreateDatabase("MychauffeurAppProfile", 0, null);
            mydb.execSQL("create table if not exists MychauffeurAppTable" +
                    "(Login varchar(100),Password varchar(100),UserId varchar(50));");
            mydb.execSQL("insert into MychauffeurAppTable values ('"+Email.trim()
                    +"','"+Password.trim()+"','"+userid1+"');");

            //Toast.makeText(getApplicationContext(), message1,Toast.LENGTH_LONG).show();
            Intent otpIntent=new Intent(getApplicationContext(),OTPActivity.class);

            otpIntent.putExtra("Email", email1);

            startActivity(otpIntent);
            //finish();
            SignUpActivity.this.finish();
        }
        else
        {
            Toast.makeText(getApplicationContext(), message1,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(SignUpActivity.this, LoginScreen.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();

    }


    //GCM Functionality

    public String registerGCM() {

        gcm = GoogleCloudMessaging.getInstance(this);
        regId = getRegistrationId(context);

        if (TextUtils.isEmpty(regId)) {

            registerInBackground();

            Log.d("RegisterActivity",
                    "registerGCM - successfully registered with GCM server - regId: "
                            + regId);
        } else {
            Toast.makeText(getApplicationContext(),
                    "RegId already available. RegId: " + regId,
                    Toast.LENGTH_LONG).show();
        }
        return regId;
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getSharedPreferences(
                MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        String registrationId = prefs.getString(REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("RegisterActivity","I never expected this! Going down, going down!" + e);
            throw new RuntimeException(e);
        }
    }



    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regId = gcm.register(Config.GOOGLE_PROJECT_ID);
                    Log.d("RegisterActivity", "registerInBackground - regId: "
                            + regId);
                    msg = "Device registered, registration ID=" + regId;


                    //Getting App Version

                    int appVersion = getAppVersion(context);

                    String str_appVersion=String.valueOf(appVersion);

                    Log.d("RegisterActivity", "GCM RegId: " + regId);
                    Log.i(TAG, "Saving regId on app version " + appVersion);


                    //Storing Device-Id and App Version in Session

                    session.createGCMSession(regId,str_appVersion);

                    getuserdata();

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.d("RegisterActivity", "Error: " + msg);
                }
                Log.d("RegisterActivity", "AsyncTask completed: " + msg);
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                // Toast.makeText(getApplicationContext(),"Registered with GCM Server." + msg, Toast.LENGTH_LONG) .show();
            }
        }.execute(null, null, null);
    }


    public void getuserdata(){

        // get user data from session
        HashMap<String, String> user = session.getGCMDetails();

        // Reg-id
        Reg_id = user.get(SessionManager.KEY_REGID);

        // App version
        App_ver = user.get(SessionManager.KEY_APP_VER);

        Log.d("Register-ID", "Fetched from Session: " + Reg_id);
        Log.d("Register-App-ver", "Fetched from Session: " + App_ver);


    }


}