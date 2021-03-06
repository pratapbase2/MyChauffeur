/*
 * Copyright (C) 2015 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.mychauffeurapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataBufferUtils;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;
import com.mychauffeurapp.R;
import com.mychauffeurapp.model.PlaceApi;
import com.mychauffeurapp.common.AlertDialogManager;
import com.mychauffeurapp.logger.Log;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Adapter that handles Autocomplete requests from the Places Geo Data API.
 * {@link AutocompletePrediction} results from the API are frozen and stored directly in this
 * adapter. (See {@link AutocompletePrediction#freeze()}.)
 * <p>
 * Note that this adapter requires a valid {@link com.google.android.gms.common.api.GoogleApiClient}.
 * The API client must be maintained in the encapsulating Activity, including all lifecycle and
 * connection states. The API client must be connected with the {@link Places#GEO_DATA_API} API.
 */
public class PlaceAutocompleteAdapter
        extends ArrayAdapter<String> implements Filterable {



    private static final String TAG = "PlaceAutocompleteAdapter";
    private static final CharacterStyle STYLE_BOLD = new StyleSpan(Typeface.BOLD);
    Context mContext;
    PlaceApi mPlaceAPI = new PlaceApi();
    /**
     * Current results returned by this adapter.
     */
    //private ArrayList<AutocompletePrediction> mResultList;

    //Abi
    private ArrayList<String> mResultList;
    AlertDialogManager alert = new AlertDialogManager();
    /**
     * Handles autocomplete requests.
     */
    private GoogleApiClient mGoogleApiClient;

    /**
     * The bounds used for Places Geo Data autocomplete API requests.
     */
    private LatLngBounds mBounds;

    /**
     * The autocomplete filter used to restrict queries to a specific set of place types.
     */
    private AutocompleteFilter mPlaceFilter;

    AlertDialog alert11;
    AlertDialog.Builder builder1;

    TextView textView1;
    //placeAPI mPlaceAPI = new PlaceAPI();

    /**
     * Initializes with a resource for text rows and autocomplete query bounds.
     *
     * @see ArrayAdapter#ArrayAdapter(Context, int)
     */
    public PlaceAutocompleteAdapter(Context context, GoogleApiClient googleApiClient,
                                    LatLngBounds bounds, AutocompleteFilter filter) {
        //super(context, android.R.layout.simple_expandable_list_item_2, android.R.id.text1);
        super(context, R.layout.autocomplete_list_item, android.R.id.text1);
        mContext = context;
        mGoogleApiClient = googleApiClient;
        mBounds = bounds;
        mPlaceFilter = filter;
    }

    /**
     * Sets the bounds for all subsequent queries.
     */
    public void setBounds(LatLngBounds bounds) {
        mBounds = bounds;
    }

    /**
     * Returns the number of results received in the last autocomplete query.
     */
    @Override
    public int getCount() {
        if(mResultList.size()==0){

            /*Toast toast=new Toast(getContext());
            if(toast.equals(null)){
                toast.setText("Please enter valid Address");
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
            }*/
            //Toast.makeText(getContext(), "No Results Found", Toast.LENGTH_SHORT).show();


            /*builder1 = new AlertDialog.Builder(getContext());
            alert11 = builder1.create();

            if(!alert11.isShowing()) {
                //alert11 = builder1.create();
                builder1.setMessage("Please enter valid Address");
                builder1.setCancelable(true);

                builder1.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder1.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


                alert11.show();
            }*/

            android.util.Log.e("MapFragment", " If part Check Log " + mResultList.size());

            /*alert.showAlertDialog(getContext(),
                    "Invalid Address",
                    "Please enter valid Address", false);*/
        }
        //Toast.makeText(getContext(),mResultList.size()+"" , Toast.LENGTH_SHORT).show();
        Log.e("PlaceAutocompleteAdapter",mResultList.size()+"");

        return mResultList.size();

    }

    /**
     * Returns an item from the last autocomplete query.
     */
    /*@Override
    public AutocompletePrediction getItem(int position) {
        return mResultList.get(position);
    }*/

    //Abi
    @Override
    public String getItem(int position) {
        return mResultList.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //ab View row = super.getView(position, convertView, parent);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.autocomplete_list_item, null);
        // Sets the primary and secondary text for a row.
        // Note that getPrimaryText() and getSecondaryText() return a CharSequence that may contain
        // styling based on the given CharacterStyle.

        //AutocompletePrediction item = getItem(position);
        //String item = getItem(position);
        //if(mResultList!=null && position!=0) {
            textView1 = (TextView) row.findViewById(android.R.id.text1);
            TextView textView2 = (TextView) row.findViewById(android.R.id.text2);
            //textView1.setText(item.getPrimaryText(STYLE_BOLD));

            /*final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textView1.setText(mResultList.get(position));
                    textView1.setTextColor(Color.rgb(30, 62, 102));
                    textView1.setTextSize(11);
                }
            }, 1500);*/

            if(mResultList.size()>position) {
                textView1.setText(mResultList.get(position));
                textView1.setTextColor(Color.rgb(30, 62, 102));
                textView1.setTextSize(11);
            }
           // Toast.makeText(getContext(),mResultList.size() , Toast.LENGTH_SHORT).show();
            //textView2.setText(item.getSecondaryText(STYLE_BOLD));
            //textView2.setTextSize(15);
        //}

        return row;
    }

    /**
     * Returns the filter for the current set of autocomplete results.
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                // Skip the autocomplete query if no constraints are given.

                if (constraint != null) {
                    // Query the autocomplete API for the (constraint) search string.
                    //mResultList = getAutocomplete(constraint);

                    mResultList = mPlaceAPI.autocomplete(constraint.toString());
                    Log.e(TAG,mResultList.size()+" mResultList "+mResultList);
                    if (mResultList != null) {
                        // The API successfully returned results.
                        results.values = mResultList;
                        results.count = mResultList.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            notifyDataSetChanged();
                        }
                    }, 1000);
                    //notifyDataSetChanged();
                } else {
                    //Toast.makeText(getContext(), "The API did not return any results", Toast.LENGTH_SHORT).show();
                    Log.e(TAG,results.count+" results "+results);
                    // The API did not return any results, invalidate the data set.
                    notifyDataSetInvalidated();
                }
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                // Override this method to display a readable result in the AutocompleteTextView
                // when clicked.
                if (resultValue instanceof AutocompletePrediction) {
                    /*Toast.makeText(getContext(), "AutocompletePrediction "+((AutocompletePrediction) resultValue).getFullText(null)
                            , Toast.LENGTH_SHORT).show();*/
                    return ((AutocompletePrediction) resultValue).getFullText(null);
                } else {
                    return super.convertResultToString(resultValue);
                }
            }
        };
    }

    /**
     * Submits an autocomplete query to the Places Geo Data Autocomplete API.
     * Results are returned as frozen AutocompletePrediction objects, ready to be cached.
     * objects to store the Place ID and description that the API returns.
     * Returns an empty list if no results were found.
     * Returns null if the API client is not available or the query did not complete
     * successfully.
     * This method MUST be called off the main UI thread, as it will block until data is returned
     * from the API, which may include a network request.
     *
     * @param constraint Autocomplete query string
     * @return Results from the autocomplete API or null if the query was not successful.
     * @see Places#GEO_DATA_API#getAutocomplete(CharSequence)
     * @see AutocompletePrediction#freeze()
     */
    private ArrayList<AutocompletePrediction> getAutocomplete(CharSequence constraint) {
        if (mGoogleApiClient.isConnected()) {
            Log.i(TAG, "Starting autocomplete query for: " + constraint);

            // Submit the query to the autocomplete API and retrieve a PendingResult that will
            // contain the results when the query completes.
            PendingResult<AutocompletePredictionBuffer> results =
                    Places.GeoDataApi
                            .getAutocompletePredictions(mGoogleApiClient, constraint.toString(),
                                    mBounds, mPlaceFilter);

            // This method should have been called off the main UI thread. Block and wait for at most 60s
            // for a result from the API.
            AutocompletePredictionBuffer autocompletePredictions = results
                    .await(60, TimeUnit.SECONDS);

            // Confirm that the query completed successfully, otherwise return null
            final Status status = autocompletePredictions.getStatus();
            if (!status.isSuccess()) {
                Toast.makeText(getContext(), "Error contacting API: " + status.toString(),
                        Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error getting autocomplete prediction API call: " + status.toString());
                autocompletePredictions.release();
                return null;
            }

            Log.i(TAG, "Query completed. Received " + autocompletePredictions.getCount()
                    + " predictions.");

            // Freeze the results immutable representation that can be stored safely.
            return DataBufferUtils.freezeAndClose(autocompletePredictions);
        }
        Log.e(TAG, "Google API client is not connected for autocomplete query.");
        return null;
    }
}
