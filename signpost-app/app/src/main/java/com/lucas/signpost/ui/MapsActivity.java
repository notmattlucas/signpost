package com.lucas.signpost.ui;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.Window;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lucas.signpost.MainApplication;
import com.lucas.signpost.R;
import com.lucas.signpost.SearchResultListDialogFragment;
import com.lucas.signpost.model.Loc;
import com.lucas.signpost.viewmodel.MessagesViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        ((MainApplication)getApplicationContext()).applicationComponent.injectMaps(this);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        // Associate searchable configuration with the SearchView
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        MessagesViewModel repository = this.messages;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                repository.search(query, messages -> {
                    messages.forEach(message -> {
                        message.setMessage(message.getMessage().replaceAll(query, "<b>" + query + "</b>"));
                    });
                    searchView.setQuery("", false);
                    searchView.clearFocus();
                    searchView.setIconified(true);
                    SearchResultListDialogFragment dialog = SearchResultListDialogFragment.newInstance(messages, repository);
                    dialog.show(getSupportFragmentManager(), "Search Results");
                });
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        configureMap();
        updateMapPosition(getCurrentPosition());
        messages.getPosition().observeForever(position -> {
            moveCamera(new LatLng(position.getLatitude(), position.getLongitude()));
        });
        messages.getMessages().observeForever(this::drawMarkers);
        map.setOnMapClickListener(latLng -> {
            WriteMessageFragment modalBottomSheet = new WriteMessageFragment(messages, loc(latLng));
            modalBottomSheet.show(getSupportFragmentManager(), "Modal Bottom Sheet");
        });
    }

    private void drawMarkers(com.lucas.signpost.model.Messages messages) {
        clearMarkers();
        messages.forEach(this::drawMarker);
    }

    private void drawMarker(com.lucas.signpost.model.Message message) {
        Loc loc = message.getLocation();
        LatLng position = new LatLng(loc.getLatitude(), loc.getLongitude());
        float hue = message.owned() ? BitmapDescriptorFactory.HUE_AZURE : BitmapDescriptorFactory.HUE_ORANGE;
        BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(hue);
        Marker marker = map.addMarker(new MarkerOptions()
                .position(position)
                .icon(icon)
                .title(message.getMessage())
        );
        marker.showInfoWindow();
        markers.add(marker);
    }

    private void clearMarkers() {
        markers.forEach(Marker::remove);
        markers.clear();
    }

    private void updateMapPosition(LatLng pos) {
        Loc loc = loc(pos);
        messages.update(loc);
    }

    private void moveCamera(LatLng pos) {
        map.animateCamera(CameraUpdateFactory.newLatLng(pos));
    }

    @NonNull
    private static Loc loc(LatLng pos) {
        return new Loc(pos.latitude, pos.longitude);
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

      