package ug.or.psu.psudrugassessmenttool.helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;
import ug.or.psu.psudrugassessmenttool.users.authentication.SignInActivity;
import ug.or.psu.psudrugassessmenttool.users.dashboards.ndasupervisor.NdaSupervisorDashboard;

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
                            //go back to NDA supervisor dashboard
                            Intent intent_save_location = new Intent(context, NdaSupervisorDashboard.class);
                            context.startActivity(intent_save_location);
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
                            //go back to NDA supervisor dashboard
                            Intent intent_save_location = new Intent(context, NdaSupervisorDashboard.class);
                            context.startActivity(intent_save_location);
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

    public void getDefaultDashboard(String member_category){
        switch (member_category) {
            case "1": {
                // TODO: go to systems administrator dashboard
                break;
            }
            case "2": {
                // TODO: go to psu administrator dashboard
                break;
            }
            case "3": {
                // TODO: go to pharmacist dashboard
                break;
            }
            case "4": {
                // TODO: go to pharmacy owner dashboard
                break;
            }
            case "5": {
                // TODO: go to nda administrator
                break;
            }
            case "6": {
                //go to nda supervisor
                Intent intent_nda_supervisor = new Intent(context, NdaSupervisorDashboard.class);
                context.startActivity(intent_nda_supervisor);
                break;
            }
            default: {
                // TODO: user details not set so clear all prefs and log out
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

}
