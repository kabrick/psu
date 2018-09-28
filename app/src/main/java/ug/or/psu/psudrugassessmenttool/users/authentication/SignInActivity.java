package ug.or.psu.psudrugassessmenttool.users.authentication;

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

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

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
        username = (EditText)findViewById(R.id.signin_username);
        password = (EditText)findViewById(R.id.signin_password);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //add validation for the fields
        mAwesomeValidation = new AwesomeValidation(BASIC);
        mAwesomeValidation.addValidation(this, R.id.signin_username, "[a-zA-Z0-9\\s]+", R.string.missing_username);
        mAwesomeValidation.addValidation(this, R.id.signin_password,"[a-zA-Z0-9\\s]+", R.string.missing_password);
    }

    public void signUp(View view){
        helperFunctions.genericSnackbar("Feature under development", activityView);
    }

    public void signIn(View view){
        if (mAwesomeValidation.validate()){
            if(helperFunctions.getConnectionStatus()){
                String network_address = helperFunctions.getIpAddress()
                        + "user_sign_in.php?username="
                        + username.getText().toString()
                        + "&password=" + password.getText().toString();

                // Request a string response from the provided URL.
                StringRequest request = new StringRequest(network_address,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(!response.equals("0")){
                                    //user credentials correct
                                    //String[] s = response.split(",");

                                    //add member_category to prefs
                                    //prefManager.setPsuId(response);

                                    //set the logged in to true
                                    //prefManager.setIsLoggedIn(true);

                                    //set the first hour
                                    //Calendar calendar = Calendar.getInstance();
                                    //prefManager.setFirstHour(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));

                                    //check to see if the locations have been set in the database
                                    //util.defineLocations(response);

                                    //log user attendance
                                    //util.registerLocation();

                                    //go to dashboard
                                    //util.setDefaultDashboard(response);
                                    helperFunctions.genericSnackbar("Log in successful", activityView);
                                } else {
                                    //user credentials are wrong
                                    helperFunctions.genericDialog("Username or password is incorrect");
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            helperFunctions.genericSnackbar("Connection Error. Please check your connection", activityView);
                        } else if (error instanceof AuthFailureError) {
                            helperFunctions.genericSnackbar("Authentication error", activityView);
                        } else if (error instanceof ServerError) {
                            helperFunctions.genericSnackbar("Server error", activityView);
                        } else if (error instanceof NetworkError) {
                            helperFunctions.genericSnackbar("Network error", activityView);
                        } else if (error instanceof ParseError) {
                            helperFunctions.genericSnackbar("Data from server not Available", activityView);
                        }
                    }
                });

                //add to request queue in singleton class
                VolleySingleton.getInstance(this).addToRequestQueue(request);
            } else {
                helperFunctions.genericSnackbar("Please ensure that you are connected to the internet", activityView);
            }
        }
    }

    @Override
    public void onBackPressed() {
        //
    }
}
