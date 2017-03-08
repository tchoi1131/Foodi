package com.foodi.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.DeadObjectException;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.style.TtsSpan;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SetDeliveryOfferFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SetDeliveryOfferFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetDeliveryOfferFragment extends Fragment implements View.OnClickListener{
    private static final String ARG_REQUESTKEY = "requestKey";
    public static final String ARG_OFFERKEY = "offerKey";

    // TODO: Rename and change types of parameters
    private String mRequestKey;
    private String mOfferKey;

    private TextView mRestaurantAddressTV;
    private TextView mDeliveryAddressTV;
    private EditText mOfferPriceEdtTxt;
    private Button mEstDeliveryTimeBtn;
    private Button mSetOfferPriceBtn;
    private Button mCancelBtn;

    private static Calendar estimatedDeliveryDate;

    private DatabaseReference mDatabase;
    private OnFragmentInteractionListener mListener;

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
    // TODO: Rename and change types and number of parameters
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

        mRestaurantAddressTV = (TextView) view.findViewById(R.id.restaurant_address_tv);
        mDeliveryAddressTV = (TextView) view.findViewById(R.id.delivery_address_tv);
        mOfferPriceEdtTxt = (EditText) view.findViewById(R.id.offer_price);
        mEstDeliveryTimeBtn = (Button) view.findViewById(R.id.est_del_time);
        mEstDeliveryTimeBtn.setOnClickListener(this);
        mSetOfferPriceBtn = (Button) view.findViewById(R.id.set_offer_price_btn);
        mSetOfferPriceBtn.setOnClickListener(this);

        mCancelBtn = (Button) view.findViewById(R.id.cancel_btn);
        mCancelBtn.setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference requestRef = mDatabase.child(SysConfig.FBDB_DELIVERY_REQUESTS).child(mRequestKey);
        requestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DeliveryRequest request = dataSnapshot.getValue(DeliveryRequest.class);
                mRestaurantAddressTV.setText(request.getRestaurantAddress());
                mDeliveryAddressTV.setText(request.getDeliveryAddress());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(mOfferKey != null && mOfferKey != ""){
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
                    if(offer.getOfferStatus() != DeliveryOffer.DELIVERY_OFFER_STATUS_PENDING_CUSTOMER_REPLY){
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
        if (i == R.id.set_offer_price_btn) {
            try {
                mListener.onSetDeliveryOfferFragmentInteraction(mRequestKey,Double.parseDouble(mOfferPriceEdtTxt.getText().toString()),
                        SysConfig.convertToStoredDateTimeFormat(estimatedDeliveryDate.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else if (i == R.id.est_del_time){
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(getActivity().getFragmentManager(), "timePicker");
        }
        else if (i == R.id.cancel_btn){
            getActivity().getFragmentManager().popBackStack();
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
        void onSetDeliveryOfferFragmentInteraction(String requestKey, double offerPrice, String estDeliveryTime);
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
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
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
