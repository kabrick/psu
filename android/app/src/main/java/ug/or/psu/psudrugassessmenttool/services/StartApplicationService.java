package ug.or.psu.psudrugassessmenttool.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class StartApplicationService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            //Toast.makeText(context, "Hello There", Toast.LENGTH_LONG).show();
        }
    }
}
