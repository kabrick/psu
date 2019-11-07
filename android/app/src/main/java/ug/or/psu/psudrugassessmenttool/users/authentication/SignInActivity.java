package ug.or.psu.psudrugassessmenttool.users.authentication;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.android.volley.NetworkError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class SignInActivity extends AppCompatActivity {

    EditText username;
    EditText password;
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
        if(helperFunctions.getConnectionStatus()){

            final String username_string = username.getText().toString();
            final String password_string = password.getText().toString();

            if(TextUtils.isEmpty(username_string)) {
                username.setError("Please fill in the username");
                return;
            }

            if(TextUtils.isEmpty(password_string)) {
                password.setError("Please fill in the password");
                return;
            }

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

                            // check if device id has not been registered yet
                            helperFunctions.checkDeviceId();

                            // user is signed in so check member category and go to respective dashboard
                            helperFunctions.getDefaultDashboard(s[1]);
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

    @Override
    public void onBackPressed() {
        //
    }
}
