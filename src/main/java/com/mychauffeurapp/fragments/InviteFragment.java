package com.mychauffeurapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mychauffeurapp.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;


public class InviteFragment extends Fragment {
   /* Calendar mcurrentTime = Calendar.getInstance();
    final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    final int minute = mcurrentTime.get(Calendar.MINUTE);
    final int seconds=mcurrentTime.get(Calendar.SECOND);
    final int milliseconds=mcurrentTime.get(Calendar.MILLISECOND);
    final int date=mcurrentTime.get(Calendar.DATE);*/
        public InviteFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.invite, container, false);
        /*    try {

            }catch(Exception e) {
                generateNoteOnSD("Invite_fragment_log"+date+":"+hour+":"+minute+":"+seconds+":"+milliseconds, e.toString());*/

            return rootView;
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
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }*/


    }










