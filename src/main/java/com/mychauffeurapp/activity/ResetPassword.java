package com.mychauffeurapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mychauffeurapp.R;
import com.mychauffeurapp.app.App_VolleyExamples;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;


public class ResetPassword extends Activity {/*

    Calendar mcurrentTime = Calendar.getInstance();
    final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    final int minute = mcurrentTime.get(Calendar.MINUTE);
    final int seconds=mcurrentTime.get(Calendar.SECOND);
    final int milliseconds=mcurrentTime.get(Calendar.MILLISECOND);
    final int date=mcurrentTime.get(Calendar.DATE);

        private Button reset;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.resetpassword);
            try {
                reset = (Button) findViewById(R.id.reset);

                reset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        App_VolleyExamples.getInstance().trackEvent("ResetPasswordButton", "Clicked", "Getting destination address ");
                        Intent passIntent = new Intent(ResetPassword.this, SplashScreen.class);
                        startActivity(passIntent);
                        ResetPassword.this.finish();
                    }
                });

            }catch (Exception e) {
                generateNoteOnSD("Reset_password_log"+date+":"+hour+":"+minute+":"+seconds+":"+milliseconds, e.toString());
            }
        }
    public void generateNoteOnSD(String sFileName, String sBody){
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
        }



