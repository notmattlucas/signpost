package com.lucas.signpost;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.lucas.signpost.model.Loc;
import com.lucas.signpost.viewmodel.MessagesViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private GoogleMap map;

    private Location initial;

    @Inject
    MessagesViewModel messages;

    private List<Marker> markers = new ArrayList<>();

    private final LocationListener locationListener = location -> {
        LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
        updateMapPosition(pos);
    };

    @RequiresApi(api = Build.VERSION_CODES.S)
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainApplication)getApplicationContext()).applicationComponent.inject(this);

        if (!canAccessLocation()) {
            requestPermissions(INITIAL_PERMS, 1);
        }

        DataBindingUtil.setContentView(this, R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        configureMap();
        LatLng pos = getCurrentPosition();
        updateMapPosition(pos);
        messages.getMessages().observeForever(messages -> {
            messages.forEach(message -> {
                Loc loc = message.getLocation();
                LatLng position = new LatLng(loc.getLatitude(), loc.getLongitude());
                Marker marker = map.addMarker(new MarkerOptions()
                        .position(position)
                        .title(message.getMessage())
                );
                markers.add(marker);
            });
        });
    }

    private void updateMapPosition(LatLng pos) {
        map.moveCamera(CameraUpdateFactory.newLatLng(pos));
        Loc loc = new Loc(pos.latitude, pos.longitude);
        messages.update(loc);
    }

    private void configureMap() {
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setCompassEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        map.moveCamera(CameraUpdateFactory.zoomTo(19));
    }

    @NonNull
    private LatLng getCurrentPosition() {
        LatLng pos = new LatLng(0, 0);
        if (initial != null) {
            pos = new LatLng(initial.getLatitude(), initial.getLongitude());
        }
        return pos;
    }

    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        return(PackageManager.PERMISSION_GRANTED==checkSelfPermission(perm));
    }

}

      