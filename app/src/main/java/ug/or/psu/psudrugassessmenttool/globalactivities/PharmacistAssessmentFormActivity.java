package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class PharmacistAssessmentFormActivity extends AppCompatActivity {

    String pharmacy_id, pharmacist_id;
    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;
    TextView pharmacy_name, pharmacist_name;
    EditText appraiser_name, appraiser_title, from_period, to_period, score_one,
            score_two, score_three, score_four, score_five;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacist_assessment_form);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        // TextViews
        pharmacy_name = findViewById(R.id.pharmacist_assessment_pharmacy_name);
        pharmacist_name = findViewById(R.id.pharmacist_assessment_pharmacist_name);

        // EditText
        appraiser_name = findViewById(R.id.pharmacist_assessment_appraiser_name);
        appraiser_title = findViewById(R.id.pharmacist_assessment_appraiser_title);
        from_period = findViewById(R.id.pharmacist_assessment_from_period);
        to_period = findViewById(R.id.pharmacist_assessment_to_period);
        score_one = findViewById(R.id.pharmacist_assessment_score_one);
        score_two = findViewById(R.id.pharmacist_assessment_score_two);
        score_three = findViewById(R.id.pharmacist_assessment_score_three);
        score_four = findViewById(R.id.pharmacist_assessment_score_four);
        score_five = findViewById(R.id.pharmacist_assessment_score_five);

        setUpForm();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setUpForm(){
        // get ids for the pharmacy
        helperFunctions.genericProgressBar("Setting up assessment form...");

        String network_address = helperFunctions.getIpAddress()
                + "get_pharmacy_owner_details.php?id=" + preferenceManager.getPsuId();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            pharmacy_id = response.getString("pharmacy_id");
                            pharmacist_id = response.getString("pharmacist_id");

                            setNames();
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

    public void setNames(){
        String network_address = helperFunctions.getIpAddress()
                + "get_pharmacy_owner_details_names.php?id=" + preferenceManager.getPsuId();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            pharmacy_name.setText(response.getString("pharmacy_name"));
                            pharmacist_name.setText(response.getString("pharmacist_name"));

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

    public void continueForm(View view){

        // calculate the average score
        int average_score_number = Integer.parseInt(score_one.getText().toString()) + Integer.parseInt(score_two.getText().toString()) +
                Integer.parseInt(score_three.getText().toString()) + Integer.parseInt(score_four.getText().toString()) + Integer.parseInt(score_five.getText().toString());

        average_score_number = average_score_number / 5;

        // create intent and add listings
        Intent continue_intent = new Intent(PharmacistAssessmentFormActivity.this, ConfirmPharmacistAssessmentFormActivity.class);
        continue_intent.putExtra("average_score_number", average_score_number);
        continue_intent.putExtra("pharmacy_id", pharmacy_id);
        continue_intent.putExtra("pharmacist_id", pharmacist_id);
        continue_intent.putExtra("appraiser_name", appraiser_name.getText().toString());
        continue_intent.putExtra("appraiser_title", appraiser_title.getText().toString());
        continue_intent.putExtra("from_period", from_period.getText().toString());
        continue_intent.putExtra("to_period", to_period.getText().toString());
        continue_intent.putExtra("score_one", score_one.getText().toString());
        continue_intent.putExtra("score_two", score_two.getText().toString());
        continue_intent.putExtra("score_three", score_three.getText().toString());
        continue_intent.putExtra("score_four", score_four.getText().toString());
        continue_intent.putExtra("score_five", score_five.getText().toString());

        startActivity(continue_intent);
    }
}
