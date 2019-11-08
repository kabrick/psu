package ug.or.psu.psudrugassessmenttool.workers;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import ug.or.psu.psudrugassessmenttool.services.PushNotificationService;

public class FetchPushNotifications extends Worker {

    private Context context;

    public FetchPushNotifications(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, PushNotificationService.class));
        } else {
            context.startService(new Intent(context, PushNotificationService.class));
        }
        return Result.success();
    }
}
