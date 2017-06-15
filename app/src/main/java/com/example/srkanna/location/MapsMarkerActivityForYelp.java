package com.example.srkanna.location;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
public class MapsMarkerActivityForYelp extends AppCompatActivity
        implements OnMapReadyCallback {

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
        String[] output = temp.split(",");
        Log.d("lati is",lati);
        Log.d("longi is", longi);
        Log.d("Address", output[2]);



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng sydney = new LatLng(Double.parseDouble(lati), Double.parseDouble(longi));
       // String tempLatlong = DirectionsUtils.buildURLForMapMarker("Corvallis" , "Pune");
       // Log.d("locccc",tempLatlong);
        String JSON=null;
//        try {
//             JSON = OpenWeatherNetworkUtils.doHTTPGet(tempLatlong);
//            Log.d("MapMarkkerActiviy","Json return is"+JSON);
//
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            Log.d("sadsa","reached here blah");
//        }


//        Map<String,String> latAndLong = DirectionsUtils.returnLatAndLong(JSON);
//
//        Log.d("lat",latAndLong.get("lat"));
//        Log.d("long",latAndLong.get("long"));
        googleMap.setMinZoomPreference(13.0f);
        googleMap.setMaxZoomPreference(20.0f);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in "+endLocation));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
