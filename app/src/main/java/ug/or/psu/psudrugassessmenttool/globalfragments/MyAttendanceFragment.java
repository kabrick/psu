package ug.or.psu.psudrugassessmenttool.globalfragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.globalactivities.EditYourPharmacies;
import ug.or.psu.psudrugassessmenttool.globalactivities.PharmacistAttendanceActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.ViewYourPharmacyActivity;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;
import ug.or.psu.psudrugassessmenttool.services.TrackPharmacistService;
import ug.or.psu.psudrugassessmenttool.users.dashboards.ndasupervisor.NdaSupervisorGetLocationActivity;
import ug.or.psu.psudrugassessmenttool.users.dashboards.psuadmin.ChoosePharmacyActivity;
import ug.or.psu.psudrugassessmenttool.users.dashboards.psuadmin.ViewGeneralAttendanceActivity;

public class MyAttendanceFragment extends Fragment {

    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;
    ArrayList<String> pharmacy_names;
    ArrayList<String> pharmacy_id;
    ArrayList<String> pharmacy_names_admin;
    ArrayList<String> pharmacy_id_admin;
    ArrayList<String> pharmacy_names_attendance;
    ArrayList<String> pharmacy_id_attendance;
    RelativeLayout relative1, relative2, relative3, relative4, relative5, relative6, relative7;

    public MyAttendanceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_attendance, container, false);

        helperFunctions = new HelperFunctions(getContext());
        preferenceManager = new PreferenceManager(Objects.requireNonNull(getContext()));

        relative1 = view.findViewById(R.id.relative1);
        relative2 = view.findViewById(R.id.relative2);
        relative3 = view.findViewById(R.id.relative3);
        relative4 = view.findViewById(R.id.relative4);
        relative5 = view.findViewById(R.id.relative5);
        relative6 = view.findViewById(R.id.relative6);
        relative7 = view.findViewById(R.id.relative7);

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
                    helperFunctions.genericDialog("You are already logged in at a pharmacy.");
                }
            }
        });

        relative6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(preferenceManager.isPharmacyLocationSet()){
                    helperFunctions.signPharmacistOutTemp();
                } else {
                    helperFunctions.genericDialog("You are not logged in to any pharmacy");
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
                viewPharmacyLocations();
            }
        });

        relative7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(preferenceManager.getMemberCategory().equals("2")){
                    Intent intent = new Intent(getContext(), ViewGeneralAttendanceActivity.class);
                    startActivity(intent);
                } else {
                    helperFunctions.genericDialog("You are not allowed to view general attendance");
                }
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
                            helperFunctions.genericDialog("You can not add another pharmacy");
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
                helperFunctions.genericDialog("Something went wrong! Please try again");
            }
        });

        //add to request queue in singleton class
        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    public void viewPharmacyLocations(){
        String[] mStringArray = {"Edit || Remove Pharmacies", "View Your Pharmacies Locations"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setTitle("Choose your action");

        builder.setItems(mStringArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0){
                    Intent intent = new Intent(getContext(), EditYourPharmacies.class);
                    startActivity(intent);
                } else if (i == 1){
                    Intent intent = new Intent(getContext(), ViewYourPharmacyActivity.class);
                    startActivity(intent);
                }
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
            helperFunctions.genericDialog("Please register your pharmacies and set their locations to continue");
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

                                    // check for the location

                                    FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getContext()));

                                    if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                        return;
                                    }
                                    mFusedLocationClient.getLastLocation()
                                            .addOnSuccessListener(new OnSuccessListener<Location>() {
                                                @Override
                                                public void onSuccess(Location location) {
                                                    // Got last known location. In some rare situations this can be null.
                                                    if (location != null) {
                                                        double current_latitude = location.getLatitude();
                                                        double current_longitude = location.getLongitude();

                                                        double pharmacy_latitude = preferenceManager.getPharmacyLatitude();
                                                        double pharmacy_longitude = preferenceManager.getPharmacyLongitude();

                                                        //calculate distance here
                                                        float distance = helperFunctions.getDistance(current_latitude, current_longitude ,pharmacy_latitude, pharmacy_longitude);

                                                        //check if distance is more than 100m and add to counter
                                                        if(distance > 100){
                                                            helperFunctions.genericDialog("You are out of bounds. Please make sure you are at the pharmacy premises before you log in");

                                                            //dismiss dialog and snack success
                                                            helperFunctions.stopProgressBar();
                                                        } else {
                                                            //user in range
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

                                                            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                                                            String currentTime = sdf.format(new Date());

                                                            helperFunctions.genericDialog("You have been logged in at " + currentTime);
                                                        }
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    helperFunctions.genericDialog("Something went wrong. Please try again");

                                                    //dismiss dialog and snack success
                                                    helperFunctions.stopProgressBar();
                                                }
                                            });

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
            helperFunctions.genericDialog("Please register or add your pharmacies to continue");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setTitle("Choose your pharmacy");

        builder.setItems(mStringArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getContext(), NdaSupervisorGetLocationActivity.class);
                intent.putExtra("pharmacy_name", pharmacy_names_admin.get(i));
                intent.putExtra("pharmacy_id", pharmacy_id_admin.get(i));
                intent.putExtra("status", "0");
                startActivity(intent);
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
            helperFunctions.genericDialog("Please register your pharmacies and set their locations to continue");
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
