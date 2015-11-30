package com.mychauffeurapp.fragments;

import android.app.AlertDialog;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mychauffeurapp.R;
import com.mychauffeurapp.adapter.myRideAdapter;
import com.mychauffeurapp.app.App_VolleyExamples;
import com.mychauffeurapp.app.MyVolley;
import com.mychauffeurapp.common.AlertDialogManager;
import com.mychauffeurapp.common.ConnectionDetector;
import com.mychauffeurapp.model.MyRide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;


public class BookingFragment extends Fragment {

    String urlJsonArry = "http://54.169.81.215/mychauffeurapi/mcapi/bookinghistory/";
    String jsonResponse;
    //public static String[] frag = {"All Rides"};
    AlertDialogManager alert = new AlertDialogManager();
    ConnectionDetector cd;
    ProgressDialog pdialog;
    ListView listView;
    /*Calendar mcurrentTime = Calendar.getInstance();
    final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    final int minute = mcurrentTime.get(Calendar.MINUTE);
    final int seconds=mcurrentTime.get(Calendar.SECOND);
    final int milliseconds=mcurrentTime.get(Calendar.MILLISECOND);
    final int date1=mcurrentTime.get(Calendar.DATE);*/
    private myRideAdapter rideAdapter;

    public static String booking_id_list[];

    public static String uid[], bid[], date[], status[], src_address[], time[],dest_address[],cost[],bactual_cost[], bfinal_cost[],bdiscount_price[],period[],brating[];
    String source, btime,unique_id,bdate,bcost,bstatus;

    private Context context;

   String userid;
    @Override
    public Context getContext() {
        return context;
    }

    public BookingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        /*public static BookingFragment newInstance(String userid)
        {
            BookingFragment frag = new BookingFragment();
            Bundle bundle = new Bundle();
            bundle.getString("UserId");
            //frag.getArguments(bundle);
            return frag;
            //fragmentDemo = StarterFragment.newInstance(fruitList, priceList);
            //adapter.addFrag(fragmentDemo, frag[i]);
        }*/


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //View rootView = inflater.inflate(R.layout.bookinghistory, container, false);
        View rootView = inflater.inflate(R.layout.fragment_my_rides, container, false);
      /*  try {*/
            userid = getArguments().getString("UserId");
            cd = new ConnectionDetector(rootView.getContext());
            pdialog = new ProgressDialog(rootView.getContext(), AlertDialog.THEME_HOLO_LIGHT);
            pdialog.setMessage("Loading ...");

            listView = (ListView) rootView.findViewById(R.id.card_listView);

            rideAdapter = new myRideAdapter(rootView.getContext(), R.layout.ride_history_list);
            listView.setAdapter(rideAdapter);

            //userid = ((MainActivity)getActivity()).userid;

            if (!cd.isConnectingToInternet()) {
                alert.showAlertDialog(getContext(),
                        "Internet Connection Lost",
                        "Please connect to Internet and Try again..", false);
            } else {
                showpDialog();

                RequestQueue queue = MyVolley.getRequestQueue();

                JsonArrayRequest req = new JsonArrayRequest(urlJsonArry + userid,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {

                                try {
                                    jsonResponse = "";
                                    uid = new String[response.length()];
                                    booking_id_list = new String[response.length()];
                                    bid = new String[response.length()];
                                    date = new String[response.length()];
                                    src_address = new String[response.length()];
                                    time = new String[response.length()];
                                    status = new String[response.length()];
                                    dest_address = new String[response.length()];
                                    cost = new String[response.length()];
                                    period = new String[response.length()];
                                    bactual_cost = new String[response.length()];
                                    bfinal_cost = new String[response.length()];
                                    time = new String[response.length()];
                                    status = new String[response.length()];
                                    bdiscount_price = new String[response.length()];
                                    brating = new String[response.length()];

                                    for (int i = 0; i < response.length(); i++) {

                                        JSONObject person = (JSONObject) response
                                                .get(i);

                                        uid[i] = person.getString("uid");
                                        bid[i] = person.getString("bunique_id");
                                        date[i] = person.getString("bdate");
                                        status[i] = person.getString("bstatus");
                                        src_address[i] = person.getString("bsource_address");
                                        time[i] = person.getString("btime");
                                        booking_id_list[i] = person.getString("booking_id");
                                        dest_address[i] = person.getString("bdest_address");
                                        period[i] = person.getString("bperiod");
                                        bactual_cost[i] = person.getString("bactual_cost");
                                        bfinal_cost[i] = person.getString("bfinal_cost");
                                        bdiscount_price[i] = person.getString("bdiscount_price");
                                        brating[i] = person.getString("breview_rating");


                                        source = person.getString("bsource_address");
                                        btime = person.getString("btime");
                                        bdate = person.getString("bdate");
                                        unique_id = person.getString("bunique_id");
                                        bcost = person.getString("bfinal_cost");
                                        bstatus = person.getString("bstatus");
                                        MyRide card = new MyRide(unique_id, bcost, bdate, bstatus);
                                        rideAdapter.add(card);

                                    }

                                    //mTvResult.setText(jsonResponse);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    //Analytics catching exceptions
                                    App_VolleyExamples.getInstance().trackException(e);
                                }
                                hidepDialog();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //tv_no_result.setText("No buses avaiable");
                        listView.setVisibility(View.GONE);
                        hidepDialog();
					/*Toast.makeText(getApplicationContext(),
							error.getMessage(), Toast.LENGTH_SHORT).show();*/
                    }


                });
                // Adding request to request queue
                queue.add(req);

                listView.setAdapter(rideAdapter);
            }
       /* }catch (Exception e){
            Calendar mcurrentTime = Calendar.getInstance();
            final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            final int minute = mcurrentTime.get(Calendar.MINUTE);
            final int seconds=mcurrentTime.get(Calendar.SECOND);
            final int milliseconds=mcurrentTime.get(Calendar.MILLISECOND);
            final int date=mcurrentTime.get(Calendar.DATE);
            generateNoteOnSD("Map_log"+date+"-"+hour+":"+minute+":"+seconds+":"+milliseconds, e.toString());
        }*/
        return rootView;
    }

   /* public void generateNoteOnSD(String sFileName, String sBody){
        try
        {
            File root = new File(Environment.getExternalStorageDirectory(), "MychauffeurLogs");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            Runtime.getRuntime().exec("logcat -d -v time -f " + gpxfile.getAbsolutePath());
            *//*FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();*//*
            Log.e("MapFragment", "saved" + sBody);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }*/

    private void showpDialog(){
        if (!pdialog.isShowing())
            pdialog.show();
    }

    private void hidepDialog(){
        if (pdialog.isShowing())
            pdialog.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Tracking the screen view
        App_VolleyExamples.getInstance().trackScreenView("Booking Fragment");
    }

  /*  public interface OnBackPressedListener {
        public void doBack();
    }
*/
    /*@Override
    public boolean onBackPressed(int keyCode, KeyEvent event){
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    String cameback = "CameBack";
                    Intent intent = new Intent(getActivity(), .class);
                    intent.putExtra("Comingback", cameback);
                    startActivity(intent);
                    return true;
            }
            return false;

    }*/
}





