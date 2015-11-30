package com.mychauffeurapp.fragments;

import android.app.Activity;
//import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mychauffeurapp.R;
import com.mychauffeurapp.activity.Booking;
import com.mychauffeurapp.adapter.PlaceAutocompleteAdapter;
import com.mychauffeurapp.app.App_VolleyExamples;
import com.mychauffeurapp.common.AlertDialogManager;
import com.mychauffeurapp.model.GeocodeJSONParser;
import com.mychauffeurapp.model.MyAutoCompleteTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.SupportMapFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;


public class MapFragmnt extends Fragment implements GoogleApiClient.OnConnectionFailedListener,OnMapReadyCallback {

    private Button book;
    private GoogleMap mMap;
    private EditText searchbox;

    //private SupportMapFragment mapFragment;
    FragmentManager fmanager;
    Fragment fragment;
    SupportMapFragment supportmapfragment;

    private MarkerOptions markerOptions;

    private ImageButton searchboxbtn;

    private double lat = 0;
    private double lng = 0;

    private com.mychauffeurapp.model.MyEditText sourceAddressText;

    private Location currentLocation;

    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;

    private static Double longitude1;
    private static Double latitude1;

    AlertDialogManager alert = new AlertDialogManager();

    /**
     * ------your api key here -------
     */
    private static final String API_KEY = "AIzaSyArHxI9UJeGOnYzrDKfAzkEL_qKwG8A1sY";//"AIzaSyDlJl5J5fBaKpFmRWkZaOw8IVegEr-tZOU";

    private Context context;

    ImageButton clearButton;

    private MyAutoCompleteTextView autoCompView;

    private com.mychauffeurapp.model.MyEditTextView mPlaceDetailsText;
    private TextView mPlaceDetailsAttribution;

    private static final LatLngBounds BOUNDS_GREATER_M = new LatLngBounds(
            new LatLng(-20.498839, 57.316949), new LatLng(-20.109818, 57.758779));

    String url;

    String pname;
    String userid;

    //GPSTracker gps;
    public MapFragmnt() {
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_map, container, false);
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();

            book = (Button) rootView.findViewById(R.id.book);
            userid = getArguments().getString("UserId");

            book.setFocusable(false);

            autoCompView = (MyAutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextView);

            //autoCompView.setOnItemClickListener(mAutocompleteClickListener);

            //autoCompView.setDropDownBackgroundResource(R.color.autocompletet_background_color);

            // Retrieve the TextViews that will display details and attributions of the selected place.
            mPlaceDetailsText = (com.mychauffeurapp.model.MyEditTextView) rootView.findViewById(R.id.place_details);
            mPlaceDetailsAttribution = (TextView) rootView.findViewById(R.id.place_attribution);

            // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
            // the entire world.

         // mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            //mapFragment = (SupportMapFragment)getFragmentManager().findFragmentById(R.id.map);

            //mapFragment.getMapAsync(this);


        fmanager = this.getChildFragmentManager();
        fragment = fmanager.findFragmentById(R.id.map);
        supportmapfragment = (SupportMapFragment)fragment;


        supportmapfragment.getMapAsync(this);

            // Getting reference to the Google Map
            //mMap = mapFragment.getMap();
           // mMap = mapFragment.getMapAsync(getContext());
            //mMap.getMapAsync();
            //markerOptions = new MarkerOptions();

      /*  try {
            MapsInitializer.initialize(getActivity());
            GooglePlayServicesNotAvailableException();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }*/

            /*mAdapter = new PlaceAutocompleteAdapter(context, mGoogleApiClient, BOUNDS_GREATER_M, null);
            autoCompView.setAdapter(mAdapter);*/

            //CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(-20.2833, 57.55), 16);
            //mMap.animateCamera(cameraUpdate);

            //mMap.setMyLocationEnabled(false);

            clearButton = (ImageButton) rootView.findViewById(R.id.clearbtn);
            /*clearButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    autoCompView.setText("");
                    mMap.clear();
                    book.setFocusable(false);
                    //mPlaceDetailsText.setText("");
                }
            });*/

        } catch (Exception e) {
           Calendar mcurrentTime = Calendar.getInstance();
           final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
           final int minute = mcurrentTime.get(Calendar.MINUTE);
           final int seconds=mcurrentTime.get(Calendar.SECOND);
           final int milliseconds=mcurrentTime.get(Calendar.MILLISECOND);
           final int date=mcurrentTime.get(Calendar.DATE);
            generateNoteOnSD("Map_log"+date+"-"+hour+":"+minute+":"+seconds+":"+milliseconds, e.toString());
        }
        return rootView;
    }


    public void generateNoteOnSD(String sFileName, String sBody){
        try
        {
            File root = new File(Environment.getExternalStorageDirectory(), "MychauffeurLogs");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            Runtime.getRuntime().exec("logcat -d -v time -f " + gpxfile.getAbsolutePath());
            /*FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();*/
            Log.e("MapFragment","saved"+sBody);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    /*AB private String getPlaceDetailsUrl(String ref){

        // reference of place
        String reference = "reference="+ref;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = reference+"&"+sensor+"&"+API_KEY;

        // Output format
        String output = "json";

        // Building the url to the web service
        //String url = "https://maps.googleapis.com/maps/api/place/details/"+output+"?"+parameters;
        String url = "https://maps.googleapis.com/maps/api/geocode/json?"+output+"?"+parameters;
        return url;
    }*/

    /*public void onItemClick(AdapterView adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        //Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }*/

    public ArrayList autocomplete(String input) {
        ArrayList resultList = null;

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(-20.2833, 57.55), 16);
        mMap.animateCamera(cameraUpdate);

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:MUS");
            //sb.append("&types=geocode");
            //sb.append("&sensor=false");
            sb.append("&input="+URLEncoder.encode(input,"utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            //Toast.makeText(getActivity(),"MalformedURLException "+ e , Toast.LENGTH_SHORT).show();
            //Log.e("MapFragment", "MalformedURLException "+ e);
            return resultList;
        } catch (IOException e) {
            //Toast.makeText(getActivity(),"Error "+ e , Toast.LENGTH_SHORT).show();
            //Log.e("MapFragment", "Error connecting to Places API "+ e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            //Toast.makeText(getActivity(),"Error "+ jsonResults.toString() , Toast.LENGTH_SHORT).show();
            System.out.println("Json Check : " + jsonResults.toString());
            Log.e("Tag{{{{{{{{{{{{kkkkkkkk", jsonResults.toString());
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            JSONObject result = jsonObj.getJSONObject("result").getJSONObject("geometry").getJSONObject("location");
            longitude1  = result.getDouble("lng");
            latitude1 =  result.getDouble("lat");
            /*if (!BOUNDS_GREATER_M.contains(visibleBounds.northeast) || !BOUNDS_GREATER_M.contains(visibleBounds.southwest)) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(lastCenter));
            } else*/

            Log.e("Tag",predsJsonArray.length()+"");
            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            // Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    /*class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
        private ArrayList resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return (String)resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }*/

    /*public void updateCircleAndPointer() {
        LatLng myLocation = new LatLng(lat, lng);
        mMap.clear();

        circleOptions = circleOptions.center(new LatLng(lat, lng));

        mMap.addCircle(circleOptions);

        mMap.addMarker(new MarkerOptions().position(myLocation).title(
                "Your current location"));

        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((new LatLng(lat, lng)), 11f));

        //mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));

        //a searchbox.setText("" + getAddressFromLocation());
    }*/

    /*public String getAddressFromLocation() {
        String addressStr = "";
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(),Locale.ENGLISH);

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getAddressLine(1);
            String country = addresses.get(0).getAddressLine(2);

            *//*if(!country.equalsIgnoreCase(null)&&country.equalsIgnoreCase(""))
                addressStr = address + ", " + city + ", " + country;
            else*//*
            addressStr = address + ", " + city;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return addressStr;
    }*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Getting reference to the SupportMapFragment
        //mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);

        // Getting reference to the Google Map
        mMap = googleMap;//mapFragment.getMap();

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(-20.2833, 57.55), 16);
        mMap.animateCamera(cameraUpdate);

        /*currentLocation=googleMap.getMyLocation();
        lat=currentLocation.getLatitude();
        lng=currentLocation.getLongitude();*/

        //Toast.makeText(getActivity(), "onMapReady,mMap "+lng, Toast.LENGTH_SHORT).show();
        markerOptions = new MarkerOptions();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter = new PlaceAutocompleteAdapter(context, mGoogleApiClient, BOUNDS_GREATER_M, null);
                autoCompView.setAdapter(mAdapter);

                autoCompView.setOnItemClickListener(mAutocompleteClickListener);

                autoCompView.setDropDownBackgroundResource(R.color.autocompletet_background_color);

            }
        }, 1000);

        /*autoCompView.setOnItemClickListener(mAutocompleteClickListener);

        autoCompView.setDropDownBackgroundResource(R.color.autocompletet_background_color);*/

        //mAdapter = new PlaceAutocompleteAdapter(context, mGoogleApiClient, BOUNDS_GREATER_M, null);
        //autoCompView.setAdapter(mAdapter);

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompView.setText("");
                mMap.clear();
                book.setFocusable(false);
                //mPlaceDetailsText.setText("");
            }
        });

    }


    /*@Override
    public void onLocationChanged(final Location location) {
        if (location != null) {

            currentLocation = location;
            lat=currentLocation.getLatitude();
            lng=currentLocation.getLongitude();

        }
    }*/

    String location;

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */

            /*final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);
*/
            String description = (String) parent.getItemAtPosition(position);

            closeKeyboard(getActivity(), autoCompView.getWindowToken());

            //Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
   /*         PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback); */

           /* Toast.makeText(context, "Clicked: " + primaryText + latitude1 + longitude1,
                    Toast.LENGTH_SHORT).show();*/


            //String location;

            /*Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {*/
            //if(autoCompView.getText().toString().trim().length()>3) {
                location = autoCompView.getText().toString();

                if (location == null || location.equals("")) {
                    Toast.makeText(context, "No Place is entered", Toast.LENGTH_SHORT).show();
                    return;
                }

                url = "https://maps.googleapis.com/maps/api/geocode/json?";

                try {
                    // encoding special characters like space in the user input place
                    location = URLEncoder.encode(location, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                String address = "address=" + location;
                    /*String region = "&region=mauritius";
                    String bounds = "&bounds=-20.498839,57.316949|-20.109818,57.758779";*/
                Log.e("MAPFRAGMENT", address);
                String sensor = "sensor=false";

                // mPlaceDetailsText.setText(address);
                mPlaceDetailsText.setText(autoCompView.getText().toString());
                // url , from where the geocoding data is fetched
                //url = url + address +region+bounds+"&" + sensor;
                url = url + address + "&" + sensor;

                DownloadTask downloadTask = new DownloadTask();

                // Start downloading the geocoding places
                downloadTask.execute(url);
            //}
                /*}
            }, 1000);*/


            /*if (location == null || location.equals("")) {
                Toast.makeText(context, "No Place is entered", Toast.LENGTH_SHORT).show();
                return;
            }

            url = "https://maps.googleapis.com/maps/api/geocode/json?";

            try {
                // encoding special characters like space in the user input place
                location = URLEncoder.encode(location, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String address = "address=" + location;
            *//*String region = "&region=mauritius";
            String bounds = "&bounds=-20.498839,57.316949|-20.109818,57.758779";*//*
            Log.e("MAPFRAGMENT", address);
            String sensor = "sensor=false";

           // mPlaceDetailsText.setText(address);
            mPlaceDetailsText.setText(autoCompView.getText().toString());
            // url , from where the geocoding data is fetched
            //url = url + address +region+bounds+"&" + sensor;
            url = url + address +"&" + sensor;*/

            /*final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {*/
                    //DownloadTask downloadTask = new DownloadTask();

                    // Start downloading the geocoding places
                    //downloadTask.execute(url);
                /*}
            }, 1500);*/
            // Instantiating DownloadTask to get places from Google Geocoding service
            // in a non-ui thread
            /*DownloadTask downloadTask = new DownloadTask();

            // Start downloading the geocoding places
            downloadTask.execute(url);*/

            // Log.i(LOG_TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    /** A class, to download Places from Geocoding webservice */
    private class DownloadTask extends AsyncTask<String, Integer, String> {

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try{
                data = downloadUrl(url[0]);
                //Log.e("Background Task",data+"");
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result){

            //Toast.makeText(getActivity(), "result "+result, Toast.LENGTH_SHORT).show();
            // Instantiating ParserTask which parses the json data from Geocoding webservice
            // in a non-ui thread
            ParserTask parserTask = new ParserTask();

            // Start parsing the places in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parserTask.execute(result);
        }

    }

    String name;

    /** A class to parse the Geocoding Places in non-ui thread */
    class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String,String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            GeocodeJSONParser parser = new GeocodeJSONParser();
            Log.d("MapFragment","parser "+parser.toString());
            try{
                jObject = new JSONObject(jsonData[0]);


                /** Getting the parsed data as a an ArrayList */
                places = parser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            //Log.d("MapFragment","places"+places+"");
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String,String>> list){

            //if(mMap!=null) {
            // Clears all the existing markers
            if(autoCompView.getText()!=null){
                //mMap.clear();

                //Log.e("MapFragment", "Check Log " + list.size());
                /*if(list.size()==0){
                    Log.e("MapFragment", " If part Check Log " + list.size());
                    alert.showAlertDialog(getActivity(),
                            "Invalid Address",
                            "Please ", false);
                }*/

                for (int i = 0; i < list.size(); i++) {

                    // Creating a marker
                    //markerOptions = new MarkerOptions();

                    // Getting a place from the places list
                    HashMap<String, String> hmPlace = list.get(i);

                    Log.e("MapFragment",hmPlace+"");
                    // Getting latitude of the place
                    lat = Double.parseDouble(hmPlace.get("lat"));
                    //  lat = latitude;

                    // Getting longitude of the place
                    lng = Double.parseDouble(hmPlace.get("lng"));
                    //lng = longitude;


                    // Getting name
                    //name = hmPlace.get("formatted_address");

                    name = mPlaceDetailsText.getText().toString();

                    LatLng latLng = new LatLng(lat, lng);

                    Log.e("MapFragmnt",latLng +"");

                    //sourceAddressText.setText(name);
                    // Setting the position for the marker
                    markerOptions.position(latLng);

                    // Setting the title for the marker
                    markerOptions.title(name);

                    //marker icon
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));

                    // Placing a marker on the touched position
                    mMap.addMarker(markerOptions);

                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            //marker.getPosition();

                            //Toast.makeText(getActivity()," "+marker.getTitle(), Toast.LENGTH_LONG).show();
                            //Toast.makeText(getActivity(), marker.getPosition().latitude+" "+marker.getPosition().longitude, Toast.LENGTH_LONG).show();

                            //  sourceAddressText.setText(marker.getTitle());
                            // searchbox.setText(marker.getTitle());
                            return true;
                        }
                    });

                    /*mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                        @Override
                        public void onMarkerDragStart(Marker marker) {
                            sourceAddressText.setText(marker.getTitle());
                            searchbox.setText(marker.getTitle());
                        }

                        @Override
                        public void onMarkerDrag(Marker marker) {

                        }

                        @Override
                        public void onMarkerDragEnd(Marker marker) {

                        }
                    });*/
                    // Locate the first location
                    if (i == 0) {
                        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                        LatLng lastCenter = new LatLng(-20.266751, 57.564562);
                        LatLng tempCenter = mMap.getCameraPosition().target;
                        LatLngBounds visibleBounds = mMap.getProjection().getVisibleRegion().latLngBounds;
                        if (!BOUNDS_GREATER_M.contains(visibleBounds.northeast) || !BOUNDS_GREATER_M.contains(visibleBounds.southwest)) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(lastCenter));
                        } else
                        {

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

                            // Zoom in, animating the camera.
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
                        }
                    }
                }

                book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(String.valueOf(lat).length()!=0&&String.valueOf(lng).length()!=0
                                &&(int)lat!=0
                                &&(int)lng!=0){
                            //Toast.makeText(getActivity(), "lat "+String.valueOf(lat)+String.valueOf(lat).length(), Toast.LENGTH_SHORT).show();
                            //Toast.makeText(getActivity(), "lng "+String.valueOf(lng)+String.valueOf(lng).length(), Toast.LENGTH_SHORT).show();
                            Intent bookIntent = new Intent(getActivity().getApplicationContext(), Booking.class);
                            bookIntent.putExtra("UserId",userid);
                            bookIntent.putExtra("SourceName",name);
                            bookIntent.putExtra("SLatitude",lat);
                            bookIntent.putExtra("SLongitude",lng);
                            startActivity(bookIntent);
                            getActivity().finish();
                        }
                        else
                            Toast.makeText(getActivity(), "Please select your PickUp location", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            else{
                //Toast.makeText(getActivity(), "mMap "+mMap, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);


            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception", e.toString());
        } finally{
            iStream.close();
            urlConnection.disconnect();
        }

        return data;

    }


    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                // Log.e(LOG_TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }

            // Get the Place object from the buffer.
            final Place place = places.get(0);
           /* LatLng location = place.getLatLng();
            latitude1 = location.latitude;
            longitude1 = location.longitude;*/


            // Format details of the place for display and show it in a TextView.
            mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(), place.getAddress()));

            // Display the third party attributions if set.
            final CharSequence thirdPartyAttribution = places.getAttributions();
            if (thirdPartyAttribution == null) {
                mPlaceDetailsAttribution.setVisibility(View.GONE);
            } else {
                mPlaceDetailsAttribution.setVisibility(View.VISIBLE);
                mPlaceDetailsAttribution.setText(Html.fromHtml(thirdPartyAttribution.toString()));
            }

            //Log.i(LOG_TAG, "Place details received: " + place.getName());

            places.release();
            /*Toast.makeText(context, "Clicked: "  +latitude1 +longitude1,
                    Toast.LENGTH_SHORT).show();*/

        }
    };

    private static Spanned formatPlaceDetails(Resources res, CharSequence name,
                                              CharSequence address) {
        // Log.e(LOG_TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
        //         websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, address));

    }


    // @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        // Log.e(LOG_TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
        //      + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(context,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);


    }
    public static void closeKeyboard(Context c, IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Tracking the screen view
        App_VolleyExamples.getInstance().trackScreenView("Map Fragment");
    }

}

/*extends Activity {
private Button book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        book = (Button) findViewById(R.id.book);

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookIntent = new Intent(MapFragmnt.this, Booking.class);
                startActivity(bookIntent);
                MapFragmnt.this.finish();
            }
        });

    }}


*/
