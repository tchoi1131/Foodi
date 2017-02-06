package com.foodi.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.foodi.foodi.R;
import com.foodi.model.DeliveryRequest;
import com.foodi.model.SysConfig;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MaintainDeliveryRequestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MaintainDeliveryRequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MaintainDeliveryRequestFragment extends Fragment implements View.OnClickListener{
    public static final String NEW_DEL_REQ_MODE = "NewDelReqMode";

    public static final String ARG_USERID = "userId";
    private static final String ARG_MODE = "mode";

    private String mUserId;
    private String mMode;

    private EditText mOrderNumberETxt;
    private EditText mRestaurantNameETxt;
    private EditText mRestaurantAddressLine1Etxt;
    private EditText mRestaurantAddressLine2Etxt;
    private EditText mRestaurantAddressLine3Etxt;
    private EditText mDeliveryAddressLine1Etxt;
    private EditText mDeliveryAddressLine2Etxt;
    private EditText mDeliveryAddressLine3Etxt;

    private DatabaseReference mDatabase;

    private OnFragmentInteractionListener mListener;

    public MaintainDeliveryRequestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userId Parameter 1.
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

        mOrderNumberETxt = (EditText) view.findViewById(R.id.order_number);
        mRestaurantNameETxt = (EditText) view.findViewById(R.id.restaurant_name);
        mRestaurantAddressLine1Etxt = (EditText) view.findViewById(R.id.restaurant_address_line1);
        mRestaurantAddressLine2Etxt = (EditText) view.findViewById(R.id.restaurant_address_line2);
        mRestaurantAddressLine3Etxt = (EditText) view.findViewById(R.id.restaurant_address_line3);
        mDeliveryAddressLine1Etxt = (EditText) view.findViewById(R.id.delivery_address_line1);
        mDeliveryAddressLine2Etxt = (EditText) view.findViewById(R.id.delivery_address_line2);
        mDeliveryAddressLine3Etxt = (EditText) view.findViewById(R.id.delivery_address_line3);

        Button createDeliveryRequestBtn = (Button) view.findViewById(R.id.action_button);
        createDeliveryRequestBtn.setOnClickListener(this);
        if(mMode == NEW_DEL_REQ_MODE){
            createDeliveryRequestBtn.setText(R.string.create_delivery_request);
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFinishCreatingRequest();
        }
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
            String key = mDatabase.child(SysConfig.FBDB_DELIVERY_REQUESTS).push().getKey();
            DeliveryRequest request = new DeliveryRequest(mOrderNumberETxt.getText().toString(),"",
                    mRestaurantNameETxt.getText().toString(),
                    mRestaurantAddressLine1Etxt.getText().toString(),
                    mRestaurantAddressLine2Etxt.getText().toString(),
                    mRestaurantAddressLine3Etxt.getText().toString(),
                    mDeliveryAddressLine1Etxt.getText().toString(),
                    mDeliveryAddressLine2Etxt.getText().toString(),
                    mDeliveryAddressLine3Etxt.getText().toString(),
                    0,DeliveryRequest.DELIVERY_REQUEST_STATUS_ACCEPTING_OFFERS);
            mDatabase.child(SysConfig.FBDB_DELIVERY_REQUESTS).child(key).setValue(request);
            mDatabase.child(SysConfig.FBDB_USER_DELIVERY_REQUESTS).child(mUserId).child(key).setValue("");
            mListener.onFinishCreatingRequest();
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
