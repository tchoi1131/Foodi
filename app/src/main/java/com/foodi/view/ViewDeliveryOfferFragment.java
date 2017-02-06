package com.foodi.view;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foodi.foodi.R;
import com.foodi.model.DeliveryOffer;
import com.foodi.model.SysConfig;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ViewDeliveryOfferFragment extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";
    public static final String ARG_USERID = "userId";

    private int mColumnCount = 1;
    private String mUserId ;
    private ViewDeliveryOfferFragment.OnListFragmentInteractionListener mListener;

    private MyViewDeliveryOfferRecyclerViewAdapter adapter;    ArrayList<String> requestKeys = new ArrayList<>();
    ArrayList<String> offerKeys = new ArrayList<>();
    ArrayList<DeliveryOffer> items = new ArrayList<>();

    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ViewDeliveryOfferFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ViewDeliveryOfferFragment newInstance(String userId, int columnCount) {
        ViewDeliveryOfferFragment fragment = new ViewDeliveryOfferFragment();
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
        View view = inflater.inflate(R.layout.fragment_view_del_offer_list, container, false);

        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

        items = new ArrayList<>();

        DatabaseReference userRequestsRef = mDatabase.child(SysConfig.FBDB_USER_DELIVERY_REQUESTS).child(mUserId);
        // Attach a listener to read the data
        userRequestsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> deliveryRequestIds = dataSnapshot.getChildren().iterator();
                while(deliveryRequestIds.hasNext()) {
                    DataSnapshot nextRequestId = deliveryRequestIds.next();
                    DatabaseReference requestOfferRef= mDatabase.child(SysConfig.FBDB_DELIVERY_REQUEST_OFFER).child(nextRequestId.getKey());
                    requestOfferRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterator<DataSnapshot> offerIds = dataSnapshot.getChildren().iterator();
                            while(offerIds.hasNext()) {
                                DataSnapshot nextOfferId = offerIds.next();
                                DatabaseReference offerRef= mDatabase.child(SysConfig.FBDB_DELIVERY_OFFERS).child(nextOfferId.getKey());
                                offerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        DeliveryOffer deliveryOffer = dataSnapshot.getValue(DeliveryOffer.class);
                                        offerKeys.add(dataSnapshot.getKey());
                                        items.add(deliveryOffer);
                                        adapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        System.out.println("The read failed: " + databaseError.getCode());
                                    }
                                });
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
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new MyViewDeliveryOfferRecyclerViewAdapter(offerKeys, items, mListener);
            recyclerView.setAdapter(adapter);
        }
        return view;
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
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onViewDeliveryOfferFragmentInteraction(String key, DeliveryOffer item, String offerStatus);
    }
}
