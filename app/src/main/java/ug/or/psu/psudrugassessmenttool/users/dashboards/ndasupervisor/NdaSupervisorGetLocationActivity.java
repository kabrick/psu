package ug.or.psu.psudrugassessmenttool.users.dashboards.ndasupervisor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;

public class NdaSupervisorGetLocationActivity extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {

    PreferenceManager prefManager;
    HelperFunctions util;
    double latitude;
    double longitude;
    double altitude;
    Button getLocationButton;
    String pharmacy_id;
    String pharmacy_name;
    String location_status;
    TextView initial_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nda_supervisor_get_location);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            pharmacy_name = extras.getString("pharmacy_name", "1");
            pharmacy_id = extras.getString("pharmacy_id", "1");
            location_status = extras.getString("status", "0");
        }

        prefManager = new PreferenceManager(this);
        util = new HelperFunctions(this);

        getLocationButton = findViewById(R.id.get_location_button);
        initial_text = findViewById(R.id.pharmacy_location_map_guide_text);

        //set initial text with pharmacy name
        String text = "Setting location for " + pharmacy_name +
                ". When on pharmacy premises, click on blue marker icon in the map to continue";

        initial_text.setText(text);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.pharmacy_location_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        altitude = location.getAltitude();

        getLocationButton.setVisibility(View.VISIBLE);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        assert googleMap != null;
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnMyLocationClickListener(this);

        //zoom into uganda
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(1.3733, 32.2903), 6));
    }

    public void getLocation(View view){
        //check if it is a new record or editing
        if(location_status.equals("0")){
            //new record
            util.setPharmacyLocations(latitude, longitude, altitude, pharmacy_id);
        } else {
            //update record
            util.editPharmacyLocations(latitude, longitude, altitude, pharmacy_id);
        }
    }
}
