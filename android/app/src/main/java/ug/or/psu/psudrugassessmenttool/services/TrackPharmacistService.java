package ug.or.psu.psudrugassessmenttool.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.MainActivity;
import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;

public class TrackPharmacistService extends Service {

    public static final String CHANNEL_ID = "trackPharmacistChannel";
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;
    int check_out_of_range = 0;
    int minutes_taken = 0;

    @Override
    public void onCreate() {
        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Location location = locationResult.getLastLocation();

                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                //set the last results
                preferenceManager.setLastLatitude(latitude);
                preferenceManager.setLastLongitude(longitude);

                double pharmacy_latitude = preferenceManager.getPharmacyLatitude();
                double pharmacy_longitude = preferenceManager.getPharmacyLongitude();

                //calculate distance here
                float distance = getDistance(latitude, longitude ,pharmacy_latitude, pharmacy_longitude);

                //check if distance is more than 100m and add to counter
                if(distance > 100){
                    //user out of range, increment by 1
                    check_out_of_range++;
                } else {
                    //user in range
                    check_out_of_range = 0;
                }

                // increment the minutes passed
                minutes_taken += 10;

                //if counter is more than 2 or 6 hours have passed then log the user out
                if(check_out_of_range > 2 || minutes_taken > 360){
                    // log the user out
                    helperFunctions.signPharmacistOut(true);
                }
            }
        };

        createLocationRequest();
        requestLocationUpdates();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (Objects.requireNonNull(intent.getAction()).equals("stop")){
            //remove the location updates
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);

            //kill the service
            stopSelf();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Track Pharmacist Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("PSU App")
                .setContentText("You are currently at your pharmacy.")
                .setSmallIcon(R.drawable.logo)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("RestrictedApi")
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(600000); // 10 minutes
        mLocationRequest.setFastestInterval(300000); // 5 minutes
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void requestLocationUpdates() {
        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.getMainLooper());
        } catch (SecurityException unlikely) {
            //
        }
    }

    public float getDistance(double lat1, double long1, double lat2, double long2){
        float[] result = new float[1];

        Location.distanceBetween(lat1, long1, lat2, long2, result);

        return result[0];
    }

}
