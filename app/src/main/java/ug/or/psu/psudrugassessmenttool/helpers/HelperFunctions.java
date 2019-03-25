package ug.or.psu.psudrugassessmenttool.helpers;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;

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
        return "https://phasouganda.000webhostapp.com/";
        //return "http://psucop.com/psu_assessment_tool/";
    }

    /**
     * get the start year for the application
     *
     * @return start year
     */
    public int getStartYear() {
        return 2018;
    }

    /**
     * display alert dialog anywhere in app
     *
     * @param message text to display
     */
    public void genericDialog(String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setMessage(message).setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //
            }
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
                + "set_new_location.php?latitude=" + String.valueOf(latitude)
                + "&longitude=" + String.valueOf(longitude)
                + "&altitude=" + String.valueOf(altitude)
                + "&pharmacy=" + pharmacy_id
                + "&id=" + prefManager.getPsuId();

        // Request a string response from the provided URL.
        StringRequest request = new StringRequest(network_address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //stop progress bar
                        stopProgressBar();

                        //check if location has been saved successfully
                        if(response.equals("1")){
                            //go back to user dashboard
                            getDefaultDashboard(prefManager.getMemberCategory());
                        } else {
                            //did not save
                            genericDialog("Oops! An error occurred. Please try again later");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //stop progress bar
                stopProgressBar();
                genericDialog("Oops! An error occurred. Please try again later");
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
                + "edit_location.php?latitude=" + String.valueOf(latitude)
                + "&longitude=" + String.valueOf(longitude)
                + "&altitude=" + String.valueOf(altitude)
                + "&pharmacy=" + pharmacy_id
                + "&id=" + prefManager.getPsuId();

        // Request a string response from the provided URL.
        StringRequest request = new StringRequest(network_address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //stop progress bar
                        stopProgressBar();

                        //check if location has been saved successfully
                        if(response.equals("1")){
                            //go back to user dashboard
                            getDefaultDashboard(prefManager.getMemberCategory());
                        } else {
                            //did not save
                            genericDialog("Oops! An error occurred. Please try again later");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //stop progress bar
                stopProgressBar();
                genericDialog("Oops! An error occurred. Please try again later");
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
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        //Toast.makeText(MyIntentService.this, "", Toast.LENGTH_SHORT).show();
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            prefManager.setCurrentLatitude(location.getLatitude());
                            prefManager.setCurrentLongitude(location.getLongitude());
                        }
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

        String network_address = getIpAddress()
                + "set_new_attendance.php?psu_id=" + prefManager.getPsuId()
                + "&time_in=" + String.valueOf(time_in)
                + "&time_out=" + String.valueOf(time_out)
                + "&latitude_in=" + String.valueOf(prefManager.getCurrentLatitude())
                + "&longitude_in=" + String.valueOf(prefManager.getCurrentLongitude())
                + "&latitude_out=" + String.valueOf(prefManager.getLastLatitude())
                + "&longitude_out=" + String.valueOf(prefManager.getLastLongitude())
                + "&working_hours=" + String.valueOf(working_hours)
                + "&pharmacy_id=" + prefManager.getPharmacyId()
                + "&day_id=" + String.valueOf(prefManager.getDayIn())
                + "&month_id=" + String.valueOf(prefManager.getMonthIn() + 1);

        // Request a string response from the provided URL.
        StringRequest request = new StringRequest(network_address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //stop progress bar
                        stopProgressBar();

                        //check if location has been saved successfully
                        if(response.equals("1")){
                            //reset the psu id
                            prefManager.setPsuId("");

                            //reset the member category
                            prefManager.setMemberCategory("");

                            //set sign in status to false
                            prefManager.setSignedIn(false);

                            //set location set to false
                            prefManager.setIsPharmacyLocationSet(false);

                            //clear the service
                            Intent intent = new Intent(context, TrackPharmacistService.class);
                            intent.setAction("stop");
                            context.startService(intent);

                            //go to sign in page
                            Intent sign_out_intent = new Intent(context, SignInActivity.class);
                            context.startActivity(sign_out_intent);
                        } else {
                            //did not save
                            genericDialog("Oops! An error occurred. Please try again later");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //stop progress bar
                stopProgressBar();
                genericDialog("Oops! An error occurred. Please try again later");
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

}
