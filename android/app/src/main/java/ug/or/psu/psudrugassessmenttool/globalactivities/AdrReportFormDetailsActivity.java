package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class AdrReportFormDetailsActivity extends AppCompatActivity {

    String id;
    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;

    TextView patient_name, patient_number, age, weight, health_facility, district, last_menstrual_period,
            trimester, suspected_drug_generic_name, suspected_drug_brand_name, suspected_drug_dose, suspected_drug_date_started,
            suspected_drug_date_stopped, suspected_drug_prescribed_for, suspected_drug_expiry_date, suspected_drug_batch_number,
            reaction_and_treatment, date_reaction_started, date_reaction_stopped, notification_date, other_drug_generic_name,
            other_drug_brand_name, other_drug_dosage, other_drug_date_started, other_drug_date_stopped, other_drug_indication,
            relevant_lab_tests,additional_relevant_information, reporter_name, reporter_contacts, reporter_date, reporter_health_facility,
            sex, outcome, seriousness_of_reaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adr_report_form_details);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        preferenceManager = new PreferenceManager(this);
        helperFunctions = new HelperFunctions(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            id = extras.getString("id", "1");

            fetchRecord();
        } else {
            // go to the default dashboard
            helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
        }

        sex = findViewById(R.id.sex);
        outcome = findViewById(R.id.outcome);
        seriousness_of_reaction = findViewById(R.id.seriousness_of_reaction);
        patient_name = findViewById(R.id.patient_name);
        patient_number = findViewById(R.id.patient_number);
        age = findViewById(R.id.age);
        weight = findViewById(R.id.weight);
        health_facility = findViewById(R.id.health_facility);
        district = findViewById(R.id.district);
        last_menstrual_period = findViewById(R.id.last_menstrual_period);
        trimester = findViewById(R.id.trimester);
        suspected_drug_generic_name = findViewById(R.id.suspected_drug_generic_name);
        suspected_drug_brand_name = findViewById(R.id.suspected_drug_brand_name);
        suspected_drug_dose = findViewById(R.id.suspected_drug_dose);
        suspected_drug_date_started = findViewById(R.id.suspected_drug_date_started);
        suspected_drug_date_stopped = findViewById(R.id.suspected_drug_date_stopped);
        suspected_drug_prescribed_for = findViewById(R.id.suspected_drug_prescribed_for);
        suspected_drug_expiry_date = findViewById(R.id.suspected_drug_expiry_date);
        suspected_drug_batch_number = findViewById(R.id.suspected_drug_batch_number);
        reaction_and_treatment = findViewById(R.id.reaction_and_treatment);
        date_reaction_started = findViewById(R.id.date_reaction_started);
        date_reaction_stopped = findViewById(R.id.date_reaction_stopped);
        notification_date = findViewById(R.id.notification_date);
        other_drug_generic_name = findViewById(R.id.other_drug_generic_name);
        other_drug_brand_name = findViewById(R.id.other_drug_brand_name);
        other_drug_dosage = findViewById(R.id.other_drug_dosage);
        other_drug_date_started = findViewById(R.id.other_drug_date_started);
        other_drug_date_stopped = findViewById(R.id.other_drug_date_stopped);
        other_drug_indication = findViewById(R.id.other_drug_indication);
        relevant_lab_tests = findViewById(R.id.relevant_lab_tests);
        additional_relevant_information = findViewById(R.id.additional_relevant_information);
        reporter_name = findViewById(R.id.reporter_name);
        reporter_contacts = findViewById(R.id.reporter_contacts);
        reporter_date = findViewById(R.id.reporter_date);
        reporter_health_facility = findViewById(R.id.reporter_health_facility);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void fetchRecord(){

        helperFunctions.genericProgressBar("Retrieving form details...");

        String network_address = helperFunctions.getIpAddress()
                + "get_adr_form_single.php?id=" + id;

        @SuppressLint("SetTextI18n") JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                response -> {
                    try {
                        sex.setText(response.getString("sex"));
                        outcome.setText(response.getString("outcome"));
                        seriousness_of_reaction.setText(response.getString("seriousness_of_reaction"));
                        patient_name.setText(response.getString("patient_name"));
                        patient_number.setText(response.getString("patient_number"));
                        age.setText(response.getString("age"));
                        weight.setText(response.getString("weight"));
                        health_facility.setText(response.getString("health_facility"));
                        district.setText(response.getString("district"));
                        last_menstrual_period.setText(response.getString("last_menstrual_period"));
                        trimester.setText(response.getString("trimester"));
                        suspected_drug_generic_name.setText(response.getString("suspected_drug_generic_name"));
                        suspected_drug_brand_name.setText(response.getString("suspected_drug_brand_name"));
                        suspected_drug_dose.setText(response.getString("suspected_drug_dose"));
                        suspected_drug_date_started.setText(response.getString("suspected_drug_date_started"));
                        suspected_drug_date_stopped.setText(response.getString("suspected_drug_date_stopped"));
                        suspected_drug_prescribed_for.setText(response.getString("suspected_drug_prescribed_for"));
                        suspected_drug_expiry_date.setText(response.getString("suspected_drug_expiry_date"));
                        suspected_drug_batch_number.setText(response.getString("suspected_drug_batch_number"));
                        reaction_and_treatment.setText(response.getString("reaction_and_treatment"));
                        date_reaction_started.setText(response.getString("date_reaction_started"));
                        date_reaction_stopped.setText(response.getString("date_reaction_stopped"));
                        notification_date.setText(response.getString("notification_date"));
                        other_drug_generic_name.setText(response.getString("other_drug_generic_name"));
                        other_drug_brand_name.setText(response.getString("other_drug_brand_name"));
                        other_drug_dosage.setText(response.getString("other_drug_dosage"));
                        other_drug_date_started.setText(response.getString("other_drug_date_started"));
                        other_drug_date_stopped.setText(response.getString("other_drug_date_stopped"));
                        other_drug_indication.setText(response.getString("other_drug_indication"));
                        relevant_lab_tests.setText(response.getString("relevant_lab_tests"));
                        additional_relevant_information.setText(response.getString("additional_relevant_information"));
                        reporter_name.setText(response.getString("reporter_name"));
                        reporter_contacts.setText(response.getString("reporter_contacts"));
                        reporter_date.setText(response.getString("reporter_date"));
                        reporter_health_facility.setText(response.getString("reporter_health_facility"));

                        helperFunctions.stopProgressBar();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        helperFunctions.stopProgressBar();
                        helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
                    }
                }, error -> {
            helperFunctions.stopProgressBar();

            if (error instanceof TimeoutError || error instanceof NetworkError) {
                helperFunctions.genericDialog("Something went wrong. Please make sure you are connected to a working internet connection.");
            } else {
                helperFunctions.genericDialog("Something went wrong. Please try again later");
            }
            onBackPressed();
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
