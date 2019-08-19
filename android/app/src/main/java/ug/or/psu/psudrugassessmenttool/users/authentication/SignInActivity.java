package ug.or.psu.psudrugassessmenttool.users.authentication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.android.volley.NetworkError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.basgeekball.awesomevalidation.AwesomeValidation;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;
import ug.or.psu.psudrugassessmenttool.users.dashboards.admin.PsuAdminDashboard;
import ug.or.psu.psudrugassessmenttool.users.dashboards.pharmacist.PsuPharmacistDashboard;
import ug.or.psu.psudrugassessmenttool.users.dashboards.pharmacyowner.PsuPharmacyOwnerDashboard;

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

        password.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i == EditorInfo.IME_ACTION_DONE){
                signInLogic();
                return false;
            } else {
                return false;
            }
        });

        //add validation for the fields
        mAwesomeValidation = new AwesomeValidation(BASIC);
        mAwesomeValidation.addValidation(this, R.id.signin_username, "[a-zA-Z0-9\\s]+", R.string.missing_username);
        mAwesomeValidation.addValidation(this, R.id.signin_password,"[a-zA-Z0-9\\s]+", R.string.missing_password);
    }

    public void signUp(View view){
        Intent sign_up_intent = new Intent(this, SignUpActivity.class);
        startActivity(sign_up_intent);
    }

    public void forgotPassword(View view){
        Intent forgot_password_intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(forgot_password_intent);
    }

    public void signIn(View view){
        signInLogic();
    }

    public void signInLogic(){
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
                        response -> {
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
                                        // go to admin dashboard
                                        Intent intent_admin = new Intent(SignInActivity.this, PsuAdminDashboard.class);
                                        startActivity(intent_admin);
                                        break;
                                    }
                                    case "2": {
                                        // go to the pharmacist dashboard
                                        Intent intent_pharmacist = new Intent(SignInActivity.this, PsuPharmacistDashboard.class);
                                        startActivity(intent_pharmacist);
                                        break;
                                    }
                                    case "3": {
                                        // go to pharmacy owner dashboard
                                        Intent intent_pharmacy_owner = new Intent(SignInActivity.this, PsuPharmacyOwnerDashboard.class);
                                        startActivity(intent_pharmacy_owner);
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
                                helperFunctions.genericDialog("Username or password is incorrect");
                            }
                        }, error -> {
                            //dismiss progress dialog
                            helperFunctions.stopProgressBar();

                            if (error instanceof TimeoutError || error instanceof NetworkError) {
                                helperFunctions.genericDialog("Something went wrong. Please make sure you are connected to a working internet connection.");
                            } else {
                                helperFunctions.genericDialog("Something went wrong. Please try again later");
                            }
                        });

                //add to request queue in singleton class
                VolleySingleton.getInstance(this).addToRequestQueue(request);
            } else {
                helperFunctions.genericDialog("Internet connection has been lost!");
            }
        }
    }

    @Override
    public void onBackPressed() {
        //
    }
}
