package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class ViewPharmacistAssessmentFormOwnerActivity extends AppCompatActivity {

    String id;
    TextView pharmacy_name, pharmacist_name, appraiser_name, appraiser_title, from_period,
            to_period, score_one, score_two, score_three, score_four, score_five, remarks;
    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pharmacist_assessment_form_owner);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        preferenceManager = new PreferenceManager(this);
        helperFunctions = new HelperFunctions(this);

        // TextView
        pharmacy_name = findViewById(R.id.view_pharmacist_assessment_owner_pharmacy_name);
        pharmacist_name = findViewById(R.id.view_pharmacist_assessment_owner_pharmacist_name);
        appraiser_name = findViewById(R.id.view_pharmacist_assessment_owner_appraiser_name);
        appraiser_title = findViewById(R.id.view_pharmacist_assessment_owner_appraiser_title);
        from_period = findViewById(R.id.view_pharmacist_assessment_owner_from_period);
        to_period = findViewById(R.id.view_pharmacist_assessment_owner_to_period);
        score_one = findViewById(R.id.view_pharmacist_assessment_owner_score_one);
        score_two = findViewById(R.id.view_pharmacist_assessment_owner_score_two);
        score_three = findViewById(R.id.view_pharmacist_assessment_owner_score_three);
        score_four = findViewById(R.id.view_pharmacist_assessment_owner_score_four);
        score_five = findViewById(R.id.view_pharmacist_assessment_owner_score_five);
        remarks = findViewById(R.id.view_pharmacist_assessment_owner_remarks);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            id = extras.getString("id", "1");

            fetchRecord();
        } else {
            // go to the default dashboard
            helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
        }
    }

    public void fetchRecord(){

        helperFunctions.genericProgressBar("Retrieving form details...");

        String network_address = helperFunctions.getIpAddress()
                + "get_pharmacist_assessment_form.php?id=" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            pharmacy_name.setText(response.getString("pharmacy_name"));
                            pharmacist_name.setText(response.getString("pharmacist_name"));
                            appraiser_name.setText(response.getString("appraiser_name"));
                            appraiser_title.setText(response.getString("appraiser_title"));
                            from_period.setText(response.getString("from_period"));
                            to_period.setText(response.getString("to_period"));
                            score_one.setText(response.getString("score_one"));
                            score_two.setText(response.getString("score_two"));
                            score_three.setText(response.getString("score_three"));
                            score_four.setText(response.getString("score_four"));
                            score_five.setText(response.getString("score_five"));
                            remarks.setText(response.getString("remarks"));

                            helperFunctions.stopProgressBar();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            helperFunctions.stopProgressBar();
                            helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                helperFunctions.stopProgressBar();

                helperFunctions.genericDialog("Something went wrong. Please try again");

                helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void deleteForm(View view){
        //
    }

    public void editForm(View view){
        //
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
