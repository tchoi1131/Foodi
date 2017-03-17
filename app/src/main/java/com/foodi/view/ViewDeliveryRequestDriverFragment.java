package com.foodi.view;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.ArrayList;

/**
 * A fragment representing a list of deliveryRequests.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ViewDeliveryRequestDriverFragment extends Fragment {
    //constant used to get arguments passed to intent
    private static final String ARG_COLUMN_COUNT = "column-count";
    public static final String ARG_USERID = "userId";

    private int mColumnCount = 1;       //number of columns
    private String mUserId ;            //user id

    //Listener to communicate with MainMenuActivity
    private OnListFragmentInteractionListener mListener;

    //UI References
    private MyViewDeliveryRequestDriverRecyclerViewAdapter adapter;
    private ArrayList<String> requestKeys = new ArrayList<>();
    private ArrayList<String> offerKeys = new ArrayList<>();
    private ArrayList<DeliveryRequest> deliveryRequests = new ArrayList<>();
    private ArrayList<DeliveryOffer> deliveryOffers = new ArrayList<>();

    private RecyclerView recyclerView;

    //Database references
    private DatabaseReference mDatabase = null;
    private DatabaseReference myRequestRef = null;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ViewDeliveryRequestDriverFragment() {
    }

    public static ViewDeliveryRequestDriverFragment newInstance(String userId, int columnCount) {
        ViewDeliveryRequestDriverFragment fragment = new ViewDeliveryRequestDriverFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putString(ARG_USERID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mUserId = getArguments().getString(ARG_USERID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View outerView = inflater.inflate(R.layout.fragment_view_del_req_driver_view, container, false);
        View listView = outerView.findViewById(R.id.delivery_request_list);

        //setup database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Set the adapter
        if (listView instanceof RecyclerView) {
            Context context = listView.getContext();
            recyclerView = (RecyclerView) listView;
            if (mColumnCount <= 1) {
                LinearLayoutManager linLayoutMgr= new LinearLayoutManager(context);
                recyclerView.setLayoutManager(linLayoutMgr);
                recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), linLayoutMgr.getOrientation()));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new MyViewDeliveryRequestDriverRecyclerViewAdapter(requestKeys, deliveryRequests,
                    offerKeys, deliveryOffers, mListener);
            recyclerView.setAdapter(adapter);

            if(myRequestRef == null) {
                loadData();
            }
        }

        return outerView;
    }

    private void loadData(){
        //read data from database to fill in the UI
        myRequestRef = mDatabase.child(SysConfig.FBDB_DELIVERY_REQUESTS);
        // Attach a listener to read the data
        myRequestRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousKey) {
                DeliveryRequest deliveryRequest = dataSnapshot.getValue(DeliveryRequest.class);

                if(!deliveryRequest.getDeliveryRequestStatus().equals(DeliveryRequest.DELIVERY_REQUEST_STATUS_DELIVERED)) {
                    requestKeys.add(dataSnapshot.getKey());
                    deliveryRequests.add(deliveryRequest);

                    offerKeys.add("");
                    deliveryOffers.add(new DeliveryOffer());

                    DatabaseReference deliveryRequestUserRef = mDatabase.child(SysConfig.FBDB_DELIVERY_REQUEST_USER_OFFER).child(dataSnapshot.getKey()).child(mUserId);
                    deliveryRequestUserRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                DatabaseReference offerRef = mDatabase.child(SysConfig.FBDB_DELIVERY_OFFERS).child(dataSnapshot.getValue().toString());
                                offerRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        DeliveryOffer deliveryOffer = dataSnapshot.getValue(DeliveryOffer.class);
                                        String requestKey = deliveryOffer.getDeliveryRequestKey();
                                        int requestKeyIndex = requestKeys.indexOf(requestKey);
                                        if (requestKeyIndex > offerKeys.size() - 1) {
                                            offerKeys.add(dataSnapshot.getKey());
                                            deliveryOffers.add(deliveryOffer);
                                        } else {
                                            offerKeys.set(requestKeyIndex, dataSnapshot.getKey());
                                            deliveryOffers.set(requestKeyIndex, deliveryOffer);
                                        }
                                        adapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        System.out.println("The read failed: " + databaseError.getCode());
                                    }
                                });
                            } else {
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getCode());
                        }
                    });
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                int requestKeyIndex = requestKeys.indexOf(dataSnapshot.getKey());
                deliveryRequests.set(requestKeyIndex, dataSnapshot.getValue(DeliveryRequest.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface is implemented by MainMenuActivity to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnListFragmentInteractionListener {

        void onViewDeliveryRequestFragmentInteraction(String requestKey, final DeliveryRequest deliveryRequest, final String offerKey, final DeliveryOffer deliveryOffer);
    }
}
