package com.mychauffeurapp.fragments;

import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mychauffeurapp.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class SwipeTabFragment extends Fragment {
    private String tab;
    private String layout;
  /*  Calendar mcurrentTime = Calendar.getInstance();
    final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    final int minute = mcurrentTime.get(Calendar.MINUTE);
    final int seconds=mcurrentTime.get(Calendar.SECOND);
    final int milliseconds=mcurrentTime.get(Calendar.MILLISECOND);
    final int date=mcurrentTime.get(Calendar.DATE);*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* try {*/
            Bundle bundle = getArguments();
            tab = bundle.getString("tab");
            layout = bundle.getString("color");
       /* }catch (Exception e) {
            generateNoteOnSD("Swipe_tav_fragment_log"+date+":"+hour+":"+minute+":"+seconds+":"+milliseconds, e.toString());
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
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, null);
        TextView tv = (TextView) view.findViewById(R.id.textView);
        tv.setText(tab);
        //view.setBackgroundResource("R.layout."+layout);
        return view;
    }
}
