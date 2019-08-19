package ug.or.psu.psudrugassessmenttool.helpers;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.android.volley.NetworkError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import java.util.concurrent.TimeUnit;

import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;
import ug.or.psu.psudrugassessmenttool.services.TrackPharmacistService;
import ug.or.psu.psudrugassessmenttool.users.authentication.SignInActivity;
import ug.or.psu.psudrugassessmenttool.users.dashboards.ndaadmin.NdaAdminDashboard;
import ug.or.psu.psudrugassessmenttool.users.dashboards.ndasupervisor.NdaSupervisorDashboard;
import ug.or.psu.psudrugassessmenttool.users.dashboards.psuadmin.PsuAdminDashboard;
import ug.or.psu.psudrugassessmenttool.users.dashboards.psupharmacist.PsuPharmacistDashboard;
import ug.or.psu.psudrugassessmenttool.users.dashboards.psupharmacyowner.PsuPharmacyOwnerDashboard;
import ug.or.psu.psudrugassessmenttool.users.dashboards.sysadmin.SystemsAdministratorDashboard;

public class HelperFunctions {

    private Context context;
    private static PreferenceManager prefManager;
    private static ProgressDialog progressDialog;

    /**
     * constructor for the class
     *
     * @param context context to use the methods in
     */
    public HelperFunctions(Context context) {
        this.context = context;
        prefManager = new PreferenceManager(this.context);
    }

    /**
     * get ip address linked to application
     *
     * @return ip address string
     */
    public String getIpAddress() {
        //return "https://phasouganda.000webhostapp.com/";
        //return "http://psucop.com/psu_assessment_tool/";
        //return "https://psucop.000webhostapp.com/";
        return "https://psucop.com/psu_assessment_tool/";
    }

    /**
     * display alert dialog anywhere in app
     *
     * @param message text to display
     */
    public void genericDialog(String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setMessage(message).setPositiveButton("Okay", (dialogInterface, i) -> {
            //
        }).show();
    }

    /**
     * generic snackbar passing the message and view
     *
     * @param message text to display
     * @param view view to display on
     */
    public void genericSnackbar(String message, View view){
        Snackbar mySnackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        mySnackbar.show();
    }

    public float getDistance(double lat1, double long1, double lat2, double long2){
        float[] result = new float[1];

        Location.distanceBetween(lat1, long1, lat2, long2, result);

        return result[0];
    }

    /**
     * check phone connection status
     *
     * @return boolean status of network
     */
    public boolean getConnectionStatus() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    /**
     * progress bar to show activity during network operations
     *
     * @param message text to be displayed
     */
    public void genericProgressBar(String message){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", (dialogInterface, i) -> VolleySingleton.getInstance(context).cancelPendingRequests());
        progressDialog.show();
    }

    /**
     * stop progress bar showing network activity status
     */
    public void stopProgressBar(){
        progressDialog.dismiss();
    }

    /**
     * sign out users who are not pharmacists
     */
    public void signAdminUsersOut(){
        //reset the psu id
        prefManager.setPsuId("");

        //reset the member category
        prefManager.setMemberCategory("");

        //set sign in status to false
        prefManager.setSignedIn(false);

        //go to sign in page
        Intent sign_out_intent = new Intent(context, SignInActivity.class);
        context.startActivity(sign_out_intent);
    }

    /**
     * set pharmacy location in database
     *
     * @param latitude latitude of pharmacy
     * @param longitude longitude of pharmacy
     * @param altitude altitude of pharmacy
     * @param pharmacy_id id of the pharmacy
     */
    public void setPharmacyLocations(double latitude, double longitude,
                             double altitude, String pharmacy_id){

        //start progress bar
        genericProgressBar("Saving pharmacy location...");

        String network_address = getIpAddress()
                + "set_new_location.php?latitude=" + latitude
                + "&longitude=" + longitude
                + "&altitude=" + altitude
                + "&pharmacy=" + pharmacy_id
                + "&id=" + prefManager.getPsuId();

        // Request a string response from the provided URL.
        StringRequest request = new StringRequest(network_address,
                response -> {
                    //stop progress bar
                    stopProgressBar();

                    //check if location has been saved successfully
                    if(response.equals("1")){
                        //go back to user dashboard
                        AlertDialog.Builder alert = new AlertDialog.Builder(context);

                        alert.setMessage("Pharmacy location set").setPositiveButton("Okay", (dialogInterface, i) -> getDefaultDashboard(prefManager.getMemberCategory())).show();
                    } else {
                        //did not save
                        genericDialog("Something went wrong! Please try again");
                    }
                }, error -> {
                    //stop progress bar
                    stopProgressBar();

                    if (error instanceof TimeoutError || error instanceof NetworkError) {
                        genericDialog("Something went wrong. Please make sure you are connected to a working internet connection.");
                    } else {
                        genericDialog("Something went wrong. Please try again later");
                    }
                });

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     * edit pharmacy location in database
     *
     * @param latitude latitude of pharmacy
     * @param longitude longitude of pharmacy
     * @param altitude altitude of pharmacy
     * @param pharmacy_id id of the pharmacy
     */
    public void editPharmacyLocations(double latitude, double longitude,
                                     double altitude, String pharmacy_id){

        //start progress bar
        genericProgressBar("Updating pharmacy location...");

        String network_address = getIpAddress()
                + "edit_location.php?latitude=" + latitude
                + "&longitude=" + longitude
                + "&altitude=" + altitude
                + "&pharmacy=" + pharmacy_id
                + "&id=" + prefManager.getPsuId();

        // Request a string response from the provided URL.
        StringRequest request = new StringRequest(network_address,
                response -> {
                    //stop progress bar
                    stopProgressBar();

                    //check if location has been saved successfully
                    if(response.equals("1")){
                        //go back to user dashboard
                        AlertDialog.Builder alert = new AlertDialog.Builder(context);

                        alert.setMessage("Pharmacy location edited").setPositiveButton("Okay", (dialogInterface, i) -> getDefaultDashboard(prefManager.getMemberCategory())).show();
                    } else {
                        //did not save
                        genericDialog("Something went wrong! Please try again");
                    }
                }, error -> {
                    //stop progress bar
                    stopProgressBar();

                    if (error instanceof TimeoutError || error instanceof NetworkError) {
                        genericDialog("Something went wrong. Please make sure you are connected to a working internet connection.");
                    } else {
                        genericDialog("Something went wrong. Please try again later");
                    }
                });

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     * After login take the user to their respective dashboards
     *
     * @param member_category member category id of user
     */
    public void getDefaultDashboard(String member_category){
        switch (member_category) {
            case "1": {
                // go to systems administrator dashboard
                Intent intent_sys_admin = new Intent(context, SystemsAdministratorDashboard.class);
                context.startActivity(intent_sys_admin);
                break;
            }
            case "2": {
                // go to psu administrator dashboard
                Intent intent_nda_admin = new Intent(context, PsuAdminDashboard.class);
                context.startActivity(intent_nda_admin);
                break;
            }
            case "3": {
                // go to pharmacist dashboard
                Intent intent_psu_pharmacist = new Intent(context, PsuPharmacistDashboard.class);
                context.startActivity(intent_psu_pharmacist);
                break;
            }
            case "4": {
                // go to pharmacy owner dashboard
                Intent intent_pharmacy_owner = new Intent(context, PsuPharmacyOwnerDashboard.class);
                context.startActivity(intent_pharmacy_owner);
                break;
            }
            case "5": {
                // go to nda administrator
                Intent intent_nda_admin = new Intent(context, NdaAdminDashboard.class);
                context.startActivity(intent_nda_admin);
                break;
            }
            case "6": {
                //go to nda supervisor
                Intent intent_nda_supervisor = new Intent(context, NdaSupervisorDashboard.class);
                context.startActivity(intent_nda_supervisor);
                break;
            }
            default: {
                // user details not set so clear all prefs and log out
                signAdminUsersOut();
                break;
            }
        }
    }

    public void addNewsRead(int id) throws JSONException {
        JSONArray array = prefManager.getNewsRead();

        //add id to the array here
        array.put(id);

        //set new array
        prefManager.setNewsRead(array);
    }

    public boolean isNewsRead(int id) throws JSONException {

        //get the array
        JSONArray array = prefManager.getNewsRead();

        for (int i = 0; i < array.length(); i++){
            if(id == array.getInt(i)){
                return true;
            }
        }

        return false;
    }

    @SuppressLint("MissingPermission")
    public void setCurrentLocation(){
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        prefManager.setCurrentLatitude(location.getLatitude());
                        prefManager.setCurrentLongitude(location.getLongitude());
                    }
                });
    }

    public void signPharmacistOut(){
        //start progress bar
        genericProgressBar("Logging you out...");

        //get the timestamp out
        Long time_out = System.currentTimeMillis();
        Long time_in = prefManager.getTimeIn();
        Long time_diff = time_out - time_in;

        //get working hours
        Long working_hours = TimeUnit.MILLISECONDS.toHours(time_diff);
        Long working_minutes = TimeUnit.MILLISECONDS.toMinutes(time_diff) % 60;

        final String content_text = "You have been logged out after a duration of " + working_hours + " hour(s) and " + working_minutes + " minute(s)";

        String network_address = getIpAddress()
                + "set_new_attendance.php?psu_id=" + prefManager.getPsuId()
                + "&time_in=" + time_in
                + "&time_out=" + time_out
                + "&latitude_in=" + prefManager.getCurrentLatitude()
                + "&longitude_in=" + prefManager.getCurrentLongitude()
                + "&latitude_out=" + prefManager.getLastLatitude()
                + "&longitude_out=" + prefManager.getLastLongitude()
                + "&working_hours=" + working_hours
                + "&pharmacy_id=" + prefManager.getPharmacyId()
                + "&day_id=" + prefManager.getDayIn();

        // Request a string response from the provided URL.
        StringRequest request = new StringRequest(network_address,
                response -> {
                    //stop progress bar
                    stopProgressBar();

                    //check if location has been saved successfully
                    if(response.equals("1")){

                        //set location set to false
                        prefManager.setIsPharmacyLocationSet(false);

                        //clear the service
                        Intent intent = new Intent(context, TrackPharmacistService.class);
                        intent.setAction("stop");
                        context.startService(intent);

                        genericDialog(content_text);
                    } else {
                        //did not save
                        genericDialog("Something went wrong! Please try again");
                    }
                }, error -> {
                    //stop progress bar
                    stopProgressBar();

                    if (error instanceof TimeoutError || error instanceof NetworkError) {
                        genericDialog("Something went wrong. Please make sure you are connected to a working internet connection.");
                    } else {
                        genericDialog("Something went wrong. Please try again later");
                    }
                });

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void openAppStore(Context context) {
        // you can also use BuildConfig.APPLICATION_ID
        String appId = context.getPackageName();
        Intent rateIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + appId));
        boolean marketFound = false;

        // find all applications able to handle our rateIntent
        final List<ResolveInfo> otherApps = context.getPackageManager()
                .queryIntentActivities(rateIntent, 0);
        for (ResolveInfo otherApp: otherApps) {
            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName
                    .equals("com.android.vending")) {

                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                );
                // make sure it does NOT open in the stack of your activity
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // task re-parenting if needed
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                // if the Google Play was already open in a search result
                //  this make sure it still go to the app page you requested
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // this make sure only the Google Play app is allowed to
                // intercept the intent
                rateIntent.setComponent(componentName);
                context.startActivity(rateIntent);
                marketFound = true;
                break;

            }
        }

        // if GP not present on device, open web browser
        if (!marketFound) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id="+appId));
            context.startActivity(webIntent);
        }
    }

}
