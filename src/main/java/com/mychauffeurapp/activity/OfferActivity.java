package com.mychauffeurapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mychauffeurapp.R;
import com.mychauffeurapp.app.App_VolleyExamples;
import com.mychauffeurapp.model.MyEditText;
import com.mychauffeurapp.model.MyTextview;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class OfferActivity extends Activity {
private Button apply;
   /* Calendar mcurrentTime = Calendar.getInstance();
    final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    final int minute = mcurrentTime.get(Calendar.MINUTE);
    final int seconds=mcurrentTime.get(Calendar.SECOND);
    final int milliseconds=mcurrentTime.get(Calendar.MILLISECOND);
    final int date=mcurrentTime.get(Calendar.DATE);*/
    MyEditText couponCode;
    String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offer);
      /*  try {*/
            //this.getActionBar().setDisplayHomeAsUpEnabled(true);
            this.getActionBar().setDisplayShowCustomEnabled(true);
            this.getActionBar().setDisplayShowTitleEnabled(false);
            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.titleview, null);

            ((MyTextview) v.findViewById(R.id.title)).setText(this.getTitle());
            this.getActionBar().setCustomView(v);

            apply = (Button) findViewById(R.id.apply);
            couponCode = (MyEditText) findViewById(R.id.couponCode);


            apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    App_VolleyExamples.getInstance().trackEvent("Button", "Click", "Apply Coupon code");
                    Intent intent = new Intent();
                    //intent.putExtra("CouponCode", couponCode.getText().toString());

                    Log.e("On click", couponCode.getText().toString());
                    if (couponCode.getText().toString().trim().length() == 0 || couponCode.getText().toString().trim().equals(null)) {
                        /*intent.putExtra("CouponCode","0");
                        setResult(1, intent);
                        OfferActivity.this.finish();*/
                        Toast.makeText(OfferActivity.this, "Invalid coupon code", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        intent.putExtra("CouponCode", couponCode.getText().toString());
                        setResult(1, intent);
                        OfferActivity.this.finish();
                    }


                }
            });
            //onBackPressed();
       /* }catch (Exception e){
            generateNoteOnSD("Offer_activity_log"+date+":"+hour+":"+minute+":"+seconds+":"+milliseconds, e.toString());
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
            Intent intent = new Intent();
            code="0";
            intent.putExtra("CouponCode", code);
            //Toast.makeText(OfferActivity.this, couponCode.getText().toString(), Toast.LENGTH_SHORT).show();
            Log.e("back pressed", couponCode.getText().toString());
            setResult(1, intent);
            OfferActivity.this.finish();
    }
}
