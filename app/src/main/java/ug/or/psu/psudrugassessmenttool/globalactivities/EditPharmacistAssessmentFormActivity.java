package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class EditPharmacistAssessmentFormActivity extends AppCompatActivity {

    String id;
    TextView pharmacy_name, pharmacist_name;
    EditText appraiser_name, appraiser_title, from_period, to_period, score_one,
            score_two, score_three, score_four, score_five, remarks;
    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pharmacist_assessment_form);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        preferenceManager = new PreferenceManager(this);
        helperFunctions = new HelperFunctions(this);

        // TextView
        pharmacy_name = findViewById(R.id.edit_pharmacist_assessment_owner_pharmacy_name);
        pharmacist_name = findViewById(R.id.edit_pharmacist_assessment_owner_pharmacist_name);

        // EditText
        appraiser_name = findViewById(R.id.edit_pharmacist_assessment_owner_appraiser_name);
        appraiser_title = findViewById(R.id.edit_pharmacist_assessment_owner_appraiser_title);
        from_period = findViewById(R.id.edit_pharmacist_assessment_owner_from_period);
        to_period = findViewById(R.id.edit_pharmacist_assessment_owner_to_period);
        score_one = findViewById(R.id.edit_pharmacist_assessment_owner_score_one);
        score_two = findViewById(R.id.edit_pharmacist_assessment_owner_score_two);
        score_three = findViewById(R.id.edit_pharmacist_assessment_owner_score_three);
        score_four = findViewById(R.id.edit_pharmacist_assessment_owner_score_four);
        score_five = findViewById(R.id.edit_pharmacist_assessment_owner_score_five);
        remarks = findViewById(R.id.edit_pharmacist_assessment_owner_remarks);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            id = extras.getString("id", "1");
            pharmacy_name.setText(extras.getString("pharmacy_name", ""));
            pharmacist_name.setText(extras.getString("pharmacist_name", ""));
            appraiser_name.setText(extras.getString("appraiser_name", ""));
            appraiser_title.setText(extras.getString("appraiser_title", ""));
            from_period.setText(extras.getString("from_period", ""));
            to_period.setText(extras.getString("to_period", ""));
            score_one.setText(extras.getString("score_one", ""));
            score_two.setText(extras.getString("score_two", ""));
            score_three.setText(extras.getString("score_three", ""));
            score_four.setText(extras.getString("score_four", ""));
            score_five.setText(extras.getString("score_five", ""));
            remarks.setText(extras.getString("remarks", ""));
        } else {
            // go to the default dashboard
            helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
        }
    }

    public void saveForm(View view){
        //start progress bar
        helperFunctions.genericProgressBar("Submitting edited form...");

        // calculate the average
        int average_score_number = Integer.parseInt(score_one.getText().toString()) + Integer.parseInt(score_two.getText().toString()) +
                Integer.parseInt(score_three.getText().toString()) + Integer.parseInt(score_four.getText().toString()) + Integer.parseInt(score_five.getText().toString());

        average_score_number = average_score_number / 5;

        //get the timestamp
        Long timestamp = System.currentTimeMillis();

        String network_address = helperFunctions.getIpAddress()
                + "edit_pharmacist_attendance.php?id=" + id
                + "&appraiser_name=" + appraiser_name.getText().toString()
                + "&appraiser_title=" + appraiser_title.getText().toString()
                + "&from_period=" + from_period.getText().toString()
                + "&to_period=" + to_period.getText().toString()
                + "&score_one=" + score_one.getText().toString()
                + "&score_two=" + score_two.getText().toString()
                + "&score_three=" + score_three.getText().toString()
                + "&score_four=" + score_four.getText().toString()
                + "&score_five=" + score_five.getText().toString()
                + "&average_score=" + String.valueOf(average_score_number)
                + "&remarks=" + remarks.getText().toString()
                + "&timestamp=" + String.valueOf(timestamp);

        // Request a string response from the provided URL.
        StringRequest request = new StringRequest(network_address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //stop progress bar
                        helperFunctions.stopProgressBar();

                        //check if location has been saved successfully
                        if(response.equals("1")){
                            //go to views page for submitted pharmacists
                            Intent intent = new Intent(EditPharmacistAssessmentFormActivity.this, PharmacistAssessmentFormFeedOwnerActivity.class);
                            startActivity(intent);
                        } else {
                            //did not save
                            helperFunctions.genericDialog("Oops! An error occurred. Please try again later");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //stop progress bar
                helperFunctions.stopProgressBar();
                helperFunctions.genericDialog("Oops! An error occurred. Please try again later");
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
