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
 * A fragment to display delivery request created by current user
 * Activities that contain this fragment must implement the
 * {@link MyDeliveryRequestsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyDeliveryRequestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyDeliveryRequestsFragment extends Fragment implements View.OnClickListener{
    //Constant string to get user Id from intent
    public static final String ARG_USERID = "userId";

    //database reference
    private DatabaseReference mDatabase;
    private DatabaseReference userRequestsRef = null;

    private String mUserId;                                                     //User Id
    private ArrayList<String> requestKeys = new ArrayList<>();                  //delivery request keys
    private ArrayList<DeliveryRequest> deliveryRequests = new ArrayList<>();    //delivery request objects
    private ArrayList<MyDeliveryRequestRowItem> rowItems = new ArrayList<>();   //delivery request row items

    //UI References
    private View view;
    private TableLayout tableLayout;

    //Listener to communicate with MainMenuActivity
    private OnFragmentInteractionListener mListener;

    public MyDeliveryRequestsFragment() {
        // Required empty public constructor
    }

    /**
     * A factory method to create a new instance of
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_delivery_requests, container, false);

        //set up UI references and action listeners
        FloatingActionButton mCreateDeliveryRequestFAB = (FloatingActionButton) view.findViewById(R.id.AddFAB);
        mCreateDeliveryRequestFAB.setOnClickListener(this);
        tableLayout = (TableLayout) view.findViewById(R.id.request_table);
        mCreateDeliveryRequestFAB.setOnClickListener(this);

        //load Data to the table view
        loadData();

        return view;
    }

    private void loadData(){
        // Use firebase database API to read JSON from the database
        mDatabase = FirebaseDatabase.getInstance().getReference();
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
                            deliveryRequests.add(deliveryRequest);
                            MyDeliveryRequestRowItem rowItem = new MyDeliveryRequestRowItem();
                            if (deliveryRequest.getRequestDateTime() != null) {
                                try {
                                    rowItem.requestDate = SysConfig.getDisplayShortDate(deliveryRequest.getRequestDateTime());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            //Restaurant Name
                            rowItem.restaurantName = deliveryRequest.getRestaurantName();
                            //Request Status
                            rowItem.requestStatus = deliveryRequest.getDeliveryRequestStatus();
                            rowItems.add(rowItem);
                            addTableRow(requestKeys.size()-1,rowItem);
                        }
                        else{
                            MyDeliveryRequestRowItem rowItem = new MyDeliveryRequestRowItem();
                            try{
                                rowItem.requestDate = SysConfig.getDisplayShortDate(deliveryRequest.getRequestDateTime());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            rowItem.restaurantName= deliveryRequest.getRestaurantName();
                            rowItem.requestStatus = deliveryRequest.getDeliveryRequestStatus();
                            updateTableRow(requestKeyIndex,rowItem);
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

    /**
     * Add one row to the table
     * @param index: the index of the row in the table
     * @param rowItem: the rowItem object which contains the text in the row
     */
    private void addTableRow(int index, MyDeliveryRequestRowItem rowItem){
            final String requestKey = requestKeys.get(index);
            final DeliveryRequest deliveryRequest = deliveryRequests.get(index);

            TableRow row = new TableRow(view.getContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            row.setWeightSum(3);
            //Date
            TextView textView = new TextView(view.getContext());
            textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            textView.setGravity(Gravity.LEFT);
            textView.setText(rowItem.requestDate);
            textView.setTag(getString(R.string.request_date));
            row.addView(textView);

            //Restaurant Name
            textView = new TextView(view.getContext());
            textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            textView.setGravity(Gravity.LEFT);
            textView.setText(rowItem.restaurantName);
            textView.setTag(getString(R.string.restaurant_name));
            row.addView(textView);

            //Request Status
            textView = new TextView(view.getContext());
            textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            textView.setGravity(Gravity.LEFT);
            textView.setText(rowItem.requestStatus);
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

    /**
     * Update a particular row in the table
     * @param index: the index of the row to be updated
     * @param rowItem: the RowItem which contains the updated data
     */
    private void updateTableRow(int index, MyDeliveryRequestRowItem rowItem){
        View tableRowView = tableLayout.getChildAt(index);
        if(tableRowView instanceof TableRow){
            TableRow tableRow = (TableRow) tableRowView;
            for(int i = 0; i < tableRow.getChildCount(); i++){
                TextView textView = (TextView)tableRow.getChildAt(i);

                if (textView.getTag() == getString(R.string.request_date)){
                    textView.setText(rowItem.requestDate);
                }
                else if(textView.getTag() == getString(R.string.restaurant_name)){
                    textView.setText(rowItem.restaurantName);
                }
                else if(textView.getTag() == getString(R.string.request_status)){
                    textView.setText(rowItem.requestStatus);
                }
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        // add table Row to the table since the UI needs to be rebuild
        if(tableLayout.getChildCount() <= 0 && rowItems.size() > 0) {
            for (int i = 0; i < rowItems.size(); i++) {
                addTableRow(i, rowItems.get(i));
            }
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
        if (i == R.id.AddFAB) {
            mListener.onCreateDelReqFragmentInteraction();
        }
    }

    //Row Item
    private class MyDeliveryRequestRowItem{
        String requestDate;
        String restaurantName;
        String requestStatus;
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
