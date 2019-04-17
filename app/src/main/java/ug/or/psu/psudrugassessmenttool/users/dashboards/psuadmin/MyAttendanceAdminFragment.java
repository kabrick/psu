package ug.or.psu.psudrugassessmenttool.users.dashboards.psuadmin;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.globalactivities.PharmacistAttendanceActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.ViewPharmacyLocationActivity;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;
import ug.or.psu.psudrugassessmenttool.services.TrackPharmacistService;
import ug.or.psu.psudrugassessmenttool.users.dashboards.ndasupervisor.NdaSupervisorGetLocationActivity;

public class MyAttendanceAdminFragment extends Fragment {

    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;
    ArrayList<String> pharmacy_names;
    ArrayList<String> pharmacy_id;
    ArrayList<String> pharmacy_names_admin;
    ArrayList<String> pharmacy_id_admin;
    ArrayList<String> pharmacy_names_attendance;
    ArrayList<String> pharmacy_id_attendance;
    RelativeLayout relative1, relative2, relative3, relative4, relative5, relative6;

    public MyAttendanceAdminFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_attendance_admin, container, false);

        helperFunctions = new HelperFunctions(getContext());
        preferenceManager = new PreferenceManager(Objects.requireNonNull(getContext()));

        relative1 = view.findViewById(R.id.relative1);
        relative2 = view.findViewById(R.id.relative2);
        relative3 = view.findViewById(R.id.relative3);
        relative4 = view.findViewById(R.id.relative4);
        relative5 = view.findViewById(R.id.relative5);
        relative6 = view.findViewById(R.id.relative6);

        relative1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isUserValid();
            }
        });

        relative2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUnsetPharmacies();
            }
        });

        relative3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!preferenceManager.isPharmacyLocationSet()){
                    //start dialog
                    helperFunctions.genericProgressBar("Getting your allocated pharmacies...");
                    //not so start procedure to set it
                    getPharmacies();
                } else {
                    helperFunctions.signPharmacistOutTemp();
                }
            }
        });

        relative4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewAttendance();
            }
        });

        relative5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ViewGeneralAttendanceActivity.class);
                startActivity(intent);
            }
        });

        relative6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start dialog
                helperFunctions.genericProgressBar("Getting your allocated pharmacies...");
                viewPharmacyLocations();
            }
        });

        //create array list objects
        pharmacy_names = new ArrayList<>();
        pharmacy_id = new ArrayList<>();
        pharmacy_names_admin = new ArrayList<>();
        pharmacy_id_admin = new ArrayList<>();
        pharmacy_names_attendance = new ArrayList<>();
        pharmacy_id_attendance = new ArrayList<>();

        return view;
    }

    public void isUserValid(){
        // check if the user has not exceeded the limit of 2 pharmacies

        //show progress dialog
        helperFunctions.genericProgressBar("Confirming your status...");

        String network_address = helperFunctions.getIpAddress() + "check_valid_pharmacies.php?id=" + preferenceManager.getPsuId();

        // Request a string response from the provided URL.
        StringRequest request = new StringRequest(network_address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //dismiss progress dialog
                        helperFunctions.stopProgressBar();

                        if(response.equals("0")){
                            // not allowed
                            new SweetAlertDialog(Objects.requireNonNull(getContext()), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("You can not add another pharmacy")
                                    .show();
                        } else if (response.equals("1")){
                            // allowed
                            Intent choose_pharmacy_intent = new Intent(getContext(), ChoosePharmacyActivity.class);
                            startActivity(choose_pharmacy_intent);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                helperFunctions.stopProgressBar();
                new SweetAlertDialog(Objects.requireNonNull(getContext()), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Something went wrong! Please try again")
                        .show();
            }
        });

        //add to request queue in singleton class
        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    public void viewPharmacyLocations(){
        String network_address = helperFunctions.getIpAddress() + "get_pharmacist_pharmacies.php?id=" + preferenceManager.getPsuId();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, network_address, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //close the generic progressbar
                        helperFunctions.stopProgressBar();

                        JSONObject obj;

                        // reset values
                        pharmacy_id.clear();
                        pharmacy_names.clear();

                        for (int i = 0; i < response.length(); i++){

                            try {
                                obj = response.getJSONObject(i);

                                pharmacy_names.add(obj.getString("pharmacy_name"));
                                pharmacy_id.add(obj.getString("pharmacy_id"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        //continue to display
                        choosePharmacyLocation();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //
            }
        });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    public void choosePharmacyLocation(){
        //convert array list to string array
        String[] mStringArray = new String[pharmacy_names.size()];
        mStringArray = pharmacy_names.toArray(mStringArray);

        // confirm that pharmacies exist
        if(mStringArray.length < 1){
            new SweetAlertDialog(Objects.requireNonNull(getContext()), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Pharmacies not available")
                    .show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setTitle("Choose your pharmacy");

        builder.setItems(mStringArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                // get the location
                helperFunctions.genericProgressBar("Retrieving pharmacy location...");

                final String pharmacy_id_string = pharmacy_id.get(i);
                final String pharmacy_name_string = pharmacy_names.get(i);

                //fetch the coordinates for the pharmacy
                String network_address = helperFunctions.getIpAddress()
                        + "get_pharmacy_coordinates.php?id=" + pharmacy_id_string;

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    //dismiss dialog
                                    helperFunctions.stopProgressBar();

                                    Intent intent = new Intent(getContext(), ViewPharmacyLocationActivity.class);
                                    intent.putExtra("pharmacy_id", pharmacy_id_string);
                                    intent.putExtra("pharmacy_name", pharmacy_name_string);
                                    intent.putExtra("latitude", Double.parseDouble(response.getString("latitude")));
                                    intent.putExtra("longitude", Double.parseDouble(response.getString("longitude")));
                                    startActivity(intent);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //
                    }
                });

                VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void getPharmacies(){
        String network_address = helperFunctions.getIpAddress() + "get_pharmacist_pharmacies.php?id=" + preferenceManager.getPsuId();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, network_address, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //close the generic progressbar
                        helperFunctions.stopProgressBar();

                        JSONObject obj;

                        // reset values
                        pharmacy_id.clear();
                        pharmacy_names.clear();

                        for (int i = 0; i < response.length(); i++){

                            try {
                                obj = response.getJSONObject(i);

                                pharmacy_names.add(obj.getString("pharmacy_name"));
                                pharmacy_id.add(obj.getString("pharmacy_id"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        //continue to display
                        choosePharmacy();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //
            }
        });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    public void choosePharmacy(){

        //convert array list to string array
        String[] mStringArray = new String[pharmacy_names.size()];
        mStringArray = pharmacy_names.toArray(mStringArray);

        // confirm that pharmacies exist
        if(mStringArray.length < 1){
            new SweetAlertDialog(Objects.requireNonNull(getContext()), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Pharmacies not available")
                    .show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setTitle("Choose your pharmacy");

        builder.setItems(mStringArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //open working on something dialog
                helperFunctions.genericProgressBar("Logging you in...");

                //set the selected pharmacy in prefs
                preferenceManager.setPharmacyId(pharmacy_id.get(i));
                preferenceManager.setPharmacyName(pharmacy_names.get(i));

                //fetch the coordinates for the pharmacy
                String network_address = helperFunctions.getIpAddress()
                        + "get_pharmacy_coordinates.php?id=" + pharmacy_id.get(i);

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    preferenceManager.setPharmacyLatitude(Double.parseDouble(response.getString("latitude")));
                                    preferenceManager.setPharmacyLongitude(Double.parseDouble(response.getString("longitude")));

                                    //set pharmacy location
                                    preferenceManager.setIsPharmacyLocationSet(true);

                                    //create instance of calender
                                    Calendar calendar = Calendar.getInstance();

                                    //get time_in timestamp
                                    preferenceManager.setTimeIn(System.currentTimeMillis());

                                    //get current location in, latitude and longitude
                                    helperFunctions.setCurrentLocation();

                                    //get day
                                    preferenceManager.setDayIn(calendar.get(Calendar.DAY_OF_WEEK));

                                    //get month
                                    preferenceManager.setMonthIn(calendar.get(Calendar.MONTH));

                                    //start service
                                    Intent intent = new Intent(getContext(), TrackPharmacistService.class);
                                    intent.setAction("start");
                                    Objects.requireNonNull(getActivity()).startService(intent);

                                    //dismiss dialog and snack success
                                    helperFunctions.stopProgressBar();

                                    new SweetAlertDialog(Objects.requireNonNull(getContext()), SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Success!")
                                            .setContentText("You have been logged in")
                                            .show();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //
                    }
                });

                VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void getUnsetPharmacies(){
        helperFunctions.genericProgressBar("Retrieving pharmacies");

        String network_address = helperFunctions.getIpAddress() + "get_unlocated_pharmacies.php?id=" + preferenceManager.getPsuId();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, network_address, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //close the generic progressbar
                        helperFunctions.stopProgressBar();

                        JSONObject obj;

                        // reset values
                        pharmacy_id_admin.clear();
                        pharmacy_names_admin.clear();

                        for (int i = 0; i < response.length(); i++){

                            try {
                                obj = response.getJSONObject(i);

                                pharmacy_names_admin.add(obj.getString("pharmacy_name"));
                                pharmacy_id_admin.add(obj.getString("pharmacy_id"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        //continue to display
                        choosePharmacyAdmin();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //
            }
        });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    public void choosePharmacyAdmin(){
        //convert array list to string array
        String[] mStringArray = new String[pharmacy_names_admin.size()];
        mStringArray = pharmacy_names_admin.toArray(mStringArray);

        // confirm that pharmacies exist
        if(mStringArray.length < 1){
            new SweetAlertDialog(Objects.requireNonNull(getContext()), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Pharmacies not available")
                    .show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setTitle("Choose your pharmacy");

        builder.setItems(mStringArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                // check if location is turned on
                LocationManager locationManager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE);

                assert locationManager != null;
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    // location not enabled

                    final AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                    builder.setMessage("Your location seems to be disabled. Do you want to enable it to continue?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, final int id) {
                                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, final int id) {
                                    //
                                }
                            });
                    final AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    Intent intent = new Intent(getContext(), NdaSupervisorGetLocationActivity.class);
                    intent.putExtra("pharmacy_name", pharmacy_names_admin.get(i));
                    intent.putExtra("pharmacy_id", pharmacy_id_admin.get(i));
                    intent.putExtra("status", "0");
                    startActivity(intent);
                }
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void viewAttendance(){
        helperFunctions.genericProgressBar("Retrieving pharmacies");

        String network_address = helperFunctions.getIpAddress() + "get_pharmacist_pharmacies.php?id=" + preferenceManager.getPsuId();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, network_address, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //close the generic progressbar
                        helperFunctions.stopProgressBar();

                        JSONObject obj;

                        pharmacy_id_attendance.clear();
                        pharmacy_names_attendance.clear();

                        for (int i = 0; i < response.length(); i++){

                            try {
                                obj = response.getJSONObject(i);

                                pharmacy_names_attendance.add(obj.getString("pharmacy_name"));
                                pharmacy_id_attendance.add(obj.getString("pharmacy_id"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        //continue to display
                        viewAttendanceAdmin();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //
            }
        });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    public void viewAttendanceAdmin(){

        //convert array list to string array
        String[] mStringArray = new String[pharmacy_names_attendance.size()];
        mStringArray = pharmacy_names_attendance.toArray(mStringArray);

        // confirm that pharmacies exist
        if(mStringArray.length < 1){
            new SweetAlertDialog(Objects.requireNonNull(getContext()), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Pharmacies not available")
                    .show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setTitle("Choose your pharmacy");

        builder.setItems(mStringArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getContext(), PharmacistAttendanceActivity.class);
                intent.putExtra("pharmacy_id", pharmacy_id_attendance.get(i));
                intent.putExtra("pharmacist_id", preferenceManager.getPsuId());
                startActivity(intent);
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
