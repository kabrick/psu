package ug.or.psu.psudrugassessmenttool.users.dashboards.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class GetLocationActivity extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener,
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
    GoogleMap googleMap;
    String instructions_text = "";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

        // set initial text with pharmacy name
        instructions_text = "Setting location for " + pharmacy_name +
                ". When on pharmacy premises, click on blue marker icon in the map to continue";

        initial_text.setText("Please wait while we try to find your location...");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.pharmacy_location_map);
        assert mapFragment != null;
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
        } else {
            this.googleMap = googleMap;
            this.googleMap.setMyLocationEnabled(true);
            this.googleMap.setOnMyLocationButtonClickListener(this);
            this.googleMap.setOnMyLocationClickListener(this);

            // zoom into uganda
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(1.3733, 32.2903), 6));

            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(1);
            locationRequest.setFastestInterval(0);
            //locationRequest.setNumUpdates(1);
            LocationServices.getFusedLocationProviderClient(this)
                    .requestLocationUpdates(
                            locationRequest, locationCallback, Looper.getMainLooper()
                    );
        }
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location location = locationResult.getLastLocation();
            LatLng deviceLocation = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deviceLocation, 20));
            initial_text.setText(instructions_text);
        }
    };

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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0) {
                boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                if (!locationAccepted) {
                    onBackPressed();
                } else {
                    recreate();
                }
            }
        }
    }
}
