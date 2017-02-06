package com.foodi.view;

import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foodi.foodi.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link ManageDeliveryRequestsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ManageDeliveryRequestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageDeliveryRequestsFragment extends Fragment implements View.OnClickListener{

    public static final String ARG_USERID = "userId";


    private DatabaseReference mDatabase;
    private String mUserId;

    private OnFragmentInteractionListener mListener;

    public ManageDeliveryRequestsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userId Parameter 1.
     * @return A new instance of fragment ManageDeliveryRequestsFragment.
     */
    public static ManageDeliveryRequestsFragment newInstance(String userId) {
        ManageDeliveryRequestsFragment fragment = new ManageDeliveryRequestsFragment();
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
        View view = inflater.inflate(R.layout.fragment_manage_delivery_requests, container, false);
        FloatingActionButton mCreateDeliveryRequestFAB = (FloatingActionButton) view.findViewById(R.id.AddFAB);
        mCreateDeliveryRequestFAB.setOnClickListener(this);

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
    }

}
