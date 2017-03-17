package com.foodi.view;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.foodi.foodi.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import java.util.ArrayList;

public class DeliveryOfferMapsActivity extends FragmentActivity implements OnMapReadyCallback {
    //Constant to get the Arguments passed to the intent
    public static final String DELIVERY_PATH = "DeliveryPath";
    public static final String MARKER_LOCATIONS = "MarkerLocations";
    public static final String MARKER_NAMES = "MarkerNames";

    private GoogleMap mMap;
    private ArrayList<LatLng> deliveryPath = null;      //Latitude and Longitude of the delivey paths
    private ArrayList<LatLng> markerLocations = null;   //Latitude and Longitude of the Markers on the map
    private ArrayList<String> markerNames = null;       //Name of the markers

    private Double maxLat = null;       //maximum Latitude of paths in the map
    private Double minLat = null;       //minimum Latitude of paths in the map
    private Double maxLng = null;       //maximum Longitude of paths in the map
    private Double minLng = null;       //minimum Longitude of paths in the map

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_offer_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //get values passed from intent
        deliveryPath = getIntent().getParcelableArrayListExtra(DELIVERY_PATH);
        markerLocations = getIntent().getParcelableArrayListExtra(MARKER_LOCATIONS);
        markerNames = getIntent().getStringArrayListExtra(MARKER_NAMES);

        //calculate max and min latitudes and longitudes
        for (LatLng point : deliveryPath) {
            // Find out the maximum and minimum latitudes & longitudes
            // Latitude
            maxLat = maxLat != null ? Math.max(point.latitude, maxLat) : point.latitude;
            minLat = minLat != null ? Math.min(point.latitude, minLat) : point.latitude;

            // Longitude
            maxLng = maxLng != null ? Math.max(point.longitude, maxLng) : point.longitude;
            minLng = minLng != null ? Math.min(point.longitude, minLng) : point.longitude;
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        PolylineOptions polylineOptions = new PolylineOptions();
        mMap = googleMap;

        //setup the LatLngBond for the camera to zoom in and display the path
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(maxLat, maxLng));
        builder.include(new LatLng(minLat, minLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));

        //draw the polyline
        polylineOptions.addAll(deliveryPath);
        polylineOptions.color(Color.BLUE);
        mMap.addPolyline(polylineOptions);

        for(int i = 0; i < markerLocations.size(); i++) {
            //setup the icon to display on the map
            if (markerNames.get(i).equals("Your Location")) {
                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_car)).position(markerLocations.get(i)).title(markerNames.get(i))).showInfoWindow();
            } else if (markerNames.get(i).equals("Restaurant")) {
                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_restaurant)).position(markerLocations.get(i)).title(markerNames.get(i))).showInfoWindow();
            } else if (markerNames.get(i).equals("Destination")) {
                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_destination)).position(markerLocations.get(i)).title(markerNames.get(i))).showInfoWindow();
            }
        }
    }
}
