package ug.or.psu.psudrugassessmenttool.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class UploadPharmacistPracticeTime extends Worker {
    private Context context;

    public UploadPharmacistPracticeTime(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        PreferenceManager pref = new PreferenceManager(context);

        String time_in = getInputData().getString("time_in");
        String time_out = getInputData().getString("time_out");
        String working_hours = getInputData().getString("working_hours");

        String network_address = new HelperFunctions(context).getIpAddress()
                + "set_new_attendance.php?psu_id=" + pref.getPsuId()
                + "&time_in=" + time_in
                + "&time_out=" + time_out
                + "&latitude_in=" + pref.getCurrentLatitude()
                + "&longitude_in=" + pref.getCurrentLongitude()
                + "&latitude_out=" + pref.getLastLatitude()
                + "&longitude_out=" + pref.getLastLongitude()
                + "&working_hours=" + working_hours
                + "&pharmacy_id=" + pref.getPharmacyId()
                + "&day_id=" + pref.getDayIn();

        //Setup a RequestFuture object
        RequestFuture<String> future = RequestFuture.newFuture();

        StringRequest request = new StringRequest(network_address, future, future);

        VolleySingleton.getInstance(context).addToRequestQueue(request);

        try {
            // Set an interval for the request to timeout. This will block the
            // worker thread and force it to wait for a response for 60 seconds
            // before timing out and raising an exception
            String response = future.get(60, TimeUnit.SECONDS);

            if (response.equals("1")){
                return Result.success();
            } else {
                return Result.retry();
            }
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            e.printStackTrace();
            return Result.retry();
        }
    }
}
