package ug.or.psu.psudrugassessmenttool.users.authentication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.basgeekball.awesomevalidation.AwesomeValidation;

import java.util.Calendar;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;
import ug.or.psu.psudrugassessmenttool.users.dashboards.ndaadmin.NdaAdminDashboard;
import ug.or.psu.psudrugassessmenttool.users.dashboards.ndasupervisor.NdaSupervisorDashboard;
import ug.or.psu.psudrugassessmenttool.users.dashboards.psuadmin.PsuAdminDashboard;
import ug.or.psu.psudrugassessmenttool.users.dashboards.psupharmacist.PsuPharmacistDashboard;
import ug.or.psu.psudrugassessmenttool.users.dashboards.psupharmacyowner.PsuPharmacyOwnerDashboard;
import ug.or.psu.psudrugassessmenttool.users.dashboards.sysadmin.SystemsAdministratorDashboard;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class SignInActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    AwesomeValidation mAwesomeValidation;
    PreferenceManager prefManager;
    HelperFunctions helperFunctions;
    View activityView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        activityView = findViewById(R.id.signin_view);

        prefManager = new PreferenceManager(this);
        helperFunctions = new HelperFunctions(this);

        //get view components
        username = findViewById(R.id.signin_username);
        password = findViewById(R.id.signin_password);

        //add validation for the fields
        mAwesomeValidation = new AwesomeValidation(BASIC);
        mAwesomeValidation.addValidation(this, R.id.signin_username, "[a-zA-Z0-9\\s]+", R.string.missing_username);
        mAwesomeValidation.addValidation(this, R.id.signin_password,"[a-zA-Z0-9\\s]+", R.string.missing_password);
    }

    public void signUp(View view){
        Intent sign_up_intent = new Intent(this, SignUpActivity.class);
        startActivity(sign_up_intent);
    }

    public void signIn(View view){
        if (mAwesomeValidation.validate()){
            if(helperFunctions.getConnectionStatus()){

                //show progress dialog
                helperFunctions.genericProgressBar("Signing you in...");

                String network_address = helperFunctions.getIpAddress()
                        + "user_sign_in.php?username="
                        + username.getText().toString()
                        + "&password=" + password.getText().toString();

                // Request a string response from the provided URL.
                StringRequest request = new StringRequest(network_address,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //dismiss progress dialog
                                helperFunctions.stopProgressBar();

                                if(!response.equals("0")){
                                    //user credentials correct so set sign in to true
                                    prefManager.setSignedIn(true);

                                    //split csv string
                                    String[] s = response.split(",");

                                    //set user psu id
                                    prefManager.setPsuId(s[0]);

                                    //set user member category
                                    prefManager.setMemberCategory(s[1]);

                                    //set the user's name
                                    prefManager.setPsuName(s[2]);

                                    switch (s[1]) {
                                        case "1": {
                                            // go to systems administrator dashboard
                                            Intent intent_sys_admin = new Intent(SignInActivity.this, SystemsAdministratorDashboard.class);
                                            startActivity(intent_sys_admin);
                                            break;
                                        }
                                        case "2": {
                                            // go to psu administrator dashboard
                                            Intent intent_nda_admin = new Intent(SignInActivity.this, PsuAdminDashboard.class);
                                            startActivity(intent_nda_admin);
                                            break;
                                        }
                                        case "3": {
                                            /*//create instance of calender
                                            Calendar calendar = Calendar.getInstance();

                                            //get time_in timestamp
                                            prefManager.setTimeIn(System.currentTimeMillis());

                                            //get current location in, latitude and longitude
                                            helperFunctions.setCurrentLocation();

                                            //get day
                                            prefManager.setDayIn(calendar.get(Calendar.DAY_OF_WEEK));

                                            //get month
                                            prefManager.setMonthIn(calendar.get(Calendar.MONTH));*/

                                            //go to the pharmacist dashboard
                                            Intent intent_psu_pharmacist = new Intent(SignInActivity.this, PsuPharmacistDashboard.class);
                                            startActivity(intent_psu_pharmacist);
                                            break;
                                        }
                                        case "4": {
                                            // go to pharmacy owner dashboard
                                            Intent intent_pharmacy_owner = new Intent(SignInActivity.this, PsuPharmacyOwnerDashboard.class);
                                            startActivity(intent_pharmacy_owner);
                                            break;
                                        }
                                        case "5": {
                                            // go to nda administrator
                                            Intent intent_nda_admin = new Intent(SignInActivity.this, NdaAdminDashboard.class);
                                            startActivity(intent_nda_admin);
                                            break;
                                        }
                                        case "6": {
                                            // go to nda supervisor
                                            Intent intent_nda_supervisor = new Intent(SignInActivity.this, NdaSupervisorDashboard.class);
                                            startActivity(intent_nda_supervisor);
                                            break;
                                        }
                                        default: {
                                            // user details not set so clear all prefs and log out
                                            helperFunctions.signAdminUsersOut();
                                            break;
                                        }
                                    }
                                } else {
                                    //user credentials are wrong
                                    new SweetAlertDialog(SignInActivity.this, SweetAlertDialog.WARNING_TYPE)
                                            .setContentText("Username or password is incorrect")
                                            .setConfirmText("Okay")
                                            .show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //dismiss progress dialog
                        helperFunctions.stopProgressBar();
                        new SweetAlertDialog(SignInActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("Something went wrong! Please try again")
                                .show();
                    }
                });

                //add to request queue in singleton class
                VolleySingleton.getInstance(this).addToRequestQueue(request);
            } else {
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Internet connection has been lost!")
                        .show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        //
    }
}
