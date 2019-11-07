package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
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
    EditText appraiser_name, appraiser_title, from_period, to_period;
    RadioGroup score_three_radio, score_one_radio,
            score_two_radio, score_four_radio, score_five_radio;
    int score_three_int = 0, score_one_int = 0,
            score_two_int = 0, score_four_int = 0, score_five_int = 0;
    String score_three = "", score_one = "",
            score_two = "", score_four = "", score_five = "";

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
        score_one_radio = findViewById(R.id.pharmacist_assessment_score_one);
        score_two_radio = findViewById(R.id.pharmacist_assessment_score_two);
        score_three_radio = findViewById(R.id.pharmacist_assessment_score_three);
        score_four_radio = findViewById(R.id.pharmacist_assessment_score_four);
        score_five_radio = findViewById(R.id.pharmacist_assessment_score_five);

        // set the listeners
        score_three_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checked_id) {
                switch (checked_id) {
                    case R.id.pharmacist_assessment_score_one1:
                        score_one_int = 100;
                        score_one = "Excellent";
                        break;
                    case R.id.pharmacist_assessment_score_one2:
                        score_one_int = 75;
                        score_one = "Good";
                        break;
                    case R.id.pharmacist_assessment_score_one3:
                        score_one_int = 50;
                        score_one = "Average";
                        break;
                    case R.id.pharmacist_assessment_score_one4:
                        score_one_int = 25;
                        score_one = "Fair";
                        break;
                    case R.id.pharmacist_assessment_score_one5:
                        score_one_int = 0;
                        score_one = "Poor";
                        break;
                }
            }
        });

        score_two_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checked_id) {
                switch (checked_id) {
                    case R.id.pharmacist_assessment_score_two1:
                        score_two_int = 100;
                        score_two = "Excellent";
                        break;
                    case R.id.pharmacist_assessment_score_two2:
                        score_two_int = 75;
                        score_two = "Good";
                        break;
                    case R.id.pharmacist_assessment_score_two3:
                        score_two_int = 50;
                        score_two = "Average";
                        break;
                    case R.id.pharmacist_assessment_score_two4:
                        score_two_int = 25;
                        score_two = "Fair";
                        break;
                    case R.id.pharmacist_assessment_score_two5:
                        score_two_int = 0;
                        score_two = "Poor";
                        break;
                }
            }
        });

        score_three_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checked_id) {
                switch (checked_id) {
                    case R.id.pharmacist_assessment_score_three1:
                        score_three_int = 100;
                        score_three = "Excellent";
                        break;
                    case R.id.pharmacist_assessment_score_three2:
                        score_three_int = 75;
                        score_three = "Good";
                        break;
                    case R.id.pharmacist_assessment_score_three3:
                        score_three_int = 100;
                        score_three = "Average";
                        break;
                    case R.id.pharmacist_assessment_score_three4:
                        score_three_int = 25;
                        score_three = "Fair";
                        break;
                    case R.id.pharmacist_assessment_score_three5:
                        score_three_int = 0;
                        score_three = "Poor";
                        break;
                }
            }
        });

        score_four_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checked_id) {
                switch (checked_id) {
                    case R.id.pharmacist_assessment_score_four1:
                        score_four_int = 100;
                        score_four = "Excellent";
                        break;
                    case R.id.pharmacist_assessment_score_four2:
                        score_four_int = 75;
                        score_four = "Good";
                        break;
                    case R.id.pharmacist_assessment_score_four3:
                        score_four_int = 50;
                        score_four = "Average";
                        break;
                    case R.id.pharmacist_assessment_score_four4:
                        score_four_int = 25;
                        score_four = "Fair";
                        break;
                    case R.id.pharmacist_assessment_score_four5:
                        score_four_int = 0;
                        score_four = "Poor";
                        break;
                }
            }
        });

        score_five_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checked_id) {
                switch (checked_id) {
                    case R.id.pharmacist_assessment_score_five1:
                        score_five_int = 100;
                        score_five = "Excellent";
                        break;
                    case R.id.pharmacist_assessment_score_five2:
                        score_five_int = 75;
                        score_five = "Good";
                        break;
                    case R.id.pharmacist_assessment_score_five3:
                        score_five_int = 50;
                        score_five = "Average";
                        break;
                    case R.id.pharmacist_assessment_score_five4:
                        score_five_int = 25;
                        score_five = "Fair";
                        break;
                    case R.id.pharmacist_assessment_score_five5:
                        score_five_int = 0;
                        score_five = "Poor";
                        break;
                }
            }
        });

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
        int average_score_number = score_one_int + score_two_int +
                score_three_int + score_four_int + score_five_int;

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
        continue_intent.putExtra("score_one", score_one);
        continue_intent.putExtra("score_two", score_two);
        continue_intent.putExtra("score_three", score_three);
        continue_intent.putExtra("score_four", score_four);
        continue_intent.putExtra("score_five", score_five);

        startActivity(continue_intent);
    }
}
