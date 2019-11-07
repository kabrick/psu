package ug.or.psu.psudrugassessmenttool.globalactivities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;

public class AdrReportFormActivity extends AppCompatActivity {

    EditText patient_name, patient_number, age, weight, health_facility, district, last_menstrual_period,
            trimester, suspected_drug_generic_name, suspected_drug_brand_name, suspected_drug_dose, suspected_drug_date_started,
            suspected_drug_date_stopped, suspected_drug_prescribed_for, suspected_drug_expiry_date, suspected_drug_batch_number,
            reaction_and_treatment, date_reaction_started, date_reaction_stopped, notification_date, other_drug_generic_name,
            other_drug_brand_name, other_drug_dosage, other_drug_date_started, other_drug_date_stopped, other_drug_indication,
            relevant_lab_tests,additional_relevant_information, reporter_name, reporter_contacts, reporter_date, reporter_health_facility;

    RadioGroup sex, outcome, seriousness_of_reaction;
    String sex_string, outcome_string, seriousness_of_reaction_string;

    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adr_report_form);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

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

        sex.setOnCheckedChangeListener((radioGroup, checked_id) -> {
            if(checked_id == R.id.sex_female){
                sex_string = "Female";
            } else if(checked_id == R.id.sex_male){
                sex_string = "Male";
            }
        });

        outcome.setOnCheckedChangeListener((radioGroup, checked_id) -> {
            if(checked_id == R.id.outcome_1){
                outcome_string = "Recovered";
            } else if(checked_id == R.id.outcome_2){
                outcome_string = "Recovering";
            } else if(checked_id == R.id.outcome_3){
                outcome_string = "Continuing";
            } else if(checked_id == R.id.outcome_4){
                outcome_string = "Death due to reaction";
            }
        });

        seriousness_of_reaction.setOnCheckedChangeListener((radioGroup, checked_id) -> {
            if(checked_id == R.id.seriousness_of_reaction_1){
                seriousness_of_reaction_string = "Patient Died";
            } else if(checked_id == R.id.seriousness_of_reaction_2){
                seriousness_of_reaction_string = "Prolonged inpatient hospitalization";
            } else if(checked_id == R.id.seriousness_of_reaction_3){
                seriousness_of_reaction_string = "Involved disability";
            } else if(checked_id == R.id.seriousness_of_reaction_4){
                seriousness_of_reaction_string = "Life Threatening";
            } else if(checked_id == R.id.seriousness_of_reaction_5){
                seriousness_of_reaction_string = "Congenital Abnormality";
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void submitForm(View view){
        final String age_string = age.getText().toString();
        final String generic_string = suspected_drug_generic_name.getText().toString();
        final String date_string = suspected_drug_date_started.getText().toString();
        final String reporter_name_string = reporter_name.getText().toString();
        final String date_reaction_started_string = date_reaction_started.getText().toString();

        if(TextUtils.isEmpty(age_string)) {
            helperFunctions.genericDialog("Please indicate age of the patient");
            return;
        }

        if(TextUtils.isEmpty(sex_string)) {
            helperFunctions.genericDialog("Please indicate sex of the patient");
            return;
        }

        if(TextUtils.isEmpty(generic_string)) {
            helperFunctions.genericDialog("Please indicate generic name of suspected drug");
            return;
        }

        if(TextUtils.isEmpty(date_string)) {
            helperFunctions.genericDialog("Please indicate the date when suspected drug was started");
            return;
        }

        if(TextUtils.isEmpty(reporter_name_string)) {
            helperFunctions.genericDialog("Please indicate reporter's name");
            return;
        }

        if(TextUtils.isEmpty(date_reaction_started_string)) {
            helperFunctions.genericDialog("Please indicate the date when reaction started");
            return;
        }

        //show progress dialog
        helperFunctions.genericProgressBar("Posting ADR Form...");

        //get the current timestamp
        final Long timestamp_long = System.currentTimeMillis();

        String url = helperFunctions.getIpAddress() + "post_adr_form.php";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, response -> {
            if(response.equals("0")){
                helperFunctions.stopProgressBar();
                helperFunctions.genericDialog("Posting failed. Please try again later");
            } else {
                helperFunctions.stopProgressBar();
                AlertDialog.Builder alert = new AlertDialog.Builder(AdrReportFormActivity.this);

                alert.setMessage("Your ADR form has been posted successfully").setPositiveButton("Okay", (dialogInterface, i) -> helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory())).show();
            }
        }, error -> {
            helperFunctions.stopProgressBar();

            if (error instanceof TimeoutError || error instanceof NetworkError) {
                helperFunctions.genericDialog("Something went wrong. Please make sure you are connected to a working internet connection.");
            } else {
                helperFunctions.genericDialog("Something went wrong. Please try again later");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> data = new HashMap<>();

                data.put("sex", sex_string);
                data.put("outcome", outcome_string);
                data.put("seriousness_of_reaction", seriousness_of_reaction_string);
                data.put("patient_name", patient_name.getText().toString());
                data.put("patient_number", patient_number.getText().toString());
                data.put("age", age_string);
                data.put("weight", weight.getText().toString());
                data.put("health_facility", health_facility.getText().toString());
                data.put("district", district.getText().toString());
                data.put("last_menstrual_period", last_menstrual_period.getText().toString());
                data.put("trimester", trimester.getText().toString());
                data.put("suspected_drug_generic_name", generic_string);
                data.put("suspected_drug_brand_name", suspected_drug_brand_name.getText().toString());
                data.put("suspected_drug_dose", suspected_drug_dose.getText().toString());
                data.put("suspected_drug_date_started", date_string);
                data.put("suspected_drug_date_stopped", suspected_drug_date_stopped.getText().toString());
                data.put("suspected_drug_prescribed_for", suspected_drug_prescribed_for.getText().toString());
                data.put("suspected_drug_expiry_date", suspected_drug_expiry_date.getText().toString());
                data.put("suspected_drug_batch_number", suspected_drug_batch_number.getText().toString());
                data.put("reaction_and_treatment", reaction_and_treatment.getText().toString());
                data.put("date_reaction_started", date_reaction_started_string);
                data.put("date_reaction_stopped", date_reaction_stopped.getText().toString());
                data.put("notification_date", notification_date.getText().toString());
                data.put("other_drug_generic_name", other_drug_generic_name.getText().toString());
                data.put("other_drug_brand_name", other_drug_brand_name.getText().toString());
                data.put("other_drug_dosage", other_drug_dosage.getText().toString());
                data.put("other_drug_date_started", other_drug_date_started.getText().toString());
                data.put("other_drug_date_stopped", other_drug_date_stopped.getText().toString());
                data.put("other_drug_indication", other_drug_indication.getText().toString());
                data.put("relevant_lab_tests", relevant_lab_tests.getText().toString());
                data.put("additional_relevant_information", additional_relevant_information.getText().toString());
                data.put("reporter_name", reporter_name_string);
                data.put("reporter_contacts", reporter_contacts.getText().toString());
                data.put("reporter_date", reporter_date.getText().toString());
                data.put("reporter_health_facility", reporter_health_facility.getText().toString());
                data.put("submitted_by", preferenceManager.getPsuId());
                data.put("timestamp", timestamp_long.toString());
                return data;
            }
        };

        requestQueue.add(MyStringRequest);
    }
}
