package com.lucas.signpost;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private GoogleMap mMap;

    private Location initial;

    private final LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(@NonNull Location location) {
            LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
        }

    };

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!canAccessLocation()) {
            requestPermissions(INITIAL_PERMS, 1);
        }

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        LocationManager locationMgr = (LocationManager)getSystemService(LOCATION_SERVICE);

        initial = locationMgr.getLastKnownLocation(LocationManager.FUSED_PROVIDER);
        locationMgr.requestLocationUpdates(LocationManager.FUSED_PROVIDER, 0, 0, locationListener);
        locationMgr.requestLocationUpdates(LocationManager.FUSED_PROVIDER, 10000, 10, locationListener);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     *
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng pos = new LatLng(0, 0);
        if (initial != null) {
            pos = new LatLng(initial.getLatitude(), initial.getLongitude());
        }

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setCompassEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(19));
    }

    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        return(PackageManager.PERMISSION_GRANTED==checkSelfPermission(perm));
    }

}

      