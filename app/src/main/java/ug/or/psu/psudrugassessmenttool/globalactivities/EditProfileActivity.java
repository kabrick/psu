package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class EditProfileActivity extends AppCompatActivity {

    PreferenceManager preferenceManager;
    HelperFunctions helperFunctions;
    EditText name, username, email, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        preferenceManager = new PreferenceManager(this);
        helperFunctions = new HelperFunctions(this);

        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);

        getProfileDetails();
    }

    public void getProfileDetails(){

        helperFunctions.genericProgressBar("Fetching records...");

        String network_address = helperFunctions.getIpAddress()
                + "get_profile_details.php?id=" + preferenceManager.getPsuId();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        helperFunctions.stopProgressBar();

                        try {
                            name.setText(response.getString("name"));
                            username.setText(response.getString("username"));
                            email.setText(response.getString("email"));
                            phone.setText(response.getString("phone"));
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

        VolleySingleton.getInstance(EditProfileActivity.this).addToRequestQueue(request);
    }

    public void saveProfile(View view){
        //show progress dialog
        helperFunctions.genericProgressBar("Updating your profile...");

        String network_address = helperFunctions.getIpAddress()
                + "update_user_profile.php?name=" + name.getText().toString()
                + "&username=" + username.getText().toString()
                + "&phone=" + phone.getText().toString()
                + "&psu_id=" + preferenceManager.getPsuId()
                + "&email=" + email.getText().toString();

        // Request a string response from the provided URL.
        StringRequest request = new StringRequest(network_address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //dismiss progress dialog
                        helperFunctions.stopProgressBar();

                        if(response.equals("1")){
                            //saved article successfully
                            helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
                        } else {
                            helperFunctions.genericDialog("Failed to update profile");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //
            }
        });

        //add to request queue in singleton class
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void editProfilePicture(View view){
        Intent intent_profile = new Intent(EditProfileActivity.this, EditProfilePictureActivity.class);
        startActivity(intent_profile);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
