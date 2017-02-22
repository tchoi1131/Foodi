package com.foodi.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.style.TtsSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.foodi.foodi.R;

import java.sql.Time;
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
    public static final String ARG_USERID = "userId";

    // TODO: Rename and change types of parameters
    private String mRequestKey;
    private String mUserId;

    private EditText mOfferPriceEdtTxt;
    private Button mEstDeliveryTimeBtn;
    private Button mSetOfferPriceBtn;

    private static Calendar estimatedDeliveryDate;

    private OnFragmentInteractionListener mListener;

    public SetDeliveryOfferFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param requestKey Parameter 1.
     * @param userId Parameter 2.
     * @return A new instance of fragment SetDeliveryOfferFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SetDeliveryOfferFragment newInstance(String requestKey, String userId) {
        SetDeliveryOfferFragment fragment = new SetDeliveryOfferFragment();
        Bundle args = new Bundle();
        args.putString(ARG_REQUESTKEY, requestKey);
        args.putString(ARG_USERID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRequestKey = getArguments().getString(ARG_REQUESTKEY);
            mUserId = getArguments().getString(ARG_USERID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set_delivery_offer, container, false);

        mOfferPriceEdtTxt = (EditText) view.findViewById(R.id.offer_price);
        mEstDeliveryTimeBtn = (Button) view.findViewById(R.id.est_del_time);
        mEstDeliveryTimeBtn.setOnClickListener(this);
        mSetOfferPriceBtn = (Button) view.findViewById(R.id.set_offer_price_btn);
        mSetOfferPriceBtn.setOnClickListener(this);

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
            SimpleDateFormat storedDateFormat = new SimpleDateFormat(getResources().getString(R.string.stored_date_format));
            mListener.onSetDeliveryOfferFragmentInteraction(mRequestKey,Double.parseDouble(mOfferPriceEdtTxt.getText().toString()),
                    storedDateFormat.format(estimatedDeliveryDate.getTime()));
        }
        else if (i == R.id.est_del_time){
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(getActivity().getFragmentManager(), "timePicker");
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
            SimpleDateFormat displayDateFormat = new SimpleDateFormat(getResources().getString(R.string.display_time_format));

            Button button = (Button) getActivity().findViewById(R.id.est_del_time);
            button.setText(displayDateFormat.format(estimatedDeliveryDate.getTime()));
        }
    }
}
