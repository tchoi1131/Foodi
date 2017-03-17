package com.foodi.view;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.foodi.foodi.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.ui.IconGenerator;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DeliveryOfferMapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public static final String DELIVERY_PATH = "DeliveryPath";
    public static final String MARKER_LOCATIONS = "MarkerLocations";
    public static final String MARKER_NAMES = "MarkerNames";

    private GoogleMap mMap;
    private ArrayList<LatLng> deliveryPath = null;
    private ArrayList<LatLng> markerLocations = null;
    private ArrayList<String> markerNames = null;

    private Double maxLat = null;
    private Double minLat = null;
    private Double maxLng = null;
    private Double minLng = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_offer_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        deliveryPath = getIntent().getParcelableArrayListExtra(DELIVERY_PATH);
        markerLocations = getIntent().getParcelableArrayListExtra(MARKER_LOCATIONS);
        markerNames = getIntent().getStringArrayListExtra(MARKER_NAMES);


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
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        PolylineOptions polylineOptions = new PolylineOptions();
        mMap = googleMap;

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(maxLat, maxLng));
        builder.include(new LatLng(minLat, minLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));

        polylineOptions.addAll(deliveryPath);
        polylineOptions.color(Color.BLUE);
        mMap.addPolyline(polylineOptions);

        for(int i = 0; i < markerLocations.size(); i++) {
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
