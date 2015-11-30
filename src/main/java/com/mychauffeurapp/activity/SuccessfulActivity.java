package com.mychauffeurapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.mychauffeurapp.R;
import com.mychauffeurapp.model.MyTextviews;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class SuccessfulActivity extends Activity {

    MyTextviews view7,view8,view9,view10,view11,view12;
   /* Calendar mcurrentTime = Calendar.getInstance();
    final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    final int minute = mcurrentTime.get(Calendar.MINUTE);
    final int seconds=mcurrentTime.get(Calendar.SECOND);
    final int milliseconds=mcurrentTime.get(Calendar.MILLISECOND);
    final int date=mcurrentTime.get(Calendar.DATE);*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.successfull);
       /* try {*/
            view7 = (MyTextviews) findViewById(R.id.view7);
            view8 = (MyTextviews) findViewById(R.id.view8);
            view9 = (MyTextviews) findViewById(R.id.view9);
            view10 = (MyTextviews) findViewById(R.id.view10);
            view11 = (MyTextviews) findViewById(R.id.view11);
            view12 = (MyTextviews) findViewById(R.id.view12);


            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                view7.setText(extras.getString("unique_id"));
                view8.setText(extras.getString("source"));
                view9.setText(extras.getString("destination"));
                view10.setText(extras.getString("duration") + " hrs");
                view11.setText("Rs. " + extras.getString("cost"));
                view12.setText(extras.getString("date"));
            }

      /*  }catch (Exception e){
            generateNoteOnSD("Successful_activity_log"+date+":"+hour+":"+minute+":"+seconds+":"+milliseconds, e.toString());
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent bookIntent = new Intent(SuccessfulActivity.this, MainActivity.class);
        startActivity(bookIntent);
        SuccessfulActivity.this.finish();

    }
}
