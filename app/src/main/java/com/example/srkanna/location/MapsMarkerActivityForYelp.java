package com.example.srkanna.location;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
public class MapsMarkerActivityForYelp extends AppCompatActivity
        implements OnMapReadyCallback {

    UiSettings uiSettings= null;

    String lati=null;
    String longi=null;
    String endLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps_for_yelp);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.

        Bundle b = getIntent().getExtras();
        Log.d("MapsMarkerAccc", "Location retrieved is"+ b.getString("locationLatAndLong"));
        String temp = b.getString("locationLatAndLong");

        lati = b.getString("latiFromDirectionYelp");
        longi = b.getString("longiFromDirectionYelp");
        endLocation = b.getString("locationformarker");
        String[] output = temp.split(",");
        Log.d("lati is",lati);
        Log.d("longi is", longi);
        Log.d("Address", output[2]);



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng sydney = new LatLng(Double.parseDouble(lati), Double.parseDouble(longi));

        String JSON=null;


        uiSettings = googleMap.getUiSettings();
        googleMap.setMinZoomPreference(3.0f);
        googleMap.setMaxZoomPreference(20.0f);

        uiSettings.setZoomControlsEnabled(true);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Your selection:  "+endLocation));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
