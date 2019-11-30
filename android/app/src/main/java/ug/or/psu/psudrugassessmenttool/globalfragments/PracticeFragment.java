package ug.or.psu.psudrugassessmenttool.globalfragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.globalactivities.AdrReportFormActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.AdrReportFormFeedActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.CommonwealthWebsiteActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.CustomAdrReportsActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.EcpdCreateActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.EcpdFeedActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.EditYourPharmacies;
import ug.or.psu.psudrugassessmenttool.globalactivities.MobilePaymentsWebsiteActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.PharmacistAttendanceActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.ViewPharmacyCoordinatesActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.ViewSubmittedCpdActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.ViewYourPharmacyActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.WholesaleInspectionActivity;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;
import ug.or.psu.psudrugassessmenttool.services.TrackPharmacistService;
import ug.or.psu.psudrugassessmenttool.users.dashboards.admin.GetLocationActivity;
import ug.or.psu.psudrugassessmenttool.users.dashboards.admin.ChoosePharmacyActivity;
import ug.or.psu.psudrugassessmenttool.users.dashboards.admin.ViewGeneralAttendanceActivity;

public class PracticeFragment extends Fragment {

    private HelperFunctions helperFunctions;
    private PreferenceManager preferenceManager;
    private ArrayList<String> pharmacy_names;
    private ArrayList<String> pharmacy_id;
    private ArrayList<String> pharmacy_names_admin;
    private ArrayList<String> pharmacy_id_admin;
    private ArrayList<String> pharmacy_names_attendance;
    private ArrayList<String> pharmacy_id_attendance;

    public PracticeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_practice, container, false);

        helperFunctions = new HelperFunctions(getContext());
        preferenceManager = new PreferenceManager(Objects.requireNonNull(getContext()));

        CardView add_practice_center = view.findViewById(R.id.add_practice_center);
        CardView set_practice_center_location = view.findViewById(R.id.set_practice_center_location);
        CardView attendance_login = view.findViewById(R.id.attendance_login);
        CardView view_your_attendance = view.findViewById(R.id.view_your_attendance);
        CardView view_your_practice_centers = view.findViewById(R.id.view_your_practice_centers);
        CardView attendance_logout = view.findViewById(R.id.attendance_logout);
        CardView view_general_attendance = view.findViewById(R.id.view_general_attendance);
        CardView support_supervision_checklist = view.findViewById(R.id.support_supervision_checklist);
        CardView view_adr_form = view.findViewById(R.id.view_adr_form);
        CardView view_ecpd = view.findViewById(R.id.view_ecpd);
        CardView make_online_payments = view.findViewById(R.id.make_online_payments);
        CardView view_commonwealth_website = view.findViewById(R.id.view_commonwealth_website);

        view_ecpd.setOnClickListener(view1 -> {

            if(preferenceManager.getMemberCategory().equals("2")){
                String[] mStringArray = {"Add e-CPD", "View Submitted e-CPD","View e-CPD Resources"};

                AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                builder.setTitle("Choose your action");

                builder.setItems(mStringArray, (dialogInterface, i) -> {
                    if (i == 0){
                        Intent submit_ecpd_intent = new Intent(getContext(), EcpdCreateActivity.class);
                        startActivity(submit_ecpd_intent);
                    } else if (i == 1){
                        Intent view_submitted_intent = new Intent(getContext(), ViewSubmittedCpdActivity.class);
                        startActivity(view_submitted_intent);
                    } else if (i == 2){
                        Intent intent = new Intent(getContext(), EcpdFeedActivity.class);
                        startActivity(intent);
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                Intent intent = new Intent(getContext(), EcpdFeedActivity.class);
                startActivity(intent);
            }
        });

        add_practice_center.setOnClickListener(view17 -> checkNumberOfUserPharmacies());

        set_practice_center_location.setOnClickListener(view16 -> getUnsetPharmacies());

        attendance_login.setOnClickListener(view15 -> {
            if(!preferenceManager.isPharmacyLocationSet()){
                //start dialog
                helperFunctions.genericProgressBar("Getting your allocated centres...");
                getPharmacies();
            } else {
                helperFunctions.genericDialog("You are already logged in at a practice centre.");
            }
        });

        attendance_logout.setOnClickListener(view14 -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(Objects.requireNonNull(getContext()));

            alert.setMessage("Are you sure you want to log out").setPositiveButton("Yes", (dialogInterface, i) -> {
                if(preferenceManager.isPharmacyLocationSet()){
                    helperFunctions.signPharmacistOut();
                } else {
                    helperFunctions.genericDialog("You are not logged in to any practice centre");
                }
            }).show();
        });

        view_your_attendance.setOnClickListener(view13 -> viewIndividualAttendance());

        view_your_practice_centers.setOnClickListener(view12 -> viewPharmacyLocations());

        view_general_attendance.setOnClickListener(view1 -> startActivity(new Intent(getContext(), ViewGeneralAttendanceActivity.class)));

        support_supervision_checklist.setOnClickListener(view18 -> {
            String[] mStringArray = {"Wholesale Pharmacies", "Retail Pharmacies"};

            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
            builder.setTitle("Choose pharmacy type");

            builder.setItems(mStringArray, (dialogInterface, i) -> {
                if (i == 0){
                    Intent support_supervision_checklist_intent = new Intent(getContext(), WholesaleInspectionActivity.class);
                    support_supervision_checklist_intent.putExtra("text", "Wholesale Pharmacies");
                    startActivity(support_supervision_checklist_intent);
                } else if (i == 1){
                    Intent support_supervision_checklist_intent = new Intent(getContext(), WholesaleInspectionActivity.class);
                    support_supervision_checklist_intent.putExtra("text", "Retail Pharmacies");
                    startActivity(support_supervision_checklist_intent);
                }
            });

            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        view_adr_form.setOnClickListener(view19 -> {

            String[] mStringArray = {"Fill ADR Form", "Withdraw || Edit Filled Form", "View Submitted ADR Forms", "ADR Reports"};

            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
            builder.setTitle("Choose your action");

            builder.setItems(mStringArray, (dialogInterface, i) -> {
                if (i == 0){
                    Intent adr_intent = new Intent(getContext(), AdrReportFormActivity.class);
                    startActivity(adr_intent);
                } else if (i == 1){
                    //
                } else if (i == 2){
                    Intent view_adr_intent = new Intent(getContext(), AdrReportFormFeedActivity.class);
                    startActivity(view_adr_intent);
                } else if (i == 3){
                    startActivity(new Intent(getContext(), CustomAdrReportsActivity.class));
                }
            });

            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        //create array list objects
        pharmacy_names = new ArrayList<>();
        pharmacy_id = new ArrayList<>();
        pharmacy_names_admin = new ArrayList<>();
        pharmacy_id_admin = new ArrayList<>();
        pharmacy_names_attendance = new ArrayList<>();
        pharmacy_id_attendance = new ArrayList<>();

        if(!preferenceManager.getMemberCategory().equals("1")){
            view_general_attendance.setVisibility(View.GONE);
        }

        view_commonwealth_website.setOnClickListener(v -> startActivity(new Intent(getContext(), CommonwealthWebsiteActivity.class)));

        make_online_payments.setOnClickListener(v -> startActivity(new Intent(getContext(), MobilePaymentsWebsiteActivity.class)));

        return view;
    }

    private void checkNumberOfUserPharmacies(){
        // check if the user has not exceeded the limit of 2 pharmacies

        //show progress dialog
        helperFunctions.genericProgressBar("Confirming your status...");

        String network_address = helperFunctions.getIpAddress() + "check_valid_pharmacies.php?id=" + preferenceManager.getPsuId();

        // Request a string response from the provided URL.
        StringRequest request = new StringRequest(network_address,
                response -> {
                    //dismiss progress dialog
                    helperFunctions.stopProgressBar();

                    if(response.equals("0")){
                        // not allowed
                        helperFunctions.genericDialog("Your practice centre limit is exceeded");
                    } else if (response.equals("1")){
                        // allowed
                        Intent choose_pharmacy_intent = new Intent(getContext(), ChoosePharmacyActivity.class);
                        startActivity(choose_pharmacy_intent);
                    }
                }, error -> {
                    helperFunctions.stopProgressBar();
                    helperFunctions.genericDialog("Something went wrong! Please try again");
                });

        //add to request queue in singleton class
        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    public void viewPharmacyLocations(){
        String[] mStringArray = {"Edit || Remove Practice Centres", "View Your Practice Centre Locations", "View Your Practice Centre Coordinates"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setTitle("Choose your action");

        builder.setItems(mStringArray, (dialogInterface, i) -> {
            if (i == 0){
                Intent intent = new Intent(getContext(), EditYourPharmacies.class);
                startActivity(intent);
            } else if (i == 1){
                Intent intent = new Intent(getContext(), ViewYourPharmacyActivity.class);
                startActivity(intent);
            } else if (i == 2){
                Intent intent = new Intent(getContext(), ViewPharmacyCoordinatesActivity.class);
                startActivity(intent);
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void getPharmacies(){
        String network_address = helperFunctions.getIpAddress() + "get_pharmacist_pharmacies.php?id=" + preferenceManager.getPsuId();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, network_address, null,
                response -> {
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
                }, error -> {
                    //
                });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    public void choosePharmacy(){

        //convert array list to string array
        String[] mStringArray = new String[pharmacy_names.size()];
        mStringArray = pharmacy_names.toArray(mStringArray);

        // confirm that pharmacies exist
        if(mStringArray.length < 1){
            helperFunctions.genericDialog("Please register your practice centres and set their locations to continue");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setTitle("Choose your practice centre");

        builder.setItems(mStringArray, (dialogInterface, i) -> {
            //open working on something dialog
            helperFunctions.genericProgressBar("Logging you in...");

            //set the selected pharmacy in prefs
            preferenceManager.setPharmacyId(pharmacy_id.get(i));
            preferenceManager.setPharmacyName(pharmacy_names.get(i));

            //fetch the coordinates for the pharmacy
            String network_address = helperFunctions.getIpAddress()
                    + "get_pharmacy_coordinates.php?id=" + pharmacy_id.get(i);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                    response -> {
                        try {
                            preferenceManager.setPharmacyLatitude(Double.parseDouble(response.getString("latitude")));
                            preferenceManager.setPharmacyLongitude(Double.parseDouble(response.getString("longitude")));

                            // check for the location

                            FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getContext()));

                            if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                helperFunctions.stopProgressBar();
                                Toast.makeText(getContext(), "Please enable location permissions to continue", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            mFusedLocationClient.getLastLocation()
                                    .addOnSuccessListener(location -> {
                                        // Get last known location. In some rare situations this can be null.
                                        if (location != null) {
                                            double current_latitude = location.getLatitude();
                                            double current_longitude = location.getLongitude();

                                            double pharmacy_latitude = preferenceManager.getPharmacyLatitude();
                                            double pharmacy_longitude = preferenceManager.getPharmacyLongitude();

                                            //calculate distance here
                                            float distance = helperFunctions.getDistance(current_latitude, current_longitude ,pharmacy_latitude, pharmacy_longitude);

                                            //check if distance is more than 50m and add to counter
                                            if(distance > 50){
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
                                    })
                                    .addOnFailureListener(e -> {
                                        helperFunctions.genericDialog("Something went wrong. Please try again");

                                        //dismiss dialog and snack success
                                        helperFunctions.stopProgressBar();
                                    });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> {
                        //
                    });

            VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getUnsetPharmacies(){
        helperFunctions.genericProgressBar("Retrieving practice centres");

        String network_address = helperFunctions.getIpAddress() + "get_unlocated_pharmacies.php?id=" + preferenceManager.getPsuId();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, network_address, null,
                response -> {
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
                    choosePharmacyToSetLocation();
                }, error -> {
                    //
                });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    private void choosePharmacyToSetLocation(){
        //convert array list to string array
        String[] mStringArray = new String[pharmacy_names_admin.size()];
        mStringArray = pharmacy_names_admin.toArray(mStringArray);

        // confirm that pharmacies exist
        if(mStringArray.length < 1){
            helperFunctions.genericDialog("Please register or add your practice centres to continue");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setTitle("Choose a practice centre");

        builder.setItems(mStringArray, (dialogInterface, i) -> {
            Intent intent = new Intent(getContext(), GetLocationActivity.class);
            intent.putExtra("pharmacy_name", pharmacy_names_admin.get(i));
            intent.putExtra("pharmacy_id", pharmacy_id_admin.get(i));
            intent.putExtra("status", "0");
            startActivity(intent);
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void viewIndividualAttendance(){
        helperFunctions.genericProgressBar("Retrieving practice centres");

        String network_address = helperFunctions.getIpAddress() + "get_pharmacist_pharmacies.php?id=" + preferenceManager.getPsuId();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, network_address, null,
                response -> {
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
                }, error -> {
                    //
                });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    public void viewAttendanceAdmin(){

        //convert array list to string array
        String[] mStringArray = new String[pharmacy_names_attendance.size()];
        mStringArray = pharmacy_names_attendance.toArray(mStringArray);

        // confirm that pharmacies exist
        if(mStringArray.length < 1){
            helperFunctions.genericDialog("Please register your practice centres and set their locations to continue");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setTitle("Choose your practice centre");

        builder.setItems(mStringArray, (dialogInterface, i) -> {
            Intent intent = new Intent(getContext(), PharmacistAttendanceActivity.class);
            intent.putExtra("pharmacy_id", pharmacy_id_attendance.get(i));
            intent.putExtra("pharmacist_id", preferenceManager.getPsuId());
            startActivity(intent);
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
