package com.mychauffeurapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mychauffeurapp.R;
import com.mychauffeurapp.app.App_VolleyExamples;
import com.mychauffeurapp.common.AlertDialogManager;
import com.mychauffeurapp.common.ConnectionDetector;
import com.mychauffeurapp.common.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.mychauffeurapp.model.MyTextview;

import khandroid.ext.apache.http.HttpResponse;
import khandroid.ext.apache.http.NameValuePair;
import khandroid.ext.apache.http.client.ClientProtocolException;
import khandroid.ext.apache.http.client.HttpClient;
import khandroid.ext.apache.http.client.entity.UrlEncodedFormEntity;
import khandroid.ext.apache.http.client.methods.HttpPost;
import khandroid.ext.apache.http.impl.client.DefaultHttpClient;
import khandroid.ext.apache.http.message.BasicNameValuePair;
import khandroid.ext.apache.http.util.EntityUtils;

public class LoginScreen extends Activity implements View.OnClickListener{

    private ImageView btnSignUp;
    private ImageView btnLogin;
    private EditText etEmail;
    private EditText etPassword;
    private MyTextview textHaveOTP;

   /* Calendar mcurrentTime = Calendar.getInstance();
    final int seconds=mcurrentTime.get(Calendar.SECOND);
    final int milliseconds=mcurrentTime.get(Calendar.MILLISECOND);
    final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    final int minute = mcurrentTime.get(Calendar.MINUTE);
    final int date=mcurrentTime.get(Calendar.DATE);
    */

    ImageView tv_forgot_pwd;
    String error1, message1, mEmail, mPassword;

    String name1, userid1, email1, mobile1;

    Context context;
    String regId;
    // Session Manager Class
    //SessionManager session;
    String Reg_id, App_ver;

    //private Activity activity;
    private ProgressDialog pDialog;
    private final String TAG = getClass().getSimpleName();

    public static final String REG_ID = "regId";
    private static final String APP_VERSION = "appVersion";

    AlertDialogManager alert = new AlertDialogManager();
    ConnectionDetector cd;

    SQLiteOpenHelper dbHelper;
    SQLiteDatabase mydb;

    String userName = null;
    String userPassword = null;
    String user_id=null;
    ProgressDialog dlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        //this.getActionBar().setDisplayHomeAsUpEnabled(true);
        //this.getActionBar().setDisplayShowCustomEnabled(true);
        //this.getActionBar().setDisplayShowTitleEnabled(false);

        /*LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.titleview, null);

        ((MyTextview)v.findViewById(R.id.title)).setText(this.getTitle());
        this.getActionBar().setCustomView(v);*/

            cd = new ConnectionDetector(getApplicationContext());
            context = getApplicationContext();
            //session = new SessionManager(getApplicationContext());

            //setContentView(R.layout.fragment_login);
            tv_forgot_pwd = (ImageView) findViewById(R.id.forgotpassword);
            //mBtnGoogleLogin = (SignInButton) view.findViewById(R.id.sign_in_button);
            btnSignUp = (ImageView) findViewById(R.id.signup_email_button);
            btnLogin = (ImageView) findViewById(R.id.signin_email_button);
            etEmail = (EditText) findViewById(R.id.email_id);
            etPassword = (EditText) findViewById(R.id.password);
            textHaveOTP= (MyTextview) findViewById(R.id.textHaveOTP);

        dlg = new ProgressDialog(LoginScreen.this, AlertDialog.THEME_HOLO_LIGHT);
            dlg.setMessage("Loading ...");

            if (!cd.isConnectingToInternet()) {
                AlertDialog.Builder helpBuilder = new AlertDialog.Builder(LoginScreen.this, AlertDialog.THEME_HOLO_LIGHT);
                helpBuilder.setTitle("Internet Connection Issue");
                helpBuilder.setMessage("Please Check the Internet Connection");
                dlg.dismiss();
                helpBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent checkIntent = new Intent(LoginScreen.this, SplashScreen.class);
                                startActivity(checkIntent);
                                finish();
                            }
                        });
                AlertDialog helpDialog = helpBuilder.create();
                helpDialog.show();
           /* alert.showAlertDialog(LoginScreen.this,
                    "Internet Connection Issue",
                    "Please Check the Internet Connection", false);*/
            /*dlg.dismiss();*/
            /*Intent in = new Intent(LoginScreen.this,SplashScreen.class);
            startActivity(in);
            finish();*/
            /*alert.showAlertDialog(LoginScreen.this,
                    "Internet Connection Issue",
                    "Please Check the Internet Connection", false);*/
            }

            if (doesDatabaseExist((ContextWrapper) getApplicationContext(), "MychauffeurAppProfile")) {

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

                    mEmail = cursor.getString(0);
                    mPassword = cursor.getString(1);
                    user_id = cursor.getString(2);

                    cursor.moveToNext();

                }
                cursor.close();
                dbHelper.close();
            /*ProgressDialog dlg = new ProgressDialog(LoginScreen.this);
            dlg.setMessage("Logging in...");
            dlg.show();*/


                new alreadyLogin().execute();


            } else {

                etEmail.setVisibility(View.VISIBLE);
                etPassword.setVisibility(View.VISIBLE);
                btnSignUp.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.VISIBLE);
                tv_forgot_pwd.setVisibility(View.VISIBLE);
                textHaveOTP.setVisibility(View.VISIBLE);

                btnSignUp.setOnClickListener(this);
                btnLogin.setOnClickListener(this);
                tv_forgot_pwd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(LoginScreen.this, ForgotPassword.class);
                        startActivity(intent);

                    }
                });
                textHaveOTP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(LoginScreen.this, ActivtionActivity.class));
                    }
                });
            }
        /*}catch (Exception e)
        {
            generateNoteOnSD("LoginScreen_log"+date+":"+hour+":"+minute+":"+seconds+":"+milliseconds, e.toString());
        }*/
    }

  /*  public void generateNoteOnSD(String sFileName, String sBody){
        try
        {
            File root = new File(Environment.getExternalStorageDirectory(), "MychauffeurLogs");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            //Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            /*case R.id.sign_in_button:

                //Google plus sign in
                showDialog(true);
                mSignInClicked = true;
                mGoogleApiClient.connect();
                break;*/
            case R.id.signup_email_button:
                hideKayboard();
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                //intent.setClass(activity, SignUpActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.signin_email_button:

                // if(isEmailValid(etEmail.getText().toString())) {
                if (etEmail.getText().toString().equalsIgnoreCase("") && etPassword.getText().toString().equalsIgnoreCase("")) {
                    etEmail.requestFocus();
                    etEmail.setError("Pls Enter Your Email ?");

                    etPassword.requestFocus();
                    etPassword.setError("Pls Enter Your Password ?");
                } else if (etEmail.getText().toString().equalsIgnoreCase("")) {
                    etEmail.requestFocus();
                    etEmail.setError("Pls Enter Your Email ?");

                } else if (etPassword.getText().toString().equalsIgnoreCase("")) {
                    etPassword.requestFocus();
                    etPassword.setError("Please enter Your Password");

                } else if (isEmailValid(etEmail.getText().toString())) {

                    if (!cd.isConnectingToInternet()) {
                        alert.showAlertDialog(LoginScreen.this,
                                "Internet Connection Issue",
                                "Please Check the Internet Connection", false);
                    } else {
                        hideKayboard();
                        new Login().execute();
                    }
                } else {
                    etEmail.setError("Pls correct your Email");
                }

                break;

        }
    }

    class alreadyLogin extends AsyncTask<String, Void, String> {
        //ProgressDialog pDialog;

        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dlg.setMessage("Logging in...");
            dlg.setCancelable(false);



            dlg.show();

            /*pDialog = new ProgressDialog(LoginScreen.this);
            pDialog.setMessage("Logging In...");
            pDialog.setProgressStyle(ProgressDialog.THEME_HOLO_LIGHT);
            pDialog.setCancelable(false);
            pDialog.show();*/
        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub

            try {
                alreadyLoginData();
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
            /*if (pDialog.isShowing())
                pDialog.dismiss();*/
            Login_func();
        }
    }

    public void alreadyLoginData() throws JSONException {

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://54.169.81.215/mychauffeurapi/mcapi/userlogin");
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<>(2);
            Log.e("LoginScreen",mEmail+"\n"+mPassword);
            nameValuePairs.add(new BasicNameValuePair("email", mEmail));
            nameValuePairs.add(new BasicNameValuePair("password", mPassword));

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

            // String name,String userid,String email,String mobile
            //session.createUserLoginSession(name1, userid1, email1, mobile1);

        } catch (ClientProtocolException e) {
            //Toast.makeText(LoginScreen.this, "Email or Password does not match", Toast.LENGTH_SHORT).show();
            Log.e(TAG, e + " ClientProtocolException");
        } catch (IOException e) {
            // Toast.makeText(LoginScreen.this, "Email or Password does not match", Toast.LENGTH_SHORT).show();
            Log.e(TAG, e + " IOException");
        }

    }

    class Login extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;

        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginScreen.this,AlertDialog.THEME_HOLO_LIGHT);
            pDialog.setMessage("Logging In...");

            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub

            try {
                loginData();
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
            Login_func();
        }
    }

    public void loginData() throws JSONException {

        mEmail = etEmail.getText().toString();
        mPassword = etPassword.getText().toString();

        //etEmail.
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://54.169.81.215/mychauffeurapi/mcapi/userlogin");
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<>(2);
            nameValuePairs.add(new BasicNameValuePair("email", mEmail));
            nameValuePairs.add(new BasicNameValuePair("password", mPassword));

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

            // String name,String userid,String email,String mobile
            //session.createUserLoginSession(name1, userid1, email1, mobile1);

        } catch (ClientProtocolException e) {
            //Toast.makeText(LoginScreen.this, "Email or Password does not match", Toast.LENGTH_SHORT).show();
            Log.e(TAG,e+" ClientProtocolException");
        } catch (IOException e) {
            //Toast.makeText(LoginScreen.this, "Email or Password does not match", Toast.LENGTH_SHORT).show();
            Log.e(TAG, e + " IOException");
        }

    }

    public boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }

    public void Login_func() {

        if (error1==("false")) {

            //Creating Database
            SQLiteDatabase mydb;
            mydb=openOrCreateDatabase("MychauffeurAppProfile", 0, null);
            mydb.execSQL("create table if not exists MychauffeurAppTable" +
                    "(Login varchar(100),Password varchar(100),UserId varchar(50));");
            mydb.execSQL("insert into MychauffeurAppTable values ('"+mEmail.trim()
                    +"','"+mPassword.trim()+"','"+userid1+"');");

            /*long delayInMillis = 10000;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (dlg != null) {
                        if (dlg.isShowing()) {
                            dlg.dismiss();
                        }
                        dlg = null;
                    }
                }
            }, delayInMillis);*/


            Intent in = new Intent(LoginScreen.this, MainActivity.class);
            in.putExtra("UserId",userid1);
            in.putExtra("Email",mEmail.trim());
            startActivity(in);
            finish();
        } else {
            Log.e("LoginScreen", error1 + "");
            if (!cd.isConnectingToInternet()) {
                /*alert.showAlertDialog(LoginScreen.this,
                        "Internet Connection Issue",
                        "Please Enable your Internet Connection", false);*/
                Toast.makeText(getApplicationContext(), "Please Enable your Internet Connection", Toast.LENGTH_LONG).show();
                dlg.dismiss();
            }
            else {
                Toast.makeText(getApplicationContext(), message1, Toast.LENGTH_LONG).show();
                if (dlg.isShowing())
                    dlg.dismiss();
            }
            //Toast.makeText(getApplicationContext(), error1,Toast.LENGTH_LONG).show();
            //Toast.makeText(LoginScreen.this, "Email or Password does not match", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (dlg.isShowing())
            dlg.dismiss();

        finish();

    }

    private boolean doesDatabaseExist(ContextWrapper applicationContext,
                                      String string) {

        File dbFile = applicationContext.getDatabasePath(string);
        return dbFile.exists();
    }

    private void hideKayboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(etEmail.getWindowToken(), 0);
        inputManager.hideSoftInputFromWindow(etPassword.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {

        if (dlg.isShowing())
            dlg.dismiss();

        Intent in = new Intent(LoginScreen.this,SplashScreen.class);
        //in.putExtra("UserId",userid);
        startActivity(in);
        finish();
    }
}
