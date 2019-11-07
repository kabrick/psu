package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.NetworkError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class ConfirmPharmacistAssessmentFormActivity extends AppCompatActivity {

    int average_score_number;
    String pharmacy_id, pharmacist_id, appraiser_name, appraiser_title, from_period,
            to_period, score_one, score_two, score_three, score_four, score_five;

    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;

    TextView pharmacist_assessment_average_score;
    EditText pharmacist_assessment_remarks;

    @SuppressLint({"SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_pharmacist_assessment_form);

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        pharmacist_assessment_average_score = findViewById(R.id.pharmacist_assessment_average_score);
        pharmacist_assessment_remarks = findViewById(R.id.pharmacist_assessment_remarks);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            average_score_number = extras.getInt("average_score_number", 0);
            pharmacy_id = extras.getString("pharmacy_id", "1");
            pharmacist_id = extras.getString("pharmacist_id", "1");
            appraiser_name = extras.getString("appraiser_name", "");
            appraiser_title = extras.getString("appraiser_title", "");
            from_period = extras.getString("from_period", "");
            to_period = extras.getString("to_period", "");
            score_one = extras.getString("score_one", "0");
            score_two = extras.getString("score_two", "0");
            score_three = extras.getString("score_three", "0");
            score_four = extras.getString("score_four", "0");
            score_five = extras.getString("score_five", "0");

            // set the average score
            pharmacist_assessment_average_score.setText(Integer.toString(average_score_number) + " %");

            // color code the average score
            if (average_score_number < 50){
                // red
                pharmacist_assessment_average_score.setTextColor(Color.RED);
            } else if (average_score_number <= 74){
                // orange
                pharmacist_assessment_average_score.setTextColor(Color.YELLOW);
            } else {
                // green
                pharmacist_assessment_average_score.setTextColor(Color.GREEN);
            }
        } else {
            // go to the default dashboard
            helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
        }
    }

    public void submitForm(View view){
        //start progress bar
        helperFunctions.genericProgressBar("Submitting form...");

        //get the timestamp
        Long timestamp = System.currentTimeMillis();

        String network_address = helperFunctions.getIpAddress()
                + "set_new_pharmacist_attendance.php?owner_id=" + preferenceManager.getPsuId()
                + "&pharmacy_id=" + pharmacy_id
                + "&pharmacist_id=" + pharmacist_id
                + "&appraiser_name=" + appraiser_name
                + "&appraiser_title=" + appraiser_title
                + "&from_period=" + from_period
                + "&to_period=" + to_period
                + "&score_one=" + score_one
                + "&score_two=" + score_two
                + "&score_three=" + score_three
                + "&score_four=" + score_four
                + "&score_five=" + score_five
                + "&average_score=" + average_score_number
                + "&remarks=" + pharmacist_assessment_remarks.getText().toString()
                + "&timestamp=" + timestamp;

        // Request a string response from the provided URL.
        StringRequest request = new StringRequest(network_address,
                response -> {
                    //stop progress bar
                    helperFunctions.stopProgressBar();

                    if(response.equals("1")){
                        //go to views page for submitted pharmacists

                        AlertDialog.Builder alert = new AlertDialog.Builder(ConfirmPharmacistAssessmentFormActivity.this);

                        alert.setMessage("Assessment form submitted").setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(ConfirmPharmacistAssessmentFormActivity.this, PharmacistAssessmentFormFeedOwnerActivity.class);
                                startActivity(intent);
                            }
                        }).show();
                    } else {
                        //did not save
                        helperFunctions.genericDialog("Something went wrong. Please try again later");
                    }
                }, error -> {
                    //stop progress bar
                    helperFunctions.stopProgressBar();

                    if (error instanceof TimeoutError || error instanceof NetworkError) {
                        helperFunctions.genericDialog("Something went wrong. Please make sure you are connected to a working internet connection.");
                    } else {
                        helperFunctions.genericDialog("Something went wrong. Please try again later");
                    }
                });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
