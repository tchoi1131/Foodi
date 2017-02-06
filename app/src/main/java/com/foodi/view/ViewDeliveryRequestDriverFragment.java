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
import com.foodi.model.DeliveryRequest;
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
public class ViewDeliveryRequestDriverFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    public static final String ARG_USERID = "userId";

    private int mColumnCount = 1;
    private String mUserId ;
    private OnListFragmentInteractionListener mListener;

    private MyViewDeliveryRequestDriverRecyclerViewAdapter adapter;
    ArrayList<String> requestKeys = new ArrayList<>();
    ArrayList<String> offerKeys = new ArrayList<>();
    ArrayList<DeliveryRequest> items = new ArrayList<>();

    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ViewDeliveryRequestDriverFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
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
        View view = inflater.inflate(R.layout.fragment_view_del_req_driver_list, container, false);
        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

        items = new ArrayList<>();

        DatabaseReference myRef = mDatabase.child(SysConfig.FBDB_USER_DELIVERY_REQUESTS);
        // Attach a listener to read the data
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> userDeliveryRequestIds = dataSnapshot.getChildren().iterator();
                while(userDeliveryRequestIds.hasNext()) {
                    DataSnapshot nextSnapshot = userDeliveryRequestIds.next();
                    requestKeys.add(nextSnapshot.getKey());
                    DeliveryRequest deliveryRequest = nextSnapshot.getValue(DeliveryRequest.class);
                    items.add(deliveryRequest);
                }
                adapter.notifyDataSetChanged();
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
            adapter = new MyViewDeliveryRequestDriverRecyclerViewAdapter(offerKeys, items, mListener);
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

        void onViewDeliveryRequestFragmentInteraction(String key, DeliveryRequest item, double offerPrice);
    }
}
