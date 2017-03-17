package com.foodi.view;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.foodi.foodi.R;
import com.foodi.model.DeliveryOffer;
import com.foodi.model.DeliveryRequest;
import com.foodi.model.SysConfig;
import com.foodi.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;

/**
 * This is the Main Menu Activity. It is a container for all the fragments.
 */

public class MainMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MyDeliveryRequestsFragment.OnFragmentInteractionListener,
        MaintainDeliveryRequestFragment.OnFragmentInteractionListener,
        ViewDeliveryRequestDriverFragment.OnListFragmentInteractionListener,
        ViewDeliveryOfferFragment.OnListFragmentInteractionListener,
        SetDeliveryOfferFragment.OnFragmentInteractionListener ,
        MainMenuFragment.OnFragmentInteractionListener{

    /**
     * Use Firebase Authentication
     */
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    // UI references.
    private TextView mEAddressTextView;
    private TextView mUserNameTextView;

    //User id and User object of the current user
    private String userId;
    private User mUser;

    //Fragment manager
    private FragmentManager fragmentManager;

    //All the fragments
    private MaintainDeliveryRequestFragment maintainDeliveryRequestFragment;
    private MyDeliveryRequestsFragment myDeliveryRequestsFragment;
    private SetDeliveryOfferFragment setDeliveryOfferFragment;
    private ViewDeliveryOfferFragment viewDeliveryOfferFragment;
    private ViewDeliveryRequestDriverFragment viewDeliveryRequestDriverFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //setup reference to UI and action listener for buttons and drawer menu
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main_menu);
        mEAddressTextView = (TextView) headerView.findViewById(R.id.eAddressTextView);
        mUserNameTextView = (TextView) headerView.findViewById(R.id.userNameTextView);

        //get firebase authentication
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        //setup firebase database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //read user info if there is any.
        if (userId != null) {
            mDatabase.child(SysConfig.FBDB_USERS).child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mUser = dataSnapshot.getValue(User.class);
                    mUserNameTextView.setText(mUser.getUserName());
                    mEAddressTextView.setText(mUser.getEmail());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        //get fragment manager and start the fragment
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_main_menu, MainMenuFragment.newInstance(), MainMenuFragment.class.toString())
                .commit();
    }

    //sign out the account
    private void signOut() {
        mAuth.signOut();
        Intent intentLoginActivity = new Intent(MainMenuActivity.this, LoginActivity.class);
        startActivity(intentLoginActivity);
        finish();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //reserve for future use
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_delivery_requests) {
            // start the my delivery request fragment
            myDeliveryRequestsFragment = MyDeliveryRequestsFragment.newInstance(userId);

            // Insert the fragment by replacing any existing fragment
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main_menu, myDeliveryRequestsFragment, MyDeliveryRequestsFragment.class.toString())
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_check_delivery_offers) {
            // start the check delivery offer fragment
            viewDeliveryOfferFragment = ViewDeliveryOfferFragment.newInstance(userId, 1);

            // Insert the fragment by replacing any existing fragment
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main_menu, viewDeliveryOfferFragment, ViewDeliveryOfferFragment.class.getName())
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_browse_delivery_requests) {
            // start the browse delivery requests fragement
            viewDeliveryRequestDriverFragment = ViewDeliveryRequestDriverFragment.newInstance(userId, 1);

            // Insert the fragment by replacing any existing fragment
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main_menu, viewDeliveryRequestDriverFragment, ViewDeliveryRequestDriverFragment.class.toString())
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_email_sign_out) {
            signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onCreateDelReqFragmentInteraction() {
        // start the maintain delivery request fragment to create delivery request
        maintainDeliveryRequestFragment = MaintainDeliveryRequestFragment.newInstance(userId, MaintainDeliveryRequestFragment.NEW_DEL_REQ_MODE);

        // Insert the fragment by replacing any existing fragment
        fragmentManager.beginTransaction()
                .replace(R.id.content_main_menu, maintainDeliveryRequestFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onChangeDelReqStatusFragmentInteraction(final String requestKey, final DeliveryRequest deliveryRequest, DeliveryOffer deliveryOffer) throws ParseException {
        if(deliveryRequest.getDeliveryRequestStatus().equals(DeliveryRequest.DELIVERY_REQUEST_STATUS_DELIVERY_IN_PROGRESS)) {
            //display dialog to ask user to confirm updating status to "Delivered"
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(getString(R.string.update_to_delivered,
                    SysConfig.getDisplayShortDate(deliveryRequest.getRequestDateTime()),
                    SysConfig.getDisplayTime(deliveryRequest.getRequestDateTime()), deliveryRequest.getRestaurantName(),
                    deliveryOffer.getDriverName(), deliveryOffer.getOfferPrice().toString()));
            alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    deliveryRequest.setDeliveryRequestStatus(DeliveryRequest.DELIVERY_REQUEST_STATUS_DELIVERED);
                    mDatabase.child(SysConfig.FBDB_DELIVERY_REQUESTS).child(requestKey).setValue(deliveryRequest);
                    //display message for the status change
                    Toast.makeText(MainMenuActivity.this, getString(R.string.request_status_changed), Toast.LENGTH_LONG).show();
                }
            });

            alertDialogBuilder.setNegativeButton(R.string.no, null);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    @Override
    public void onFinishCreatingRequest() {
        fragmentManager.popBackStack();
    }

    @Override
    public void onViewDeliveryRequestFragmentInteraction(final String requestKey, final DeliveryRequest deliveryRequest, final String offerKey, final DeliveryOffer deliveryOffer) {
        if (offerKey == "") {
            // start a fragment to create new offer
            setDeliveryOfferFragment = SetDeliveryOfferFragment.newInstance(requestKey, offerKey);

            // Insert the fragment by replacing any existing fragment
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main_menu, setDeliveryOfferFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (deliveryOffer.getOfferStatus().equals(DeliveryOffer.DELIVERY_OFFER_STATUS_PENDING_CUSTOMER_REPLY)) {
            //change offer
            setDeliveryOfferFragment = SetDeliveryOfferFragment.newInstance(requestKey, offerKey);

            // Insert the fragment by replacing any existing fragment
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main_menu, setDeliveryOfferFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (deliveryOffer.getOfferStatus().equals(DeliveryOffer.DELIVERY_OFFER_STATUS_PENDING_DRIVER_CONFIRM)) {
            //display dialog to ask user's confirmation on the food delivery
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(getString(R.string.confirm_offer, deliveryRequest.getRestaurantAddressCity(),
                    deliveryRequest.getDeliveryAddressCity(), deliveryOffer.getOfferPrice().toString()));
            alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    //update data
                    deliveryOffer.setOfferStatus(DeliveryOffer.DELIVERY_OFFER_STATUS_CONFIRMED);
                    deliveryRequest.setDeliveryRequestStatus(DeliveryRequest.DELIVERY_REQUEST_STATUS_DELIVERY_IN_PROGRESS);
                    mDatabase.child(SysConfig.FBDB_DELIVERY_OFFERS).child(offerKey).setValue(deliveryOffer);
                    mDatabase.child(SysConfig.FBDB_DELIVERY_REQUESTS).child(requestKey).setValue(deliveryRequest);
                    mDatabase.child(SysConfig.FBDB_DELIVERY_REQUEST_CONFIRMED_OFFER).child(requestKey).setValue(offerKey);
                    Toast.makeText(MainMenuActivity.this, getString(R.string.confirmed_offer), Toast.LENGTH_LONG).show();
                }
            });

            alertDialogBuilder.setNegativeButton(R.string.no, null);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        else{
            //view offer
            setDeliveryOfferFragment = SetDeliveryOfferFragment.newInstance(requestKey, offerKey);

            // Insert the fragment by replacing any existing fragment
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main_menu, setDeliveryOfferFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onViewDeliveryOfferFragmentInteraction(final String key, final DeliveryOffer deliveryOffer) {
        if(deliveryOffer.getOfferStatus().equals(DeliveryOffer.DELIVERY_OFFER_STATUS_PENDING_CUSTOMER_REPLY)) {
            //display dialog to ask if user really want to accept the offer
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(getString(R.string.acceptOffer, deliveryOffer.getDriverName()));
            alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    //update offer status to "Pending driver confirmation.
                    deliveryOffer.setOfferStatus(DeliveryOffer.DELIVERY_OFFER_STATUS_PENDING_DRIVER_CONFIRM);
                    mDatabase.child(SysConfig.FBDB_DELIVERY_OFFERS).child(key).setValue(deliveryOffer);
                    Toast.makeText(MainMenuActivity.this, getString(R.string.acceptedOffer, deliveryOffer.getDriverName()), Toast.LENGTH_LONG).show();
                }
            });

            alertDialogBuilder.setNegativeButton(R.string.no, null);

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    @Override
    public void onSetDeliveryOfferFragmentInteraction(String requestKey, double offerPrice, String estDeliveryTime) {
        //Create delivery offer and store in database
        DeliveryOffer deliveryOffer = new DeliveryOffer(mUser.getUserName(), offerPrice, estDeliveryTime, DeliveryOffer.DELIVERY_OFFER_STATUS_PENDING_CUSTOMER_REPLY, requestKey);
        String offerKey = mDatabase.child(SysConfig.FBDB_DELIVERY_OFFERS).push().getKey();
        mDatabase.child(SysConfig.FBDB_DELIVERY_REQUEST_USER_OFFER).child(requestKey).child(mUser.getUserId()).setValue(offerKey);
        mDatabase.child(SysConfig.FBDB_USER_DELIVERY_OFFER).child(mUser.getUserId()).child(offerKey).setValue(true);
        mDatabase.child(SysConfig.FBDB_DELIVERY_OFFERS).child(offerKey).setValue(deliveryOffer);
        fragmentManager.popBackStack();
    }

    @Override
    public void onMainMenuFragmentInteraction(int id) {
        if (id == R.id.my_delivery_requests_btn) {
            //start the my delivery request fragment
            myDeliveryRequestsFragment = MyDeliveryRequestsFragment.newInstance(userId);

            // Insert the fragment by replacing any existing fragment
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main_menu, myDeliveryRequestsFragment, MyDeliveryRequestsFragment.class.toString())
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.check_delivery_offers_btn) {
            //start the view delivery offer fragment
            viewDeliveryOfferFragment = ViewDeliveryOfferFragment.newInstance(userId, 1);

            // Insert the fragment by replacing any existing fragment
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main_menu, viewDeliveryOfferFragment, ViewDeliveryOfferFragment.class.getName())
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.browse_delivery_request_btn) {
            // start the browse delivery request fragment
            viewDeliveryRequestDriverFragment = ViewDeliveryRequestDriverFragment.newInstance(userId, 1);

            // Insert the fragment by replacing any existing fragment
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main_menu, viewDeliveryRequestDriverFragment, ViewDeliveryRequestDriverFragment.class.toString())
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        //handle the request code returned after requesting for permission
        switch (requestCode) {
            case SetDeliveryOfferFragment.MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        setDeliveryOfferFragment.getEstimatedTime();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
        }
    }

    /**
     * update the duration in the SetDeliveryOffer Fragment to display the estimation delivery duration
     */
    public void updateDurationSetDeliveryOfferFragment(){
        setDeliveryOfferFragment.updateDuration();
    }
}
