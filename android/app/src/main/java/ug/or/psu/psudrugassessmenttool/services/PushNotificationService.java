package ug.or.psu.psudrugassessmenttool.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONException;
import org.json.JSONObject;

import ug.or.psu.psudrugassessmenttool.MainActivity;
import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class PushNotificationService extends Service {

    public static final String CHANNEL_ID = "push_notification";
    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;

    @Override
    public void onCreate() {
        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        String url = helperFunctions.getIpAddress() + "fetch_notifications.php?id=" + preferenceManager.getDeviceId();

        JsonArrayRequest request = new JsonArrayRequest(url,
                response -> {
                    if (response.length() > 0){
                        JSONObject obj;
                        for (int i = 0; i < response.length(); i++){
                            try {
                                obj = response.getJSONObject(i);
                                String title = obj.getString("title");
                                String message = obj.getString("message");
                                helperFunctions.displayNotification(this, title, message);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    stopSelf();
                }, error -> stopSelf());

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("PSU App")
                .setContentText("Checking for new notifications...")
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

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Push Notification Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
