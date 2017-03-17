package com.foodi.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.foodi.foodi.R;
import com.foodi.model.DeliveryRequest;
import com.foodi.model.SysConfig;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MaintainDeliveryRequestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MaintainDeliveryRequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MaintainDeliveryRequestFragment extends Fragment implements View.OnClickListener{
    public static final String NEW_DEL_REQ_MODE = "NewDelReqMode";      //Create new delivery request mode
    public static final String VIEW_DEL_REQ_MODE = "ViewDelReqMode";    //View delivery request

    /**
     * Request code passed to the PlacePicker intent to identify its result when it returns.
     */
    private static final int RESTAURANT_ADDRESS_PICKER = 1;
    private static final int DELIVERY_ADDRESS_PICKER = 2;

    //constant string to get user id and mode in Arguement
    public static final String ARG_USERID = "userId";
    private static final String ARG_MODE = "mode";

    private String mUserId;                     //User Id
    private String mMode;                       //Mode
    private String mRestaurantAddressCity;      //City of the restaurant address
    private String mDeliveryAddressCity;        //City of the delivery address
    private LatLng mRestaurantAddressLatLng;    //Latitude and Longitude of the restaurant address
    private LatLng mDeliveryAddressLatLng;      //Latitude and Longitude of the delivery address

    //References to UI Objects
    private EditText mOrderNumberETxt;
    private EditText mRestaurantNameETxt;
    private Button mSelectRestaurantAddressBtn;
    private Button mSelectDeliveryAddressBtn;

    //Firebase database reference
    private DatabaseReference mDatabase;

    //FragmentInteractionListener to communicate withe the MainMenuActivity class
    private OnFragmentInteractionListener mListener;

    //default constructor
    public MaintainDeliveryRequestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userId: user id of current user.
     * @param mode: view mode ( reserve for future use)
     * @return A new instance of fragment MaintainDeliveryRequestFragment.
     */
    public static MaintainDeliveryRequestFragment newInstance(String userId, String mode) {
        MaintainDeliveryRequestFragment fragment = new MaintainDeliveryRequestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERID, userId);
        args.putString(ARG_MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserId = getArguments().getString(ARG_USERID);
            mMode = getArguments().getString(ARG_MODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_maintain_delivery_request, container, false);

        //setup reference to UI Objects and setup listener for buttons
        mOrderNumberETxt = (EditText) view.findViewById(R.id.order_number);
        mRestaurantNameETxt = (EditText) view.findViewById(R.id.restaurant_name);
        mSelectRestaurantAddressBtn = (Button) view.findViewById(R.id.select_restaurant_address_btn);
        mSelectRestaurantAddressBtn.setOnClickListener(this);
        mSelectDeliveryAddressBtn = (Button) view.findViewById(R.id.select_delivery_address_btn);
        mSelectDeliveryAddressBtn.setOnClickListener(this);

        Button actionBtn = (Button) view.findViewById(R.id.action_button);
        actionBtn.setOnClickListener(this);

        Button cancelBtn = (Button) view.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(this);

        if(mMode == NEW_DEL_REQ_MODE){
            actionBtn.setText(R.string.create_request);
        }else if (mMode == VIEW_DEL_REQ_MODE){
            //reserve for future use
        }

        //setup firebase database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

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

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.action_button && mMode == NEW_DEL_REQ_MODE){
            //Create delivery request and store in database if action button is clicked and it is in New delivery request mode
            String key = mDatabase.child(SysConfig.FBDB_DELIVERY_REQUESTS).push().getKey();
            DeliveryRequest request = null;
            try {
                request = new DeliveryRequest(SysConfig.convertToStoredDateTimeFormat(Calendar.getInstance().getTime()), mOrderNumberETxt.getText().toString(),"",
                        mRestaurantNameETxt.getText().toString(),
                        mSelectRestaurantAddressBtn.getText().toString(),
                        mRestaurantAddressCity,mRestaurantAddressLatLng.latitude, mRestaurantAddressLatLng.longitude, mSelectDeliveryAddressBtn.getText().toString(),
                        mDeliveryAddressCity, mDeliveryAddressLatLng.latitude, mDeliveryAddressLatLng.longitude, 0,DeliveryRequest.DELIVERY_REQUEST_STATUS_ACCEPTING_OFFERS);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //store delivery request and the corresponding data key in different nodes to maintain the node relationships
            mDatabase.child(SysConfig.FBDB_DELIVERY_REQUESTS).child(key).setValue(request);
            mDatabase.child(SysConfig.FBDB_USER_DELIVERY_REQUESTS).child(mUserId).child(key).setValue(true);
            mDatabase.child(SysConfig.FBDB_DELIVERY_REQUEST_USER_OFFER).child(key).setValue(true);
            Toast.makeText(getActivity(), R.string.delivery_request_created,
                    Toast.LENGTH_SHORT).show();
            mListener.onFinishCreatingRequest();
        }
        else if(i == R.id.cancel_btn){
            //return to previous fragment
            getActivity().getFragmentManager().popBackStack();
        }
        else if(i == R.id.select_restaurant_address_btn){
            //Start an activity for users to select location for restaurant address and name
            try {
                PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                Intent intent = intentBuilder.build(getActivity());
                // Start the Intent by requesting a result, identified by a request code.
                startActivityForResult(intent, RESTAURANT_ADDRESS_PICKER);
            } catch (GooglePlayServicesRepairableException e) {
                GooglePlayServicesUtil
                        .getErrorDialog(e.getConnectionStatusCode(), getActivity(), 0);
            } catch (GooglePlayServicesNotAvailableException e) {
                Toast.makeText(getActivity(), "Google Play Services is not available.",
                        Toast.LENGTH_LONG)
                        .show();
            }
        }
        else if(i == R.id.select_delivery_address_btn){
            //start an activity for users to select location for delivery address
            try {
                PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                Intent intent = intentBuilder.build(getActivity());
                // Start the Intent by requesting a result, identified by a request code.
                startActivityForResult(intent, DELIVERY_ADDRESS_PICKER);
            } catch (GooglePlayServicesRepairableException e) {
                GooglePlayServicesUtil
                        .getErrorDialog(e.getConnectionStatusCode(), getActivity(), 0);
            } catch (GooglePlayServicesNotAvailableException e) {
                Toast.makeText(getActivity(), "Google Play Services is not available.",
                        Toast.LENGTH_LONG)
                        .show();
            }
        }
    }


    /**
     * Extracts data from PlacePicker result.
     * This method is called when an Intent has been started by calling
     * {@link #startActivityForResult(android.content.Intent, int)}. The Intent for the
     * {@link com.google.android.gms.location.places.ui.PlacePicker} is started with
     * {@link #RESTAURANT_ADDRESS_PICKER} or {@link #DELIVERY_ADDRESS_PICKER}request code.
     * When a result with this request code is received in this method, its data is extracted by
     * converting the Intent data to a {@link Place} through the
     * {@link com.google.android.gms.location.places.ui.PlacePicker#getPlace(android.content.Intent,
     * android.content.Context)} call.
     *
     * @param requestCode: RESTAURANT_ADDRESS_PICKER or DELIVERY_ADDRESS_PICKER for selection of restaurant address or delivery address
     * @param resultCode: Activity.RESULT_OK if the place is selected
     * @param data: contains the selected place data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESTAURANT_ADDRESS_PICKER) {
            // This result is from the PlacePicker dialog.
            if (resultCode == Activity.RESULT_OK) {
                /**User has picked a place, extract data.
                 *Setup the text on Restaurant address button,
                 * the Latitude and Longitude of the restaurant address
                 * and the city of the address.
                 */
                final Place place = PlacePicker.getPlace(getActivity(),data);
                mSelectRestaurantAddressBtn.setText(place.getAddress());
                mRestaurantAddressLatLng = place.getLatLng();
                /* A Place object contains details about that place, such as its name, address
                and phone number. Extract the name, address, phone number, place ID and place types.
                 */
                CharSequence placeName = place.getName();
                if(placeName != null && placeName != "") {
                    mRestaurantNameETxt.setText(placeName);
                }

                Geocoder geocoder = new Geocoder(getActivity());
                try
                {
                    List<Address> addresses = geocoder.getFromLocation(mRestaurantAddressLatLng.latitude,mRestaurantAddressLatLng.longitude, 1);
                    mRestaurantAddressCity = addresses.get(0).getLocality();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        else if (requestCode == DELIVERY_ADDRESS_PICKER) {
            // This result is from the PlacePicker dialog.
            if (resultCode == Activity.RESULT_OK) {
                /**User has picked a place, extract data.
                 *Setup the text on Delivery address button,
                 * the Latitude and Longitude of the delivery address
                 * and the city of the address.
                 */
                final Place place = PlacePicker.getPlace(getActivity(),data);

                /* A Place object contains details about that place, such as its name, address
                and phone number. Extract the name, address, phone number, place ID and place types.
                 */

                mSelectDeliveryAddressBtn.setText(place.getAddress());
                mDeliveryAddressLatLng = place.getLatLng();

                Geocoder geocoder = new Geocoder(getActivity());
                try
                {
                    List<Address> addresses = geocoder.getFromLocation(mDeliveryAddressLatLng.latitude,mDeliveryAddressLatLng.longitude, 1);
                    mDeliveryAddressCity = addresses.get(0).getLocality();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFinishCreatingRequest();
    }
}
