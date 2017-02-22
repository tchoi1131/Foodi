package com.foodi.view;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.foodi.foodi.R;
import com.foodi.model.DeliveryOffer;
import com.foodi.model.DeliveryRequest;
import com.foodi.model.SysConfig;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link MyDeliveryRequestsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyDeliveryRequestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyDeliveryRequestsFragment extends Fragment implements View.OnClickListener{

    public static final String ARG_USERID = "userId";


    private DatabaseReference mDatabase;
    private DatabaseReference userRequestsRef = null;

    private String mUserId;
    private ArrayList<String> requestKeys = new ArrayList<>();
    private ArrayList<DeliveryRequest> deliveryRequests = new ArrayList<>();

    private View view;
    private TableLayout tableLayout;

    private OnFragmentInteractionListener mListener;

    public MyDeliveryRequestsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userId Parameter 1.
     * @return A new instance of fragment MyDeliveryRequestsFragment.
     */
    public static MyDeliveryRequestsFragment newInstance(String userId) {
        MyDeliveryRequestsFragment fragment = new MyDeliveryRequestsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserId = getArguments().getString(ARG_USERID);
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_delivery_requests, container, false);
        FloatingActionButton mCreateDeliveryRequestFAB = (FloatingActionButton) view.findViewById(R.id.AddFAB);
        mCreateDeliveryRequestFAB.setOnClickListener(this);

        tableLayout = (TableLayout) view.findViewById(R.id.request_table);
        mCreateDeliveryRequestFAB.setOnClickListener(this);

        if(userRequestsRef == null) {
            loadData();
        }


        return view;
    }

    private void loadData(){
        userRequestsRef = mDatabase.child(SysConfig.FBDB_USER_DELIVERY_REQUESTS).child(mUserId);
        userRequestsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DatabaseReference deliveryRequestRef = mDatabase.child(SysConfig.FBDB_DELIVERY_REQUESTS).child(dataSnapshot.getKey().toString());
                deliveryRequestRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        final DeliveryRequest deliveryRequest = dataSnapshot.getValue(DeliveryRequest.class);
                        final String requestKey = dataSnapshot.getKey().toString();
                        int requestKeyIndex = requestKeys.indexOf(requestKey);
                        if(requestKeyIndex == -1) {
                            requestKeys.add(requestKey);
                            //create new row
                            TableRow row = new TableRow(view.getContext());
                            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                            row.setWeightSum(3);
                            //Date
                            TextView textView = new TextView(view.getContext());
                            textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                            textView.setGravity(Gravity.LEFT);
                            if (deliveryRequest.getRequestDateTime() != null) {
                                try {
                                    textView.setText(SysConfig.getDisplayShortDate(deliveryRequest.getRequestDateTime()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            textView.setTag(getString(R.string.request_date));
                            row.addView(textView);

                            //Restaurant Name
                            textView = new TextView(view.getContext());
                            textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                            textView.setGravity(Gravity.LEFT);
                            textView.setText(deliveryRequest.getRestaurantName());
                            textView.setTag(getString(R.string.restaurant_name));
                            row.addView(textView);

                            //Request Status
                            textView = new TextView(view.getContext());
                            textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                            textView.setGravity(Gravity.LEFT);
                            textView.setText(deliveryRequest.getDeliveryRequestStatus());
                            textView.setTag(getString(R.string.request_status));
                            textView.setClickable(true);
                            textView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DatabaseReference requestConfirmedOfferRef = mDatabase.child(SysConfig.FBDB_DELIVERY_REQUEST_CONFIRMED_OFFER).child(requestKey);
                                    requestConfirmedOfferRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                String offerKey = dataSnapshot.getValue().toString();
                                                DatabaseReference offerRef = mDatabase.child(SysConfig.FBDB_DELIVERY_OFFERS).child(offerKey);
                                                offerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        DeliveryOffer deliveryOffer = dataSnapshot.getValue(DeliveryOffer.class);
                                                        try {
                                                            mListener.onChangeDelReqStatusFragmentInteraction(requestKey, deliveryRequest, deliveryOffer);
                                                        } catch (ParseException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });
                            row.addView(textView);
                            tableLayout.addView(row);
                        }
                        else{
                            View tableRowView = tableLayout.getChildAt(requestKeyIndex);
                            if(tableRowView instanceof TableRow){
                                TableRow tableRow = (TableRow) tableRowView;
                                for(int i = 0; i < tableRow.getChildCount(); i++){
                                    TextView textView = (TextView)tableRow.getChildAt(i);

                                    if (textView.getTag() == getString(R.string.request_date)){
                                        try {
                                            textView.setText(SysConfig.getDisplayShortDate(deliveryRequest.getRequestDateTime()));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    else if(textView.getTag() == getString(R.string.restaurant_name)){
                                        textView.setText(deliveryRequest.getRestaurantName());
                                    }
                                    else if(textView.getTag() == getString(R.string.request_status)){
                                        textView.setText(deliveryRequest.getDeliveryRequestStatus());
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
        if (i == R.id.AddFAB) {
            mListener.onCreateDelReqFragmentInteraction();
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
        void onCreateDelReqFragmentInteraction();
        void onChangeDelReqStatusFragmentInteraction(String requestKey, DeliveryRequest deliveryRequest, DeliveryOffer deliveryOffer) throws ParseException;
    }

}
