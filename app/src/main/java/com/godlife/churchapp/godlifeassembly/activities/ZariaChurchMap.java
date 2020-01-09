package com.godlife.churchapp.godlifeassembly.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;

import com.godlife.churchapp.godlifeassembly.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ZariaChurchMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zaria_church_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in God-Life Assembly Zaria and move the camera
        LatLng godlifeZaria = new LatLng(11.151198, 7.654623);
        mMap.addMarker(new MarkerOptions().position(godlifeZaria).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icons)).title("God-Life Assembly Zaria"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(godlife));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(godlifeZaria, 16));
        //mMap.setBuildingsEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //LatLng godlifeZaria = new LatLng(-34.0000000, 151.0000000);
        //mMap.addMarker(new MarkerOptions().position(godlifeZaria).title("God-Life Assembly Zaria"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(godlife));
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(godlifeZaria, 10));
        //mMap.setBuildingsEnabled(true);
    }
}
