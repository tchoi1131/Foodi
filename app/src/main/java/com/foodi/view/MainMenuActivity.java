package com.foodi.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.foodi.foodi.R;
import com.foodi.model.DeliveryOffer;
import com.foodi.model.DeliveryRequest;
import com.foodi.model.SysConfig;
import com.foodi.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class MainMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MyDeliveryRequestsFragment.OnFragmentInteractionListener,
        MaintainDeliveryRequestFragment.OnFragmentInteractionListener,
        ViewDeliveryRequestDriverFragment.OnListFragmentInteractionListener,
        ViewDeliveryOfferFragment.OnListFragmentInteractionListener{
    //Tag for debug
    private static final String TAG = "MainMenuActivity";

    /**
     * Use Firebase Authentication
     */
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    // UI references.
    private TextView mEAddressTextView;

    private String userId;
    private User user;

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main_menu);
        mEAddressTextView = (TextView) headerView.findViewById(R.id.eAddressTextView);

        //get firebase authentication
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(SysConfig.FBDB_USERS).child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Setup Authentication Listener
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    mEAddressTextView.setText(user.getEmail());
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Intent intentLoginActivity = new Intent(MainMenuActivity.this, LoginActivity.class);
                    startActivity(intentLoginActivity);
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    finish();
                }
            }
        };
    }

    private void signOut() {
        mAuth.signOut();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
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

        //noinspection SimplifiableIfStatement
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
            // Handle the customer order action
            Fragment fragment = MyDeliveryRequestsFragment.newInstance(userId);

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main_menu, fragment)
                    .commit();
        } else if (id == R.id.nav_check_delivery_offers) {
            // Handle the customer order action
            Fragment fragment = ViewDeliveryOfferFragment.newInstance(userId,1);

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main_menu, fragment)
                    .commit();
        } else if (id == R.id.nav_manage_delivery_offers) {
            // Handle the customer order action
            Fragment fragment = ViewDeliveryRequestDriverFragment.newInstance(userId,1);

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main_menu, fragment)
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
        // Handle the customer order action
        Fragment fragment = MaintainDeliveryRequestFragment.newInstance(userId,MaintainDeliveryRequestFragment.NEW_DEL_REQ_MODE);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_main_menu, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onFinishCreatingRequest() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack();
    }

    @Override
    public void onViewDeliveryRequestFragmentInteraction(String requestKey, DeliveryRequest item, double offerPrice) {
        DeliveryOffer deliveryOffer = new DeliveryOffer(item.getOrderNumber(),user.getUserName(),offerPrice, Calendar.getInstance().getTime(), DeliveryOffer.DELIVERY_Offer_STATUS_WAITING_FOR_REPLY);
        String offerKey = mDatabase.child(SysConfig.FBDB_DELIVERY_OFFERS).push().getKey();
        mDatabase.child(SysConfig.FBDB_DELIVERY_OFFERS).child(offerKey).setValue(deliveryOffer);
        mDatabase.child(SysConfig.FBDB_DELIVERY_REQUEST_OFFER).child(requestKey).child(offerKey).setValue("");
    }

    @Override
    public void onViewDeliveryOfferFragmentInteraction(String key, DeliveryOffer item, String offerStatus) {

    }
}
