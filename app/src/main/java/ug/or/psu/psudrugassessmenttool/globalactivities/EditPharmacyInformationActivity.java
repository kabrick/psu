package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;
import ug.or.psu.psudrugassessmenttool.users.dashboards.psuadmin.ChoosePharmacyActivity;

public class EditPharmacyInformationActivity extends AppCompatActivity {

    String id;
    EditText name, location;
    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pharmacy_information);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        name = findViewById(R.id.pharmacy_name);
        location = findViewById(R.id.pharmacy_location);

        if (extras != null) {
            id = extras.getString("id", "1");

            helperFunctions.genericProgressBar("Retrieving details...");
            //fetch the coordinates for the pharmacy
            String network_address = helperFunctions.getIpAddress()
                    + "get_pharmacy_details.php?id=" + id;

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //dismiss dialog
                                helperFunctions.stopProgressBar();

                                name.setText(response.getString("pharmacy"));
                                location.setText(response.getString("location"));

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

            VolleySingleton.getInstance(this).addToRequestQueue(request);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
        return true;
    }

    public void save(View view){
        //start progress bar
        helperFunctions.genericProgressBar("Saving pharmacy information...");

        String network_address = helperFunctions.getIpAddress()
                + "edit_pharmacy_information.php?name=" + name.getText().toString()
                + "&location=" + location.getText().toString()
                + "&id=" + id;

        // Request a string response from the provided URL.
        StringRequest request = new StringRequest(network_address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //stop progress bar
                        helperFunctions.stopProgressBar();

                        //check if location has been saved successfully
                        if(response.equals("1")){
                            new SweetAlertDialog(EditPharmacyInformationActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success!")
                                    .setContentText("Pharmacy saved successfully")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
                                        }
                                    })
                                    .show();
                        } else {
                            //did not save
                            new SweetAlertDialog(EditPharmacyInformationActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Something went wrong! Please try again")
                                    .show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //stop progress bar
                helperFunctions.stopProgressBar();
                new SweetAlertDialog(EditPharmacyInformationActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Something went wrong! Please try again")
                        .show();
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}