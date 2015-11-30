package com.mychauffeurapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mychauffeurapp.R;
import com.mychauffeurapp.adapter.PlaceAutocompleteAdapter;
import com.mychauffeurapp.app.App_VolleyExamples;
import com.mychauffeurapp.common.AlertDialogManager;
import com.mychauffeurapp.common.ConnectionDetector;
import com.mychauffeurapp.model.GeocodeJSONParser;
import com.mychauffeurapp.model.MyAutoCompleteTextView;
import com.mychauffeurapp.model.MyTextview;

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

public class DestinationMap extends Activity implements GoogleApiClient.OnConnectionFailedListener {

    private Button book;
    private GoogleMap mMap;
    private EditText searchbox;
   /* Calendar mcurrentTime = Calendar.getInstance();
    final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    final int minute = mcurrentTime.get(Calendar.MINUTE);
    final int seconds=mcurrentTime.get(Calendar.SECOND);
    final int milliseconds=mcurrentTime.get(Calendar.MILLISECOND);
    final int date=mcurrentTime.get(Calendar.DATE);*/
    protected GoogleApiClient mGoogleApiClient;

    AlertDialogManager alert = new AlertDialogManager();

    private PlaceAutocompleteAdapter mAdapter;

    private MapFragment mapFragment;

    private MarkerOptions markerOptions;

    private CircleOptions circleOptions;

    private ImageButton searchboxbtn;
    private ImageButton cleartbtn;

    private MyAutoCompleteTextView mAutocompleteView;
    private TextView mPlaceDetailsText;

    private TextView mPlaceDetailsAttribution;

    private String url;

    String destAddress;
    private static double longitude1,latitude1;

    private double dlat = 0;
    private double dlng = 0;

    String Userid;
    private com.mychauffeurapp.model.MyTextviews addressText;


    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static final LatLngBounds BOUNDS_GREATER_M = new LatLngBounds(
            new LatLng(-20.498839, 57.316949), new LatLng(-20.109818, 57.758779));

    /**
 ------your api key here -------
    */
    private static final String API_KEY = "AIzaSyArHxI9UJeGOnYzrDKfAzkEL_qKwG8A1sY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.destinmap);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        Bundle extras = getIntent().getExtras();
        if(extras!=null)
        {
            Userid = extras.getString("UserId");
        }

        try {
            //this.getActionBar().setDisplayHomeAsUpEnabled(true);
            this.getActionBar().setDisplayShowCustomEnabled(true);
            this.getActionBar().setDisplayShowTitleEnabled(false);
            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.titleview, null);

            ((MyTextview) v.findViewById(R.id.title)).setText(this.getTitle());
            this.getActionBar().setCustomView(v);

            book = (Button) findViewById(R.id.book);

            // addressText = (com.mychauffeurapp.model.MyTextviews) findViewById(R.id.addressText);
            // searchbox = (EditText) findViewById(R.id.searchbox);
            //searchboxbtn = (ImageButton) findViewById(R.id.searchboxbtn);

            cleartbtn = (ImageButton) findViewById(R.id.clearbtn);

            //FragmentManager fm = getChildFragmentManager();
            // Getting reference to the SupportMapFragment
            mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.destinationmap);

            //Autocomplete

            mAutocompleteView = (MyAutoCompleteTextView) findViewById(R.id.autoCompleteTextViewDest);

            // Register a listener that receives callbacks when a suggestion has been selected
            mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);
            mAutocompleteView.setDropDownBackgroundResource(R.color.autocompletet_background_color);
            //mAutocompleteView.setBackgroundColor(Color.rgb(24,170,230));
            // Retrieve the TextViews that will display details and attributions of the selected place.
            mPlaceDetailsText = (com.mychauffeurapp.model.MyTextviews) findViewById(R.id.addressText);
            mPlaceDetailsAttribution = (TextView) findViewById(R.id.place_attribution);

            // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
            // the entire world.
            mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_GREATER_M, null);
            mAutocompleteView.setAdapter(mAdapter);

            // Getting reference to the Google Map
            mMap = mapFragment.getMap();

            //display Mmap
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(-20.2833, 57.55), 16);
            mMap.animateCamera(cameraUpdate);

            markerOptions = new MarkerOptions();

            circleOptions = new CircleOptions().center(new LatLng(dlat, dlng));
            mMap.addCircle(circleOptions);

            mMap.setMyLocationEnabled(false);

            cleartbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAutocompleteView.setText("");
                    mMap.clear();
                    book.setFocusable(false);
                }
            });

            mAutocompleteView.setText("");
            mPlaceDetailsText.setText("");
            mMap.clear();

            /**Ab
             searchboxbtn.setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View v) {
            // Getting the place entered
            String location =mAutocompleteView.getText().toString();

            if (location == null || location.equals("")) {
            Toast.makeText(DestinationMap.this, "No Place is entered", Toast.LENGTH_SHORT).show();
            return;
            }

            String url = "https://maps.googleapis.com/maps/api/geocode/json?";

            try {
            // encoding special characters like space in the user input place
            location = URLEncoder.encode(location, "utf-8");
            } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            }

            String address = "address=" + location;

            Log.e("MAPFRAGMENT", address);
            String sensor = "sensor=false";


            // url , from where the geocoding data is fetched
            url = url + address + "&" + sensor;

            // Instantiating DownloadTask to get places from Google Geocoding service
            // in a non-ui thread
            DownloadTask downloadTask = new DownloadTask();

            // Start downloading the geocoding places
            downloadTask.execute(url);


            }
            });

             **/
        /*book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(lat).length() != 0 && String.valueOf(lng).length() != 0
                        && (int) lat != 0
                        && (int) lng != 0) {
                    //Toast.makeText(getActivity(), "lat "+String.valueOf(lat)+String.valueOf(lat).length(), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getActivity(), "lng "+String.valueOf(lng)+String.valueOf(lng).length(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("DLatitude", lat);
                    intent.putExtra("DLongitude", lng);
                    intent.putExtra("DestAddress", destAddress);
                    setResult(1, intent);
                    finish();

                } else
                    Toast.makeText(DestinationMap.this, "Please select your Destination location", Toast.LENGTH_SHORT).show();
            }
        });*/

        }catch (Exception e) {
            generateNoteOnSD("Destination_map_log"+new Date().getTime(), e.toString());
        }
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
            Log.e("MapFragment", "saved" + sBody);
            //Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        //Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public static ArrayList autocomplete(String input) {
        ArrayList resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:mu");
            sb.append("&types=geocode");
            sb.append("&sensor=false");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

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
            //Log.d(LOG_TAG, "MalformedURLException", e);
            return resultList;
        } catch (IOException e) {
            //Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();

            }
        }

        try {

            System.out.println("Json : "+jsonResults.toString());
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            JSONObject result = jsonObj.getJSONObject("result").getJSONObject("geometry").getJSONObject("location");
            longitude1  = result.getDouble("lng");
            latitude1 =  result.getDouble("lat");

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

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
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
    }


    // manoj
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);


            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }

        return data;

    }


    /**
     * A class, to download Places from Geocoding webservice
     */
    private class DownloadTask extends AsyncTask<String, Integer, String> {

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result) {

            // Instantiating ParserTask which parses the json data from Geocoding webservice
            // in a non-ui thread
            ParserTask parserTask = new ParserTask();

            // Start parsing the places in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parserTask.execute(result);
        }

    }

    String name;

    /**
     * A class to parse the Geocoding Places in non-ui thread
     */
    class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            GeocodeJSONParser parser = new GeocodeJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                /** Getting the parsed data as a an ArrayList */
                places = parser.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {

            if (mMap != null) {
                // Clears all the existing markers
                mMap.clear();

                Log.e("Destination", "Check Log " + list.size());
                if(list.size()==0){
                    Log.e("Destination", " If part Check Log " + list.size());
                    alert.showAlertDialog(DestinationMap.this,
                            "Invalid Address",
                            "Please ", false);
                }

                for (int i = 0; i < list.size(); i++) {

                    // Creating a marker
                    //markerOptions = new MarkerOptions();

                    // Getting a place from the places list
                    HashMap<String, String> hmPlace = list.get(i);

                    Log.e("MapFragment","Check Log "+ list.size()+ " "+hmPlace );
                    // Getting latitude of the place
                    dlat = Double.parseDouble(hmPlace.get("lat"));

                    // Getting longitude of the place
                    dlng = Double.parseDouble(hmPlace.get("lng"));

                    // Getting name
                    name = hmPlace.get("formatted_address");

                    LatLng latLng = new LatLng(dlat, dlng);

                   // addressText.setText(name);

                    destAddress = name;
                    // Setting the position for the marker
                    markerOptions.position(latLng);

                    // Setting the title for the marker
                    markerOptions.title(name);

                    // Placing a marker on the touched position
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                    mMap.addMarker(markerOptions);

                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            //marker.getPosition();

                            //Toast.makeText(getActivity()," "+marker.getTitle(), Toast.LENGTH_LONG).show();
                            //Toast.makeText(getActivity(), marker.getPosition().latitude+" "+marker.getPosition().longitude, Toast.LENGTH_LONG).show();

                            //addressText.setText(marker.getTitle());
                            //searchbox.setText(marker.getTitle());
                            destAddress = marker.getTitle();
                            //Toast.makeText(getApplicationContext(),addressText+ "\n"+searchbox+ "\n"+destAddress, Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });

                    book.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            App_VolleyExamples.getInstance().trackEvent("DestinationMap Button", "Clicked", "Getting destination address ");
                            if (String.valueOf(dlat).length() != 0 && String.valueOf(dlng).length() != 0
                                    && (int) dlat != 0
                                    && (int) dlng != 0) {
                                //Toast.makeText(getActivity(), "lat "+String.valueOf(lat)+String.valueOf(lat).length(), Toast.LENGTH_SHORT).show();
                                //Toast.makeText(getActivity(), "lng "+String.valueOf(lng)+String.valueOf(lng).length(), Toast.LENGTH_SHORT).show();
                                Log.e("Destination",destAddress+ "\n"+dlat+ "\n"+dlng);
                                Intent intent = new Intent();
                                intent.putExtra("DLatitude", dlat);
                                intent.putExtra("DLongitude", dlng);
                                intent.putExtra("DestAddress", destAddress);
                                //Toast.makeText(getAc, "", Toast.LENGTH_SHORT).show();
                                setResult(1, intent);
                                finish();

                            } else
                                Toast.makeText(DestinationMap.this, "Please select your Destination location", Toast.LENGTH_SHORT).show();
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
                    if (i == 0){
                        LatLng lastCenter = new LatLng(-20.266751, 57.564562);
                        LatLng tempCenter = mMap.getCameraPosition().target;
                        LatLngBounds visibleBounds = mMap.getProjection().getVisibleRegion().latLngBounds;
                        if (!BOUNDS_GREATER_M.contains(visibleBounds.northeast) || !BOUNDS_GREATER_M.contains(visibleBounds.southwest)) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(lastCenter));
                        } else {
                            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

                            // Zoom in, animating the camera.
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
                            //mMap.animateCamera(CameraUpdateFactory.zoomIn());
                        }
                    }
                }
            } else {
                //Toast.makeText(getActivity(), "mMap "+mMap, Toast.LENGTH_SHORT).show();
            }
        }
    }


    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
         /*Ab   final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);*/

            String description = (String) parent.getItemAtPosition(position);


            closeKeyboard(getApplicationContext(), mAutocompleteView.getWindowToken());
          //  Log.i(LOG_TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
          /*Ab  PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);*/

          //  Toast.makeText(getApplicationContext(), "Clicked: " + primaryText, Toast.LENGTH_SHORT).show();
           // Log.i(LOG_TAG, "Called getPlaceById to get Place details for " + placeId);

            String location =mAutocompleteView.getText().toString();

            if (location == null || location.equals("")) {
                Toast.makeText(DestinationMap.this, "No Place is entered", Toast.LENGTH_SHORT).show();
                return;
            }

            url = "https://maps.googleapis.com/maps/api/geocode/json?";

            try {
                // encoding special characters like space in the user input place
                location = URLEncoder.encode(location, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            mPlaceDetailsText.setText(mAutocompleteView.getText().toString());

            String address = "address=" + location;

            Log.e("MAPFRAGMENT", address);
            String sensor = "sensor=false";


            // url , from where the geocoding data is fetched
            url = url + address + "&" + sensor;

            // Instantiating DownloadTask to get places from Google Geocoding service
            // in a non-ui thread
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    DownloadTask downloadTask = new DownloadTask();

                    // Start downloading the geocoding places
                    downloadTask.execute(url);
                }
            }, 1500);
        }
    };

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
             //   Log.e(LOG_TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);

            // Format details of the place for display and show it in a TextView.
            mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
                    place.getId(), place.getAddress()));

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
        }
    };

    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address) {
        //Log.e(LOG_TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
         //       websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, address));

    }

    /**
     * Called when the Activity could not connect to Google Play services and the auto manager
     * could resolve the error automatically.
     * In this case the API is not available and notify the user.
     *
     * @param connectionResult can be inspected to determine the cause of the failure
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        //Log.e(LOG_TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
        //        + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

    public static void closeKeyboard(Context c, IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
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
    public void onBackPressed() {

            Intent in = new Intent(DestinationMap.this,MainActivity.class);
            in.putExtra("UserId",Userid);
            startActivity(in);
            finish();
    }


}



