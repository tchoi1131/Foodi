package com.foodi.view;

import android.Manifest;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import com.foodi.foodi.R;
import com.foodi.model.DeliveryOffer;
import com.foodi.model.DeliveryRequest;
import com.foodi.model.SysConfig;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import com.google.maps.android.PolyUtil;


/**
 * A simple {@link Fragment} subclass to setup the delivery offer.
 * Activities that contain this fragment must implement the
 * {@link SetDeliveryOfferFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SetDeliveryOfferFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetDeliveryOfferFragment extends Fragment
        implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    //Constant to get request key and offer key from intent
    private static final String ARG_REQUESTKEY = "requestKey";
    public static final String ARG_OFFERKEY = "offerKey";

    //key to define the location access request
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;


    //UI references
    private TextView mRestaurantAddressTV;
    private TextView mDeliveryAddressTV;
    private EditText mOfferPriceEdtTxt;
    private Button mEstDeliveryTimeBtn;
    private Button mShowMapBtn;
    private Button mRefreshBtn;
    private TextView mDurationTV;
    private Button mSetOfferPriceBtn;
    private Button mCancelBtn;

    private static Calendar estimatedDeliveryDate;

    //firebase database reference
    private DatabaseReference mDatabase;

    //listener to communicate with MainMenuActivity
    private OnFragmentInteractionListener mListener;

    private String mRequestKey;                 //delivery request key
    private String mOfferKey;                   //delivery offer key
    private DeliveryRequest mDeliveryRequest;   //Delivery request object
    //google API client
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private String mDurationtxt ;
    private int mDurationVal ;
    private ArrayList<LatLng> deliveryPath = new ArrayList<>();

    public SetDeliveryOfferFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param requestKey Parameter 1.
     * @param offerKey Parameter 2.
     * @return A new instance of fragment SetDeliveryOfferFragment.
     */
    public static SetDeliveryOfferFragment newInstance(String requestKey, String offerKey) {
        SetDeliveryOfferFragment fragment = new SetDeliveryOfferFragment();
        Bundle args = new Bundle();
        args.putString(ARG_REQUESTKEY, requestKey);
        args.putString(ARG_OFFERKEY, offerKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRequestKey = getArguments().getString(ARG_REQUESTKEY);
            mOfferKey = getArguments().getString(ARG_OFFERKEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set_delivery_offer, container, false);

        //setup UI References and Listeners
        mRestaurantAddressTV = (TextView) view.findViewById(R.id.restaurant_address_tv);
        mDeliveryAddressTV = (TextView) view.findViewById(R.id.delivery_address_tv);
        mOfferPriceEdtTxt = (EditText) view.findViewById(R.id.offer_price);
        mEstDeliveryTimeBtn = (Button) view.findViewById(R.id.est_del_time);
        mEstDeliveryTimeBtn.setOnClickListener(this);

        mShowMapBtn = (Button) view.findViewById(R.id.show_map_btn);
        mShowMapBtn.setOnClickListener(this);
        mShowMapBtn.setEnabled(false);

        mRefreshBtn = (Button) view.findViewById(R.id.refresh_btn);
        mRefreshBtn.setOnClickListener(this);

        mDurationTV = (TextView) view.findViewById(R.id.duration_tv);

        mSetOfferPriceBtn = (Button) view.findViewById(R.id.set_offer_price_btn);
        mSetOfferPriceBtn.setOnClickListener(this);

        mCancelBtn = (Button) view.findViewById(R.id.cancel_btn);
        mCancelBtn.setOnClickListener(this);

        //read data from database and fill in the UI
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference requestRef = mDatabase.child(SysConfig.FBDB_DELIVERY_REQUESTS).child(mRequestKey);
        requestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDeliveryRequest = dataSnapshot.getValue(DeliveryRequest.class);
                mRestaurantAddressTV.setText(mDeliveryRequest.getRestaurantAddressLine());
                mDeliveryAddressTV.setText(mDeliveryRequest.getDeliveryAddressLine());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (mOfferKey != null && mOfferKey != "") {
            DatabaseReference offerRef = mDatabase.child(SysConfig.FBDB_DELIVERY_OFFERS).child(mOfferKey);
            offerRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DeliveryOffer offer = dataSnapshot.getValue(DeliveryOffer.class);
                    mOfferPriceEdtTxt.setText(offer.getOfferPrice().toString());
                    try {
                        mEstDeliveryTimeBtn.setText(SysConfig.getDisplayTime(offer.getEstimatedDeliveryTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (offer.getOfferStatus() != DeliveryOffer.DELIVERY_OFFER_STATUS_PENDING_CUSTOMER_REPLY) {
                        mEstDeliveryTimeBtn.setEnabled(false);
                        mSetOfferPriceBtn.setVisibility(View.GONE);
                        mSetOfferPriceBtn.setEnabled(false);
                        mCancelBtn.setEnabled(false);
                        mCancelBtn.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onStart() {
        //connect to google api client
        mGoogleApiClient.connect();
        super.onStart();
    }

    public void onStop() {
        //disconnect from the API client
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.set_offer_price_btn) {
            //Create delivery offer
            try {
                mListener.onSetDeliveryOfferFragmentInteraction(mRequestKey, Double.parseDouble(mOfferPriceEdtTxt.getText().toString()),
                        SysConfig.convertToStoredDateTimeFormat(estimatedDeliveryDate.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (i == R.id.est_del_time) {
            //start TimePickerFragment for user to select estimated delivery time
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(getActivity().getFragmentManager(), "timePicker");
        } else if(i == R.id.refresh_btn) {
            //refresh the estimated delivery time based on current time and the estimated delivery duration
            try {
                getEstimatedTime();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else if(i == R.id.show_map_btn){
            //start an activity to show the recommended delivery path
            ArrayList<LatLng> markerLocations = new ArrayList<>();
            ArrayList<String> markerNames = new ArrayList<>();
            markerNames.add("Your Location");
            markerLocations.add(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()));

            markerLocations.add(new LatLng(mDeliveryRequest.getRestaurantAddressLat(),mDeliveryRequest.getRestaurantAddressLng()));
            markerNames.add("Restaurant");

            markerLocations.add(new LatLng(mDeliveryRequest.getDeliveryAddressLat(),mDeliveryRequest.getDeliveryAddressLng()));
            markerNames.add("Destination");

            Intent mapIntent = new Intent(getActivity(),DeliveryOfferMapsActivity.class);
            mapIntent.putParcelableArrayListExtra(DeliveryOfferMapsActivity.DELIVERY_PATH, deliveryPath);
            mapIntent.putParcelableArrayListExtra(DeliveryOfferMapsActivity.MARKER_LOCATIONS,markerLocations);
            mapIntent.putStringArrayListExtra(DeliveryOfferMapsActivity.MARKER_NAMES,markerNames);
            startActivity(mapIntent);
        }
        else if (i == R.id.cancel_btn) {
            getActivity().getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //get estimated delivery time when connected
        try {
            getEstimatedTime();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void getEstimatedTime() throws MalformedURLException {
        //request permission if necessary
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                }
            }

            return;
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            //call Google Maps Direction Webservice to estimate the delivery time and path
            GetEstimatedPath task = new GetEstimatedPath();
            Uri.Builder uriBuilder = new Uri.Builder();
            uriBuilder.scheme("https")
                    .authority("maps.googleapis.com")
                    .appendPath("maps")
                    .appendPath("api")
                    .appendPath("directions")
                    .appendPath("json")
                    .appendQueryParameter("origin", String.valueOf(mLastLocation.getLatitude()) + "," + String.valueOf(mLastLocation.getLongitude()))
                    .appendQueryParameter("destination", String.valueOf(mDeliveryRequest.getDeliveryAddressLat()) + "," + String.valueOf(mDeliveryRequest.getDeliveryAddressLng()))
                    .appendQueryParameter("waypoints", "via:" + String.valueOf(mDeliveryRequest.getRestaurantAddressLat()) + "," + String.valueOf(mDeliveryRequest.getRestaurantAddressLng()))
                    .appendQueryParameter("mode", "driving")
                    .appendQueryParameter("departure_time", "now")
                    .appendQueryParameter("traffic_model", "best_guess")
                    .appendQueryParameter("key",getString(R.string.api_key));
            task.execute(new URL(uriBuilder.build().toString()));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * This interface is implemented by MainMenuActivity to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        void onSetDeliveryOfferFragmentInteraction(String requestKey, double offerPrice, String estDeliveryTime);
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            Calendar c ;
            if(estimatedDeliveryDate == null){
                c = Calendar.getInstance();
            }
            else{
                c = estimatedDeliveryDate;
            }
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            estimatedDeliveryDate = Calendar.getInstance();
            estimatedDeliveryDate.set(Calendar.HOUR_OF_DAY,hourOfDay);
            estimatedDeliveryDate.set(Calendar.MINUTE,minute);

            Button button = (Button) getActivity().findViewById(R.id.est_del_time);
            try {
                button.setText(SysConfig.convertToDisplayDateTimeFormat(estimatedDeliveryDate.getTime()));
                ((MainMenuActivity)getActivity()).updateDurationSetDeliveryOfferFragment();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateDuration(){
        //update the Duration value by calculating the different between estimated delivery time and current time
        Calendar now = Calendar.getInstance();
        int durationSecond = (int) ((estimatedDeliveryDate.getTimeInMillis() - now.getTimeInMillis())/ 1000);
        mDurationTV.setText(SysConfig.printDuration(durationSecond));
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetEstimatedPath extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... urls) {
            URL url ;
            HttpURLConnection conn ;
            InputStream inputStream = null;

            try {
                //call the web service
                url = urls[0];
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                // read the response
                inputStream = conn.getInputStream();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (inputStream != null) {
                //use JSON reader to read the returned JSON file
                JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
                try {
                    reader.beginObject();
                    while(reader.hasNext()){
                        String nextName = reader.nextName();
                        if(nextName.equals("routes")){
                            reader.beginArray();
                            while (reader.hasNext()){
                                reader.beginObject();
                                while (reader.hasNext()){
                                    nextName = reader.nextName();
                                    if(nextName.equals("legs")){
                                        reader.beginArray();
                                        while (reader.hasNext()){
                                            reader.beginObject();
                                            while (reader.hasNext()){
                                                nextName = reader.nextName();
                                                if(nextName.equals("duration_in_traffic")){
                                                    reader.beginObject();
                                                    while(reader.hasNext()){
                                                        nextName = reader.nextName();
                                                        if(nextName.equals("text")){
                                                            mDurationtxt = reader.nextString();
                                                        }
                                                        else if(nextName.equals("value")){
                                                            mDurationVal = reader.nextInt();
                                                        }

                                                    }
                                                    reader.endObject();
                                                }
                                                else if(nextName.equals("steps")){
                                                    Double endLat = null;
                                                    Double endLng = null;
                                                    reader.beginArray();
                                                    while (reader.hasNext()){
                                                        reader.beginObject();
                                                        while(reader.hasNext()) {
                                                            nextName = reader.nextName();
                                                            if(nextName.equals("polyline")){
                                                                reader.beginObject();
                                                                while(reader.hasNext()){
                                                                    nextName = reader.nextName();
                                                                    if(nextName.equals("points")) {
                                                                        deliveryPath.addAll(PolyUtil.decode(reader.nextString()));
                                                                    }
                                                                }
                                                                reader.endObject();
                                                            }
                                                            else{
                                                                reader.skipValue();
                                                            }
                                                        }
                                                        reader.endObject();
                                                    }
                                                    reader.endArray();
                                                    if(endLat != null && endLng != null) {
                                                        deliveryPath.add(new LatLng(endLat, endLng));
                                                    }
                                                }
                                                else{
                                                    reader.skipValue();
                                                }
                                            }
                                            reader.endObject();
                                        }
                                        reader.endArray();
                                    }
                                    else{
                                        reader.skipValue();
                                    }
                                }
                                reader.endObject();
                            }
                            reader.endArray();
                        }
                        else{
                            reader.skipValue();
                        }
                    }
                    reader.endObject();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return mDurationtxt;
        }

        @Override
        protected void onPostExecute(String result){
            //display the duration value in the UI
            estimatedDeliveryDate = Calendar.getInstance();
            estimatedDeliveryDate.add(Calendar.SECOND,mDurationVal);
            try {
                mEstDeliveryTimeBtn.setText(SysConfig.convertToDisplayDateTimeFormat(estimatedDeliveryDate.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            updateDuration();
            mShowMapBtn.setEnabled(true);
        }
    }
}
