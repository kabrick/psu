package ug.or.psu.psudrugassessmenttool.users.authentication;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.android.volley.NetworkError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText email;
    HelperFunctions helperFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email = findViewById(R.id.email_reset);

        helperFunctions = new HelperFunctions(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void resetPassword(View view){
        helperFunctions.genericProgressBar("Resetting password");
        String email_text = email.getText().toString();

        if(TextUtils.isEmpty(email_text)) {
            helperFunctions.genericDialog("Please fill in the email");
            return;
        }

        final String network_address = helperFunctions.getIpAddress()
                + "reset_password.php?email=" + email_text;

        // Request a string response from the provided URL.
        StringRequest request = new StringRequest(network_address,
                response -> {
                    //dismiss progress dialog
                    helperFunctions.stopProgressBar();

                    if(response.equals("0")){
                        //saved article successfully
                        AlertDialog.Builder alert = new AlertDialog.Builder(ForgotPasswordActivity.this);

                        alert.setMessage("Email does not exist in the system").setPositiveButton("Okay", (dialogInterface, i) -> {
                            //
                        }).show();
                    } else if(response.equals("1")){
                        //saved article successfully
                        AlertDialog.Builder alert = new AlertDialog.Builder(ForgotPasswordActivity.this);

                        alert.setMessage("A new password has been sent to your email").setPositiveButton("Okay", (dialogInterface, i) -> {
                            finish();
                            onBackPressed();
                        }).show();
                    } else {
                        helperFunctions.genericDialog("Something went wrong! Please try again");
                    }
                }, error -> {

                    if (error instanceof TimeoutError || error instanceof NetworkError) {
                        helperFunctions.genericDialog("Something went wrong. Please make sure you are connected to a working internet connection.");
                    } else {
                        helperFunctions.genericDialog("Something went wrong. Please try again later");
                    }
                });

        //add to request queue in singleton class
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
