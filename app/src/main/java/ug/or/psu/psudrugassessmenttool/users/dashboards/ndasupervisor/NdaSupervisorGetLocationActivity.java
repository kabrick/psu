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

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

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
    TextView initial_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nda_supervisor_get_location);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            pharmacy_name = extras.getString("pharmacy_name", "1");
            pharmacy_id = extras.getString("pharmacy_id", "1");
        }

        prefManager = new PreferenceManager(this);
        util = new HelperFunctions(this);

        getLocationButton = (Button)findViewById(R.id.get_location_button);
        initial_text = (TextView)findViewById(R.id.pharmacy_location_map_guide_text);

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
    }

    public void getLocation(View view){
        //set location of pharmacy through the method
        util.setPharmacyLocations(latitude, longitude, altitude, pharmacy_id);
    }
}
