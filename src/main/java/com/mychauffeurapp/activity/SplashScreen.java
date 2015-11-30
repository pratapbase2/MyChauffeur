package com.mychauffeurapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mychauffeurapp.R;
import com.mychauffeurapp.model.ButtonText;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class SplashScreen extends Activity {
    private  Button Splash;
   /* Calendar mcurrentTime = Calendar.getInstance();
    final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    final int minute = mcurrentTime.get(Calendar.MINUTE);
    final int seconds=mcurrentTime.get(Calendar.SECOND);
    final int milliseconds=mcurrentTime.get(Calendar.MILLISECOND);
    final int date=mcurrentTime.get(Calendar.DATE);*/
    private ButtonText driver_car, driver_van;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        /*try {*/
        /*driver_car = (ButtonText) findViewById(R.id.car);
        driver_van = (ButtonText) findViewById(R.id.van);
*/
            Splash = (Button) findViewById(R.id.start);

            Splash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent openIntent = new Intent(SplashScreen.this, LoginScreen.class);
                    startActivity(openIntent);
                    SplashScreen.this.finish();


                }
            });
            ;
        /*driver_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(SplashScreen.this,Book_driver_and_car.class);
                startActivity(in);
                SplashScreen.this.finish();
            }
        });

        driver_van.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(SplashScreen.this,Book_driver_and_van.class);
                startActivity(in);
                SplashScreen.this.finish();
            }
        });*/
        /*}catch (Exception e){
            generateNoteOnSD("Splash_screen_log"+date+":"+hour+":"+minute+":"+seconds+":"+milliseconds, e.toString());
        }*/
    }
 /*   public void generateNoteOnSD(String sFileName, String sBody){
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
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }*/
}