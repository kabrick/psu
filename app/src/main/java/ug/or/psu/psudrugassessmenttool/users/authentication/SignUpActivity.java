package ug.or.psu.psudrugassessmenttool.users.authentication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

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

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class SignUpActivity extends AppCompatActivity {

    AwesomeValidation mAwesomeValidation;
    PreferenceManager prefManager;
    HelperFunctions helperFunctions;
    View activityView;
    Spinner sign_up_mem_status;
    EditText sign_up_password, sign_up_username, sign_up_phone, sign_up_email, sign_up_full_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        activityView = findViewById(R.id.signup_view);

        prefManager = new PreferenceManager(this);
        helperFunctions = new HelperFunctions(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        sign_up_password = findViewById(R.id.sign_up_password);
        sign_up_username = findViewById(R.id.sign_up_username);
        sign_up_phone = findViewById(R.id.sign_up_phone);
        sign_up_email = findViewById(R.id.sign_up_email);
        sign_up_full_name = findViewById(R.id.sign_up_full_name);
        sign_up_mem_status = findViewById(R.id.sign_up_mem_status);

        //add validation for the fields
        mAwesomeValidation = new AwesomeValidation(BASIC);
        mAwesomeValidation.addValidation(this, R.id.sign_up_password, "[a-zA-Z0-9\\s]+", R.string.missing_password);
        mAwesomeValidation.addValidation(this, R.id.sign_up_username,"[a-zA-Z0-9\\s]+", R.string.missing_username);
        mAwesomeValidation.addValidation(this, R.id.sign_up_phone, "[a-zA-Z0-9\\s]+", R.string.missing_telephone);
        mAwesomeValidation.addValidation(this, R.id.sign_up_full_name, "[a-zA-Z0-9\\s]+", R.string.missing_fullname);
        // missing_mem_type
    }

    public void signUp(View view){
        if (mAwesomeValidation.validate()){
            if(helperFunctions.getConnectionStatus()){

                //show progress dialog
                helperFunctions.genericProgressBar("Signing you up...");

                String mem_status = sign_up_mem_status.getSelectedItem().toString();

                switch (mem_status) {
                    case "Pharmacist":
                        mem_status = "pharmacists";
                        break;
                    case "Pharmacy Owner":
                        mem_status = "pharmdirector";
                        break;
                    case "Other":
                        mem_status = "other";
                        break;
                    default:
                        helperFunctions.stopProgressBar();
                        helperFunctions.genericDialog("Please select membership status");
                        return;
                }

                String network_address = helperFunctions.getIpAddress()
                        + "user_sign_up.php?full_names=" + sign_up_full_name.getText().toString()
                        + "&email=" + sign_up_email.getText().toString()
                        + "&phone=" + sign_up_phone.getText().toString()
                        + "&password=" + sign_up_password.getText().toString()
                        + "&username=" + sign_up_username.getText().toString()
                        + "&mem_status=" + mem_status;

                // Request a string response from the provided URL.
                StringRequest request = new StringRequest(network_address,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //dismiss progress dialog
                                helperFunctions.stopProgressBar();

                                if(response.equals("1")){
                                    // user registered successfully
                                    helperFunctions.genericSnackbar("Your profile has been created. Sign in to continue", activityView);
                                    Intent sign_in_intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                    startActivity(sign_in_intent);
                                } else {
                                    //user credentials are wrong
                                    helperFunctions.genericDialog("Something went wrong. Please try again later");
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //dismiss progress dialog
                        helperFunctions.stopProgressBar();

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

    public void signIn(View view){
        Intent sign_in_intent = new Intent(this, SignInActivity.class);
        startActivity(sign_in_intent);
    }
}
